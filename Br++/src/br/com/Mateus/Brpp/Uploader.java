package br.com.Mateus.Brpp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Uploader {
	
	public static boolean upload(String file, String com) throws IOException {
		boolean success=false;
		boolean erro = false;
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd \"C:\\Program Files (x86)\\Arduino\" && arduino_debug --upload "
						+ file + " --port "+com);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			
			if (line == null) {
				break;
			}
			System.out.println(line);
			BrppCompilerBorderFrame.LOG.append(line+"\n");
//			BrppCompilerFrame.setText(line);
//			if (line.contains("O sketch usa")) {
//				BrppCompilerFrame.setText("Compilado");
//				erro=false;
//			}
//			if (line.contains("can't open device")) {
//				success = false;
//				erro=true;
//				BrppCompilerFrame.setText("Compilado");
//			}
			
		}
		if (!erro) {
			success=true;
		}
		return success;
	}

	public static boolean compile(String file) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd \"C:\\Program Files (x86)\\Arduino\" && arduino_debug --verify "
						+ file);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
			BrppCompilerBorderFrame.LOG.append(line+"\n");
			if (line.contains("O sketch usa"))
				return true;

		}
		return false;
	}
}
