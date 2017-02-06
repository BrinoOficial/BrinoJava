package cc.brino.Brpp.Pref;

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
* Gerenciador de preferencias
* 
* @author Mateus Berardo de Souza Terra
* @contributors 
* @version 2/3/2017
*/

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Formatter;

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
 * Classe para controle das preferências
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors 
 * @version 10/1/2017
 */

import java.util.TreeMap;

public class PrefManager {

	private static String[] keys = { "placa.index", "placa.nome","porta" };

	private static TreeMap<String, String> preferences = new TreeMap<String, String>();

	public static String getPref(String key) {
		return preferences.get(key);
	}

	public static void setPref(String key, String value) {
		preferences.put(key, value);
	}

	public static void setPrefs() throws IOException {
		if (new File("." + System.getProperty("file.separator") + "brino.prefs").exists()) {
			byte[] encoded = Files.readAllBytes(Paths.get("." + System.getProperty("file.separator") + "brino.prefs"));
			String file = new String(encoded);
			String[] list = file.split("\n");
			for (String prefs : list) {
				String[] pref = prefs.split("=");
				pref[0].trim();
				pref[1].trim();
				preferences.put(pref[0], pref[1]);
			}
		} else {
			// TODO criar o arquivo de preferencias
			String prefs;
			Formatter output = new Formatter("." + System.getProperty("file.separator") + "brino.prefs");
			for (String key : keys) {
				String value="null";
				output.format("%s=%s\r\n",key,value);
			}
		}

	}
}
