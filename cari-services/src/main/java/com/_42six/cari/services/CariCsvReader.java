package com._42six.cari.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com._42six.cari.commons.model.Field;
import com._42six.cari.commons.model.MeasurementRecord;

public class CariCsvReader {
	
	public List<MeasurementRecord> toRecordList(List<Field> fieldList, InputStream inputStream) throws IOException {
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(new InputStreamReader(inputStream));

		List<MeasurementRecord> recordList = new ArrayList<MeasurementRecord>();
		for (CSVRecord csvRecord : records) {
			boolean useRecord = true;
			MeasurementRecord record = new MeasurementRecord();
		    for (Field field : fieldList) {
		    	String value = csvRecord.get(field.getFieldName());
		    	if (value != null && !value.isEmpty()) {
		    		record.put(field.getFieldName(), value);
		    	}
		    	else if (field.isMandatory()) {
		    		useRecord = false;
		    		break;
		    	}
		    }
		    if (useRecord) {
		    	recordList.add(record);
		    }
		}
		return recordList;
	}
}
