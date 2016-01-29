package br.com.Mateus.Brpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Highlight2 extends JFrame {

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem novoItem;
	private JMenuItem salvarItem;
	private JMenuItem abrirItem;
	private File diretorio = null;

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

	public Highlight2() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);

		Color azul = new Color(66, 119, 255);
		Color laranja = new Color(255, 56, 0);
		Color corBosta = new Color(252, 201, 25);
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final javax.swing.text.AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				azul);
		final javax.swing.text.AttributeSet attri = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				corBosta);
		final javax.swing.text.AttributeSet attrib = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				laranja);
		final javax.swing.text.AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.BLACK);
		final javax.swing.text.AttributeSet attrBold = cont.addAttribute(cont.getEmptySet(), StyleConstants.Bold, true);

		final javax.swing.text.AttributeSet attrNoBold = cont.addAttribute(cont.getEmptySet(), StyleConstants.Bold,
				false);
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			public void insertString(int offset, String str, javax.swing.text.AttributeSet a)
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
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
						if (text.substring(wordL, wordR).matches(
								"(\\W)*(private|public|protected|Numero|NumeroDecimal|Longo|Palavra|Condicao|Modulo|Constante|Verdadeiro|Falso|SemRetorno|ENTRADA|SAIDA)"))
							setCharacterAttributes(wordL, wordR - wordL, attr, false);
						else if (text.substring(wordL, wordR).matches(
								"(\\W)*(Configuracao|Principal|usar|definir|para|se|enquanto|senao|faca|e|ou|responder)"))
							setCharacterAttributes(wordL, wordR - wordL, attri, false);
						else if (text.substring(wordL, wordR).matches(
								"(\\W)*(soar|pararSoar|esperar|proporcionar|definirModo|usar|conectar|enviar|enviarln|disponivel|ler|escrever|ler|ligar|desligar|tamanho|formatar|posicao|limpar|conectar|escreverAngulo|escreverMicros|frente|tras|parar|transmitir|pararTransitir|solicitar|solicitado|recebido)"))
							setCharacterAttributes(wordL, wordR - wordL, attrib, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*(Memoria|Pino|LCD|USB|I2C|Servo)")) {
							setCharacterAttributes(wordL, wordR - wordL, attrib, false);
							setCharacterAttributes(wordL, wordR - wordL, attrBold, false);
						} else {
							setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
							setCharacterAttributes(wordL, wordR - wordL, attrNoBold, false);
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

				if (text.substring(before, after).matches("(\\W)*(private|public|protected)")) {
					setCharacterAttributes(before, after - before, attr, false);
				} else {
					setCharacterAttributes(before, after - before, attrBlack, false);
				}
			}
		};

		final JTextPane txt = new JTextPane(doc);
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
				String name = JOptionPane.showInputDialog("Qual o nome do rascunho?");
				File f = new File("C://Arduino/Brino/" + name + "/" + name + ".brpp");
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
		novoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
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
						Scanner in = new Scanner(new File(diretorio.getAbsolutePath()));
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
					JOptionPane.showMessageDialog(null, "Voce nao selecionou nenhum diretorio.");
			}
		};
		abrirAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
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
					String name = JOptionPane.showInputDialog("Qual o nome do rascunho?");
					File f = new File("C://Arduino/Brino/" + name + "/" + name + ".brpp");
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
				}
			}
		};
		salvarAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		salvarItem.setAction(salvarAction);
		fileMenu.add(salvarItem);
		setJMenuBar(menuBar);

		setVisible(true);
	}

	public static void main(String args[]) {
		new Highlight2();
	}

}
