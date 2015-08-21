package com._42six.cari.web;

import java.io.IOException;
import java.text.ParseException;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.geojson.FeatureCollection;

import com._42six.cari.services.model.InvalidRequestException;
import com._42six.cari.services.model.Parameters;
import com._42six.cari.services.model.QueryRequest;
import com._42six.cari.services.model.QueryRequest.ViewType;
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
	
	@POST
    @Path("/query")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FeatureCollection query(
    		QueryRequest request
    		) throws IOException, InvalidRequestException {
    	return responseTranslator.getFeatures(request);
    }
	
	@GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public FeatureCollection getAllFeatures(
    		@DefaultValue("latest") @QueryParam("viewType") ViewType viewType
    		) throws IOException, InvalidRequestException {
    	return responseTranslator.getAllFeatures(viewType);
    }
	
	@GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Parameters list(
    		) {
    	return responseTranslator.getParameters();
    }
	
    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public String helloWorld(
    		) {
    	return "{ \"Hello\" : \"world\" }";
    }

}
