package br.com.Mateus.Brpp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Uploader {
	public static void upload(String file) throws IOException {
		boolean erro=false;
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd \"C:\\Program Files (x86)\\Arduino\" && arduino_debug --upload "
						+ file + " --port COM3");
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
			if (line.contains("O sketch usa"))
				BrppCompilerFrame.setText("Compilado");
			
			if (line.contains("can't open device")){
				erro=true;
				BrppCompilerFrame.setText("Compilado");
			}
			if (!erro){
				BrppCompilerFrame.setText("Compilado \nCarregado");
			}
		}
	}

}
