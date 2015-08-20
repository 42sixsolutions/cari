package com._42six.cari.commons.model;

public class Field {
	private String fieldName;
	private boolean mandatory;
	
	public Field(String fieldName, boolean mandatory) {
		this.fieldName = fieldName;
		this.mandatory = mandatory;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
}