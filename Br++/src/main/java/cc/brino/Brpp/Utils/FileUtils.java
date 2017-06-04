package cc.brino.Brpp.Utils;

/*
 * Copyright (c) 2016 StarFruitBrasil
 * 
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without
 * limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice
 * shall be included in all copies or substantial portions
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
/**
 * Classe utilizada para interfaciar o acesso ao disco de
 * mem�ria
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors Rafael Mascarenhas Dal Moro
 * @version 5/2/2016
 */
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalFileChooserUI;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import cc.brino.Brpp.IDEui.BrppIDEFrame;
import cc.brino.Brpp.IDEui.BubbleBorder;
import cc.brino.Brpp.IDEui.UIConstants;
import cc.brino.Brpp.IDEui.ScrollBar.HorizontalScrollLeanUI;
import cc.brino.Brpp.IDEui.ScrollBar.ScrollLeanUI;


public class FileUtils {

	private static File diretorio = null;
	private static final String BrinoDirectory = System.getProperty("user.home")
			+ System.getProperty("file.separator")
			+ "Documents"
			+ System.getProperty("file.separator") + "Brino";
	private static ImageIcon goup = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/goup.png"));
	private static ImageIcon goupFocus = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/goupFocus.png"));
	private static ImageIcon newFolder = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/newFolder.png"));
	private static ImageIcon newFolderFocus = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/newFolderFocus.png"));
	private static ImageIcon homeFolder = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/homeFolder.png"));
	private static ImageIcon homeFolderFocus = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/homeFolderFocus.png"));
	private static ImageIcon list = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/list.png"));
	private static ImageIcon listFocus = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/listFocus.png"));
	private static ImageIcon details = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/details.png"));
	private static ImageIcon detailsFocus = new ImageIcon(
			BrppIDEFrame.class.getClassLoader()
					.getResource("resources/detailsFocus.png"));

	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			// if directory not exists, create
			// it
			if (!dest.exists()) {
				dest.mkdir();
			}
			// list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				// construct the src and
				// dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(
						dest
								+ System.getProperty("file.separator"),
						file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}
		} else {
			// if file, then copy it
			// Use bytes stream to
			// support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			// copy the file content in
			// bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
			System.out.println("File copied from " + src + " to "
					+ dest);
		}
	}

	public static void createFile(RSyntaxTextArea rSyntaxTextArea) {
		String name = JOptionPane.showInputDialog("Qual o nome do rascunho?");
		File f = new File(BrinoDirectory
				+ System.getProperty("file.separator") + name
				+ System.getProperty("file.separator") + name
				+ ".brpp");
		if (f.getParentFile().mkdirs()) {
			try {
				f.createNewFile();
				FileWriter fw = new FileWriter(
						f.getAbsoluteFile(), false);
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
			fw = new FileWriter(getDiretorio().getAbsoluteFile(),
					false);
			rSyntaxTextArea.write(fw);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void abrirFile(RSyntaxTextArea rSyntaxTextArea) {
		JFileChooser FC = new JFileChooser(BrinoDirectory);
		FC.addChoosableFileFilter(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.getName()
						.toUpperCase()
						.contains(".BRPP")
						|| file.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Arquivos de Código Brino";
			}
		});
		for (Component i : FC.getComponents()) {
			try {
				boolean a=true;
				for (Component c : ((JPanel) i).getComponents()) {
					if(c.getClass().toString().equals("class javax.swing.plaf.metal.MetalFileChooserUI$1")){
						JComboBox jcom = (JComboBox) c;
						jcom.setBackground(UIConstants.VERDE);
						jcom.setForeground(Color.white);
						jcom.setBorder(new BubbleBorder(
								UIConstants.VERDE));
						for (int z = 0; z < jcom.getComponentCount(); z++) {
							if (jcom.getComponent(z) instanceof JComponent) {
								((JComponent) jcom.getComponent(z)).setBorder(UIConstants.BORDAVAZIA);
							}
							if (jcom.getComponent(z) instanceof AbstractButton) {
								((AbstractButton) jcom.getComponent(z)).setBorderPainted(false);
							}
						}
					}
					if (c.getClass() == javax.swing.JLabel.class) {
						c.setForeground(Color.WHITE);
					}
					
					if (c.getClass() == javax.swing.JPanel.class) {
						for (Component d : ((JPanel) c).getComponents()) {
							if(d.getClass() ==  javax.swing.JToggleButton.class){
								JToggleButton jtg = (JToggleButton) d; 
								if(jtg.getToolTipText().equals("List")){
									jtg.setIcon(list);
									jtg.setSelectedIcon(listFocus);
									jtg.setBackground(UIConstants.CINZA);
									jtg.setBorder(UIConstants.BORDAVAZIA);
									jtg.setRolloverIcon(listFocus);
								} else{
									jtg.setIcon(details);
									jtg.setSelectedIcon(detailsFocus);
									jtg.setBorder(UIConstants.BORDAVAZIA);
									jtg.setBackground(UIConstants.CINZA);
									jtg.setRolloverIcon(detailsFocus);
								}
							}
							if (d.getClass() == javax.swing.JComboBox.class) {
								JComboBox jcom = (JComboBox) d;
								jcom.setBackground(UIConstants.VERDE);
								jcom.setForeground(Color.white);
								jcom.setBorder(new BubbleBorder(
										UIConstants.VERDE));
								for (int z = 0; z < jcom.getComponentCount(); z++) {
									if (jcom.getComponent(z) instanceof JComponent) {
										((JComponent) jcom.getComponent(z)).setBorder(UIConstants.BORDAVAZIA);
									}
									if (jcom.getComponent(z) instanceof AbstractButton) {
										((AbstractButton) jcom.getComponent(z)).setBorderPainted(false);
									}
								}
							}
							if (d.getClass() != javax.swing.JButton.class
									&& d.getClass() != javax.swing.JComboBox.class)
								d.setForeground(Color.WHITE);
							else if (d.getClass() == javax.swing.JButton.class) {
								// d.setBackground(UIConstants.VERDE);
								JButton btn = (JButton) d;
								if (btn.getText() != null) {
									if (btn.getText()
											.equals("Open")) {
										btn.setBorder(new BubbleBorder(
												UIConstants.VERDE));
										btn.setForeground(Color.white);
										btn.setText("Abrir");
										btn.setBackground(UIConstants.VERDE);
									}
									if (btn.getText()
											.equals("Cancel")) {
										btn.setBorder(new BubbleBorder(
												UIConstants.CINZACLARO));
										btn.setForeground(Color.white);
										btn.setText("Cancelar");
										btn.setBackground(UIConstants.CINZACLARO);
									}
								}
								if (btn.getActionCommand()
										.equals("Go Up")) {
									btn.setIcon(goup);
									btn.setBorder(UIConstants.BORDAVAZIA);
									btn.setBackground(UIConstants.CINZA);
									btn.setRolloverIcon(goupFocus);
								} else if (btn.getActionCommand()
										.equals("New Folder")) {
									btn.setIcon(newFolder);
									btn.setBorder(UIConstants.BORDAVAZIA);
									btn.setBackground(UIConstants.CINZA);
									btn.setRolloverIcon(newFolderFocus);
								} else if (btn.getToolTipText()
										.equals("Home")) {
									btn.setIcon(homeFolder);
									btn.setBorder(UIConstants.BORDAVAZIA);
									btn.setBackground(UIConstants.CINZA);
									btn.setRolloverIcon(homeFolderFocus);
								}
							}
							if (d.getClass() == javax.swing.JScrollPane.class) {
								setScroll((JScrollPane) d);
							}
						}
					}
				}
			} catch (java.lang.ClassCastException e) {
				e.printStackTrace();
			}
		}
		FC.setAcceptAllFileFilterUsed(false);
		int res = FC.showOpenDialog(null);
		if (res == JFileChooser.APPROVE_OPTION) {
			rSyntaxTextArea.setText(null);
			setDiretorio(FC.getSelectedFile());
			try {
				String f = "";
				Scanner in = new Scanner(
						new File(
								getDiretorio().getAbsolutePath()));
				while (in.hasNext()) {
					String line = in.nextLine();
					// Highlight
					f += (line + "\n");
					System.out.println(line);
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

	public static void abrirFile(String string,
			RSyntaxTextArea txt,
			boolean diff) {
		System.out.println(string);
		if (diff)
			setDiretorio(new File(string));
		else
			setDiretorio(null);
		try {
			String f = "";
			Scanner in;
			if (diff)
				in = new Scanner(
						new File(
								getDiretorio().getAbsolutePath()));
			else
				in = new Scanner(new File(string));
			while (in.hasNext()) {
				String line = in.nextLine();
				// Highlight
				f += (line + "\n");
				System.out.println(line);
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

	public static void copy(File sourceLocation, File targetLocation) throws IOException {
		if (!targetLocation.exists()) {
			targetLocation.mkdirs();
		}
		if (sourceLocation.isDirectory()) {
			copyDirectory(sourceLocation, targetLocation);
		} else {
			copyFile(sourceLocation, targetLocation);
		}
	}

	private static void copyDirectory(File source, File target) throws IOException {
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

	private static void setScroll(JScrollPane scroll) {
		scroll.setBackground(UIConstants.CINZA);
		scroll.setForeground(Color.white);
		scroll.setBorder(UIConstants.BORDACINZAESCUROARREDONDADA);
		scroll.setViewportBorder(UIConstants.BORDAVAZIA);
		for (Component comp : scroll.getComponents()) {
			if(comp.getClass()==(JViewport.class))
				((JViewport)comp).getComponent(0).setForeground(Color.white);
		}
		JScrollBar horBar = scroll.getHorizontalScrollBar();
		horBar.setUI(new HorizontalScrollLeanUI());
		JScrollBar verBar = scroll.getVerticalScrollBar();
		verBar.setUI(new ScrollLeanUI());
		horBar.setBackground(UIConstants.CINZAESCURO);
		horBar.setBorder(UIConstants.BORDAVAZIA);
		verBar.setBackground(UIConstants.CINZAESCURO);
		verBar.setBorder(UIConstants.BORDAVAZIA);
	}
}
