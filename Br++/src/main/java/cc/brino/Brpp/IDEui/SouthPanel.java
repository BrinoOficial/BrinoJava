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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;
import cc.brino.Brpp.IDEui.ScrollBar.ScrollLeanUI;
import cc.brino.Brpp.Pref.PrefManager;


public class SouthPanel extends JPanel {

	private static JLabel placaCom;
	private static Font font, boldFont;
	private static final long serialVersionUID = 1L;
	private static JTextArea LOG = new JTextArea(5, 10);
	private static JScrollPane LogPanel = new JScrollPane(LOG);
	private static JPanel centralPanel; 

	public SouthPanel() {
		setLayout(new BorderLayout());
		setBackground(UIConstants.CINZA);
		centralPanel = new JPanel();
		centralPanel.setBackground(UIConstants.CINZA);
		centralPanel.setBorder(UIConstants.BORDATRANSPARENTE);
		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(LogPanel, BorderLayout.CENTER);
		LogPanel.setBorder(UIConstants.BORDACINZAESCUROARREDONDADA);
		LogPanel.setBackground(UIConstants.CINZA);
		LogPanel.setForeground(UIConstants.CINZAESCURO);
		LogPanel.setViewportBorder(UIConstants.BORDAVAZIA);
		JScrollBar sb = LogPanel.getVerticalScrollBar();
		sb.setPreferredSize(new Dimension(6, sb.getHeight()));
		sb.setUI(new ScrollLeanUI());
		sb.setBackground(UIConstants.CINZA);
		sb.setBorder(UIConstants.BORDAVAZIA);
		LogPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		LOG.setLineWrap(true);
		LOG.setEditable(false);
		LOG.setFocusable(false);
		LOG.setForeground(Color.white);
		LOG.setBackground(UIConstants.CINZAESCURO);
		LOG.setBorder(UIConstants.BORDACINZAESCUROARREDONDADA);
		add(centralPanel, BorderLayout.CENTER);
		DefaultCaret caret = (DefaultCaret) LOG.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		placaCom = new JLabel(PrefManager.getPref("placa.nome")
				+ " na " + PrefManager.getPref("porta"));
		placaCom.setForeground(Color.white);
		placaCom.setBorder(UIConstants.BORDATRANSPARENTE);
		font = placaCom.getFont();
		boldFont = new Font(font.getFontName(),
				Font.ITALIC + Font.BOLD, font.getSize() - 2);
		placaCom.setFont(boldFont);
		add(placaCom, BorderLayout.SOUTH);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		placaCom.setHorizontalAlignment(SwingConstants.RIGHT);
	}

	public static void updatePlacaCom(String Board, String com) {
		placaCom.setText(Board + " na " + com);
	}

	public static JTextArea getLOG() {
		return LOG;
	}

	public static void scroll(int value) {
		LogPanel.getVerticalScrollBar().setValue(value);
	}
}