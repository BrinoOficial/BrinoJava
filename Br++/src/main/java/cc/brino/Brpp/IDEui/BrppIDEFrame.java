package cc.brino.Brpp.IDEui;

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
 * Janela do IDE e construcao da area de edicao de codigo
 * 
 * @author Mateus Berardo de Souza Terra e Rafael
 *         Mascarenhas Dal Moro
 * @contributors
 * @version 5/2/2016
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.Utils.FileUtils;


public class BrppIDEFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private ImageIcon logo = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/logo.png"));
	private JMenuBar menuBar;
	private WestPanel westPanel;
	private SouthPanel southPanel;
	private JPanel centralPane;
	private final Color cinza = new Color(46, 46, 46);
	Border emptyBorder = BorderFactory.createEmptyBorder();
	Border translucidBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
	private Color verde = new Color(72, 155, 0);// 11,
							// 125,
							// 73
	private static RSyntaxTextArea textArea;
	private RTextScrollPane code;
	private static final String min = "Configuracao() {\r\n"
			+ "//Coloque aqui seu codigo de Configuracao que sera executado uma vez\r\n"
			+ "\r\n"
			+ "}\r\n"
			+ "Principal(){\r\n"
			+ "//Coloque aqui seu codigo Principal, para rodar repetidamente\r\n"
			+ "\r\n" + "}";

	public BrppIDEFrame(String title) {
		super(title);
		this.setIconImage(logo.getImage());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(400, 400);
		pack();
		setLocationRelativeTo(null);
		BorderLayout bl = new BorderLayout();
		setLayout(bl);
		// eastPanel = new JPanel();
		// eastPanel.setMinimumSize(new
		// Dimension(10,1));
		// add(eastPanel, BorderLayout.EAST);
		setBackground(verde);
		westPanel = new WestPanel();
		westPanel.setBackground(verde);
		add(westPanel, BorderLayout.WEST);
		westPanel.setVisible(true);
		southPanel = new SouthPanel();
		textArea = new RSyntaxTextArea(20, 60);
		try {
			Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"));
			theme.apply(textArea);
		} catch (IOException ioe) { // Never happens
			ioe.printStackTrace();
		}
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/myLanguage",
				"cc.brino.Brpp.Syntax.BrinoSyntax",
				getClass().getClassLoader());
		textArea.setSyntaxEditingStyle("text/myLanguage");
		textArea.setCodeFoldingEnabled(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText(getMin());
		textArea.setBorder(emptyBorder);
		code = new RTextScrollPane(textArea);
		code.setHorizontalScrollBarPolicy(RTextScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		code.setSize(400, 500);
		code.setViewportBorder(translucidBorder);
		code.setBackground(cinza);
		code.setBorder(translucidBorder);
		centralPane = new JPanel();
		centralPane.setLayout(new BorderLayout());
		centralPane.add(southPanel, BorderLayout.SOUTH);
		centralPane.add(code, BorderLayout.CENTER);
		centralPane.setBorder(emptyBorder);
		add(centralPane, BorderLayout.CENTER);
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				int dialogResult = JOptionPane.showConfirmDialog(BrppIDEFrame.this,
						"Você deseja salvar seu rascunho antes de sair?",
						"Salvar",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (FileUtils.getDiretorio() == null) {
						FileUtils.createFile(BrppIDEFrame.getTextArea());
					} else {
						FileUtils.saveFile(BrppIDEFrame.getTextArea());
					}
				}
				try {
					PrefManager.savePrefs();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
	}

	public static RSyntaxTextArea getTextArea() {
		return textArea;
	}

	public static String getMin() {
		return min;
	}

	public static void posicionarCaret(int off) {
		textArea.setCaretPosition(off);
	}

	protected static void comentar() throws BadLocationException {
		int car = textArea.getCaretPosition();
		int lineNumber = textArea.getLineOfOffset(car);
		int startOffset = textArea.getLineStartOffset(lineNumber);
		int endOffset = textArea.getLineEndOffset(lineNumber);
		if (!textArea.getText(startOffset, endOffset - startOffset)
				.trim()
				.contains("//"))
			textArea.insert("//", startOffset);
		else {
			String line = textArea.getText(startOffset, endOffset
					- startOffset);
			int index = line.indexOf("//");
			textArea.setCaretPosition(startOffset + index);
			textArea.moveCaretPosition(startOffset + index + 2);
			textArea.replaceSelection("");
		}
	}
}