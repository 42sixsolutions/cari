package com._42six.cari.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.geojson.FeatureCollection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com._42six.cari.services.model.QueryRequest.ViewType;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ResponseTranslatorTest {
	
	private static ResponseTranslator responseTranslator;
	
	@BeforeClass
	public static void before() throws FileNotFoundException, IOException, ParseException {
		responseTranslator = ResponseTranslator.getInstance(new FileInputStream("src/test/resources/csv/SampleData-SecondCut.csv"));
	}
	
	@Test
	public void testGetAllFeatures() throws IOException {
		FeatureCollection featureCollectionLatest = responseTranslator.getAllFeatures(ViewType.latest);
		Assert.assertEquals(113, featureCollectionLatest.getFeatures().size());
		
		FeatureCollection featureCollectionLast = responseTranslator.getAllFeatures(ViewType.highest);
		Assert.assertEquals(113, featureCollectionLast.getFeatures().size());
	}
	
	@Ignore
	@Test
	public void createGeoJsonForAllFeatures() throws IOException {
		FeatureCollection featureCollection = responseTranslator.getAllFeatures(ViewType.latest);
		new ObjectMapper().writeValue(new File("src/test/resources/json/SampleData-FirstCut-gjson.json"), featureCollection);
	}

}
