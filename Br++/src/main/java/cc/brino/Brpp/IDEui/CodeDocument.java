package cc.brino.Brpp.IDEui;

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

import java.awt.Color;
import java.util.Iterator;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cc.brino.Brpp.Utils.JSONUtils;
import cc.brino.Brpp.Utils.KeywordManagerUtils;

public class CodeDocument extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	private Color verde = new Color(11, 125, 73);
	private Color azul = new Color(66, 119, 255);
	private Color vermelho = new Color(255, 56, 0);
	private Color laranja = new Color(252, 145, 20);
	private String KEYWORDS_1 = "(\\W)*(";
	private String KEYWORDS_2 = "(\\W)*(";
	private String KEYWORDS_3 = "(\\W)*(";
	private String KEYWORDS_4 = "(\\W)*(";
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
		processKeywords();
		container = BrppIDEFrame.getTextPane();
		doc = this;
		rootElement = doc.getDefaultRootElement();

	}

	private void processKeywords() {
		JSONArray Keywords = JSONUtils.getKeywords();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = Keywords.iterator();
		while (iterator.hasNext()) {
			JSONObject key = iterator.next();
			String highType = (String) key.get("highlight-type");
			if (!highType.equals("null")) {
				int type = Integer.parseInt(highType);
				switch (type) {
				case 1:
					KEYWORDS_1 = KEYWORDS_1.concat(((String) key
							.get("highlight")).trim() + "|");
					break;
				case 2:
					KEYWORDS_2 = KEYWORDS_2.concat(((String) key
							.get("highlight")).trim() + "|");
					break;
				case 3:
					KEYWORDS_3 = KEYWORDS_3.concat(((String) key
							.get("highlight")).trim() + "|");
					break;
				case 4:
					KEYWORDS_4 = KEYWORDS_4.concat(((String) key
							.get("highlight")).trim() + "|");
					break;
				}
			}
		}

		KEYWORDS_4 = KEYWORDS_4.concat(KeywordManagerUtils.getKey());
		KEYWORDS_3 = KEYWORDS_3.concat(KeywordManagerUtils.getKeyTwo());
		KEYWORDS_1 = KEYWORDS_1.substring(0, KEYWORDS_1.length() - 1).concat(
				")");
		KEYWORDS_2 = KEYWORDS_2.substring(0, KEYWORDS_2.length() - 1).concat(
				")");
		if(KEYWORDS_3.contains("||")) KEYWORDS_3 = KEYWORDS_3.replace("||", "|");
		if(KEYWORDS_4.contains("||")) KEYWORDS_4 = KEYWORDS_4.replace("||", "|");
		KEYWORDS_3 = KEYWORDS_3.endsWith("|") ? KEYWORDS_3.substring(0,
				KEYWORDS_3.length() - 1).concat(")") : KEYWORDS_3.concat(")");
		KEYWORDS_4 = KEYWORDS_4.endsWith("|") ? KEYWORDS_4.substring(0,
				KEYWORDS_4.length() - 1).concat(")") : KEYWORDS_4.concat(")");
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
