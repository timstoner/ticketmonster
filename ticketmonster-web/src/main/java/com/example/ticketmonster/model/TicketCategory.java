package com.example.ticketmonster.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;

import com.example.ticketmonster.rest.dto.NestedTicketCategoryDTO;
import com.example.ticketmonster.rest.dto.TicketCategoryDTO;

/**
 * <p>
 * A lookup table containing the various ticket categories. E.g. Adult, Child,
 * Pensioner, etc.
 * </p>
 * 
 * @author Shane Bryzak
 * @author Pete Muir
 */
/*
 * We suppress the warning about not specifying a serialVersionUID, as we are
 * still developing this app, and want the JVM to generate the serialVersionUID
 * for us. When we put this app into production, we'll generate and embed the
 * serialVersionUID
 */
@SuppressWarnings("serial")
@Entity
public class TicketCategory extends BaseEntity<TicketCategoryDTO> implements
		Serializable {

	/* Declaration of fields */

	/**
	 * The synthetic id of the object.
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	/**
	 * <p>
	 * The description of the of ticket category.
	 * </p>
	 * 
	 * <p>
	 * The description forms the natural id of the ticket category, and so must
	 * be unique.
	 * </p>
	 * 
	 * <p>
	 * The description must not be null and must be one or more characters, the
	 * Bean Validation constrain <code>@NotEmpty</code> enforces this.
	 * </p>
	 * 
	 */
	@Column(unique = true)
	@NotEmpty
	private String description;

	/* Boilerplate getters and setters */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * toString(), equals() and hashCode() for TicketCategory, using the natural
	 * identity of the object
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		TicketCategory that = (TicketCategory) o;

		if (description != null ? !description.equals(that.description)
				: that.description != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return description != null ? description.hashCode() : 0;
	}

	@Override
	public String toString() {
		return description;
	}

	public TicketCategoryDTO buildDTO() {
		TicketCategoryDTO dto = new TicketCategoryDTO();

		dto.setDescription(description);
		dto.setId(id);

		return dto;
	}

	public NestedTicketCategoryDTO buildNestedDTO() {
		NestedTicketCategoryDTO dto = new NestedTicketCategoryDTO();

		dto.setDescription(description);
		dto.setId(id);

		return dto;
	}

	@Override
	public TicketCategoryDTO convertToDTO() {
		TicketCategoryDTO dto = new TicketCategoryDTO();
		dto.setDescription(description);
		dto.setId(id);
		return dto;
	}

	@Override
	public void convertFromDTO(TicketCategoryDTO dto, EntityManager em) {
		this.description = dto.getDescription();
		this.id = dto.getId();
	}

	public String getFindByIdQuery() {
		return "SELECT DISTINCT t FROM TicketCategory t WHERE t.id = :entityId ORDER BY t.id";
	}

	public String getFindAllQuery() {
		return "SELECT DISTINCT t FROM TicketCategory t ORDER BY t.id";
	}

	public static TicketCategory buildTicketCategory(
			NestedTicketCategoryDTO dto, EntityManager em) {
		TicketCategory entity = em.find(TicketCategory.class, dto.getId());

		return entity;
	}
}
