package cc.brino.Brpp.Utils;

/*
 Copyright (c) 2016 StarFruitBrasil

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/**
 * 
 * Classe para controle das linguas
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors 
 * @version 10/1/2017
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LanguageVersionUtils {

	private static JSONArray lings;

	public static Map<String, Integer> getVersion()
			throws MalformedURLException, IOException, ParseException {
		String URL = "http://brino.cc/brino/lib/ling/version.json";
		// pega a URL e conecta ao servidor
		URL website = new URL(URL);
		URLConnection connection = website.openConnection();
		// abre a stream para ler a tesposta do servidor
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		// le linha a linha da resposta do servidor
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		// fecha a stream
		in.close();
		// pega a resposta e converte em um objeto json
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject) parser.parse(response.toString());
		// pega o array de linguas
		lings = (JSONArray) json.get("Linguas");
		// pega o iterador do array de linguas e cria um mapa para armazenar o
		// par key-value
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = lings.iterator();
		Map<String, Integer> linguas = new TreeMap<String, Integer>();
		// itera pelo array e armazena os valores no map
		while (iterator.hasNext()) {
			JSONObject lingua = iterator.next();
			String linguaNome = (String) lingua.get("ling");
			int version = Integer.parseInt((String) lingua.get("version"));
			linguas.put(linguaNome, version);
		}
		// itera pelas linguas e printa as linguas
		// for (String key : linguas.keySet()) {
		// Capturamos o valor a partir da chave
		// Integer value = linguas.get(key);
		// System.out.println(key + " = " + value);
		// }
		// return response.toString();
		return linguas;
	}

	public static void updateLanguages(String path)
			throws MalformedURLException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		// baixa a lista de linguas do servidor e suas versões
		Map<String, Integer> content = LanguageVersionUtils.getVersion();
		// cria um file com o diretorio dos json's
		File f = new File(path + System.getProperty("file.separator") + "lib");
		// itera pelos arquivos da pasta
		for (String a : f.list()) {
			// verifica se e um arquivo json
			if (a.contains(".json")) {
				// faz o parse do json da lingua
				Object obj = parser.parse(new FileReader(path
						+ System.getProperty("file.separator") + "lib"
						+ System.getProperty("file.separator") + a));
				JSONObject jsonObject = (JSONObject) obj;
				// recupera a versao do arquivo
				int version = Integer.parseInt((String) jsonObject
						.get("version"));
				// pega o nome da lingua tirando a extensao
				String lang = a.replace(".json", "");
				// verifica a versao remota para a lingua atual
				int remoteVersion = content.get(lang);
				// compara as versoes
				if (version < remoteVersion) {
					String url = "http://brino.cc/brino/lib/ling/" + lang + "/"
							+ a;
					URL website = new URL(url);
					String outputFileName = path
							+ System.getProperty("file.separator") + "lib"
							+ System.getProperty("file.separator") + a;
					// baixa a versão mais nova do site e salva
					try (InputStream inputStream = website.openStream();
							ReadableByteChannel readableByteChannel = Channels
									.newChannel(inputStream);
							FileOutputStream fileOutputStream = new FileOutputStream(
									outputFileName)) {
						fileOutputStream.getChannel().transferFrom(
								readableByteChannel, 0, 1 << 24);
					}
				}
				JSONUtils.config(path);
			}
		}
	}

	public static void main(String[] args) throws Exception {
	}
}
