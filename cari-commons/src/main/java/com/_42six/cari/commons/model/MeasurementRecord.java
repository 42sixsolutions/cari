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
	private final double roundedLat;
	private final double roundedLon;
	
	public MeasurementRecord(Date sampleDate, double roundedLat, double roundedLon)  {
		this.sampleDate = sampleDate;
		this.roundedLat = roundedLat;
		this.roundedLon = roundedLon;
	}
	
	public Date getSampleDate() {
		return sampleDate;
	}

	public double getRoundedLat() {
		return roundedLat;
	}

	public double getRoundedLon() {
		return roundedLon;
	}
	
	public String get(MeasurementField field) {
		return this.get(field.toString());
	}
	
	public double getBottomDepthInches() {
		String depth = this.get(MeasurementField.BOTTOM_DEPTH);
		String units = this.get(MeasurementField.BOTTOM_DEPTH_UNITS);
		return getInches(depth, units);
	}
	
	public double getTopDepthInches() {
		String depth = this.get(MeasurementField.TOP_DEPTH);
		String units = this.get(MeasurementField.TOP_DEPTH_UNITS);
		return getInches(depth, units);
	}
	
	private double getInches(String value, String units) {
		if (value == null || value.isEmpty() || units == null || units.isEmpty()) {
			return 0;
		}
		Double valueDbl = Double.parseDouble(value);
		if (valueDbl == 0) {
			return 0;
		}
		else {
			units = units.toLowerCase().split(" ")[0];
			if (units.equals("inches")) {
				return valueDbl;
			}
			else if (units.equals("feet")) {
				return valueDbl * 12.0;
			}
			else if (units.equals("centimeters") || units.equals("cm")) {
				return valueDbl * 0.393701;
			}
			else {
				return 0;
			}
		}
	}
	
	public double getFinalResultNormalized() {
		String finalResult = this.get(MeasurementField.FINAL_RESULT.toString());
		//TODO: normalize?
		return finalResult != null && !finalResult.isEmpty() ? Double.parseDouble(finalResult) : 0;
	}
	
	public double getWeightedContaminantValue(Double maxContamination) {
		return maxContamination == null || maxContamination == 0 ? 0 : this.getFinalResultNormalized() / maxContamination;
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
		TOP_DEPTH(false),
		BOTTOM_DEPTH(false),
		TOP_DEPTH_UNITS(false),
		BOTTOM_DEPTH_UNITS(false),
		FINAL_RESULT(false),
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
