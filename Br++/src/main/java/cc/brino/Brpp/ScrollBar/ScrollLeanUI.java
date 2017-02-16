package cc.brino.Brpp.ScrollBar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.metal.MetalScrollBarUI;


public class ScrollLeanUI extends MetalScrollBarUI {

	public static void main(String[] args) {
		JScrollPane after = makeExamplePane();
		JScrollBar sb = after.getVerticalScrollBar();
		sb.setUI(new ScrollLeanUI());
		after.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new GridLayout(0, 1));
		f.add(after);
		f.pack();
		f.setSize(400, 400);
		f.setVisible(true);
	}

	private static JScrollPane makeExamplePane() {
		JTextArea text = new JTextArea(16, 16);
		text.append("Lorem ipsum dolor sit amet…");
		JScrollPane scroll = new JScrollPane(text);
		return scroll;
	}

	protected void paintThumb(Graphics g,
			JComponent c,
			Rectangle thumbBounds) {
		g.setColor(Color.green);
		g.fillRoundRect(thumbBounds.x,
				thumbBounds.y,
				6,
				thumbBounds.height,
				3,
				6);
	}

	protected void paintTrack(Graphics g,
			JComponent c,
			Rectangle trackBounds) {
		g.setColor(Color.gray);
		g.fillRoundRect(trackBounds.x,
				trackBounds.y,
				6,
				trackBounds.height,
				3,
				6);
	}
}