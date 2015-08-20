package com._42six.cari.web;

import java.io.IOException;
import java.text.ParseException;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Class to handle the REST API endpoints
 */
@Path("/")
@Singleton
public class CariResource extends CommonResource {

	public CariResource() throws JsonParseException, JsonMappingException,
			IOException, ParseException {
		super();
	}
	
    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public String helloWorld(
    		//@PathParam("drugName") String drugName
    		) {
    	//return responseTranslator.get();
    	return "{ \"Hello\" : \"world\" }";
    }

}
