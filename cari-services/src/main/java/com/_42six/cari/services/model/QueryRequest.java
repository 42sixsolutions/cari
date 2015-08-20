package com._42six.cari.services.model;

import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QueryRequest {
	
	public enum ViewType { latest, highest };
	
	private Date toDate;
	private Set<String> contaminants;
	private Set<String> locationZones;
	private Set<String> uuids;
	private ViewType viewType;
	
	public QueryRequest() {
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Set<String> getContaminants() {
		return contaminants;
	}

	public void setContaminants(Set<String> contaminants) {
		this.contaminants = contaminants;
	}

	public Set<String> getLocationZones() {
		return locationZones;
	}

	public void setLocationZones(Set<String> locationZones) {
		this.locationZones = locationZones;
	}

	public Set<String> getUuids() {
		return uuids;
	}

	public void setUuids(Set<String> uuids) {
		this.uuids = uuids;
	}

	public ViewType getViewType() {
		return viewType;
	}

	public void setViewType(ViewType viewType) {
		this.viewType = viewType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contaminants == null) ? 0 : contaminants.hashCode());
		result = prime * result
				+ ((locationZones == null) ? 0 : locationZones.hashCode());
		result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
		result = prime * result + ((uuids == null) ? 0 : uuids.hashCode());
		result = prime * result
				+ ((viewType == null) ? 0 : viewType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryRequest other = (QueryRequest) obj;
		if (contaminants == null) {
			if (other.contaminants != null)
				return false;
		} else if (!contaminants.equals(other.contaminants))
			return false;
		if (locationZones == null) {
			if (other.locationZones != null)
				return false;
		} else if (!locationZones.equals(other.locationZones))
			return false;
		if (toDate == null) {
			if (other.toDate != null)
				return false;
		} else if (!toDate.equals(other.toDate))
			return false;
		if (uuids == null) {
			if (other.uuids != null)
				return false;
		} else if (!uuids.equals(other.uuids))
			return false;
		if (viewType != other.viewType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QueryRequest [toDate=" + toDate + ", contaminants="
				+ contaminants + ", locationZones=" + locationZones
				+ ", uuids=" + uuids + ", viewType=" + viewType + "]";
	}
}
