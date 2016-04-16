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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextArea;

import br.com.RatosDePC.Brpp.IDEui.BrppIDEFrame;

public class UploaderUtils {
	static JTextArea out = BrppIDEFrame.LOG;
	
	private static String[] boards = { "arduino:avr:uno", "arduino:avr:mega", "arduino:avr:mega:cpu=atmega2560",
			"arduino:avr:nano:cpu=atmega328", "arduino:avr:nano:cpu=atmega168", "arduino:avr:diecimila" };

	

	public static boolean upload(String file, String com, int board) throws IOException {
		boolean success = false;
		boolean erro = false;
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd Arduino && arduino_debug --upload " + file + " --board "
						+ boards[board] + " --port " + com);
		System.out.println(com);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		Timer t = new Timer();
		while (true) {
			UploaderUtils b = new UploaderUtils();
			line = r.readLine();

			if (line == null) {
				break;
			}
			if (line.contains("Verificando...") || line.contains("Verifying...")){
				out.append(line + " Isso pode levar algum tempo...\n");
				t.schedule(b.new ResponseTask(),0, 500);
			}
			out.append(line + "\n");
			out.update(out.getGraphics());

		}
		if (!erro) {
			success = true;
		}
		return success;
	}

	public static boolean compile(String file) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c",
				"cd Arduino && arduino_debug --verify " + file);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		Timer t = new Timer();
		while (true) {
			UploaderUtils b = new UploaderUtils();
			line = r.readLine();
			if (line == null) {
				break;
			}
			
			if (line.contains("Verificando...") || line.contains("Verifying...")){
				out.append(line + " Isso pode levar algum tempo...\n");
				t.schedule(b.new ResponseTask(),0, 500);
			}
			else out.append(line+"\n");
			out.update(out.getGraphics());
			if (line.contains("O sketch usa")){
				t.cancel();
				return true;
				
			}
				
			
		}
		t.cancel();
		return false;
	}
	class ResponseTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			out.append(".");
			out.update(out.getGraphics());
		}
		
	}
}
