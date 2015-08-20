package com._42six.cari.commons.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MeasurementRecord extends HashMap<String, String> {

	private static final long serialVersionUID = -4521483048633833990L;
	
	public static final List<Field> FIELD_LIST = createFieldList();

	
	private static List<Field> createFieldList() {
		List<Field> list = new ArrayList<Field>();
		
		for (MeasurementField field : MeasurementField.values()) {
			list.add(new Field(field.toString(), field.isMandatory));
		}
		
		return Collections.unmodifiableList(list);
	}
	
	public enum MeasurementField {
		
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
}
