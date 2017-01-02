package cc.brino.Brpp.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LanguageVersionUtils {

	private static JSONArray lings;

	public static String getVersion(String url) throws Exception {
		String URL ="http://brino.cc/brino/lib/ling/version.json";
//		pega a URL e conecta ao servidor
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
//		abre a stream para ler a tesposta do servidor
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String inputLine;
//		le linha a linha da resposta do servidor
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
//		fecha a stream
		in.close();
//		pega a resposta e converte em um objeto json
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.toString());
//		pega o array de linguas
		lings = (JSONArray) json.get("Linguas");
//		pega o iterador do array de linguas e cria um mapa para armazenar o par key-value
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = lings.iterator();
		Map<String, Integer> linguas = new TreeMap<String, Integer>();
//		itera pelo array e armazena os valores no map
		while (iterator.hasNext()) {
			JSONObject lingua = iterator.next();
			String linguaNome = (String) lingua.get("ling");
			int version = Integer.parseInt((String) lingua.get("version"));
			linguas.put(linguaNome, version);
		}
//		itera pelas linguas e printa as linguas
//		for (String key : linguas.keySet()) {
//			// Capturamos o valor a partir da chave
//			Integer value = linguas.get(key);
//			System.out.println(key + " = " + value);
//		}
		return response.toString();
//		return linguas;
	}

	public static void main(String[] args) throws Exception {
		String content = LanguageVersionUtils
				.getVersion("http://brino.cc/brino/lib/ling/version.json");
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(content);
		lings = (JSONArray) json.get("Linguas");
		Iterator<JSONObject> iterator = lings.iterator();
		Map<String, Integer> linguas = new TreeMap<String, Integer>();
		while (iterator.hasNext()) {
			JSONObject lingua = iterator.next();
			String linguaNome = (String) lingua.get("ling");
			int version = Integer.parseInt((String) lingua.get("version"));
			linguas.put(linguaNome, version);
		}
		for (String key : linguas.keySet()) {
			// Capturamos o valor a partir da chave
			Integer value = linguas.get(key);
			System.out.println(key + " = " + value);
		}
		System.out.println(linguas);
	}
}
