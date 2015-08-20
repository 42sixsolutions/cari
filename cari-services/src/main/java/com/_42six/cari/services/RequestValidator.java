package com._42six.cari.services;

import com._42six.cari.services.model.InvalidRequestException;
import com._42six.cari.services.model.QueryRequest;

public class RequestValidator {
	
	public void validateQueryRequest(QueryRequest request) throws InvalidRequestException {
		if (request == null) {
			throw new InvalidRequestException("QueryRequest cannot be null");
		}
		//TODO: more validation?
	}

}
