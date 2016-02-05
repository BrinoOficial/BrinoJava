package br.com.RatosDePC.Brpp.IDEui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class BrppIDEFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	static JTextPane txt = null;
	private JMenuBar menuBar;
	private JPanel NorthPanel;
	public static JTextArea LOG = new JTextArea(5, 10);
	private JScrollPane SouthPanel = new JScrollPane(LOG);
	Border emptyBorder = BorderFactory.createEmptyBorder();
	private Color GREEN = new Color(11, 125, 73);
	private Color WHITE = new Color(255, 255, 255);
	private Color azul = new Color(66, 119, 255);
	private Color laranja = new Color(255, 56, 0);
	private Color vermelho = new Color(252, 145, 20);
	private static final String min = "Configuracao() {\n"
			+ "//Coloque aqui seu codigo de Configuracao que sera executado uma vez\n"
			+ "\n" + "}\n" + "Principal(){\n"
			+ "//Coloque aqui seu codigo Principal, para rodar repetidamente\n"
			+ "\n" + "}\n";

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

//	private int findFirstCommentChar(String text, int index) {
//		while (index < text.length()) {
//			if (String.valueOf(text.charAt(index)).matches("\\")) {
//				break;
//			}
//			index++;
//		}
//		return index;
//	}
//
//	private int findLastCommentChar(String text, int index) {
//		while (--index >= 0) {
//			if (String.valueOf(text.charAt(index)).matches("\n")) {
//				break;
//			}
//		}
//		return index;
//	}

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
		BorderLayout bl = new BorderLayout();
		setLayout(bl);
		setBackground(GREEN);
		NorthPanel = new NorthPanel();
		NorthPanel.setBackground(GREEN);
		NorthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(NorthPanel, BorderLayout.NORTH);
		NorthPanel.setVisible(true);
		add(SouthPanel, BorderLayout.SOUTH);
		SouthPanel.setBackground(WHITE);
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final AttributeSet attr = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, azul);
		final AttributeSet attri = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, vermelho);
		final AttributeSet attrib = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, laranja);
		final AttributeSet attrBlack = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
		final AttributeSet attrBold = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Bold, true);
		final AttributeSet attrNoBold = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Bold, false);
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			private static final long serialVersionUID = 1L;
			private String KEYWORDS_1 = "(\\W)*(Ligado|Desligado|Numero|NumeroDecimal|Longo|Palavra|Condicao|Modulo|Constante|Verdadeiro|Falso|SemRetorno|Entrada|Saida)";
			private String KEYWORDS_2 = "(\\W)*(Configuracao|Principal|usar|definir|para|se|enquanto|senao|faca|e|ou|responder)";
			private String KEYWORDS_3 = "(\\W)*(soar|pararSoar|esperar|proporcionar|definirModo|usar|conectar|enviar|enviarln|disponivel|ler|escrever|ler|ligar|desligar|tamanho|formatar|posicao|limpar|conectar|escreverAngulo|escreverMicros|frente|tras|parar|transmitir|pararTransitir|solicitar|solicitado|recebido)";
			private String KEYWORDS_4 = "(\\W)*(Memoria|Pino|LCD|USB|I2C|Servo)";
			private String KEYWORDS_5 = "(\\W)*(//)";

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
				boolean comment = false;
				while (wordR <= after) {
					if (wordR == after
							|| String.valueOf(text.charAt(wordR))
									.matches("\\W")) {
						if (text.substring(wordL, wordR).matches(KEYWORDS_1) && comment==false)
							setCharacterAttributes(wordL, wordR - wordL, attr,
									false);
						else if (text.substring(wordL, wordR).matches(KEYWORDS_5)){
							setCharacterAttributes(wordL, wordR - wordL, attrBlack,
									false);
							comment=true;
						}
						else if (text.substring(wordL, wordR).matches(
								KEYWORDS_2)&&comment==false)
							setCharacterAttributes(wordL, wordR - wordL, attri,
									false);
						else if (text.substring(wordL, wordR).matches(
								KEYWORDS_3) && comment == false)
							setCharacterAttributes(wordL, wordR - wordL,
									attrib, false);
						else if (text.substring(wordL, wordR).matches(
								KEYWORDS_4) && comment == false) {
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
		txt.setText(getMin());
		add(new JScrollPane(txt), BorderLayout.CENTER);
		txt.setBackground(WHITE);
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
	}



	public static JTextPane getTextPane() {
		return txt;
	}

	public static String getMin() {
		return min;
	}
}