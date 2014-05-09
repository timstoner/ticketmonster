package com.example.ticketmonster.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.example.ticketmonster.model.Performance;
import com.example.ticketmonster.rest.dto.PerformanceDTO;

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
 */
public abstract class BaseEntityService<T, S> implements
		ApplicationEventPublisherAware {

	private static final Logger LOG = LoggerFactory
			.getLogger(BaseEntityService.class);

	protected ApplicationEventPublisher publisher;

	@PersistenceContext
	private EntityManager entityManager;

	private Class<T> entityClass;

	private Class<S> dtoClass;

	public BaseEntityService() {

	}

	public BaseEntityService(Class<T> entityClass, Class<S> dtoClass) {
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	// @POST
	// @Consumes("application/json")
	// @Transactional
	// public Response create(S dto) {
	// // convert performance dto to a performance entity
	// Performance entity = dto.fromDTO(null, getEntityManager());
	// // persist the performance to the database
	// getEntityManager().persist(entity);
	//
	// // build the performance uri for future requests
	// String path = String.valueOf(entity.getId());
	// URI uri =
	// UriBuilder.fromResource(PerformanceService.class).path(path)
	// .build();
	// LOG.info("Performance created path: {}", path);
	//
	// // return 201 'created' response
	// return Response.created(uri).build();
	// }

	/**
	 * <p>
	 * A method for retrieving all entities of a given type. Supports the query
	 * parameters <code>first</code> and <code>maxResults</code> for pagination.
	 * </p>
	 * 
	 * @param uriInfo
	 *            application and request context information (see {@see
	 *            UriInfo} class information for more details)
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<T> getAll(@Context UriInfo uriInfo) {
		return getAll(uriInfo.getQueryParameters());
	}

	public List<T> getAll(MultivaluedMap<String, String> queryParameters) {
		final CriteriaBuilder criteriaBuilder = entityManager
				.getCriteriaBuilder();

		StringBuilder debugMsgBuilder = new StringBuilder("getAll ");

		final CriteriaQuery<T> criteriaQuery = criteriaBuilder
				.createQuery(entityClass);

		Root<T> root = criteriaQuery.from(entityClass);

		Predicate[] predicates = extractPredicates(queryParameters,
				criteriaBuilder, root);

		criteriaQuery.select(criteriaQuery.getSelection()).where(predicates);
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));

		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		String queryParam;

		if (queryParameters.containsKey("first")) {
			queryParam = queryParameters.getFirst("first");
			debugMsgBuilder.append("first: ").append(queryParam).append(" ");
			Integer firstRecord = Integer.parseInt(queryParam) - 1;
			query.setFirstResult(firstRecord);
		}

		if (queryParameters.containsKey("maxResults")) {
			queryParam = queryParameters.getFirst("maxResults");
			debugMsgBuilder.append("maxResults: ").append(queryParam)
					.append(" ");
			Integer maxResults = Integer.parseInt(queryParam);
			query.setMaxResults(maxResults);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug(debugMsgBuilder.toString());
		}

		return query.getResultList();
	}

	/**
	 * <p>
	 * A method for counting all entities of a given type
	 * </p>
	 * 
	 * @param uriInfo
	 *            application and request context information (see {@see
	 *            UriInfo} class information for more details)
	 * @return
	 */
	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Long> getCount(@Context UriInfo uriInfo) {
		LOG.debug("getCount");

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteriaQuery = criteriaBuilder
				.createQuery(Long.class);

		Root<T> root = criteriaQuery.from(entityClass);

		criteriaQuery.select(criteriaBuilder.count(root));

		Predicate[] predicates = extractPredicates(
				uriInfo.getQueryParameters(), criteriaBuilder, root);

		criteriaQuery.where(predicates);

		Map<String, Long> result = new HashMap<String, Long>();

		result.put("count", entityManager.createQuery(criteriaQuery)
				.getSingleResult());
		return result;
	}

	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
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

	/**
	 * <p>
	 * A method for retrieving individual entity instances.
	 * </p>
	 * 
	 * @param id
	 *            entity id
	 * @return
	 */
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSingle(@PathParam("id") Long id) {
		LOG.debug("getSingle {}", id);

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);

		Root<T> root = criteriaQuery.from(entityClass);
		Predicate condition = builder.equal(root.get("id"), id);

		Selection<T> selection = builder.createQuery(entityClass)
				.getSelection();
		criteriaQuery.select(selection).where(condition);

		T result;
		ResponseBuilder rb;

		try {
			result = entityManager.createQuery(criteriaQuery).getSingleResult();
			rb = Response.ok(result);
		} catch (NoResultException nre) {
			rb = Response.status(Status.NOT_FOUND);
		}

		return rb.build();
	}
}
