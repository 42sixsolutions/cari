package com._42six.cari.services.model;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Parameters {
	
	private final Date firstDate;
	private final Date lastDate;
	private final Map<String, Double> maxFinalResult;
	private final Set<String> locationZones;
	
	public Parameters(
			Date firstDate, 
			Date lastDate, 
			SortedMap<String, Double> maxFinalResult, 
			SortedSet<String> locationZones
			) {
		this.firstDate = firstDate;
		this.lastDate = lastDate;
		this.maxFinalResult = Collections.unmodifiableMap(maxFinalResult);
		this.locationZones = Collections.unmodifiableSet(locationZones);
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

	public Set<String> getLocationZones() {
		return locationZones;
	}
}
