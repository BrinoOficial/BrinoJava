package br.com.RatosDePC.Brpp.compiler;

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
 * Compilador Brino criado para transformar código arduino em 
 * código Brpp.
 * @author Mateus Berardo de Souza Terra
 * @contributors Rafael Mascarenhas Dal Moro 
 * @version 2.3.13-beta
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import br.com.RatosDePC.Brpp.IDEui.SouthPanel;
import br.com.RatosDePC.Brpp.Utils.FileUtils;

public class CppCompiler {

	private static Map<String, String> variaveis = new HashMap<String, String>();
	private static Formatter program;
	private static String file;
	private static JTextArea out = SouthPanel.LOG;
	public static String version = "2.4.0";

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
			program = new Formatter(getFile());
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			String liness = new String(encoded);
			String[] lines = liness.split("\n");
			if (proccess(lines)) {
				return true;
			} else
				return false;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public static boolean proccess(String[] lines) {
		boolean comment = false;
		boolean linecomment = false;
		for (String line : lines) {
			String command = line;
			linecomment = false;
			if (command.contains("//"))
				linecomment = true;
			if (command.contains("/*"))
				comment = true;
			if (command.contains("*/"))
				comment = false;
			if (command.contains("#define"))
				if (command.indexOf("#define") < command.indexOf("//")
						|| !linecomment)
					command = command.replace("#define ", "definir ");
			if (command.contains("#include <")
					&& (command.indexOf("#include <") < command.indexOf("//") || !linecomment)) {
				command = command.replace("#include <", "usar ");
				command = command.replace(".h>", "");
			}
			if (command.contains("LiquidCrystal"))
				command = command.replace("LiquidCrystal", "LCD");
			if (command.contains("EEPROM"))
				command = command.replace("EEPROM", "Memoria");
			if (command.contains("usar SD"))
				command = command.replace("\r\n#include <SPI.h>\r\n", "");
			if (command.contains("Wire"))
				command = command.replace("Wire", "I2C");
			if ((command.contains(";") || command.contains("{") || command
					.contains("}")) && comment == false) {

				if (command.contains("void setup"))
					command = command.replace("void setup","Configuracao");
				if (command.contains("void loop"))
					command = command.replace("void loop", "Principal");
				if (command.contains("tone"))
					command = command.replace("tone", "soar");
				if (command.contains("noTone"))
					command = command.replace("noTone", "pararSoar");
				if (command.contains("map"))
					command = command.replace("map", "proporcionar");
				if (command.contains("delay("))
					command = command.replace("delay", "esperar");
				if (command.contains("void"))
					command = command.replace("void", "SemRetorno");
				if (command.contains("else")) {
					command = command.replace("else", "senao");
				}
				if (command.contains("if (") || command.contains("if(")) {
					command = command.replace("if(", "se(");
					command = command.replace("if (", "se(");
					if (command.contains("=")
							&& !((command.contains("==")
									|| command.contains("<")
									|| command.contains(">") || command
										.contains("!")))) {
						System.out.println(line);
						out.append("\nHá um erro em seu código!\n" + line);
						out.update(out.getGraphics());
						return false;
					}
				}
				if (command.contains("for (") || command.contains("for(")) {
					command = command.replace("for(", "para(");
					command = command.replace("for (", "para(");
					if (command.contains("=")
							&& !((command.contains(";")
									|| command.contains("==")
									|| command.contains("<") || command
										.contains(">")))) {
						System.out.println(line);
						out.append("\nHá um erro em seu código!\n" + line);
						out.update(out.getGraphics());
						return false;
					}
				}
				if (command.contains("while")) {
					command = command.replace("while", "enquanto");
					if (command.contains("=")
							&& !((command.contains("==")
									|| command.contains("<")
									|| command.contains(">") || command
										.contains("!")))) {
						System.out.println(line);
						out.append("\nHá um erro em seu código!\n" + line);
						out.update(out.getGraphics());
						return false;
					}
				}

				if (command.contains("int") || command.contains("String")
						|| command.contains("boolean")
						|| command.contains("true")
						|| command.contains("false")
						|| command.contains("char")) {
					while (command.contains("int")
							|| command.contains("String")
							|| command.contains("boolean")
							|| command.contains("true")
							|| command.contains("false")
							|| command.contains("char")) {

						command = addVar(command, command.contains("="));
					}
				}
				if (command.contains("pinMode")) {
					command = command.replace("pinMode", "Pino.definirModo");
					command = command.replace("OUTPUT", "Saida");
					command = command.replace("INPUT", "Entrada");
				}
				if (command.contains("analogRead(")) {
					command = command
							.replace("Pino.lerAnalogico", "Pino.lerAnalogico");
					command = command.replace("A", "Analogico.");
				}
				if (command.contains("digitalRead(")) {
					command = command.replace("digitalRead", "Pino.ler");
					command = command.replace(".ler(", ".ler(Digital.");
				}
				if (command.contains("analogWrite(")) {
					command = command.replace("analogWrite",
							"Pino.escreverAnalogico");
					command = command.replace("A", "Analogico.");
				}
				if (command.contains("shiftOut(")) {
					command = command.replace("enviarBinario", "enviarBinario");
					command = command.replace("MSBFIRST", "Esquerdo");
					command = command.replace("LSBFIRST", "Direito");
				}
				if (command.contains("pulseIn(")) {
					command = command.replace("pulseIn(", "Pino.pulsoEm(");
				}
				if (command.contains("digitalWrite(")) {
					command = command.replace("digitalWrite", "Pino.escrever");
					command = command.replace(".escrever(", ".escrever(Digital.");
				}
				if (command.contains("HIGH"))
					command = command.replace("HIGH", "Ligado");
				if (command.contains("LOW"))
					command = command.replace("LOW", "Desligado");
				if (command.contains(" && ") || command.contains(" || ")) {
					command = command.replace(" && ", " e ");
					command = command.replace(" || ", " ou ");
				}
				if (command.contains("return"))
					command = command.replace("return", "responder");
				if (command.contains("//")) {
					command = command.replace(
							command.substring(command.indexOf("//")),
							line.substring(line.indexOf("//")));
				}
				if (command.contains(".attach")) {
					command = command.replace(".attach", ".conectar(Digital.");
				}
				if (command.contains("attachInterrupt(digitalPinToInterrupt")) {
					command = command.replace("attachInterrupt(digitalPinToInterrupt(",
							"conectarInterruptor(");
					command = command.replaceFirst("),", ",");
					command = command.replace("CHANGE", "Mudando");
					command = command.replace("RISING", "Ligando");
					command = command.replace("FALLING", "Desligando");
				}
				if (command.contains("detachInterrupt(digitalPinToInterrupt"))
					command = command.replace("detachInterrupt(digitalPinToInterrupt(",
							"desconectarInterruptor(");
				if (command.contains("interrupts"))
					command = command.replace("interrupts",
							"ligarInterruptores");
				if (command.contains("noInterrupts"))
					command = command.replace("noInterrupts",
							"desligarInterruptores");
				if (command.contains(".exists("))
					command = command.replace("exists", "existe");
				if (command.contains(".mkdir("))
					command = command.replace("mkdir", "criarPasta");
				if (command.contains(".rmdir("))
					command = command.replace("rmdir", "removerPasta");
				if (command.contains(".open("))
					command = command.replace("open", "abrir");
				if (command.contains(".remove("))
					command = command.replace("remove", "remover");
				if (command.contains("File"))
					command = command.replace("File", "Arquivo");
				if (command.contains(".close("))
					command = command.replace(".close(", ".fechar(");
				if (command.contains("SD.print"))
					command = command.replace("SD.print", ".gravar");
				if (command.contains(".flush("))
					command = command.replace("flush", "descarregar");
				if (command.contains("FILE_WRITE"))
					command = command.replace("FILE_WRITE", "ArquivoGravar");
				if (command.contains("shiftOut"))
					command = command.replace("shiftOut", "enviarBinario");
				if (command.contains("write"))
					command = command.replace("write", "escreverAngulo");
				if (command.contains("writeMicroseconds"))
					command = command.replace("writeMicroseconds",
							"escreverMicros");
				if (command.contains("1700"))
					command = command.replace("1700", "Servo.frente");
				if (command.contains("1300"))
					command = command.replace("1300", "Servo.tras");
				if (command.contains("1500"))
					command = command.replace("1500", "Servo.parar");
				if (command.contains(".begin("))
					command = command.replace(".begin(", ".conectar(");
				if (command.contains(".clear"))
					command = command.replace("clear", "limpar");
				if (command.contains(".beginTransmission"))
					command = command
							.replace("beginTransmission", "transmitir");
				if (command.contains(".endTransmission"))
					command = command.replace("endTransmission",
							"pararTransmitir");
				if (command.contains(".requestFrom"))
					command = command.replace(".solicitar", ".solicitar");
				if (command.contains(".onRequest"))
					command = command.replace("onRequest", "solicitado");
				if (command.contains(".onReceive"))
					command = command.replace("onReceive", "recebido");
				if (command.contains("Serial"))
					command = command.replace("Serial", "USB");
				if (command.contains(".available"))
					command = command.replace("available", "disponivel");
				if (command.contains("Serial.print"))
					command = command.replace("Serial.print", ".enviar");
				if (command.contains("serCursor"))
					command = command.replace("serCursor", "posicao");
				if (command.contains(".write"))
					command = command.replace("write", "escrever");
				if (command.contains(".read"))
					command = command.replace("read", "ler");
				if (command.contains(".length"))
					command = command.replace("length", "tamanho");
				if (command.contains("for (int i = 0 ; i < EEPROM.length() ; i++)"
									+ " EEPROM.write(i, 0);"))
					command = command.replace("for (int i = 0 ; i < EEPROM.length() ; i++)"
									+ " EEPROM.write(i, 0);",
							"Memoria.formatar()");
				if (command.contains("\"")) {
					command = command.replace(
							command.substring(command.indexOf("\""),
									command.lastIndexOf("\"")),
							line.substring(line.indexOf("\""),
									line.lastIndexOf("\"")));
				}
				program.format("%s\n", command);
				System.out.println(command);
				command = "";
			} else if (line.length() > 3 && linecomment == false
					&& line.contains("#define") == false && comment == false
					&& line.contains("*/") == false
					&& line.contains("#include") == false) {
				System.out.println(line);
				System.out.println(linecomment);
				program.flush();
				program.close();
				out.append("\nHá um erro em seu código!\n" + line);
				out.update(out.getGraphics());
				return false;
			} else {
				program.format("%s\n", command);
				System.out.println(command);

			}
		}
		program.close();
		return true;

	}

