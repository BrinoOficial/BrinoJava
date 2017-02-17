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
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.ScrollBar.ScrollLeanUI;


public class SouthPanel extends JPanel {

	private static JLabel placaCom;
	private static final long serialVersionUID = 1L;
	private Color branco = new Color(255, 255, 255);
	private Color verde = new Color(72, 155, 0);// 11,
							// 125,
							// 73
	private final Color cinza = new Color(46, 46, 46);
	private final Color cinzaEscuro = new Color(30, 30, 30);
	private final Border roundedBorder = new LineBorder(cinzaEscuro, 15,
			true);
	private Border translucidBorder = BorderFactory.createEmptyBorder(2,
			5,
			5,
			5);
	private Border emptyBorder = BorderFactory.createEmptyBorder();
	public static JTextArea LOG = new JTextArea(5, 10);
	public static JScrollPane LogPanel = new JScrollPane(LOG);

	public SouthPanel() {
		setLayout(new BorderLayout());
		setBackground(cinza);
		JPanel centralPanel = new JPanel();
		centralPanel.setBackground(cinza);
		centralPanel.setBorder(translucidBorder);
		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(LogPanel, BorderLayout.CENTER);
		LogPanel.setBorder(roundedBorder);
		LogPanel.setBackground(cinza);
		LogPanel.setForeground(cinzaEscuro);
		LogPanel.setViewportBorder(emptyBorder);
		JScrollBar sb = LogPanel.getVerticalScrollBar();
		sb.setPreferredSize(new Dimension(6, sb.getHeight()));
		sb.setUI(new ScrollLeanUI());
		sb.setBackground(cinza);
		sb.setBorder(emptyBorder);
		LogPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		LOG.setLineWrap(true);
		LOG.setEditable(false);
		LOG.setFocusable(false);
		LOG.setForeground(branco);
		LOG.setBackground(cinzaEscuro);
		LOG.setBorder(roundedBorder);
		add(centralPanel, BorderLayout.CENTER);
		DefaultCaret caret = (DefaultCaret) LOG.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		placaCom = new JLabel(PrefManager.getPref("placa.nome")
				+ " na " + PrefManager.getPref("porta"));
		placaCom.setForeground(branco);
		placaCom.setBorder(translucidBorder);
		Font font = placaCom.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.ITALIC
				+ Font.BOLD, font.getSize() - 2);
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
}
