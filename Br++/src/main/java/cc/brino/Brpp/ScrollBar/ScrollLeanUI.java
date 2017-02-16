package cc.brino.Brpp.ScrollBar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class ScrollLeanUI extends BasicScrollBarUI {
	

	private static Border emptyBorder = BorderFactory.createEmptyBorder();

	public static void main(String[] args) {
		JScrollPane after = makeExamplePane();
		JScrollBar sb = after.getVerticalScrollBar();
		sb.setPreferredSize(new Dimension(6, sb.getHeight()));
		sb.setUI(new ScrollLeanUI());
		sb.setBorder(emptyBorder);
		sb.setBackground(Color.black);
		ScrollLeanUI ui = new ScrollLeanUI();
		ui.trackHighlightColor = Color.white ;
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
		text.setBackground(Color.black);
		text.append("Lorem ipsum dolor sit amet…\n\n\n\n\n\n\n\n\n\n");
		JScrollPane scroll = new JScrollPane(text);
		return scroll;
	}
	
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
		r.setSize(new Dimension(6, r.height));
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = null;
		c.setBorder(emptyBorder);
		if (isDragging) {
			color = new Color(25, 25, 25, 100);
		} else {
			color = new Color(220, 220, 200, 2);
		}
		g2.setStroke(new BasicStroke(0));
		g2.setPaint(color);
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
		g2.dispose();
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = null;
		JScrollBar sb = (JScrollBar) c;
		c.setBorder(emptyBorder);
		if (!sb.isEnabled() || r.width > r.height) {
			return;
		}
		color = Color.green;
		r.setSize(new Dimension(6, r.height));
		g2.setStroke(new BasicStroke(0));
		g2.setPaint(color);
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
		g2.dispose();
	}

	@Override
	protected void setThumbBounds(int x, int y, int width, int height) {
		super.setThumbBounds(x, y, 6, height);
		scrollbar.repaint();
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		return createZeroButton();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		return createZeroButton();
	}

	private JButton createZeroButton() {
		JButton jbutton = new JButton();
		jbutton.setPreferredSize(new Dimension(0, 0));
		jbutton.setMinimumSize(new Dimension(0, 0));
		jbutton.setMaximumSize(new Dimension(0, 0));
		return jbutton;
	}
}