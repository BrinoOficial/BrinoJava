package br.com.RatosDePC.Brpp.Utils;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONUtils {

	private static JSONArray Keywords;
	private static JSONObject JSON;

	public static void config() throws FileNotFoundException, IOException, ParseException {
		System.out.println("chamado");
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(
				"C:\\Users\\Mateus Terra\\Documents\\GitHub\\IDELang\\lib\\pt-br.json"));
		JSONObject jsonObject = (JSONObject) obj;
		Keywords = (JSONArray) jsonObject.get("Keywords");
	}
	
	public static JSONArray getKeywords(){
		return Keywords;
	}

}
