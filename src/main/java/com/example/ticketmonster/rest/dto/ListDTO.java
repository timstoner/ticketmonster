package com.example.ticketmonster.rest.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

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

}
