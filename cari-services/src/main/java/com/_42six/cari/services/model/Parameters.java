package com._42six.cari.services.model;

import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Parameters {
	
	private final Date firstDate;
	private final Date lastDate;
	private final Map<String, Double> maxFinalResult;
	
	public Parameters(Date firstDate, Date lastDate, Map<String, Double> maxFinalResult) {
		this.firstDate = firstDate;
		this.lastDate = lastDate;
		this.maxFinalResult = maxFinalResult;
	}
	
	public Date getFirstDate() {
		return firstDate;
	}
	
	public Date getLastDate() {
		return lastDate;
	}
	
	public Map<String, Double> getMaxFinalResult() {
		return maxFinalResult;
	}
}
