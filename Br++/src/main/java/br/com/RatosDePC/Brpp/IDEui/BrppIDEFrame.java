package br.com.RatosDePC.Brpp.IDEui;

/**
 * Janela do IDE e construção da área de edição de código
 * 
 * @author Mateus Berardo de Souza Terra e Rafael Mascarenhas Dal Moro
 * @contributors 
 * @version 5/2/2016
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class BrppIDEFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	static JTextPane txt = null;
	private ImageIcon logo = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/logo.png"));
	private JMenuBar menuBar;
	private JPanel NorthPanel;
	public static JTextArea LOG = new JTextArea(5, 10);
	public static JScrollPane SouthPanel = new JScrollPane(LOG);
	Border emptyBorder = BorderFactory.createEmptyBorder();
	private Color GREEN = new Color(11, 125, 73);
	private Color WHITE = new Color(255, 255, 255);
	private Color azul = new Color(66, 119, 255);
	private Color vermelho = new Color(255, 56, 0);
	private Color laranja = new Color(252, 145, 20);
	private static final String min = "Configuracao() {\r\n"
			+ "//Coloque aqui seu codigo de Configuracao que sera executado uma vez\r\n"
			+ "\r\n"
			+ "}\r\n"
			+ "Principal(){\r\n"
			+ "//Coloque aqui seu codigo Principal, para rodar repetidamente\r\n"
			+ "\r\n" + "}\r\n";

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
		this.setIconImage(logo.getImage());
		DefaultCaret caret = (DefaultCaret) LOG.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 400);
		pack();
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
		final AttributeSet attrAzul = cont.addAttribute(cont.getEmptySet(),
				StyleConstants.Foreground, azul);
		final AttributeSet attrVerde = cont.addAttribute(cont.getEmptySet(),
				StyleConstants.Foreground, GREEN);
		final AttributeSet attrLaranja = cont.addAttribute(cont.getEmptySet(),
				StyleConstants.Foreground, laranja);
		final AttributeSet attrVermelho = cont.addAttribute(cont.getEmptySet(),
				StyleConstants.Foreground, vermelho);
		final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(),
				StyleConstants.Foreground, Color.BLACK);
		final AttributeSet attrBold = cont.addAttribute(cont.getEmptySet(),
				StyleConstants.Bold, true);
		final AttributeSet attrNoBold = cont.addAttribute(cont.getEmptySet(),
				StyleConstants.Bold, false);
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			private static final long serialVersionUID = 1L;
			private String KEYWORDS_1 = "(\\W)*(Mudando|Ligando|Desligando|ArquivoGravar|Ligado|Desligado|Numero|NumeroDecimal|Letra|Longo|Palavra|Condicao|Modulo|Constante|Verdadeiro|Falso|SemRetorno|Entrada|Saida)";
			private String KEYWORDS_2 = "(\\W)*(Configuracao|Principal|usar|definir|para|se|enquanto|senao|e|ou|responder)";
			private String KEYWORDS_3 = "(\\W)*(fechar|gravar|descarregar|enviarBinario|Arquivo|existe|criarPasta|abrir|remover|removerPasta|soar|pararSoar|esperar|proporcionar|definirModo|usar|conectar|enviar|enviarln|disponivel|ler|escrever|ler|ligar|desligar|tamanho|formatar|posicao|limpar|conectar|escreverAngulo|escreverMicros|frente|tras|parar|transmitir|pararTransitir|solicitar|solicitado|recebido|conectarInterruptor|desconectarInterruptor|ligarInterruptores|desligarInterruptores)";
			private String KEYWORDS_4 = "(\\W)*(Memoria|Pino|LCD|USB|I2C|Servo|SD)";

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
						if (text.substring(wordL, wordR).matches(KEYWORDS_1)) {
							if (text.substring(wordL, wordR).contains("}"))
								setCharacterAttributes(wordL + 1,
										wordR - wordL, attrAzul, false);
							else
								setCharacterAttributes(wordL, wordR - wordL,
										attrAzul, false);

						} else if (text.substring(wordL, wordR).matches(
								KEYWORDS_2)) {
							if (text.substring(wordL, wordR).contains("}"))
								setCharacterAttributes(wordL + 1,
										wordR - wordL, attrLaranja, false);
							else
								setCharacterAttributes(wordL, wordR - wordL,
										attrLaranja, false);
						} else if (text.substring(wordL, wordR).matches(
								KEYWORDS_3)) {
							if (text.substring(wordL, wordR).contains("}"))
								setCharacterAttributes(wordL + 1,
										wordR - wordL, attrVermelho, false);
							else
								setCharacterAttributes(wordL, wordR - wordL,
										attrVermelho, false);
						} else if (text.substring(wordL, wordR).matches(
								KEYWORDS_4)) {
							if (text.substring(wordL, wordR).contains("}")) {
								setCharacterAttributes(wordL + 1,
										wordR - wordL, attrVermelho, false);
								setCharacterAttributes(wordL + 1,
										wordR - wordL, attrBold, false);
							} else {
								setCharacterAttributes(wordL + 1,
										wordR - wordL, attrVermelho, false);
								setCharacterAttributes(wordL + 1,
										wordR - wordL, attrBold, false);
							}
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
				try {
					int beforeC = 0;
					int afterC = 0;
					int off = 0;
					do {
						beforeC = text.indexOf("//", beforeC + off);
						afterC = text.indexOf("\n", beforeC);
						if (beforeC != -1)
							setCharacterAttributes(beforeC, afterC - beforeC,
									attrVerde, true);
						off = 2;
					} while (beforeC != -1);
					beforeC = 0;
					afterC = 0;
					off = 0;
					do {
						beforeC = text.indexOf("/* ", beforeC + off);
						afterC = text.indexOf("*/", beforeC) + 2;
						if (beforeC != -1)
							setCharacterAttributes(beforeC, afterC - beforeC,
									attrVerde, true);
						off = 2;
					} while (beforeC != -1);
					beforeC = 0;
					afterC = 0;
					off = 0;
					do {
						beforeC = text.indexOf("\"", afterC+off);
						afterC = text.indexOf("\"", beforeC+1);
						if (afterC != -1){
							afterC += 1;
							System.out.println("entrei");
						}
						String string = text.substring(beforeC, afterC);
						if (beforeC != -1 && afterC != -1 && !string.contains("\n"))
							setCharacterAttributes(beforeC, afterC - beforeC,
									attrAzul, true);
						off = 1;
					} while (beforeC > 0 && afterC > 0);
					beforeC = 0;
					afterC = 0;
					off = 0;
					do {
						beforeC = text.indexOf("'", beforeC + off);
						afterC = text.indexOf("'", beforeC + 1);
						if (afterC != -1)
							afterC += 1;
						if (beforeC != -1 && afterC != -1)
							setCharacterAttributes(beforeC, afterC - beforeC,
									attrAzul, true);
						off = 2;
					} while (beforeC > 0 && afterC > 0);
				} catch (Exception e) {
					e.printStackTrace();
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
					setCharacterAttributes(before, after - before, attrAzul,
							false);
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