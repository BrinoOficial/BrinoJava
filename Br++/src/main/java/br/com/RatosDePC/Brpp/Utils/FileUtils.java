package br.com.RatosDePC.Brpp.Utils;

/**
 * Classe utilizada para interfaciar o acesso ao disco de memória
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors Rafael Mascarenhas Dal Moro
 * @version 5/2/2016
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class FileUtils {
	
	private static File diretorio = null;
	private static final String BrinoDirectory = System.getProperty("user.home")+ System.getProperty("file.separator")+"Brino"; 

	public static void createFile(JTextPane txt) {
		String name = JOptionPane.showInputDialog("Qual o nome do rascunho?");
		File f = new File(BrinoDirectory+System.getProperty("file.separator")+ name +System.getProperty("file.separator")+ name + ".brpp");
		if (f.getParentFile().mkdirs()) {

			try {
				f.createNewFile();
				FileWriter fw = new FileWriter(f.getAbsoluteFile(), false);
				txt.write(fw);
				fw.close();

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		setDiretorio(f.getAbsoluteFile());
	}

	public static void saveFile(JTextPane txt) {
		FileWriter fw;
		try {
			fw = new FileWriter(getDiretorio().getAbsoluteFile(), false);
			txt.write(fw);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void abrirFile(JTextPane txt){
		txt.setText(null);
		JFileChooser FC = new JFileChooser(BrinoDirectory);
		int res = FC.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			setDiretorio(FC.getSelectedFile());
			try {
				String f = "";
				Scanner in = new Scanner(new File(
						getDiretorio().getAbsolutePath()));
				while (in.hasNext()) {
					String line = in.nextLine();
					// Highlight
					f += (line + "\n");
				}
				txt.setText(f);
				in.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else
			JOptionPane.showMessageDialog(null,
					"Voce nao selecionou nenhum diretorio.");
	}

	public static File getDiretorio() {
		return diretorio;
	}

	public static void setDiretorio(File diretorio) {
		FileUtils.diretorio = diretorio;
	}

	public static String getBrinodirectory() {
		return BrinoDirectory;
	}

}
