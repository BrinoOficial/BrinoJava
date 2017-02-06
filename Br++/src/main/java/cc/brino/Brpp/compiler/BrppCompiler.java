package cc.brino.Brpp.compiler;

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
 * Compilador Brino criado para transformar codigo Brpp em 
 * codigo arduino.
 * @author Mateus Berardo de Souza Terra
 * @contributors Rafael Mascarenhas Dal Moro
 * @version 2.5.0-beta
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.swing.JTextArea;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cc.brino.Brpp.IDEui.SouthPanel;
import cc.brino.Brpp.Utils.FileUtils;
import cc.brino.Brpp.Utils.JSONUtils;

public class BrppCompiler {

	private static String file;
	private static JTextArea out = SouthPanel.LOG;
	public static String version = "2.5.1-beta";

	public static boolean compile(String path) {
		setFile(FileUtils.getBrinodirectory()
				+ System.getProperty("file.separator") + "Arduino");
		setFile(getFile().concat(
				path.substring(
						path.lastIndexOf(System.getProperty("file.separator")),
						path.length() - 5)));
		setFile(getFile().concat(
				path.substring(
						path.lastIndexOf(System.getProperty("file.separator")),
						path.length() - 4)));
		setFile(getFile().concat("ino"));
		File ino = new File(getFile());
		if (!ino.exists()) {
			try {
				ino.getParentFile().mkdirs();
				ino.createNewFile();
			} catch (IOException e) {

			}
		}
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			String code = new String(encoded);
			JSONArray Keywords = JSONUtils.getKeywords();
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = Keywords.iterator();
			while (iterator.hasNext()) {
				JSONObject key = iterator.next();
				String arg = (String) key.get("arg");
				if (arg.equals("false")) {
					code = code.replace((String) key.get("translate"),
							(String) key.get("arduino"));
				} else {
					code = code.replaceAll((String) key.get("translate"),
							(String) key.get("arduino"));
				}
			}

			try (FileWriter file = new FileWriter(getFile())) {
				file.write(code);
			}
			System.out.println(code);
			return true;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public static String getFile() {
		return file;
	}

	private static void setFile(String file) {
		BrppCompiler.file = file;
	}
}
