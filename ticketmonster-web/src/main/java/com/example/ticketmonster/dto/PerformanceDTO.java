package com.example.ticketmonster.dto;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PerformanceDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private NestedShowDTO show;
	private Date date;
	private String displayTitle;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public NestedShowDTO getShow() {
		return this.show;
	}

	public void setShow(NestedShowDTO show) {
		this.show = show;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDisplayTitle() {
		return this.displayTitle;
	}

	public void setDisplayTitle(String title) {
		this.displayTitle = title;
	}
}
