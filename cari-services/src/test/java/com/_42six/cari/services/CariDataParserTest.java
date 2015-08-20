package com._42six.cari.services;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class CariDataParserTest {
	
	private static CariDataParser cariDataParser;
	
	@BeforeClass
	public static void before() {
		cariDataParser = new CariDataParser();
	}
	
	@Ignore
	@Test
	public void createGeoJsonFile() throws IOException {
		cariDataParser.toGeoJson(new File("src/test/resources/csv/SampleData-FirstCut.csv"), new File("src/test/resources/json/SampleData-FirstCut-gjson.json"));
	}

}
