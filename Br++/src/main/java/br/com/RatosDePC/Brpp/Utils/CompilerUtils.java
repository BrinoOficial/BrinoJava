package br.com.RatosDePC.Brpp.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompilerUtils {
	private static String line = "(\n*.*[;}{]\n)|(\n*//.*\n)";
	private static Pattern Rascunho = Pattern.compile("Configuracao\\Q()\\E\\s*\\Q{\\E("+line+")*\n*\\Q}\\E("+line+")*\n*Principal\\Q()\\E\\s*\\Q{\\E("+line+")*\n*\\Q}\\E");

	public static void main(String[] args) {
		Matcher matcher = Rascunho.matcher( "Configuracao() {\n"
				+ "//Coloque aqui seu codigo de Configuracao que sera executado uma vez\n"
				+ "Numero n = 7;\n" 
				+ "}\n"
				+ "Numero n(){\n"
				+ "retornar 7;\n"
				+ "}\n" 
				+ "Principal(){\n"
				+ "//Coloque aqui seu codigo Principal, para rodar repetidamente\n"
				+ "\n" 
				+ "}\n"
				+ "SemRetorno teste(){\n"
				+ "\n"
				+ "}\n");
		boolean found = false;
		while (matcher.find()) {
			System.out.format("I found the text" + " \"%s\" starting at " + "index %d and ending at index %d.%n",
					matcher.group(), matcher.start(), matcher.end());
			found = true;
		}
		if (!found) {
			System.out.format("No match found.%n");
		}
	}
	
	public static boolean metodosObrigatorios(String rasc){
		Matcher matcher = Rascunho.matcher(rasc);
		if (matcher.find()) return true;
		return false;
	}
}
