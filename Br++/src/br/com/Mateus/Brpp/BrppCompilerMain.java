package br.com.Mateus.Brpp;

import javax.swing.JFrame;

public class BrppCompilerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BrppCompilerBorderFrame frame = new BrppCompilerBorderFrame("Compilador Brino"+BrppCompiler.version);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.setVisible(true);
	}

}
