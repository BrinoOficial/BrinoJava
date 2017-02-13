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

/**
 * Classe utilizada para interfaciar o acesso ao disco de mem�ria
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors Rafael Mascarenhas Dal Moro
 * @version 5/2/2016
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class FileUtils {

	private static File diretorio = null;
	private static final String BrinoDirectory = System
			.getProperty("user.home")
			+ System.getProperty("file.separator")
			+ "Documents" + System.getProperty("file.separator") + "Brino";

	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
			}
			// list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest
						+ System.getProperty("file.separator"), file);
				// recursive copy
				if (!destFile.exists())
					copyFolder(srcFile, destFile);
			}
		} else {
			System.out.println(dest.exists());
			if (!dest.exists()) {
				// if file, then copy it
				// Use bytes stream to support all file types
				InputStream in = new FileInputStream(src);
				OutputStream out = new FileOutputStream(dest);
				byte[] buffer = new byte[1024];
				int length;
				// copy the file content in bytes
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.close();
				System.out.println("File copied from " + src + " to " + dest);
			}
		}
	}

	public static void createFile(RSyntaxTextArea rSyntaxTextArea) {
		String name = JOptionPane.showInputDialog("Qual o nome do rascunho?");
		File f = new File(BrinoDirectory + System.getProperty("file.separator")
				+ name + System.getProperty("file.separator") + name + ".brpp");
		if (f.getParentFile().mkdirs()) {

			try {
				f.createNewFile();
				FileWriter fw = new FileWriter(f.getAbsoluteFile(), false);
				rSyntaxTextArea.write(fw);
				fw.close();

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		setDiretorio(f.getAbsoluteFile());
	}

	public static void saveFile(RSyntaxTextArea rSyntaxTextArea) {
		FileWriter fw;
		try {
			fw = new FileWriter(getDiretorio().getAbsoluteFile(), false);
			rSyntaxTextArea.write(fw);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void abrirFile(RSyntaxTextArea rSyntaxTextArea) {
		rSyntaxTextArea.setText(null);
		JFileChooser FC = new JFileChooser(BrinoDirectory);
		int res = FC.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			setDiretorio(FC.getSelectedFile());
			try {
				String f = "";
				Scanner in = new Scanner(new File(getDiretorio()
						.getAbsolutePath()));
				while (in.hasNext()) {
					String line = in.nextLine();
					// Highlight
					f += (line + "\n");
				}
				rSyntaxTextArea.setText(f);
				in.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

		} else
			JOptionPane.showMessageDialog(null,
					"Voce nao selecionou nenhum diretorio.");
	}
	
	public static void abrirFile(String string, RSyntaxTextArea txt) {
		System.out.println(string);
		setDiretorio(new File(string));
		try {
			String f = "";
			Scanner in = new Scanner(new File(getDiretorio()
					.getAbsolutePath()));
			while (in.hasNext()) {
				String line = in.nextLine();
				// Highlight
				f += (line + "\n");
			}
			txt.setText(f);
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
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

	public static void copy(File sourceLocation, File targetLocation)
			throws IOException {
		if (!targetLocation.exists()) {
			targetLocation.mkdirs();
		}
		if (sourceLocation.isDirectory()) {
			copyDirectory(sourceLocation, targetLocation);
		} else {
			copyFile(sourceLocation, targetLocation);
		}
	}

	private static void copyDirectory(File source, File target)
			throws IOException {
		if (!target.exists()) {
			target.mkdir();
		}

		for (String f : source.list()) {
			copy(new File(source, f), new File(target, f));
		}
	}

	private static void copyFile(File source, File target) throws IOException {
		try (InputStream in = new FileInputStream(source);
				OutputStream out = new FileOutputStream(target)) {
			byte[] buf = new byte[1024];
			int length;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
		}
	}

}
