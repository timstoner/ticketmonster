package com.example.ticketmonster.rest.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedList<T> {

	private int offset;

	private int count;

	private List<T> items;

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

}
