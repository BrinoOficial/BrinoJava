package br.com.RatosDePC.Brpp.Utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtils {

	private static JSONArray Keywords;
	private static JSONObject JSON;

	public static void config(String path) throws FileNotFoundException, IOException,
			ParseException {
		System.out.println("chamado");
		JSONParser parser = new JSONParser();

		Object obj = parser.parse(new FileReader(path + System.getProperty("file.separator")
				+ "lib" + System.getProperty("file.separator")
				+ "pt-br.json"));
		JSONObject jsonObject = (JSONObject) obj;
		Keywords = (JSONArray) jsonObject.get("Keywords");
	}

	public static JSONArray getKeywords() {
		return Keywords;
	}

}
