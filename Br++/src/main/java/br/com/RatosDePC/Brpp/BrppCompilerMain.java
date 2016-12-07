package br.com.RatosDePC.Brpp;

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
 * 
 * Classe principal do programa
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors 
 * @version 18/11/2016
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.parser.ParseException;

import br.com.RatosDePC.Brpp.IDEui.BrppIDEFrame;
import br.com.RatosDePC.Brpp.Utils.FileUtils;
import br.com.RatosDePC.Brpp.Utils.JSONUtils;
import br.com.RatosDePC.Brpp.Utils.KeywordManagerUtils;
import br.com.RatosDePC.Brpp.compiler.BrppCompiler;

public class BrppCompilerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f = new File(FileUtils.getBrinodirectory());
		f.mkdirs();
		File l = new File(FileUtils.getBrinodirectory() + "/bibliotecas");
		l.mkdirs();
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		File destDir = new File(s + System.getProperty("file.separator")
				+ "Arduino" + System.getProperty("file.separator")
				+ "libraries");
		try {
			JSONUtils.config(s);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FileUtils.copyFolder(l, destDir);
			KeywordManagerUtils.processLibraries();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		BrppIDEFrame frame = new BrppIDEFrame("Brino " + BrppCompiler.version);
		frame.setSize(500, 600);
		frame.setVisible(true);
		frame.setLocation(100, 30);
		

	}

}