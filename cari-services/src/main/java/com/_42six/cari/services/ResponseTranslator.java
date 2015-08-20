package com._42six.cari.services;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
		this.recordList = reader.toRecordList(MeasurementRecord.FIELD_LIST, inputCsv);
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
		for (MeasurementRecord record : recordList) {
			Feature f = new Feature();
			Double lat = Double.parseDouble(record.get(MeasurementField.LATITUDE.toString()));
			Double lon = Double.parseDouble(record.get(MeasurementField.LONGITUDE.toString()));
			Point point = new Point(lon, lat);
			f.setGeometry(point);
			for (String key : record.keySet()) {
				f.setProperty(key, record.get(key));
			}
			featureCollection.add(f);
		}
		
		return featureCollection;
	}

	public FeatureCollection getFeatures(QueryRequest request) throws IOException, InvalidRequestException {
		this.requestValidator.validateQueryRequest(request);
		return this.getAllFeatures();
	}

	public Parameters getParameters() {
		return this.parameters;
	}	
}
