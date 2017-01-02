package cc.brino.SerialMonitor;

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.TooManyListenersException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import cc.brino.Brpp.IDEui.SouthPanel;
import cc.brino.Brpp.Utils.CommPortUtils;

@SuppressWarnings("serial")
public class SerialMonitor extends JFrame {

	public static boolean isOpen = false;
	private boolean connected = true;
	private BorderLayout main = new BorderLayout();
	private JTextPane Jp;
	private static JTextArea OUT = new JTextArea(600, 500);
	private JScrollPane out = new JScrollPane(OUT);
	private static JCheckBox autorolagem = new JCheckBox("Auto-rolagem");
	private JButton Enviar = new JButton("Enviar");
	static String messageString = "Hello, world!\n";

	public SerialMonitor(String com) throws TooManyListenersException {
		// TODO Auto-generated constructor stub
		super("Monitor Serial");
		isOpen = true;
		OUT.setText("");
		this.setLayout(main);
		if (!CommPortUtils.openPort(com)) {
//			System.out.println("A porta selecionada não está disponível!");
			SouthPanel.LOG
					.append("A porta selecionada não está disponível!\r\n");
			dispatchEvent(new WindowEvent(this,
					WindowEvent.WINDOW_CLOSING));
			setVisible(false);
			dispose();
			connected=false;
		}
		Jp = new JTextPane();
		Action enviaAction = new AbstractAction("Envia") {
			private static final long serialVersionUID = -1528090166842624429L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String msg = Jp.getText();
				CommPortUtils.send(msg);
				Jp.setText("");
			}
		};
		enviaAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK));
		Enviar.setAction(enviaAction);
		Border b = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Jp.setBorder(BorderFactory.createLineBorder(Color.black));
		Jp.setEditable(true);
		out.setBorder(BorderFactory.createCompoundBorder(b,
				BorderFactory.createLineBorder(Color.black, 1)));
		out.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		out.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(out, BorderLayout.CENTER);
		add(autorolagem, BorderLayout.SOUTH);
		OUT.setEditable(false);
		JPanel NorthPanel = new JPanel();
		BorderLayout NLayout = new BorderLayout();
		NorthPanel.setLayout(NLayout);
		NLayout.setHgap(5);
		NorthPanel.add(Jp, BorderLayout.CENTER);
		NorthPanel.add(Enviar, BorderLayout.EAST);
		NorthPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.black, 0), b));
		add(NorthPanel, BorderLayout.NORTH);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					CommPortUtils.closePort();
				} catch (NullPointerException e) {
//					System.out.println("Não há portas abertas!");
				} finally {
					isOpen = false;
				}
			}
		});
	}

	public static void display(String string) {
		// TODO Auto-generated method stub
		OUT.append(string);
		if(autorolagem.isSelected()){
			OUT.setCaretPosition(OUT.getDocument().getLength());
		}
	}

	public boolean getConnected() {
		// TODO Auto-generated method stub
		return connected;
	}


}