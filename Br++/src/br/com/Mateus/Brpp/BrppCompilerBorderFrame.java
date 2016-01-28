package br.com.Mateus.Brpp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class BrppCompilerBorderFrame extends JFrame {
	public static JTextArea LOG = new JTextArea();
	private JButton COMP;
	private JButton COMPUP;
	private JButton CANCEL;
	private JButton ABR;
	private JButton SALVAR;
	private JPanel NorthPanel = new JPanel();
	public JFileChooser FC;
	private JScrollPane IDEScroll;
	private BorderLayout layout;
	private JComboBox<String> COM;
	private String[] coms = { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6",
			"COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13",
			"COM14", "COM15" };

	private JTextArea IDE;
	File diretorio = null;

	public BrppCompilerBorderFrame(String title) {
		super(title);
		layout = new BorderLayout();
		setLayout(layout);
		NorthPanel.setLayout(new FlowLayout());
		ABR = new JButton("Abrir");
		NorthPanel.add(ABR);
		AbrirHandler abrhandler = new AbrirHandler();
		ABR.addActionListener(abrhandler);
		SALVAR = new JButton("Salvar");
		NorthPanel.add(SALVAR);
		SaveHandler savehandler = new SaveHandler();
		SALVAR.addActionListener(savehandler);
		COMP = new JButton("Compilar");
		NorthPanel.add(COMP);
		CompHandler handler = new CompHandler();
		COMP.addActionListener(handler);
		COMPUP = new JButton("Compilar e Carregar");
		NorthPanel.add(COMPUP);
		UploadHandler uphandler = new UploadHandler();
		COMPUP.addActionListener(uphandler);
		COM = new JComboBox<String>(coms);
		NorthPanel.add(COM);
		CANCEL = new JButton("Abort");
		NorthPanel.add(CANCEL);
		CanButtonHandler CanHandler = new CanButtonHandler();
		CANCEL.addActionListener(CanHandler);
		add(NorthPanel, BorderLayout.NORTH);
		NorthPanel.setVisible(true);
		IDE = new JTextArea(200, 400);
		IDEScroll = new JScrollPane(IDE);
		IDEScroll.setVisible(true);
		IDEScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		IDEScroll
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(IDEScroll, BorderLayout.CENTER);
		add(LOG, BorderLayout.SOUTH);
		LOG.setEditable(false);
	}

	public static void setText(String line) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LOG.setText(line);
			}
		});
	}

	private class CompHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (diretorio == null) {
				JFileChooser FC = new JFileChooser();
				int res = FC.showOpenDialog(null);
				File diretorio = null;
				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = FC.getSelectedFile();
					if (BrppCompiler.compile(diretorio.getAbsolutePath()))
						try {
							if (Uploader.compile(BrppCompiler.getFile()))
								BrppCompilerBorderFrame.setText("Compilado");
							else
								BrppCompilerBorderFrame
										.setText("Falha ao compilar...");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					// JOptionPane.showMessageDialog(null, diretorio.getName()
					// + " compilado");
				} else
					JOptionPane.showMessageDialog(null,
							"Voce nao selecionou nenhum diretorio.");
			} else {
				FileWriter fw;
				try {
					fw = new FileWriter(diretorio.getAbsoluteFile(), false);
					IDE.write(fw);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (BrppCompiler.compile(diretorio.getAbsolutePath()))
					try {
						if (Uploader.compile(BrppCompiler.getFile()))
							BrppCompilerBorderFrame.setText("Compilado");
						else
							BrppCompilerBorderFrame
									.setText("Falha ao compilar...");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
			}

		}

	}

	private class AbrirHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			IDE.setText(null);
			JFileChooser FC = new JFileChooser();
			int res = FC.showOpenDialog(null);

			if (res == JFileChooser.APPROVE_OPTION) {
				diretorio = FC.getSelectedFile();
				try {
					Scanner in = new Scanner(new File(
							diretorio.getAbsolutePath()));
					while (in.hasNext()) {
						String line = in.nextLine();
						//Highlight
						IDE.append(line + "\n");
					}
					in.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else
				JOptionPane.showMessageDialog(null,
						"Voce nao selecionou nenhum diretorio.");

		}

	}

	private class SaveHandler implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (diretorio != null) {
				FileWriter fw;
				try {
					fw = new FileWriter(diretorio.getAbsoluteFile(), false);
					IDE.write(fw);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private class UploadHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (diretorio != null) {
				FileWriter fw;
				try {
					fw = new FileWriter(diretorio.getAbsoluteFile(), false);
					IDE.write(fw);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (BrppCompiler.compile(diretorio.getAbsolutePath()))
					try {
						if (Uploader.upload(BrppCompiler.getFile(), COM
								.getSelectedItem().toString()))
							BrppCompilerBorderFrame
									.setText("Compilado e Carregado");
						else
							BrppCompilerBorderFrame
									.setText("Falha ao compilar e/ou carregar...");
					} catch (IOException e) {
						e.printStackTrace();
					}
			} else {
				JFileChooser FC = new JFileChooser();
				int res = FC.showOpenDialog(null);
				File diretorio = null;
				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = FC.getSelectedFile();
					if (BrppCompiler.compile(diretorio.getAbsolutePath()))
						try {
							if (Uploader.upload(BrppCompiler.getFile(), COM
									.getSelectedItem().toString()))
								BrppCompilerBorderFrame
										.setText("Compilado e Carregado");
							else
								BrppCompilerBorderFrame
										.setText("Falha ao compilar e/ou carregar...");
						} catch (IOException e) {
							e.printStackTrace();
						}
				} else
					JOptionPane.showMessageDialog(null,
							"Voce nao selecionou nenhum diretorio.");
			}
		}
	}

	// private class UploadHandler implements ActionListener {
	// public void actionPerformed(ActionEvent event) {
	//
	// JFileChooser FC = new JFileChooser();
	// int res = FC.showOpenDialog(null);
	// File diretorio = null;
	// if (res == JFileChooser.APPROVE_OPTION) {
	// diretorio = FC.getSelectedFile();
	// if (BrppCompiler.compile(diretorio.getAbsolutePath()))
	// try {
	// if (Uploader.upload(BrppCompiler.getFile(), COM
	// .getSelectedItem().toString()))
	// BrppCompilerBorderFrame.setText("Compilado e Carregado");
	// else
	// BrppCompilerBorderFrame
	// .setText("Falha ao compilar e/ou carregar...");
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// } else
	// JOptionPane.showMessageDialog(null,
	// "Voce nao selecionou nenhum diretorio.");
	//
	// }
	// }

	private class CanButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);

		}

	}
}
