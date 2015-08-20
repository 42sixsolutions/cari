package com._42six.cari.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import com._42six.cari.commons.model.MeasurementRecord;
import com._42six.cari.commons.model.MeasurementRecord.MeasurementField;

public class ResponseTranslator {
	
	private static ResponseTranslator instance;
	
	private List<MeasurementRecord> recordList;
	
	public ResponseTranslator(InputStream inputCsv) throws IOException {
		CariCsvReader reader = new CariCsvReader();
		this.recordList = reader.toRecordList(MeasurementRecord.FIELD_LIST, inputCsv);
		System.out.println(this.recordList.size());
	}

	public static ResponseTranslator getInstance(InputStream inputCsv) throws IOException {
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
	
}
