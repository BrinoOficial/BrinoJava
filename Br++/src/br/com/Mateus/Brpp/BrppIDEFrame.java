package br.com.Mateus.Brpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class BrppIDEFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	final JTextPane txt;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem novoItem;
	private JMenuItem salvarItem;
	private JMenuItem abrirItem;
	private JPanel NorthPanel;
	private JButton COMP;
	private JButton COMPUP;
	private JButton CANCEL;
	private JComboBox<String> COM;
	private JComboBox<String> BOARD;
	private File diretorio = null;
	private String[] coms;
	private String[] boards = {"Uno","Mega","Mega2560","Nano","Nano 168","Diecimila ou Duemilanove"};
	private String min = "Configuracao() {\n}\nPrincipal(){\n}\n";

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	public BrppIDEFrame(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);
		NorthPanel=new JPanel();
		NorthPanel.setLayout(new FlowLayout());
		COMP = new JButton("Compilar");
		NorthPanel.add(COMP);
		CompHandler handler = new CompHandler();
		COMP.addActionListener(handler);
		COMPUP = new JButton("Compilar e Carregar");
		NorthPanel.add(COMPUP);
		UploadHandler uphandler = new UploadHandler();
		COMPUP.addActionListener(uphandler);
		BOARD = new JComboBox<String>(boards);
		NorthPanel.add(BOARD);
		coms = new String[50];
		for (int x = 0; x < 50; x++) {
			coms[x] = "COM" + (x + 1);
		}
		COM = new JComboBox<String>(coms);
		NorthPanel.add(COM);
		CANCEL = new JButton("Abort");
		NorthPanel.add(CANCEL);
		CanButtonHandler CanHandler = new CanButtonHandler();
		CANCEL.addActionListener(CanHandler);
		add(NorthPanel, BorderLayout.NORTH);
		NorthPanel.setVisible(true);

		Color azul = new Color(66, 119, 255);
		Color laranja = new Color(255, 56, 0);
		Color corBosta = new Color(252, 201, 25);
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final javax.swing.text.AttributeSet attr = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, azul);
		final javax.swing.text.AttributeSet attri = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, corBosta);
		final javax.swing.text.AttributeSet attrib = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, laranja);
		final javax.swing.text.AttributeSet attrBlack = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
		final javax.swing.text.AttributeSet attrBold = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Bold, true);

		final javax.swing.text.AttributeSet attrNoBold = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Bold, false);
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			private static final long serialVersionUID = 1L;

			public void insertString(int offset, String str,
					javax.swing.text.AttributeSet a)
					throws BadLocationException {
				super.insertString(offset, str, a);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offset);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after
							|| String.valueOf(text.charAt(wordR))
									.matches("\\W")) {
						if (text.substring(wordL, wordR)
								.matches(
										"(\\W)*(Ligado|Desligado|Numero|NumeroDecimal|Longo|Palavra|Condicao|Modulo|Constante|Verdadeiro|Falso|SemRetorno|Entrada|Saida)"))
							setCharacterAttributes(wordL, wordR - wordL, attr,
									false);
						else if (text
								.substring(wordL, wordR)
								.matches(
										"(\\W)*(Configuracao|Principal|usar|definir|para|se|enquanto|senao|faca|e|ou|responder)"))
							setCharacterAttributes(wordL, wordR - wordL, attri,
									false);
						else if (text
								.substring(wordL, wordR)
								.matches(
										"(\\W)*(soar|pararSoar|esperar|proporcionar|definirModo|usar|conectar|enviar|enviarln|disponivel|ler|escrever|ler|ligar|desligar|tamanho|formatar|posicao|limpar|conectar|escreverAngulo|escreverMicros|frente|tras|parar|transmitir|pararTransitir|solicitar|solicitado|recebido)"))
							setCharacterAttributes(wordL, wordR - wordL,
									attrib, false);
						else if (text.substring(wordL, wordR).matches(
								"(\\W)*(Memoria|Pino|LCD|USB|I2C|Servo)")) {
							setCharacterAttributes(wordL, wordR - wordL,
									attrib, false);
							setCharacterAttributes(wordL, wordR - wordL,
									attrBold, false);
						} else {
							setCharacterAttributes(wordL, wordR - wordL,
									attrBlack, false);
							setCharacterAttributes(wordL, wordR - wordL,
									attrNoBold, false);
						}
						wordL = wordR;
					}
					wordR++;
				}
			}

			public void remove(int offs, int len) throws BadLocationException {
				super.remove(offs, len);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offs);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offs);

				if (text.substring(before, after).matches(
						"(\\W)*(private|public|protected)")) {
					setCharacterAttributes(before, after - before, attr, false);
				} else {
					setCharacterAttributes(before, after - before, attrBlack,
							false);
				}
			}
		};

		txt = new JTextPane(doc);
		txt.setSize(200, 400);
		txt.setText(min);
		add(new JScrollPane(txt), BorderLayout.CENTER);

		menuBar = new JMenuBar();
		fileMenu = new JMenu("Arquivo");
		fileMenu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(fileMenu);

		novoItem = new JMenuItem("Novo");
		Action novoAction = new AbstractAction("Novo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane
						.showInputDialog("Qual o nome do rascunho?");
				File f = new File("C://Arduino/Brino/" + name + "/" + name
						+ ".brpp");
				if (f.getParentFile().mkdirs()) {

					try {
						f.createNewFile();
						txt.setText(min);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				diretorio = f.getAbsoluteFile();

			}
		};
		novoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		novoItem.setAction(novoAction);
		fileMenu.add(novoItem);

		abrirItem = new JMenuItem("Abrir");
		Action abrirAction = new AbstractAction("Abrir") {
			@Override
			public void actionPerformed(ActionEvent e) {
				txt.setText(null);
				JFileChooser FC = new JFileChooser();
				int res = FC.showOpenDialog(null);

				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = FC.getSelectedFile();
					try {
						String f = "";
						Scanner in = new Scanner(new File(
								diretorio.getAbsolutePath()));
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
		};
		abrirAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		abrirItem.setAction(abrirAction);
		fileMenu.add(abrirItem);

		salvarItem = new JMenuItem("Salvar");
		Action salvarAction = new AbstractAction("Salvar") {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (diretorio != null) {
					FileWriter fw;
					try {
						fw = new FileWriter(diretorio.getAbsoluteFile(), false);
						txt.write(fw);
						fw.close();
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
				} else {
					String name = JOptionPane
							.showInputDialog("Qual o nome do rascunho?");
					File f = new File("C://Arduino/Brino/" + name + "/" + name
							+ ".brpp");
					if (f.getParentFile().mkdirs()) {

						try {
							f.createNewFile();
							FileWriter fw = new FileWriter(f.getAbsoluteFile(),
									false);
							txt.write(fw);
							fw.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				}
			}
		};
		salvarAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		salvarItem.setAction(salvarAction);
		fileMenu.add(salvarItem);
		setJMenuBar(menuBar);

		setVisible(true);
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
					txt.write(fw);
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

	private class UploadHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (diretorio != null) {
				FileWriter fw;
				try {
					fw = new FileWriter(diretorio.getAbsoluteFile(), false);
					txt.write(fw);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (BrppCompiler.compile(diretorio.getAbsolutePath()))
					try {
						if (Uploader.upload(BrppCompiler.getFile(), COM
								.getSelectedItem().toString(),BOARD.getSelectedIndex()))
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
									.getSelectedItem().toString(),BOARD.getSelectedIndex()))
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

	private class CanButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);

		}

	}

}
