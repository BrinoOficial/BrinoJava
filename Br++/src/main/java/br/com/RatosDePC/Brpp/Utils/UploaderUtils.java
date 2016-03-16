package br.com.RatosDePC.Brpp.Utils;

/**
 * Classe utilizada para realizar a interface com a IDE do Arduino para compilação e upload
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors Rafael Mascarenhas Dal Moro
 * @version 5/2/2016
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.com.RatosDePC.Brpp.IDEui.BrppIDEFrame;

public class UploaderUtils {

	private static String[] boards = { "arduino:avr:uno", "arduino:avr:mega",
			"arduino:avr:mega:cpu=atmega2560", "arduino:avr:nano:cpu=atmega328",
			"arduino:avr:nano:cpu=atmega168", "arduino:avr:diecimila" };

	public static boolean upload(String file, String com, int board)
			throws IOException {
		boolean success = false;
		boolean erro = false;
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd \"C:\\Program Files (x86)\\Arduino\" && arduino_debug --upload "
						+ file + " --board " + boards[board] + " --port " + com);
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
			BrppIDEFrame.LOG.append(line + "\n");

		}
		if (!erro) {
			success = true;
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
			BrppIDEFrame.LOG.append(line + "\n");
			BrppIDEFrame.LOG.invalidate();
			BrppIDEFrame.LOG.revalidate();
			BrppIDEFrame.LOG.repaint();
			if (line.contains("O sketch usa"))
				return true;

		}
		return false;
	}
}
