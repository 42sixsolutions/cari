package com._42six.cari.services;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import com._42six.cari.commons.model.MeasurementRecord;
import com._42six.cari.commons.model.MeasurementRecord.MeasurementField;
import com._42six.cari.services.model.InvalidRequestException;
import com._42six.cari.services.model.Parameters;
import com._42six.cari.services.model.QueryRequest;

public class ResponseTranslator {
	
	private static ResponseTranslator instance;
	private RequestValidator requestValidator;
	
	private List<MeasurementRecord> recordList;
	private final Parameters parameters;
	
	public ResponseTranslator(InputStream inputCsv) throws IOException, ParseException {
		CariCsvReader reader = new CariCsvReader();
		this.recordList = Collections.unmodifiableList(reader.toRecordList(MeasurementRecord.FIELD_LIST, inputCsv));
		this.requestValidator = new RequestValidator();
		this.parameters = createParameters();
	}

	private Parameters createParameters() {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.setTimeInMillis(Long.MAX_VALUE);
		endDate.setTimeInMillis(Long.MIN_VALUE);
		for (MeasurementRecord record : recordList) {
			Date date = record.getSampleDate();
			if (date != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				if (cal.before(startDate)) {
					startDate.setTime(date);
				}
				if (cal.after(endDate)) {
					endDate.setTime(date);
				}
			}
		}
		
		return new Parameters(
				startDate.getTime(), 
				endDate.getTime()
				);
	}

	public static ResponseTranslator getInstance(InputStream inputCsv) throws IOException, ParseException {
		if(instance == null) {
			instance = new ResponseTranslator(
					inputCsv);
		}
		return instance;
	}
	
	public FeatureCollection getAllFeatures() throws IOException {
		return toGeoJson(this.recordList);
	}

	public FeatureCollection toGeoJson(List<MeasurementRecord> recordList) throws IOException {
		
		FeatureCollection featureCollection = new FeatureCollection();
		for (final MeasurementRecord record : recordList) {
			
			//TODO: summary logic here
			
			Feature feature = createFeature(new ArrayList<MeasurementRecord>() {
				private static final long serialVersionUID = -5011036963234904340L;
			{ add(record); }});
			
			featureCollection.add(feature);
		}
		
		return featureCollection;
	}
	
	public Feature createFeature(Collection<MeasurementRecord> recordList) {
		Feature feature = new Feature();
		List<Map<String, String>> propertyMapList = new ArrayList<Map<String, String>>();
		Map<String, String> summaryMap = new HashMap<String, String>();
		for (MeasurementRecord record : recordList) {
			//set geometry
			Double lat = Double.parseDouble(record.get(MeasurementField.LATITUDE.toString()));
			Double lon = Double.parseDouble(record.get(MeasurementField.LONGITUDE.toString()));
			Point point = new Point(lon, lat);
			feature.setGeometry(point);
			
			//set properties
			Map<String, String> propertyMap = new HashMap<String, String>();
			for (String key : record.keySet()) {
				propertyMap.put(key, record.get(key));
			}
			propertyMapList.add(propertyMap);
			
			//set summary
			summaryMap.put("Contaminant", record.get(MeasurementField.ANALYTE_NAME.toString()));
			//TODO:
		}
		feature.setProperty("events", propertyMapList);
		feature.setProperty("summary", summaryMap);
		//TODO: tooltips
		
		return feature;
	}

	public FeatureCollection getFeatures(QueryRequest request) throws IOException, InvalidRequestException {
		this.requestValidator.validateQueryRequest(request);
		
		List<MeasurementRecord> returnList = new ArrayList<MeasurementRecord>();
		
		for (MeasurementRecord record : this.recordList) {
			boolean keepRecord = true;
			
			//if (keepRecord && request.get)
			
			if (keepRecord) {
				returnList.add(record);
			}
		}
		
		return toGeoJson(returnList);
	}

	public Parameters getParameters() {
		return this.parameters;
	}	
}
