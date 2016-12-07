package br.com.RatosDePC.Brpp.compiler;

/**
 * Compilador Brino criado para transformar código Brpp em 
 * código arduino.
 * @author Mateus Berardo de Souza Terra
 * @contributors Rafael Mascarenhas Dal Moro
 * @version 2.4.3-beta
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

import br.com.RatosDePC.Brpp.IDEui.SouthPanel;
import br.com.RatosDePC.Brpp.Utils.FileUtils;
import br.com.RatosDePC.Brpp.Utils.JSONUtils;

public class BrppCompiler {

	private static String file;
	private static JTextArea out = SouthPanel.LOG;
	public static String version = "2.4.3-beta";

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
