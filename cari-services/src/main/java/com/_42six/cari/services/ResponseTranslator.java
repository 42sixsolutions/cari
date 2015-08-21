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
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import com._42six.cari.commons.model.MeasurementRecord;
import com._42six.cari.commons.model.MeasurementRecord.MeasurementField;
import com._42six.cari.services.model.InvalidRequestException;
import com._42six.cari.services.model.Parameters;
import com._42six.cari.services.model.QueryRequest;
import com._42six.cari.services.model.QueryRequest.ViewType;

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
		Map<String, Double> maxFinalResultMap = new HashMap<String, Double>();
		for (MeasurementRecord record : recordList) {
			//calculate first/last dates
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
			
			//calculate max final result for each contaminant
			String name = record.get(MeasurementField.ANALYTE_NAME.toString());
			double finalResult = record.getFinalResultNormalized();
			//System.out.println(finalResult);
			if (!maxFinalResultMap.containsKey(name) || maxFinalResultMap.get(name) < finalResult) {
				maxFinalResultMap.put(name, finalResult);
			}
		}
		//System.out.println(maxFinalResultMap);
		return new Parameters(
				startDate.getTime(), 
				endDate.getTime(),
				maxFinalResultMap
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
		return toGeoJson(this.recordList, ViewType.latest);
	}

	public FeatureCollection toGeoJson(List<MeasurementRecord> recordList, ViewType viewType) throws IOException {
		
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
			Feature feature = createFeature(recordMap.get(point), point, viewType);
			
			featureCollection.add(feature);
		}
		
		return featureCollection;
	}
	
	public Feature createFeature(Collection<MeasurementRecord> recordList, Point point, ViewType viewType) {
		Feature feature = new Feature();
		feature.setGeometry(point);
		
		List<Map<String, Object>> propertyMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> summaryMap = new HashMap<String, Object>();
		Set<String> locationZoneSet = new HashSet<String>();
		//Set<String> contaminantSet = new HashSet<String>();
		//TreeMap<Calendar, Double> contaminantByDateMap = new TreeMap<Calendar, Double>();
		TreeMap<Calendar, SortedSet<Double>> weightedContaminationByDate = new TreeMap<Calendar, SortedSet<Double>>();
		for (MeasurementRecord record : recordList) {
			//set geometry
			//Double lat = Double.parseDouble(record.get(MeasurementField.LATITUDE.toString()));
			//Double lon = Double.parseDouble(record.get(MeasurementField.LONGITUDE.toString()));
			//Point point = new Point(lon, lat);
			//feature.setGeometry(point);
			
			locationZoneSet.add(record.get(MeasurementField.LOCATION_ZONE.toString()));
			
			//set summary
			String contaminant = record.get(MeasurementField.ANALYTE_NAME.toString());
			//contaminantSet.add(contaminant);
			
			
			
			//set events
			Map<String, Object> propertyMap = new HashMap<String, Object>();
			propertyMapList.add(propertyMap);
			//add all properties to table
			for (String key : record.keySet()) {
				propertyMap.put(key, record.get(key));
			}
			//set weighted value
			//System.out.println(this.parameters.getMaxFinalResult().get(contaminant));
			double weightedContaminationValue = record.getWeightedContaminantValue(this.parameters.getMaxFinalResult().get(contaminant));
			propertyMap.put("weightedContaminationValue", weightedContaminationValue);
			
			
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(record.getSampleDate());
			
			if (!weightedContaminationByDate.containsKey(calendar)) {
				weightedContaminationByDate.put(calendar, new TreeSet<Double>());
			}
			
			SortedSet<Double> contaminantSet = weightedContaminationByDate.get(calendar);
			contaminantSet.add(weightedContaminationValue);
			
			//Contaminant contaminant = Contaminant.valueOf(contaminantStr);
			//String finalResult = record.get(MeasurementField.FINAL_RESULT.toString());
			//double contantminationValue = contaminant.getContaminationValue(Double.parseDouble(finalResult));
			//contaminantByDateMap.put(calendar, contantminationValue);
			
			//max per contaminant aggregated contamination level, most recent contamination level (aggregated for the last date)
			
			//value / max = contamination value (for each contaminant type)
			//total contamination value = SUM (contamination values)
			//TODO:
		}
		summaryMap.put("locationZones", locationZoneSet);
		//summaryMap.put("contaminants", contaminantSet); //remove per Ted
		summaryMap.put("contaminationValue", calculateContaminationValue(weightedContaminationByDate, viewType));	
		
		feature.setProperty("events", propertyMapList);
		feature.setProperty("summary", summaryMap);
		//TODO: tooltips
		
		return feature;
	}
	
	private double calculateContaminationValue(
			TreeMap<Calendar, SortedSet<Double>> weightedContaminationByDate,
			ViewType viewType) {
		switch (viewType) {
		case highest : {
			double highest = 0;
			for (SortedSet<Double> set : weightedContaminationByDate.values()) {
				if (set.last() > highest) {
					highest = set.last();
				}
			}
			return highest;
		}
		case latest : {
			SortedSet<Double> contaminationValueSet = weightedContaminationByDate.get(weightedContaminationByDate.lastKey());
			return contaminationValueSet.last();
		}
		default : {
			return 0;
		}
		}
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
		
		return toGeoJson(returnList, request.getViewType());
	}

	public Parameters getParameters() {
		return this.parameters;
	}	
}
