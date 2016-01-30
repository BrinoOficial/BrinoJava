package br.com.Mateus.Brpp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Uploader {
	
	private static String[] boards = {"arduino:avr:uno","arduino:avr:mega","arduino:avr:mega:cpu=atmega2560","arduino:avr:nano", "arduino:avr:nano:cpu=atmega168","arduino:avr:diecimila"};
	
	public static boolean upload(String file, String com, int board) throws IOException {
		boolean success=false;
		boolean erro = false;
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd \"C:\\Program Files (x86)\\Arduino\" && arduino_debug --upload "
						+ file + " --board "+boards[board]+" --port "+com);
		System.out.println(com);
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
			BrppIDEFrame.LOG.append(line+"\n");
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
			BrppIDEFrame.LOG.append(line+"\n");
			BrppIDEFrame.LOG.invalidate();
			BrppIDEFrame.LOG.repaint();
			if (line.contains("O sketch usa"))
				return true;

		}
		return false;
	}
}
