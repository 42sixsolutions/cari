package com._42six.cari.web;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import com._42six.cari.services.ResponseTranslator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


/**
 * Common resource to be used for sharing the ResponseTranslator
 */
public abstract class CommonResource {

	protected ResponseTranslator responseTranslator;

	public CommonResource() throws JsonParseException, JsonMappingException, IOException, ParseException {
		InputStream inputCsv = this.getClass().getClassLoader().getResourceAsStream("/csv/epaEvents.csv");
		this.responseTranslator = ResponseTranslator.getInstance(
				inputCsv);
	}
}
