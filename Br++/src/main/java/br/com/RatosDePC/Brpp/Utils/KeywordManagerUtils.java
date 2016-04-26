package br.com.RatosDePC.Brpp.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordManagerUtils {
//	private static String libraryString;
	private static StringBuilder wholekw = new StringBuilder(); // | keyword
																// |keyword |
																// ...
	private static StringBuilder wholekw2 = new StringBuilder();// | keyword
																// |keyword |
																// ...
	static ArrayList<String> keywords1 = new ArrayList<String>();
	static ArrayList<String> keywords2 = new ArrayList<String>();
	static ArrayList<String> libraries = new ArrayList<String>();
	static File f = new File(
			"C:\\Program Files (x86)\\Brino\\Arduino\\libraries");

	public static void processLibraries() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		for (String a : f.list()) {
			File lib = new File(s + "\\Arduino\\libraries\\" + a
					+ "\\keywords.txt");
			if (lib.exists()) {
				try {
					byte[] encoded = Files.readAllBytes(Paths.get(s
							+ "\\Arduino\\libraries\\" + a + "\\keywords.txt"));
					String keys = new String(encoded);
					sortKeywords(keys);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				libraries.add(a);
			}
//			for (String libraries : libraries) {
//				libraryString += "|" + libraries;
//			}
		}
	}

	private static void sortKeywords(String l) throws FileNotFoundException {
		// String[] line = l.split(" ");
		Pattern keys = Pattern.compile("[A-Za-z]*\tKEYWORD\\d");
		Matcher m = keys.matcher(l);
		while (m.find()) {
			String k = m.group();
			String[] K = k.split("\t");
			if (K[1].contains("1")){
				if(!keywords1.contains(K[0]))keywords1.add(K[0]);
			}
			if (K[1].contains("2"))
				keywords2.add(K[0]);
		}
		for (String keyword : keywords1) {
			if (keyword != null && !keyword.equals(""))
				wholekw.append("|" + keyword);
		}
		for (String keyword : keywords2) {
			if (keyword != null && !keyword.equals(""))
				wholekw2.append("|" + keyword);
		}
	}

	public static String getKey() {
		return wholekw.toString();
	}

	public static String getKeyTwo() {
		return wholekw2.toString();
	}
}