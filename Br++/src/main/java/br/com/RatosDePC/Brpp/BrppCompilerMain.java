package br.com.RatosDePC.Brpp;

import java.io.File;
import javax.swing.JFrame;
import br.com.RatosDePC.Brpp.Utils.FileUtils;
import br.com.RatosDePC.Brpp.IDEui.BrppIDEFrame;
import br.com.RatosDePC.Brpp.compiler.BrppCompiler;

public class BrppCompilerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f = new File(FileUtils.getBrinodirectory());
		f.mkdirs();
		BrppIDEFrame frame = new BrppIDEFrame("Compilador Brino"+BrppCompiler.version);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 700);
		frame.setVisible(true);
	}

}
