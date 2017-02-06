package cc.brino.Brpp.Utils;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompilerUtils {
	private static String line = "(\r*\n*.*[;}{]\r*\n)|(\r*\n*//.*\r*\n)";
	private static Pattern Rascunho = Pattern.compile("Configuracao\\Q()\\E\\s*\\Q{\\E("+line+")*\r*\n*\\Q}\\E("+line+")*\r*\n*Principal\\Q()\\E\\s*\\Q{\\E("+line+")*\r*\n*\\Q}\\E");

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
