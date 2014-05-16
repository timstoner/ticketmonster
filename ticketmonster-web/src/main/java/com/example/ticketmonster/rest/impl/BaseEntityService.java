package com.example.ticketmonster.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ticketmonster.model.BaseEntity;
import com.example.ticketmonster.rest.BaseService;
import com.example.ticketmonster.rest.dto.BaseDTO;

/**
 * <p>
 * A number of RESTful services implement GET operations on a particular type of
 * entity. For observing the DRY principle, the generic operations are
 * implemented in the <code>BaseEntityService</code> class, and the other
 * services can inherit from here.
 * </p>
 * 
 * <p>
 * Subclasses will declare a base path using the JAX-RS {@link Path} annotation,
 * for example:
 * </p>
 * 
 * <pre>
 * <code>
 * &#064;Path("/widgets")
 * public class WidgetService extends BaseEntityService<Widget> {
 * ...
 * }
 * </code>
 * </pre>
 * 
 * <p>
 * will support the following methods:
 * </p>
 * 
 * <pre>
 * <code>
 *   GET /widgets
 *   GET /widgets/:id
 *   GET /widgets/count
 * </code>
 * </pre>
 * 
 * <p>
 * Subclasses may specify various criteria for filtering entities when
 * retrieving a list of them, by supporting custom query parameters. Pagination
 * is supported by default through the query parameters <code>first</code> and
 * <code>maxResults</code>.
 * </p>
 * 
 * <p>
 * The class is abstract because it is not intended to be used directly, but
 * subclassed by actual JAX-RS endpoints.
 * </p>
 * 
 * 
 * @author Marius Bogoevici
 * @param <S>
 * @param <S>
 */
public abstract class BaseEntityService<T extends BaseEntity<S>, S extends BaseDTO>
		implements BaseService<S> {

	public static final MultivaluedHashMap<String, String> EMPTY = new MultivaluedHashMap<String, String>();

	private static Logger LOG = LoggerFactory
			.getLogger(BaseEntityService.class);

	@PersistenceContext
	private EntityManager entityManager;

	private Class<T> entityClass;

	public BaseEntityService() {
	}

	public BaseEntityService(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public Response create(S dto) {
		T entity = buildEntity(dto);

		// persist to the database
		persist(entity);

		// return the path to the new resource
		URI path = UriBuilder.fromResource(entityClass)
				.path(String.valueOf(entity.getId())).build();
		return Response.created(path).build();
	}

	public Response getCount(UriInfo uriInfo) {
		long count = this.getCount(uriInfo.getQueryParameters());
		Map<String, Long> result = new HashMap<String, Long>();
		result.put("count", count);

		return Response.ok(result).build();
	}

	public Response deleteById(Long id) {
		// query for the T entity
		T entity = getEntityManager().find(entityClass, id);

		ResponseBuilder rb;

		// if entity found, remove from entity manager
		if (entity != null) {
			rb = Response.noContent();
			getEntityManager().remove(entity);
		} else {
			// if no entity found, set 404 status
			rb = Response.status(Status.NOT_FOUND);
		}

		LOG.debug("deleteById Result: {}", rb);

		return rb.build();
	}

	public Response deleteAll(UriInfo uriInfo) {
		List<T> entities = getAll(uriInfo.getQueryParameters());

		for (T entity : entities) {
			entityManager.remove(entity);
		}

		return Response.noContent().build();
	}

	public Response findById(Long id) {
		T entity = getSingleInstance(id);

		if (entity != null) {
			S dto = buildDTO(entity);
			return Response.ok(dto).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@Override
	public Response findAll(UriInfo uriInfo) {
		List<T> entities = this.getAll(uriInfo.getQueryParameters());
		List<S> dtoResults = new ArrayList<>();

		// convert entities to data transfer objects
		for (T entity : entities) {
			S dto = buildDTO(entity);
			dtoResults.add(dto);
		}

		return Response.ok(dtoResults).build();
	}

	@Override
	public Response update(Long id, S dto) {
		LOG.debug("update {}", id);

		T entity = getSingleInstance(id);

		entity = getEntityManager().find(entityClass, id);
		entity = getEntityManager().merge(entity);
		return Response.noContent().build();
	}

	protected T getSingleInstance(Long id) {
		TypedQuery<T> findByIdQuery = getEntityManager().createQuery(
				getFindByIdQuery(), entityClass);
		findByIdQuery.setParameter("entityId", id);

		T entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException e) {
			entity = null;
		}

		return entity;
	}

	protected List<T> getAll(MultivaluedMap<String, String> queryParameters) {
		TypedQuery<T> query = entityManager.createQuery(getFindAllQuery(),
				entityClass);

		Integer value;
		if (queryParameters.containsKey("first")) {
			value = Integer.parseInt(queryParameters.getFirst("first")) - 1;
			query.setFirstResult(value);
		}
		if (queryParameters.containsKey("maxResults")) {
			value = Integer.parseInt(queryParameters.getFirst("maxResults"));
			query.setMaxResults(value);
		}

		return query.getResultList();
	}

	protected long getCount(MultivaluedMap<String, String> queryParameters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder
				.createQuery(Long.class);

		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(criteriaBuilder.count(root));

		Predicate[] predicates = extractPredicates(queryParameters,
				criteriaBuilder, root);

		criteriaQuery.where(predicates);

		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	/**
	 * <p>
	 * Subclasses may choose to expand the set of supported query parameters
	 * (for adding more filtering criteria on search and count) by overriding
	 * this method.
	 * </p>
	 * 
	 * @param queryParameters
	 *            - the HTTP query parameters received by the endpoint
	 * @param criteriaBuilder
	 *            - @{link CriteriaBuilder} used by the invoker
	 * @param root
	 * @{link Root} used by the invoker
	 * @return a list of {@link Predicate}s that will added as query parameters
	 */
	protected Predicate[] extractPredicates(
			MultivaluedMap<String, String> queryParameters,
			CriteriaBuilder criteriaBuilder, Root<T> root) {
		return new Predicate[] {};
	}

	public void persist(T entity) {
		// persist entity in database
		entityManager.persist(entity);
		entityManager.flush();
	}

	protected abstract S buildDTO(T entity);

	protected abstract T buildEntity(S dto);

	protected abstract String getFindAllQuery();

	protected abstract String getFindByIdQuery();

}