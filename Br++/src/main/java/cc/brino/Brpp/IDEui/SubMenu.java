package cc.brino.Brpp.IDEui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.JMenu;


public class SubMenu extends JMenu implements MouseListener {

	/**
	 * 
	 */
	private Color color = Color.black;
	private static final long serialVersionUID = 1L;

	public void setColor(Color color){
		this.color=color;
	}

	public SubMenu() {
		super();
	}

	public SubMenu(String arg0) {
		super(arg0);
	}

	public SubMenu(Action arg0) {
		super(arg0);
	}

	public SubMenu(String arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setOpaque(true);
		setBackground(color);
		setBorder(UIConstants.BORDAVAZIA);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		setColor(UIConstants.CINZA);
		setBackground(color);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		setColor(Color.black);
		setBackground(color);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
