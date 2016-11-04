package br.com.RatosDePC.Brpp.IDEui;

/**
 * Janela do IDE e construção da área de edição de código
 * 
 * @author Mateus Berardo de Souza Terra e Rafael Mascarenhas Dal Moro
 * @contributors 
 * @version 5/2/2016
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

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
import javax.swing.text.Element;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import javax.swing.undo.UndoManager;

import br.com.RatosDePC.Brpp.Utils.FileUtils;

public class BrppIDEFrame extends JFrame {
	protected UndoHandler undoHandler;
	protected UndoManager undoManager;
	private UndoAction undoAction = null;
	private RedoAction redoAction = null;
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
		undoHandler = new UndoHandler();
		code.addUndoableEditListener(undoHandler);
		KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_DOWN_MASK);
		KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				KeyEvent.CTRL_DOWN_MASK);
		undoAction = new UndoAction();
		CODE.getInputMap().put(undoKeystroke, "undoKeystroke");
		CODE.getActionMap().put("undoKeystroke", undoAction);
		redoAction = new RedoAction();
		CODE.getInputMap().put(redoKeystroke, "redoKeystroke");
		CODE.getActionMap().put("redoKeystroke", redoAction);

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

	// java undo and redo action classes

	class UndoHandler implements UndoableEditListener {

		/**
		 * Messaged when the Document has created an edit, the edit is added to
		 * <code>undoManager</code>, an instance of UndoManager.
		 */
		public void undoableEditHappened(UndoableEditEvent e) {
			undoManager.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
	}

	@SuppressWarnings("serial")
	class UndoAction extends AbstractAction {
		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.undo();
			} catch (CannotUndoException ex) {
				// TODO deal with this
				// ex.printStackTrace();
			}
			update();
			redoAction.update();
		}

		protected void update() {
			if (undoManager.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undoManager.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	@SuppressWarnings("serial")
	class RedoAction extends AbstractAction {
		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undoManager.redo();
			} catch (CannotRedoException ex) {
				// TODO deal with this
				ex.printStackTrace();
			}
			update();
			undoAction.update();
		}

		protected void update() {
			if (undoManager.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undoManager.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}
}
