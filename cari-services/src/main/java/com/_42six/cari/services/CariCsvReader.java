package com._42six.cari.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com._42six.cari.commons.model.Field;
import com._42six.cari.commons.model.MeasurementRecord;
import com._42six.cari.commons.model.MeasurementRecord.MeasurementField;

public class CariCsvReader {
	
	//sample: 7/20/15 14:15
	private static final String DATE_INPUT_FORMAT = "MM/dd/yy HH:mm";

	public List<MeasurementRecord> toRecordList(List<Field> fieldList, InputStream inputStream) throws IOException, ParseException {
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(new InputStreamReader(inputStream));

		List<MeasurementRecord> recordList = new ArrayList<MeasurementRecord>();
		for (CSVRecord csvRecord : records) {
			boolean useRecord = true;
			MeasurementRecord record = null;
			
			String sampleDate = csvRecord.get(MeasurementField.SAMPLE_DATE_TIME);
			if (sampleDate == null || sampleDate.isEmpty()) {
				useRecord = false;
			}
			else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new SimpleDateFormat(DATE_INPUT_FORMAT).parse(sampleDate));
				record = new MeasurementRecord(cal.getTime());

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
			}
			if (useRecord && record != null) {
				recordList.add(record);
			}
		}
		return recordList;
	}
}
