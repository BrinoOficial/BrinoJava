package br.com.RatosDePC.Brpp.IDEui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import br.com.RatosDePC.Brpp.Utils.FileUtils;
import br.com.RatosDePC.Brpp.Utils.KeywordManagerUtils;

public class CodeDocument extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	private Color verde = new Color(11, 125, 73);
	private Color azul = new Color(66, 119, 255);
	private Color vermelho = new Color(255, 56, 0);
	private Color laranja = new Color(252, 145, 20);
	private String KEYWORDS_1 = "(\\W)*(Mudando|Ligando|Desligando|ArquivoGravar|Ligado|Desligado|Numero|NumeroDecimal|Letra|Longo|Palavra|Condicao|Modulo|Constante|Verdadeiro|Falso|SemRetorno|Entrada|Saida)";
	private String KEYWORDS_2 = "(\\W)*(Configuracao|Principal|usar|definir|para|se|enquanto|senao|e|ou|responder)";
	private String KEYWORDS_3 = "(\\W)*(fechar|gravar|descarregar|enviarBinario|Arquivo|existe|criarPasta|abrir|remover|removerPasta|soar|pararSoar|esperar|proporcionar|definirModo|usar|conectar|enviar|enviarln|disponivel|ler|escrever|ler|ligar|desligar|tamanho|formatar|posicao|limpar|conectar|escreverAngulo|escreverMicros|frente|tras|parar|transmitir|pararTransitir|solicitar|solicitado|recebido|conectarInterruptor|desconectarInterruptor|ligarInterruptores|desligarInterruptores|escreverAnalogico|lerAnalogico";
	private String KEYWORDS_4 = "(\\W)*(Memoria|Pino|LCD|USB|I2C|Servo|SD";
	final StyleContext cont = StyleContext.getDefaultStyleContext();
	final AttributeSet attrAzul = cont.addAttribute(cont.getEmptySet(),
			StyleConstants.Foreground, azul);
	final AttributeSet attrVerde = cont.addAttribute(cont.getEmptySet(),
			StyleConstants.Foreground, verde);
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
	private Element rootElement;
	private CodeDocument doc;
	private JTextPane container;

	public CodeDocument() {
		KEYWORDS_4 = KEYWORDS_4.concat(KeywordManagerUtils.getKey());
		KEYWORDS_4 = KEYWORDS_4.concat(")");
		KEYWORDS_3 = KEYWORDS_3.concat(KeywordManagerUtils.getKeyTwo());
		KEYWORDS_3 = KEYWORDS_3.concat(")");
		System.out.println(KEYWORDS_4);
		container = BrppIDEFrame.getTextPane();
		doc = this;
		rootElement = doc.getDefaultRootElement();

	}

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

	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		highlight(offs, "");
	}

	public void insertString(int offset, String str,
			javax.swing.text.AttributeSet a) throws BadLocationException {
		boolean car = false;
		if (str.equals("{")) {
			str = addCloseBrace(offset);
			car = true;
		}
		if (str.equals("(")) {
			str = addCloseParenthesis(offset);
			car = true;
		}
		if (str.equals("\"")) {
			str = addCloseAspas(offset);
			car = true;
		}
		super.insertString(offset, str, a);
		if (car) {
			BrppIDEFrame.posicionarCaret(offset + str.length()
					- (str.contains("{") ? 2 : 1));
		}
		highlight(offset, str);

	}

	public void highlight(int offset, String str) {
		String text = "";
		try {
			text = getText(0, getLength());
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int before = findLastNonWordChar(text, offset);
		if (before < 0)
			before = 0;
		int after = findFirstNonWordChar(text, offset + str.length());
		int wordL = before;
		int wordR = before;
		// boolean comment = false;
		while (wordR <= after) {
			if (wordR == after
					|| String.valueOf(text.charAt(wordR)).matches("\\W")) {
				if (text.substring(wordL, wordR).matches(KEYWORDS_1)) {
					if (text.substring(wordL, wordR).contains("}"))
						setCharacterAttributes(wordL + 1, wordR - wordL,
								attrAzul, false);
					else
						setCharacterAttributes(wordL, wordR - wordL, attrAzul,
								false);

				} else if (text.substring(wordL, wordR).matches(KEYWORDS_2)) {
					if (text.substring(wordL, wordR).contains("}"))
						setCharacterAttributes(wordL + 1, wordR - wordL,
								attrLaranja, false);
					else
						setCharacterAttributes(wordL, wordR - wordL,
								attrLaranja, false);
				} else if (text.substring(wordL, wordR).matches(KEYWORDS_3)) {
					if (text.substring(wordL, wordR).contains("}"))
						setCharacterAttributes(wordL + 1, wordR - wordL,
								attrVermelho, false);
					else
						setCharacterAttributes(wordL, wordR - wordL,
								attrVermelho, false);
				} else if (text.substring(wordL, wordR).matches(KEYWORDS_4)) {
					if (text.substring(wordL, wordR).contains("}")) {
						setCharacterAttributes(wordL + 1, wordR - wordL,
								attrVermelho, false);
						setCharacterAttributes(wordL + 1, wordR - wordL,
								attrBold, false);
					} else {
						setCharacterAttributes(wordL + 1, wordR - wordL,
								attrVermelho, false);
						setCharacterAttributes(wordL + 1, wordR - wordL,
								attrBold, false);
					}
				} else {
					setCharacterAttributes(wordL, wordR - wordL, attrBlack,
							true);
					setCharacterAttributes(wordL, wordR - wordL, attrNoBold,
							true);
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
				beforeC = text.indexOf("\"", afterC + off);
				afterC = text.indexOf("\"", beforeC + 1);
				if (afterC != -1) {
					afterC += 1;
					System.out.println("entrei");
				}
				if (beforeC != -1 && afterC != -1) {
					String string = text.substring(beforeC, afterC);
					if (!string.contains("\n"))
						setCharacterAttributes(beforeC, afterC - beforeC,
								attrAzul, true);
				}
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
					setCharacterAttributes(beforeC, afterC - beforeC, attrAzul,
							true);
				off = 2;
			} while (beforeC > 0 && afterC > 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected String addCloseBrace(int offset) throws BadLocationException {
		StringBuffer wSpace = new StringBuffer();
		int line = rootElement.getElementIndex(offset);
		int i = rootElement.getElement(line).getStartOffset();

		while (true) {
			String temp = doc.getText(i, 1);
			if (temp.equals(" ") || temp.equals("\t")) {
				wSpace.append(temp);
				i++;
			} else
				break;
		}
		return "{\n" + wSpace.toString() + "\t\n" + wSpace.toString() + "}";
	}

	protected String addCloseAspas(int offset) throws BadLocationException {
		StringBuffer wSpace = new StringBuffer();
		int line = rootElement.getElementIndex(offset);
		int i = rootElement.getElement(line).getStartOffset();

		while (true) {
			String temp = doc.getText(i, 1);
			if (temp.equals(" ") || temp.equals("\t")) {
				wSpace.append(temp);
				i++;
			} else
				break;
		}
		return "\"" + wSpace.toString() + "\"";
	}

	protected String addCloseParenthesis(int offset)
			throws BadLocationException {
		StringBuffer wSpace = new StringBuffer();
		int line = rootElement.getElementIndex(offset);
		int i = rootElement.getElement(line).getStartOffset();

		while (true) {
			String temp = doc.getText(i, 1);
			if (temp.equals(" ") || temp.equals("\t")) {
				wSpace.append(temp);
				i++;
			} else
				break;
		}
		return "(" + wSpace.toString() + ")";
	}

}
