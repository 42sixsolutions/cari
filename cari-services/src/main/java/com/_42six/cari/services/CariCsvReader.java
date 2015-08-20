package com._42six.cari.services;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com._42six.cari.commons.model.Field;
import com._42six.cari.commons.model.MeasurementRecord;

public class CariCsvReader {
	
	private final File csvFile;
	
	public CariCsvReader(File csvFile)  {
		this.csvFile = csvFile;
	}
	
	public List<MeasurementRecord> toRecordList(List<Field> fieldList) throws IOException {
		Reader in = new FileReader(csvFile);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);

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
