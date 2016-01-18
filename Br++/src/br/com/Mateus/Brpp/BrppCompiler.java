/* version 1.0.1
 * 
 * Compilador Brino criado para transformar código Brpp em 
 * código arduino. 
 * 
 * @author Mateus Berardo de Souza Terra
 * @date 27/12/2015
 * 
 */
package br.com.Mateus.Brpp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class BrppCompiler {

	private static Map<String, String> variaveis = new HashMap<String, String>();
	private static Formatter program;
	private static String file;
	
	public static boolean compile(String path) {
		setFile("C:\\Arduino\\Brino");
		setFile(getFile().concat(path.substring(path.lastIndexOf('\\'),
				path.length() - 5)));
		setFile(getFile().concat("\\"
				+ path.substring(path.lastIndexOf('\\'), path.length() - 4)));
		setFile(getFile().concat("ino"));
		try {
			// inputFile = new Scanner(input);
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
		} finally {

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
			if (command.contains("definir ") && !linecomment)
				command = command.replace("definir ", "#define ");
			if (command.contains("usar") && !linecomment) {
				command = command.replace("usar ", "#include <");
				command = command.trim();
				command = command.concat(".h>");
			}
			if ((command.contains(";") || command.contains("{") || command
					.contains("}")) && comment == false) {

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
					command = command.replace("faca", "");
				}
				if (command.contains("se (") || command.contains("se(")) {
					command = command.replace("se(", "if(");
					command = command.replace("se (", "if(");
					command = command.replace("faca", "");
					if (command.contains("=")
							&& !((command.contains("==")
									|| command.contains("<")
									|| command.contains(">") || command
										.contains("!")))) {
						System.out.println(line);
						return false;
					}
				}
				if (command.contains("para (") || command.contains("para(")) {
					command = command.replace("for(", "if(");
					command = command.replace("for (", "if(");
					command = command.replace("faca", "");
					if (command.contains("=")
							&& !((command.contains(";")
									|| command.contains("==")
									|| command.contains("<") || command
										.contains(">")))) {
						System.out.println(line);
						return false;
					}
				}
				if (command.contains("enquanto (")
						|| command.contains("enquanto(")) {
					command = command.replace("enquanto ", "while ");
					command = command.replace("faca", "");
					if (command.contains("=")
							&& !((command.contains("==")
									|| command.contains("<")
									|| command.contains(">") || command
										.contains("!")))) {
						System.out.println(line);
						return false;
					}
				}
				if (command.contains("USB.conectar()"))
					command = command.replace("USB.conectar()",
							"Serial.begin(9600)");
				if (command.contains("USB.enviar"))
					command = command.replace("USB.enviar", "Serial.print");

				if (command.contains("Numero") || command.contains("Palavra")
						|| command.contains("Condicao")
						|| command.contains("Verdadeiro")
						|| command.contains("Falso")) {
					while (command.contains("Numero")
							|| command.contains("Palavra")
							|| command.contains("Condicao")
							|| command.contains("Verdadeiro")
							|| command.contains("Falso")) {

						command = addVar(command, command.contains("=") ? true
								: false);
						System.out.println(command);
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
				if (command.contains("Pino.ler(")) {
					command = command.replace("Pino.ler", "digitalRead");
					command = command.replace("Digital.", "");
					command = command.replace("D.", "");
				}
				if (command.contains("Pino.escrever(A")) {
					command = command.replace("Pino.escrever", "analogWrite");
					command = command.replace("Analogico.", "A");
					command = command.replace("APD.", "");
				}
				if (command.contains("Pino.escrever(")) {
					command = command.replace("Pino.escrever", "digitalWrite");
					command = command.replace("Digital.", "");
					command = command.replace("D.", "");
				}
				if (command.contains("Pino.ligar(")) {
					command = command.replace("Digital.", "");
					command = command.replace("D.", "");
					String pin = command.substring(command.indexOf('(') + 1,
							command.indexOf(')'));
					command = command.replace("Pino.ligar(" + pin + ")",
							"digitalWrite(" + pin.trim() + ",HIGH)");
				}
				if (command.contains("Pino.desligar(")) {
					command = command.replace("Digital.", "");
					command = command.replace("D.", "");
					String pin = command.substring(command.indexOf('(') + 1,
							command.indexOf(')'));
					command = command.replace("Pino.desligar(" + pin + ")",
							"digitalWrite(" + pin.trim() + ",LOW)");
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
					command = command.replace(
							command.substring(command.indexOf("<native>")),
							line.substring(line.indexOf("e>") + 2));
				if (command.contains("//")) {
					command = command.replace(
							command.substring(command.indexOf("//")),
							line.substring(line.indexOf("//")));
				}
				program.format("%s\n", command);
				System.out.println(command);
				command = "";
			} else if (line.length() > 3 && linecomment == false
					&& line.contains("definir") == false && comment == false
					&& line.contains("*/") == false
					&& line.contains("usar") == false) {
				System.out.println(line);
				System.out.println(linecomment);
				program.flush();
				program.close();
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
		String value = "-";
		String var = "";
		if (line.contains("Constante"))
			line = line.replace("Constante", "const");
		if (line.contains("Modulo"))
			line = line.replace("Modulo", "unsigned");
		if (line.contains("Numero")) {

			if (!line.contains("Decimal") && !line.contains("Longo")) {
				var = line.replace("Numero", "int");

			} else {

				boolean co;
				if (line.contains(".") || line.contains("+")
						|| line.contains("-") || line.contains("/")
						|| line.contains("%")) {
					co = true;
				} else {
					co = false;
				}
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
		if (line.contains("Condicao") || line.contains("Verdadeiro")
				|| line.contains("Falso")) {
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
			var = line.replace("Condicao", "boolean");
			var = var.replace("Falso", "false");
			var = var.replace("Verdadeiro", "true");
			// name = var.substring(var.indexOf('n') + 2,
			// contains ? var.indexOf('=') - 1 : var.indexOf(';') - 1);
			// value = contains ? value : "-";

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
