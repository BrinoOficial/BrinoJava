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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import cc.brino.Brpp.IDEui.MenuBar.MenuBar;
import cc.brino.Brpp.IDEui.ScrollBar.ScrollLeanUI;
import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.Syntax.BrinoCompletionProvider;
import cc.brino.Brpp.Utils.FileUtils;


public class BrppIDEFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private ImageIcon logo = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/logo.png"));
	private JMenuBar menuBar;
	private WestPanel westPanel;
	private SouthPanel southPanel;
	private JPanel centralPane;
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
		InputStream in = getClass().getResourceAsStream("/cc/brino/Brpp/IDEui/theme/monokai.xml");
		this.setIconImage(logo.getImage());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(400, 400);
		pack();
		setLocationRelativeTo(null);
		BorderLayout bl = new BorderLayout();
		setLayout(bl);
		setBackground(UIConstants.VERDE);
		westPanel = new WestPanel();
		westPanel.setBackground(UIConstants.VERDE);
		add(westPanel, BorderLayout.WEST);
		westPanel.setVisible(true);
		southPanel = new SouthPanel();
		textArea = new RSyntaxTextArea(20, 60);
		try {
			Theme theme = Theme.load(in);
			theme.apply(textArea);
		} catch (IOException ioe) { // Never happens
			ioe.printStackTrace();
		}
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/Brpp",
				"cc.brino.Brpp.Syntax.BrinoSyntax",
				getClass().getClassLoader());
		textArea.setSyntaxEditingStyle("text/Brpp");
		textArea.setCodeFoldingEnabled(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setMarkOccurrences(true);
		CompletionProvider provider = new BrinoCompletionProvider();
		AutoCompletion ac = new AutoCompletion(provider);
		ac.setAutoActivationDelay(2000);
		ac.setAutoCompleteSingleChoices(true);
		ac.setTriggerKey(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
				KeyEvent.CTRL_DOWN_MASK));
		ac.install(textArea);
		textArea.setText(getMin());
		textArea.setBorder(UIConstants.BORDAVAZIA);
		code = new RTextScrollPane(textArea);
		code.setHorizontalScrollBarPolicy(RTextScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		code.setSize(400, 500);
		code.setViewportBorder(UIConstants.BORDATRANSPARENTE);
		code.setBackground(UIConstants.CINZA);
		code.setBorder(UIConstants.BORDATRANSPARENTE);
		JScrollBar sb = code.getVerticalScrollBar();
		sb.setPreferredSize(new Dimension(6, sb.getHeight()));
		sb.setUI(new ScrollLeanUI());
		sb.setBackground(UIConstants.CINZA);
		sb.setBorder(UIConstants.BORDAVAZIA);
		centralPane = new JPanel();
		centralPane.setLayout(new BorderLayout());
		centralPane.add(southPanel, BorderLayout.SOUTH);
		centralPane.add(code, BorderLayout.CENTER);
		centralPane.setBorder(UIConstants.BORDAVAZIA);
		add(centralPane, BorderLayout.CENTER);
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		textArea.addKeyListener(new KeyAdapter() {
		        boolean ctrlPressed = false;
		        boolean slashPressed = false;

		        @Override
		        public void keyPressed(KeyEvent e) {
		            switch(e.getKeyCode()) {
		            case KeyEvent.VK_SLASH:
		                slashPressed=true;

		                break;
		            case KeyEvent.VK_CONTROL:
		                ctrlPressed=true;
		                break;
		            }

		            if(ctrlPressed && slashPressed) {
		                try {
					comentar();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		                e.consume();// Stop the event from propagating.
		            }
		        }
		        
		        @Override
		        public void keyTyped(KeyEvent e) {
		            switch(e.getKeyCode()) {
		            case KeyEvent.VK_SLASH:
		                slashPressed=true;

		                break;
		            case KeyEvent.VK_CONTROL:
		                ctrlPressed=true;
		                break;
		            }

		            if(ctrlPressed && slashPressed) {
		                e.consume();// Stop the event from propagating.
		            }
		        }

		        @Override
		        public void keyReleased(KeyEvent e) {
		            switch(e.getKeyCode()) {
		            case KeyEvent.VK_SLASH:
		                slashPressed=false;

		                break;
		            case KeyEvent.VK_CONTROL:
		                ctrlPressed=false;
		                break;
		            }

		            if(ctrlPressed && slashPressed) {
		                e.consume();// Stop the event from propagating.
		            }
		        }
		    });
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				final JButton okay = new JButton("Sim");
				okay.setBackground(UIConstants.VERDE);
				okay.setOpaque(true);
				okay.setBorder(new BubbleBorder(UIConstants.VERDE));
				okay.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane pane = getOptionPane((JComponent) e.getSource());
						pane.setValue(okay);
					}
				});
				final JButton no = new JButton("Não");
				no.setBackground(Color.RED);
				no.setBorder(new BubbleBorder(Color.RED));
				no.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane pane = getOptionPane((JComponent) e.getSource());
						pane.setValue(no);
					}
				});
				final JButton cancel = new JButton("Cancelar");
				cancel.setBackground(UIConstants.CINZAESCURO);
				cancel.setBorder(new BubbleBorder(UIConstants.CINZAESCURO));
				cancel.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane pane = getOptionPane((JComponent) e.getSource());
						pane.setValue(cancel);
					}
				});
				int dialogResult = JOptionPane.showOptionDialog(BrppIDEFrame.this,
						"Você deseja salvar seu rascunho antes de sair",
						"Salvar",
						JOptionPane.NO_OPTION,
						JOptionPane.PLAIN_MESSAGE,
						null,
						new JButton[] { okay, no,
								cancel },
						okay);
				System.out.println(dialogResult);
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
				if (dialogResult != JOptionPane.CANCEL_OPTION && dialogResult!=-1)
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

	public static void comentar() throws BadLocationException {
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
			// textArea.setCaretPosition(startOffset
			// + index );
			// textArea.moveCaretPosition(startOffset
			// + index + 3);
			textArea.replaceRange("\r\n",
					startOffset + index - 1,
					startOffset + index + 2);
			// textArea.undoLastAction();
			// textArea.replaceRange("",
			// textArea.getCaretPosition(),
			// textArea.getCaretPosition()+1);
			// textArea.replaceSelection("");
		}
	}

	protected JOptionPane getOptionPane(JComponent parent) {
		JOptionPane pane = null;
		if (!(parent instanceof JOptionPane)) {
			pane = getOptionPane((JComponent) parent.getParent());
		} else {
			pane = (JOptionPane) parent;
		}
		return pane;
	}
}
