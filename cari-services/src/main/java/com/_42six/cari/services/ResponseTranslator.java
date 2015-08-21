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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
		Map<Point, List<MeasurementRecord>> recordMap = new HashMap<Point, List<MeasurementRecord>>();
		
		//summarize points by rounded lat/lon
		for (final MeasurementRecord record : recordList) {

			Point point = new Point(record.getRoundedLon(), record.getRoundedLat());
			
			if (!recordMap.containsKey(point)) {
				recordMap.put(point, new ArrayList<MeasurementRecord>());
			}
			recordMap.get(point).add(record);
			/*
			Feature feature = createFeature(new ArrayList<MeasurementRecord>() {
				private static final long serialVersionUID = -5011036963234904340L;
			{ add(record); }});
			*/
		}
		
		//get feature for each point
		for (Point point : recordMap.keySet()) {
			Feature feature = createFeature(recordMap.get(point), point);
			
			featureCollection.add(feature);
		}
		
		return featureCollection;
	}
	
	public Feature createFeature(Collection<MeasurementRecord> recordList, Point point) {
		Feature feature = new Feature();
		feature.setGeometry(point);
		
		List<Map<String, String>> propertyMapList = new ArrayList<Map<String, String>>();
		Map<String, Object> summaryMap = new HashMap<String, Object>();
		Set<String> locationZoneSet = new HashSet<String>();
		Set<String> contaminantSet = new HashSet<String>();
		TreeMap<Calendar, Double> contaminantByDateMap = new TreeMap<Calendar, Double>();
		for (MeasurementRecord record : recordList) {
			//set geometry
			//Double lat = Double.parseDouble(record.get(MeasurementField.LATITUDE.toString()));
			//Double lon = Double.parseDouble(record.get(MeasurementField.LONGITUDE.toString()));
			//Point point = new Point(lon, lat);
			//feature.setGeometry(point);
			
			//set properties
			Map<String, String> propertyMap = new HashMap<String, String>();
			for (String key : record.keySet()) {
				propertyMap.put(key, record.get(key));
			}
			propertyMapList.add(propertyMap);
			
			//set summary
			String contaminantStr = record.get(MeasurementField.ANALYTE_NAME.toString());
			contaminantSet.add(contaminantStr);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(record.getSampleDate());
			
			//Contaminant contaminant = Contaminant.valueOf(contaminantStr);
			//String finalResult = record.get(MeasurementField.FINAL_RESULT.toString());
			//double contantminationValue = contaminant.getContaminationValue(Double.parseDouble(finalResult));
			//contaminantByDateMap.put(calendar, contantminationValue);
			
			//max per contaminant aggregated contamination level, most recent contamination level (aggregated for the last date)
			locationZoneSet.add(record.get(MeasurementField.LOCATION_ZONE.toString()));
			
			//TODO:
		}
		summaryMap.put("locationZones", locationZoneSet);
		//summaryMap.put("contaminants", contaminantSet); //remove per Ted
		
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