	public static String addVar(String line, boolean contains) {

		String name = "";
		// String value = "-";
		String var = "";
		if (line.contains("const"))
			line = line.replace("const", "Constante");
		if (line.contains("unsigned"))
			line = line.replace("unsigned", "Modulo");
		if (line.contains("int")) {

			if (!line.contains("float") && !line.contains("long")) {
				var = line.replace("int", "Numero");

			} else {

				// boolean co;
				// if (line.contains(".") || line.contains("+")
				// || line.contains("-") || line.contains("/")
				// || line.contains("%")) {
				// co = true;
				// } else {
				// co = false;
				// }
				if (line.contains("float"))
					var = line.replace("float", "NumeroDecimal");
				if (line.contains("long"))
					var = line.replace("long", "NumeroLongo");
			}
			// System.out.println(var);

			// name = var.substring(var.indexOf('t') + 2,
			// contains ? var.indexOf('=') - 1 : var.indexOf(';'));
			// value = contains ? var.substring(var.indexOf('=') + 2,
			// var.indexOf(';')) : "-";
		}

		if (line.contains("String")) {
			var = line.replace("String", "Palavra");
			var = var.replace('\'', '\"');
			// name = var.substring(var.indexOf('g') + 2,
			// contains ? var.indexOf('=') - 1 : var.indexOf(';'));
			// value = contains ? var.substring(var.indexOf('\"') + 1,
			// var.lastIndexOf('\"')) : "-";
		}
		if (line.contains("boolean") || line.contains("true")
				|| line.contains("false")) {
			/*
			 * if (line.contains("Verdadeiro")) { // value = "true"; } else if
			 * (line.contains("Falso")) { // value = "false"; } else if
			 * (!line.contains("=")) { // value = "-"; } else
			 */
			if (line.contains("=")
					&& !(line.contains("<") || line.contains("<=")
							|| line.contains(">") || line.contains(">=")
							|| line.contains("==") || line.contains("!="))) {

				System.out
						.println("há um erro na declaração desta variável..."
								+ line.substring(line.indexOf('o', 'a') + 2,
										line.indexOf(';') + 1)
								+ " ela só pode assumir como valores Verdadeiro ou Falso.");
			}
			var = line.replace("boolean", "Condicao");
			var = var.replace("false", "Falso");
			var = var.replace("true", "Verdadeiro");
			// name = var.substring(var.indexOf('n') + 2,
			// contains ? var.indexOf('=') - 1 : var.indexOf(';') - 1);
			// value = contains ? value : "-";

		}
		if (line.contains("char")) {
			var = line.replace("char", "Letra");
		}
		if (!variaveis.containsKey(name)) {
			// saveVar(name, value);

		} else {
			System.out.println("Variável já existe com esse nome");
		}
		return var;
	}

	public static void saveVar(String name, String value) {
		variaveis.put(name, value);
	}

	public static String getFile() {
		return file;
	}

	private static void setFile(String file) {
		CppCompiler.file = file;
	}
}
