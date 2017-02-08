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

/**
 * Janela do IDE e construcao da area de edicao de codigo
 * 
 * @author Mateus Berardo de Souza Terra e Rafael Mascarenhas Dal Moro
 * @contributors 
 * @version 5/2/2016
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;

import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.Utils.FileUtils;

public class BrppIDEFrame extends JFrame {
	private UndoManager undoManager;
	private static final long serialVersionUID = 1L;
	private static JTextPane CODE;
	private ImageIcon logo = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/logo.png"));
	private JMenuBar menuBar;
	private NorthPanel NorthPanel;
	private SouthPanel SouthPanel;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	private Color verde = new Color(72, 155, 0);// 11, 125, 73
	private static final String min = "Configuracao() {\r\n"
			+ "//Coloque aqui seu codigo de Configuracao que sera executado uma vez\r\n"
			+ "\r\n"
			+ "}\r\n"
			+ "Principal(){\r\n"
			+ "//Coloque aqui seu codigo Principal, para rodar repetidamente\r\n"
			+ "\r\n" + "}";
	private static CodeDocument code = new CodeDocument();

	public BrppIDEFrame(String title) {
		super(title);
		this.setIconImage(logo.getImage());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(400, 400);
		pack();
		setLocationRelativeTo(null);
		BorderLayout bl = new BorderLayout();
		setLayout(bl);
		setBackground(verde);
		NorthPanel = new NorthPanel();
		NorthPanel.setBackground(verde);
		NorthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(NorthPanel, BorderLayout.NORTH);
		NorthPanel.setVisible(true);
		SouthPanel = new SouthPanel();
		add(SouthPanel, BorderLayout.SOUTH);
		CODE = new JTextPane(code);
		CODE.setSize(200, 400);
		CODE.setText(getMin());
		JScrollPane CentralPane = new JScrollPane(CODE);
		CODE.setBorder(emptyBorder);
		add(CentralPane, BorderLayout.CENTER);
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		undoManager = new UndoManager();
		Document doc = CODE.getDocument();
		doc.addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());

			}
		});

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				int dialogResult = JOptionPane.showConfirmDialog(
						BrppIDEFrame.this,
						"Você deseja salvar seu rascunho antes de sair?",
						"Salvar", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (FileUtils.getDiretorio() == null) {
						FileUtils.createFile(BrppIDEFrame.getTextPane());
					} else {
						FileUtils.saveFile(BrppIDEFrame.getTextPane());
					}
				}
				try {
					PrefManager.savePrefs();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);

			}
		});
	}

	public static JTextPane getTextPane() {
		return CODE;
	}

	public static String getMin() {
		return min;
	}

	public static void posicionarCaret(int off) {
		CODE.setCaretPosition(off);
	}

	protected static void comentar() throws BadLocationException {
		// TODO Auto-generated method stub
		int car = CODE.getCaretPosition();
		AttributeSet a = null;
		Element lineElement = code.getParagraphElement(car);
		if (!code
				.getText(
						lineElement.getStartOffset(),
						lineElement.getEndOffset()
								- lineElement.getStartOffset()).trim()
				.contains("//"))
			code.insertString(lineElement.getStartOffset(), "//", a);
		else
			code.remove(lineElement.getStartOffset(), 2);
	}
}