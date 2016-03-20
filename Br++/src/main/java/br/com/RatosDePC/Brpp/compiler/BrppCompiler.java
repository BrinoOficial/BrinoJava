package br.com.RatosDePC.Brpp.compiler;

/**
 * Compilador Brino criado para transformar código Brpp em 
 * código arduino.
 * @author Mateus Berardo de Souza Terra
 * @contributors 
 * @version 2.1.3
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

import br.com.RatosDePC.Brpp.IDEui.BrppIDEFrame;
import br.com.RatosDePC.Brpp.Utils.CompilerUtils;
import br.com.RatosDePC.Brpp.Utils.FileUtils;

public class BrppCompiler {

	private static Map<String, String> variaveis = new HashMap<String, String>();
	private static Formatter program;
	private static String file;
	private static JTextArea out = BrppIDEFrame.LOG;
	public static String version = "2.3.3-beta";

	public static boolean compile(String path) {
		setFile(FileUtils.getBrinodirectory() + System.getProperty("file.separator") + "Arduino");
		setFile(getFile()
				.concat(path.substring(path.lastIndexOf(System.getProperty("file.separator")), path.length() - 5)));
		setFile(getFile()
				.concat(path.substring(path.lastIndexOf(System.getProperty("file.separator")), path.length() - 4)));
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
			if (CompilerUtils.metodosObrigatorios(liness)) {
				if (proccess(lines)) {
					return true;
				} else
					return false;
			} else {
				out.append("\nHá um erro em seu código!");
				out.update(out.getGraphics());
			}
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
			if (command.contains("definir "))
				if (command.indexOf("definir") < command.indexOf("//") || !linecomment)
					command = command.replace("definir ", "#define ");
			if (command.contains("usar") && (command.indexOf("usar") < command.indexOf("//") || !linecomment)) {
				command = command.replace("usar ", "#include <");
				if (linecomment) {
					String lib = command.substring(command.indexOf('<'), command.indexOf('/'));
					lib = lib.trim();
					String comentario = command.substring(command.indexOf('/'), command.length());
					command = command.substring(0, command.indexOf('<'));
					command = command.concat(lib + ".h>" + " " + comentario);
				} else {
					command = command.replace("usar ", "#include <");
					command = command.trim();
					command = command.concat(".h>");
				}
			}
			if (command.contains("LCD"))
				command = command.replace("LCD", "LiquidCrystal");
			if (command.contains("Memoria"))
				command = command.replace("Memoria", "EEPROM");
			if (command.contains("I2C"))
				command = command.replace("I2C", "Wire");
			if ((command.contains(";") || command.contains("{") || command.contains("}")) && comment == false) {

				if (command.contains("Configuracao"))
					command = command.replace("Configuracao", "void setup");
				if (command.contains("Principal"))
					command = command.replace("Principal", "void loop");
				if (command.contains("soar"))
					command = command.replace("soar", "tone");
				if (command.contains("pararSoar"))
					command = command.replace("pararSoar", "noTone");
				if (command.contains("proporcionar"))
					command = command.replace("proporcionar", "map");
				if (command.contains("esperar("))
					command = command.replace("esperar", "delay");
				if (command.contains("SemRetorno"))
					command = command.replace("SemRetorno", "void");
				if (command.contains("senao")) {
					command = command.replace("senao", "else");
				}
				if (command.contains("se (") || command.contains("se(")) {
					command = command.replace("se(", "if(");
					command = command.replace("se (", "if(");
					if (command.contains("=") && !((command.contains("==") || command.contains("<")
							|| command.contains(">") || command.contains("!")))) {
						System.out.println(line);
						out.append("\nHá um erro em seu código!\n"+line);
						out.update(out.getGraphics());
						return false;
					}
				}
				if (command.contains("para (") || command.contains("para(")) {
					command = command.replace("para(", "for(");
					command = command.replace("para (", "for(");
					if (command.contains("=") && !((command.contains(";") || command.contains("==")
							|| command.contains("<") || command.contains(">")))) {
						System.out.println(line);
						out.append("\nHá um erro em seu código!\n"+line);
						out.update(out.getGraphics());
						return false;
					}
				}
				if (command.contains("enquanto") || command.contains("enquanto")) {
					command = command.replace("enquanto ", "while ");
					if (command.contains("=") && !((command.contains("==") || command.contains("<")
							|| command.contains(">") || command.contains("!")))) {
						System.out.println(line);
						out.append("\nHá um erro em seu código!\n"+line);
						out.update(out.getGraphics());
						return false;
					}
				}

				if (command.contains("Numero") || command.contains("Palavra") || command.contains("Condicao")
						|| command.contains("Verdadeiro") || command.contains("Falso") || command.contains("Letra")) {
					while (command.contains("Numero") || command.contains("Palavra") || command.contains("Condicao")
							|| command.contains("Verdadeiro") || command.contains("Falso")
							|| command.contains("Letra")) {

						command = addVar(command, command.contains("="));
					}
				}
				if (command.contains("Pino.definirModo")) {
					command = command.replace("Pino.definirModo", "pinMode");
					command = command.replace("Saida", "OUTPUT");
					command = command.replace("Entrada", "INPUT");
				}
				if (command.contains("Pino.ler(A")) {
					command = command.replace("Pino.ler", "analogRead");
					command = command.replace("Analogico.", "A");
				}
				if (command.contains("Pino.lerAnalogico(")) {
					command = command.replace("Pino.lerAnalogico", "analogRead");
					command = command.replace("Analogico.", "A");
				}
				if (command.contains("Pino.ler(")) {
					command = command.replace("Pino.ler", "digitalRead");
					command = command.replace("Digital.", "");
				}
				if (command.contains("Pino.escrever(A")) {
					command = command.replace("Pino.escrever", "analogWrite");
					command = command.replace("Analogico.", "A");
					command = command.replace("APD.", "");
				}
				if (command.contains("Pino.escreverAnalogico(")) {
					command = command.replace("Pino.escreverAnalogico", "analogWrite");
					command = command.replace("Analogico.", "A");
					command = command.replace("APD.", "");
				}
				if (command.contains("Pino.escrever(")) {
					command = command.replace("Pino.escrever", "digitalWrite");
					command = command.replace("Digital.", "");
				}
				if (command.contains("Pino.ligar(")) {
					command = command.replace("Digital.", "");
					String pin = command.substring(command.indexOf('(') + 1, command.indexOf(')'));
					command = command.replace("Pino.ligar(" + pin + ")", "digitalWrite(" + pin.trim() + ",HIGH)");
				}
				if (command.contains("Pino.desligar(")) {
					command = command.replace("Digital.", "");
					String pin = command.substring(command.indexOf('(') + 1, command.indexOf(')'));
					command = command.replace("Pino.desligar(" + pin + ")", "digitalWrite(" + pin.trim() + ",LOW)");
				}
				if (command.contains("Ligado"))
					command = command.replace("Ligado", "HIGH");
				if (command.contains("Desligado"))
					command = command.replace("Desligado", "LOW");
				if (command.contains(" e ") || command.contains(" ou ")) {
					command = command.replace(" e ", "&&");
					command = command.replace(" ou ", "||");
				}
				if (command.contains("responder"))
					command = command.replace("responder", "return");
				if (command.contains("<native>"))
					command = command.replace(command.substring(command.indexOf("<native>")),
							line.substring(line.indexOf("e>") + 2));
				if (command.contains("//")) {
					command = command.replace(command.substring(command.indexOf("//")),
							line.substring(line.indexOf("//")));
				}
				if (command.contains(".conectar(D")) {
					command = command.replace(".conectar(Digital.", ".attach(");
				}
				if (command.contains("escreverAngulo"))
					command = command.replace("escreverAngulo", "write");
				if (command.contains("escreverMicros"))
					command = command.replace("escreverMicros", "writeMicroseconds");
				if (command.contains("Servo.frente"))
					command = command.replace("Servo.frente", "1700");
				if (command.contains("Servo.tras"))
					command = command.replace("Servo.tras", "1300");
				if (command.contains("Servo.parar"))
					command = command.replace("Servo.parar", "1500");
				if (command.contains(".conectar("))
					command = command.replace(".conectar(", ".begin(");
				if (command.contains(".limpar"))
					command = command.replace("limpar", "clear");
				if (command.contains(".transmitir"))
					command = command.replace("transmitir", "beginTransmission");
				if (command.contains(".pararTransmitir"))
					command = command.replace("pararTransmitir", "endTransmission");
				if (command.contains(".solicitar"))
					command = command.replace(".solicitar", ".requestFrom");
				if (command.contains(".solicitado"))
					command = command.replace("solicitado", "onRequest");
				if (command.contains(".recebido"))
					command = command.replace("recebido", "onReceive");
				if (command.contains("USB"))
					command = command.replace("USB", "Serial");
				if (command.contains(".disponivel"))
					command = command.replace("disponivel", "available");
				if (command.contains(".enviar"))
					command = command.replace(".enviar", ".print");
				if (command.contains(".posicao"))
					command = command.replace("posicao", "serCursor");
				if (command.contains(".escrever"))
					command = command.replace("escrever", "write");
				if (command.contains(".ler"))
					command = command.replace("ler", "read");
				if (command.contains(".tamanho"))
					command = command.replace("tamanho", "length");
				if (command.contains("Memoria.formatar()"))
					command = command.replace("Memoria.formatar()",
							"for (int i = 0 ; i < EEPROM.length() ; i++)" + " EEPROM.write(i, 0);");
				if (command.contains("\"")) {
					command = command.replace(command.substring(command.indexOf("\""), command.lastIndexOf("\"")),
							line.substring(line.indexOf("\""), line.lastIndexOf("\"")));
				}
				program.format("%s\n", command);
				System.out.println(command);
				command = "";
			} else if (line.length() > 3 && linecomment == false && line.contains("definir") == false
					&& comment == false && line.contains("*/") == false && line.contains("usar") == false) {
				System.out.println(line);
				System.out.println(linecomment);
				program.flush();
				program.close();
				out.append("\nHá um erro em seu código!\n"+line);
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
		if (line.contains("Constante"))
			line = line.replace("Constante", "const");
		if (line.contains("Modulo"))
			line = line.replace("Modulo", "unsigned");
		if (line.contains("Numero")) {

			if (!line.contains("Decimal") && !line.contains("Longo")) {
				var = line.replace("Numero", "int");

			} else {

				// boolean co;
				// if (line.contains(".") || line.contains("+")
				// || line.contains("-") || line.contains("/")
				// || line.contains("%")) {
				// co = true;
				// } else {
				// co = false;
				// }
				if (line.contains("Decimal"))
					var = line.replace("NumeroDecimal", "float");
				if (line.contains("Longo"))
					var = line.replace("NumeroLongo", "long");
			}
			// System.out.println(var);

			// name = var.substring(var.indexOf('t') + 2,
			// contains ? var.indexOf('=') - 1 : var.indexOf(';'));
			// value = contains ? var.substring(var.indexOf('=') + 2,
			// var.indexOf(';')) : "-";
		}

		if (line.contains("Palavra")) {
			var = line.replace("Palavra", "String");
			var = var.replace('\'', '\"');
			// name = var.substring(var.indexOf('g') + 2,
			// contains ? var.indexOf('=') - 1 : var.indexOf(';'));
			// value = contains ? var.substring(var.indexOf('\"') + 1,
			// var.lastIndexOf('\"')) : "-";
		}
		if (line.contains("Condicao") || line.contains("Verdadeiro") || line.contains("Falso")) {
			/*
			 * if (line.contains("Verdadeiro")) { // value = "true"; } else if
			 * (line.contains("Falso")) { // value = "false"; } else if
			 * (!line.contains("=")) { // value = "-"; } else
			 */
			if (line.contains("=") && !(line.contains("<") || line.contains("<=") || line.contains(">")
					|| line.contains(">=") || line.contains("==") || line.contains("!="))) {

				System.out.println("há um erro na declaração desta variável..."
						+ line.substring(line.indexOf('o', 'a') + 2, line.indexOf(';') + 1)
						+ " ela só pode assumir como valores Verdadeiro ou Falso.");
			}
			var = line.replace("Condicao", "boolean");
			var = var.replace("Falso", "false");
			var = var.replace("Verdadeiro", "true");
			// name = var.substring(var.indexOf('n') + 2,
			// contains ? var.indexOf('=') - 1 : var.indexOf(';') - 1);
			// value = contains ? value : "-";

		}
		if (line.contains("Letra")) {
			var = line.replace("Letra", "char");
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
		BrppCompiler.file = file;
	}
}
