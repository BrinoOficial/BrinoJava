package br.com.Mateus.Brpp;

import javax.swing.JFrame;

public class BrppCompilerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BrppIDEFrame frame = new BrppIDEFrame("Compilador Brino"+BrppCompiler.version);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 700);
		frame.setVisible(true);
	}

}
