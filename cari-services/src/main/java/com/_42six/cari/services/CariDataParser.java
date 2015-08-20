package com._42six.cari.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import com._42six.cari.commons.model.MeasurementRecord;
import com._42six.cari.commons.model.MeasurementRecord.MeasurementField;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CariDataParser {

	public void toGeoJson(File csvFile, File outputFile) throws IOException {
		CariCsvReader reader = new CariCsvReader(csvFile);
		List<MeasurementRecord> recordList = reader.toRecordList(MeasurementRecord.FIELD_LIST);

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

		new ObjectMapper().writeValue(outputFile, featureCollection);
	}

}
