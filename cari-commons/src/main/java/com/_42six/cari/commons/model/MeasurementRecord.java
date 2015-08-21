package com._42six.cari.commons.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MeasurementRecord extends HashMap<String, String> {

	private static final long serialVersionUID = -4521483048633833990L;
	
	public static final List<Field> FIELD_LIST = createFieldList();

	private final Date sampleDate;
	
	public MeasurementRecord(Date sampleDate)  {
		this.sampleDate = sampleDate;
	}
	
	public Date getSampleDate() {
		return sampleDate;
	}

	private static List<Field> createFieldList() {
		List<Field> list = new ArrayList<Field>();
		
		for (MeasurementField field : MeasurementField.values()) {
			list.add(new Field(field.toString(), field.isMandatory));
		}
		
		return Collections.unmodifiableList(list);
	}
	
	public enum MeasurementField {
		
		SAMPLE_DATE_TIME(true),
		CASE_NUMBER(true),
		SITE_EVENT_ID(true),
		ANALYTE_NAME(true),
		SAMPLE_SUBMATRIX(true),
		LOCATION_ZONE(true),
		LATITUDE(true),
		LONGITUDE(true),
		//TOP_DEPTH(),
		//BOTTOM_DEPTH(),
		FINAL_RESULT(true),
		RESULT_UNITS(true);
		
		private final boolean isMandatory;
		
		MeasurementField(boolean isMandatory) {
			this.isMandatory = isMandatory;
		}

		public boolean isMandatory() {
			return isMandatory;
		}
	}
	
	/*
	public enum Contaminant {
		
		Zinc(5000),
		Phosphorus(10),
		Mercury(2),
		Magnesium(1000),
		Lead(1),
		Copper(1300),
		Calcium(1000),
		Cadmium(5),
		Arsenic(1),
		
		//new ones
		
		;


		
		private final double minContaminantValue;
		
		Contaminant(double minContaminantValue) {
			this.minContaminantValue = minContaminantValue;
		}
		
		public boolean isContaminated(double value) {
			return value > minContaminantValue;
		}
		
		public double getContaminationValue(double value) {
			return value / this.minContaminantValue;
		}
	}*/
}
