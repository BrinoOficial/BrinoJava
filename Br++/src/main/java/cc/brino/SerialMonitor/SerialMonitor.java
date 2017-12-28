package cc.brino.SerialMonitor;

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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import cc.brino.Brpp.IDEui.BubbleBorder;
import cc.brino.Brpp.IDEui.SouthPanel;
import cc.brino.Brpp.IDEui.ScrollBar.ScrollLeanUI;
import cc.brino.Brpp.Utils.CommPortUtils;


@SuppressWarnings("serial")
public class SerialMonitor extends JFrame {

	public static boolean isOpen = false, connected = true;
	private final Color cinza = new Color(46, 46, 46),
			cinzaEscuro = new Color(30, 30, 30), verde = new Color(
					72, 155, 0);
	private final Border roundedBorder = new LineBorder(cinzaEscuro, 15,
			true),
			translucidBorder = BorderFactory.createEmptyBorder(2,
					5,
					5,
					5),
			emptyBorder = BorderFactory.createEmptyBorder();
	private BorderLayout main = new BorderLayout();
	private JTextPane Jp;
	private static JTextArea OUT = new JTextArea(600, 500);
	private JScrollPane out = new JScrollPane(OUT);
	private JPanel NorthPanel;
	private static JCheckBox autorolagem = new JCheckBox("Auto-rolagem");
	private JButton Enviar = new JButton("Enviar");
	static String messageString = "Hello, world!\n";
	private CommPortUtils PortUtils;

	public SerialMonitor(String com) throws TooManyListenersException {
		// TODO Auto-generated constructor stub
		super("Monitor Serial");
		PortUtils = new CommPortUtils();
		isOpen = true;
		connected = true;
		if (!PortUtils.openPort(com)) {
			SouthPanel.getLOG()
					.append("A porta selecionada não está disponível!\r\n");
			dispatchEvent(new WindowEvent(this,
					WindowEvent.WINDOW_CLOSING));
			setVisible(false);
			dispose();
			connected = false;
			isOpen = false;
		}
		this.setLayout(main);
		setBackground(cinza);
		main.setHgap(10);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OUT.setBackground(cinzaEscuro);
		OUT.setForeground(Color.WHITE);
		OUT.setBorder(emptyBorder);
		OUT.setText("");
		OUT.setEditable(false);
		Action enviaAction = new AbstractAction("Enviar") {

			private static final long serialVersionUID = -1528090166842624429L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated
				// method stub
				String msg = Jp.getText();
				PortUtils.send(msg);
				Jp.setText("");
			}
		};
		enviaAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
						KeyEvent.CTRL_DOWN_MASK));
		Enviar.setAction(enviaAction);
		Enviar.setBorder(new BubbleBorder(verde));
		Enviar.setBackground(verde);
		Enviar.setForeground(Color.WHITE);
		Enviar.setOpaque(true);
		Jp = new JTextPane();
		Jp.setBorder(new BubbleBorder(cinzaEscuro));
		Jp.setEditable(true);
		Jp.setOpaque(true);
		Jp.setBackground(cinzaEscuro);
		Jp.setForeground(Color.WHITE);
		Jp.setCaretColor(Color.WHITE);
		JScrollBar sb = out.getVerticalScrollBar();
		sb.setPreferredSize(new Dimension(6, sb.getHeight()));
		sb.setUI(new ScrollLeanUI());
		sb.setBackground(cinza);
		sb.setBorder(emptyBorder);
		JPanel centerPanel = new JPanel(new BorderLayout());
		out.setBorder(roundedBorder);
		out.setBackground(cinza);
		out.setForeground(cinzaEscuro);
		out.setViewportBorder(emptyBorder);
		out.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		out.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		centerPanel.add(out, BorderLayout.CENTER);
		centerPanel.setBorder(translucidBorder);
		centerPanel.setBackground(cinza);
		add(centerPanel, BorderLayout.CENTER);
		autorolagem.setBackground(cinza);
		autorolagem.setForeground(Color.WHITE);
		add(autorolagem, BorderLayout.SOUTH);
		BorderLayout NLayout = new BorderLayout();
		NLayout.setHgap(5);
		NorthPanel = new JPanel();
		NorthPanel.setLayout(NLayout);
		NorthPanel.setBackground(cinza);
		NorthPanel.add(Jp, BorderLayout.CENTER);
		NorthPanel.add(Enviar, BorderLayout.EAST);
		NorthPanel.setBorder(translucidBorder);
		NorthPanel.setSize(getWidth(), 10);
		add(NorthPanel, BorderLayout.NORTH);
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					CommPortUtils.closePort();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} finally {
					isOpen = false;
				}
			}
		});
	}

	public static void display(String string) {
		OUT.append(string);
		if (autorolagem.isSelected()) {
			OUT.setCaretPosition(OUT.getDocument().getLength());
		}
	}

	public boolean getConnected() {
		return connected;
	}
}
