package br.com.RatosDePC.Brpp.IDEui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

public class SouthPanel extends JPanel {
	private static JLabel placaCom;
	private static final long serialVersionUID = 1L;
	private Color branco = new Color(255, 255, 255);
	private Color verde = new Color(72, 155, 0);//11, 125, 73
	public static JTextArea LOG = new JTextArea(5, 10);
	public static JScrollPane LogPanel = new JScrollPane(LOG);
	
	public SouthPanel() {
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		add(LogPanel, BorderLayout.CENTER);
		LogPanel.setBackground(branco);
		DefaultCaret caret = (DefaultCaret) LOG.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		placaCom = new JLabel("Arduino Uno na COM1");
		setBackground(verde);
		placaCom.setForeground(branco);
		Font font = placaCom.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.ITALIC, font.getSize()-2);
		placaCom.setFont(boldFont);
		add(placaCom,BorderLayout.SOUTH);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		placaCom.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	public static void updatePlacaCom(String Board, String com){
		placaCom.setText(Board+" na "+com);
	}

}
