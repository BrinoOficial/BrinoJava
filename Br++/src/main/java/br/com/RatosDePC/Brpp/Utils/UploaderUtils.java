package br.com.RatosDePC.Brpp.Utils;

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
 * Classe utilizada para realizar a interface com a IDE do Arduino para compila��o e upload
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

import br.com.RatosDePC.Brpp.IDEui.SouthPanel;
import br.com.RatosDePC.SerialMonitor.SerialMonitor;

public class UploaderUtils {
	static JTextArea out = SouthPanel.LOG;

	private static String[] boards = { "arduino:avr:uno", "arduino:avr:mega:cpu=atmega1280",
			"arduino:avr:mega:cpu=atmega2560", "arduino:avr:megaADK", "arduino:avr:nano:cpu=atmega328",
			"arduino:avr:nano:cpu=atmega168", "arduino:avr:diecimila:cpu=atmega328",
			"arduino:avr:diecimila:cpu=atmega168", "arduino:avr:leonardo", "arduino:avr:micro", "arduino:avr:esplora",
			"arduino:avr:mini:cpu=atmega328", "arduino:avr:mini:cpu=atmega168", "arduino:avr:ethernet",
			"arduino:avr:fio", "arduino:avr:bt:cpu=atmega328", "arduino:avr:bt:cpu=atmega168", "arduino:avr:LilyPadUSB",
			"arduino:avr:lilypad:cpu=atmega328", "arduino:avr:lilypad:cpu=atmega168",
			"arduino:avr:pro:cpu=16MHzatmega328", "arduino:avr:pro:cpu=8MHzatmega328",
			"arduino:avr:pro:cpu=16MHzatmega168", "arduino:avr:pro:cpu=8MHzatmega168", "arduino:avr:gemma",
			"odb:avr:odb" };

	public static void upload(String file, String com, int board) throws IOException {
		if (SerialMonitor.isOpen)
			try {
				CommPortUtils.closePort();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		String port = " --port " + com;
		ProcessBuilder builder;
		if (System.getProperty("os.name").contains("Windows")) {
			String proc = "cd Arduino && arduino_debug --upload " + file + " --board " + boards[board]
					+ (com == null ? "" : port);
			System.out.println(proc);
			builder = new ProcessBuilder("cmd.exe", "/c", proc);
		} else {
			String proc = "cd Arduino && ./arduino --upload " + file + " --board " + boards[board]
					+ (com == null ? "" : port);
			System.out.println(proc);
			builder = new ProcessBuilder("/bin/bash", "-c", proc);
		}
		builder.redirectErrorStream(true);
		processar(builder,board);
		if (SerialMonitor.isOpen)
			CommPortUtils.openPort(com);
	}

	public static void compile(String file) throws IOException {
		ProcessBuilder builder;
		System.out.println(System.getProperty("os.name"));
		if (System.getProperty("os.name").contains("Windows")) {
			builder = new ProcessBuilder("cmd.exe", "/c", "cd Arduino && arduino_debug --verify " + file);
		} else {
			builder = new ProcessBuilder("/bin/bash", "-c", "cd Arduino && ./arduino --verify " + file);
		}
		builder.redirectErrorStream(true);
		processar(builder,-1);
	}

	private static void processar(ProcessBuilder builder,int board) throws IOException {
		Process p = builder.start();
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		out.setText("");
		SouthPanel.LogPanel.getVerticalScrollBar().setValue(0);
		out.update(out.getGraphics());
		Timer t = new Timer();
		
		while (true) {
			UploaderUtils b = new UploaderUtils();
			line = r.readLine();
			if (line == null) {
				break;
			}
			if (line.contains("Verificando") || line.contains("Verifying")) {
				if(board!=-1){
					if (boards[board].contains("odb")) out.append("Reinicie ou conecte sua placa");
				}
				out.append(line + " Isso pode levar algum tempo...\n");
				t.schedule(b.new ResponseTask(), 0, 500);
			} else
				out.append(line + "\n");
			if (line.contains("O sketch usa") | line.contains("The sketch")) {
				t.cancel();
			}
			out.update(out.getGraphics());
		}
		t.cancel();
	}

	class ResponseTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			out.append(".");
			out.update(out.getGraphics());
		}
	}
}
