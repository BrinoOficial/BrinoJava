package br.com.RatosDePC.Brpp;

/**
 * 
 * Classe principal do programa
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors 
 * @version 5/2/2016
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.RatosDePC.Brpp.IDEui.BrppIDEFrame;
import br.com.RatosDePC.Brpp.Utils.FileUtils;
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
			FileUtils.copyFolder(l,destDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BrppIDEFrame frame = new BrppIDEFrame("Compilador Brino "
				+ BrppCompiler.version);
		frame.setSize(500, 600);
		frame.setVisible(true);
		frame.setLocation(100, 30);

	}

}
