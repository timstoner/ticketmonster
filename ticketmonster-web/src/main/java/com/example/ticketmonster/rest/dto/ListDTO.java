package com.example.ticketmonster.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement
public class ListDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<T> items;

	public ListDTO() {
		items = new ArrayList<T>();
	}

	public void addAll(List<T> list) {
		items.addAll(list);
	}

	public List<T> getItems() {
		return items;
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}
