package cc.brino.Brpp.Utils;

/*
 * Copyright (c) 2016 StarFruitBrasil
 * 
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without
 * limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice
 * shall be included in all copies or substantial portions
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
/**
 * 
 * Classe para controle dos JSON's
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors
 * @version 10/1/2017
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import cc.brino.Brpp.Pref.PrefManager;


public class JSONUtils {

	private static JSONArray Keywords;
	
			

	public static void config(String path) throws FileNotFoundException,
			IOException,
			ParseException {
		JSONParser parser = new JSONParser();
		final Path file = Paths.get(path, "lib", "ling", PrefManager.getPref("lingua")+".json");
		Object obj = parser.parse(new FileReader(file.toFile()));
		JSONObject jsonObject = (JSONObject) obj;
		Keywords = (JSONArray) jsonObject.get("Keywords");
	}

	public static JSONArray getKeywords() {
		return Keywords;
	}
}
