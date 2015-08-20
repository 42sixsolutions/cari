package com._42six.cari.services.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Parameters {
	
	private final Date firstDate;
	private final Date lastDate;
	
	public Parameters(Date firstDate, Date lastDate) {
		this.firstDate = firstDate;
		this.lastDate = lastDate;
	}
	
	public Date getFirstDate() {
		return firstDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
}
