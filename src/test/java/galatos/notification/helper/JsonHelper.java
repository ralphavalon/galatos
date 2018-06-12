package galatos.notification.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonHelper {
	
	private static final boolean MATCH_EVERY_JSON_FIELDS = true;
	
	public static void assertJsonAssignable(String json, Class<?> assignableClass) {
		try {
			new ObjectMapper().readValue(json, assignableClass);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Cannot parse this json to: " + assignableClass);
		}
	}
	
	public static <T> T parse(String json, Class<T> assignableClass) {
		try {
			return new Gson().fromJson(json, assignableClass);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not parse json to: " + assignableClass);
		}
	}
	
	private static JsonObject getJsonFileAsJsonObject(String file, String type) {
		String filename = "/json/" + type + "/" + file;
		InputStream jsonFile = JsonHelper.class.getResourceAsStream (filename);
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(jsonFile);
		} catch (Exception e) {
			throw new RuntimeException("Cannot read/find file: '" + filename + "'");
		}
		BufferedReader br = new BufferedReader(in);
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(br).getAsJsonObject();
		assertNotNull(json);
		return json;
	}
	
	public static JsonObject getRequestFileAsJsonObject(String file) {
		return getJsonFileAsJsonObject(file, "request");
	}
	
	public static JsonObject getResponseFileAsJsonObject(String file) {
		return getJsonFileAsJsonObject(file, "response");
	}
	
	public static void assertJson(String expectedJson, String responseJson) {
		assertJson(expectedJson, responseJson, MATCH_EVERY_JSON_FIELDS);
	}
	
	public static void assertJson(String expectedJson, String responseJson, boolean matchEveryJsonFields) {
		JSONAssert.assertEquals(expectedJson, responseJson, matchEveryJsonFields);
	}

}
