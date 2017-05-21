package cc.brino.Brpp.IDEui.MenuBar;

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
 * Barra de menu da IDE
 * 
 * @author Mateus Berardo de Souza Terra e Rafael
 *         Mascarenhas Dal Moro
 * @contributors
 * @version 5/2/2016
 */
import gnu.io.CommPortIdentifier;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import org.fife.rsta.ui.GoToDialog;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.FindToolBar;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.rsta.ui.search.ReplaceToolBar;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import org.json.simple.parser.ParseException;
import cc.brino.Brpp.BrppCompilerMain;
import cc.brino.Brpp.IDEui.BrppIDEFrame;
import cc.brino.Brpp.IDEui.BubbleBorder;
import cc.brino.Brpp.IDEui.SelecionadorDeLinguaFrame;
import cc.brino.Brpp.IDEui.SouthPanel;
import cc.brino.Brpp.IDEui.SubMenu;
import cc.brino.Brpp.IDEui.UIConstants;
import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.Utils.CommPortUtils;
import cc.brino.Brpp.Utils.FileUtils;
import cc.brino.Brpp.Utils.JSONUtils;
import cc.brino.Brpp.Utils.LanguageVersionUtils;
import cc.brino.Brpp.Utils.UploaderUtils;
import cc.brino.Brpp.Utils.abrirExemploAction;
import cc.brino.Brpp.compiler.BrppCompiler;
import cc.brino.SerialMonitor.SerialMonitor;


@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements SearchListener {

	private FindDialog findDialog;
	private ReplaceDialog replaceDialog;
	private FindToolBar findToolBar;
	private ReplaceToolBar replaceToolBar;
	private static RSyntaxTextArea textArea = BrppIDEFrame.getTextArea();
	private static final JSeparator separator = new JSeparator(
			JSeparator.HORIZONTAL), separator2 = new JSeparator(
			JSeparator.HORIZONTAL);
	private String fileSeparator = System.getProperty("file.separator");
	private static HashMap<String, String> listaExemplos = new HashMap<String, String>();
	private static String[] coms = new String[1]; // fix
	boolean first = true;
	private static boolean hasCom = false;
	private static String[] boards = { "Uno", "Mega 1280", "Mega 2560",
			"Mega ADK", "Nano", "Nano 168",
			"Diecimila ou Duemilanove 328",
			"Diecimila ou Duemilanove 168", "Leonardo", "Micro",
			"Esplora", "Mini 328", "Mini 168", "Ethernet", "Fio",
			"BT 328", "BT 168", "LilyPad USB", "LilyPad 328",
			"LilyPad 168", "Pro ou Pro Mini 328 5V",
			"Pro ou Pro Mini 328 3V3", "Pro ou Pro Mini 168 5V",
			"Pro ou Pro Mini 168 3V3", "Gemma", "One Dollar Board" };
	private static JRadioButtonMenuItem[] radioBoards, radioLing,
			radioCOMS;
	private ButtonGroup gp, gpL, gpCom;
	private JMenu fileMenu, editMenu, ferrMenu, sketchMenu;
	private SubMenu exemplosMenu, linguaMenu, subCOM, subBoard;
	private JMenuItem novoItem, salvarItem, salvarComoItem, abrirItem,
			serialMonitor, verifyItem, loadItem, comentarItem,
			findItem, replaceItem, goToItem, gerenciadorLingItem;

	public MenuBar() {
		initUIManager(this);
		initSearchDialogs();
		separator.setForeground(Color.green);
		separator.setBackground(Color.BLACK);
		separator.setBorder(UIConstants.BORDATRANSPARENTE);
		separator.setOpaque(true);
		separator.setPreferredSize(new Dimension(getWidth(), 3));
		separator2.setForeground(Color.green);
		separator2.setBackground(Color.BLACK);
		separator2.setBorder(UIConstants.BORDATRANSPARENTE);
		separator2.setOpaque(true);
		separator2.setPreferredSize(new Dimension(getWidth(), 3));
		coms = new String[15];
		// Menu Arquivo
		fileMenu = new JMenu("Arquivo");
		// Adiciona A como atalho
		fileMenu.setMnemonic(KeyEvent.VK_A);
		fileMenu.setBorder(UIConstants.BORDAVAZIA);
		// Adicona o item "novo"
		novoItem = new JMenuItem("Novo");
		novoItem.setBorder(UIConstants.BORDAVAZIA);
		// Cria a acao do item
		Action novoAction = new AbstractAction("Novo") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Você quer salvar o rascunho antes de criar um novo?");
				RSyntaxTextArea txt = BrppIDEFrame.getTextArea();
				switch (choice) {
					case 0:
						FileUtils.saveFile(txt);
					case 1:
						BrppIDEFrame.getTextArea()
								.setText(BrppIDEFrame.getMin());
						FileUtils.createFile(txt);
						break;
					case 2:
						break;
				}
			}
		};
		// Adiciona o atalho CTRL+N
		novoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N,
						KeyEvent.CTRL_DOWN_MASK));
		// Adiciona a acao ao item
		novoItem.setAction(novoAction);
		// Adiciona o item abrir
		abrirItem = new JMenuItem("Abrir");
		abrirItem.setBorder(UIConstants.BORDAVAZIA);
		// Cria a acao do item
		Action abrirAction = new AbstractAction("Abrir") {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Você quer salvar o rascunho antes de abrir um novo?");
				RSyntaxTextArea txt = BrppIDEFrame.getTextArea();
				switch (choice) {
					case 0:
						FileUtils.saveFile(txt);
					case 1:
						FileUtils.abrirFile(BrppIDEFrame.getTextArea());
						break;
					case 2:
						break;
				}
			}
		};
		// Adiciona o atalho CTRL+O
		abrirAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_O,
						KeyEvent.CTRL_DOWN_MASK));
		// Adiciona a acao ao item
		abrirItem.setAction(abrirAction);
		// Cria o submenu exemplos
		exemplosMenu = new SubMenu("Exemplos");
		exemplosMenu.setBackground(Color.BLACK);
		exemplosMenu.setBorder(UIConstants.BORDAVAZIA);
		// Percorre a pasta exemplos
		try (Stream<Path> paths = Files.walk(Paths.get("."
				+ fileSeparator + "exemplos" + fileSeparator),
				1)) {
			Stream<Path> pathin = paths.sorted();
			Iterator<Path> files = pathin.iterator();
			files.next();
			while (files.hasNext()) {
				File f = new File(files.next().toString());
				Stream<Path> path = Files.walk(Paths.get(f.getCanonicalPath()),
						2);
				Stream<Path> exPathin = path.sorted();
				SubMenu tipoDeExemplo = new SubMenu(f.getName());
				tipoDeExemplo.setBorder(UIConstants.BORDAVAZIA);
				Iterator<Path> exemplos = exPathin.iterator();
				while (exemplos.hasNext()) {
					File exemplo = new File(exemplos.next()
							.toString());
					if (!exemplo.isDirectory()) {
						JMenuItem exemploItem = new JMenuItem(
								exemplo.getName()
										.replace(".brpp",
												""));
						exemploItem.setBorder(UIConstants.BORDAVAZIA);
						listaExemplos.put(exemplo.getName()
								.replace(".brpp",
										""),
								exemplo.getAbsolutePath());
						exemploItem.setAction(new abrirExemploAction(
								exemplo.getName()
										.replace(".brpp",
												"")));
						tipoDeExemplo.add(exemploItem);
					}
				}
				exemplosMenu.add(tipoDeExemplo);
				path.close();
			}
			paths.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Adiciona o item salvar
		salvarItem = new JMenuItem("Salvar");
		salvarItem.setBorder(UIConstants.BORDAVAZIA);
		// Cria a acao do item
		Action salvarAction = new AbstractAction("Salvar") {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextArea());
				} else {
					FileUtils.saveFile(BrppIDEFrame.getTextArea());
				}
			}
		};
		// Adiciona o atalho CTRL+S
		salvarAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S,
						KeyEvent.CTRL_DOWN_MASK));
		// Adiciona a acao ao item
		salvarItem.setAction(salvarAction);
		// Adiciona o item salvar como
		salvarComoItem = new JMenuItem("Salvar como");
		salvarComoItem.setBorder(UIConstants.BORDAVAZIA);
		// Cria a acao do item
		Action salvarComoAction = new AbstractAction("Salvar como") {

			@Override
			public void actionPerformed(ActionEvent e) {
				FileUtils.createFile(BrppIDEFrame.getTextArea());
			}
		};
		// Adiciona o atalho CTRL+SHIFT+S
		salvarComoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S,
						KeyEvent.CTRL_DOWN_MASK
								| KeyEvent.SHIFT_DOWN_MASK));
		// Adiciona a acao ao item
		salvarComoItem.setAction(salvarComoAction);
		// Menu Editar
		editMenu = new JMenu("Editar");
		editMenu.setBorder(UIConstants.BORDAVAZIA);
		findItem = new JMenuItem(new ShowFindDialogAction());
		findItem.setBorder(UIConstants.BORDAVAZIA);
		replaceItem = new JMenuItem(new ShowReplaceDialogAction());
		replaceItem.setBorder(UIConstants.BORDAVAZIA);
		goToItem = new JMenuItem(new GoToLineAction());
		goToItem.setBorder(UIConstants.BORDAVAZIA);
		comentarItem = new JMenuItem("Comentar/Descomentar");
		comentarItem.setBorder(UIConstants.BORDAVAZIA);
		// Cria a acao do item
		Action commentAction = new AbstractAction("Comentar linha") {

			private static final long serialVersionUID = 2474949258335385702L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					BrppIDEFrame.comentar();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				System.out.println("Comentar");
			}
		};
		commentAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_SLASH,
						KeyEvent.CTRL_DOWN_MASK));
		comentarItem.setAction(commentAction);
		// menu Ferramentas
		ferrMenu = new JMenu("Ferramentas");
		ferrMenu.setBorder(UIConstants.BORDAVAZIA);
		ferrMenu.addMenuListener(new MenuListener() {

			public void menuSelected(MenuEvent e) {
				setComs();
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
			}
		});
		subBoard = new SubMenu("Placa");
		subBoard.setBorder(UIConstants.BORDAVAZIA);
		subCOM = new SubMenu("Porta");
		subCOM.setBorder(UIConstants.BORDAVAZIA);
		linguaMenu = new SubMenu("Língua");
		linguaMenu.setBorder(UIConstants.BORDAVAZIA);
		int x = 0;
		gp = new ButtonGroup();
		radioBoards = new JRadioButtonMenuItem[boards.length];
		for (String a : boards) {
			radioBoards[x] = new JRadioButtonMenuItem(a);
			radioBoards[x].setBorder(UIConstants.BORDAVAZIA);
			radioBoards[x].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setSelectedBoard();
					SouthPanel.updatePlacaCom(PrefManager.getPref("placa.nome"),
							PrefManager.getPref("porta")
									.equals("null") ? "--"
									: PrefManager.getPref("porta"));
				}
			});
			gp.add(radioBoards[x]);
			subBoard.add(radioBoards[x]);
			x++;
		}
		gerenciadorLingItem = new JMenuItem("Gerenciador de Línguas");
		gerenciadorLingItem.setBorder(UIConstants.BORDAVAZIA);
		linguaMenu.add(gerenciadorLingItem);
		Action lingGeren = new AbstractAction("Gerenciador de Línguas") {

			@Override
			public void actionPerformed(ActionEvent e) {
				SelecionadorDeLinguaFrame selecionador = new SelecionadorDeLinguaFrame();
				selecionador.setVisible(true);
			}
		};
		lingGeren.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_L,
						KeyEvent.CTRL_DOWN_MASK));
		gerenciadorLingItem.setAction(lingGeren);
		linguaMenu.add(separator2);
		x = 0;
		gpL = new ButtonGroup();
		Map<String, Integer> lings;
		try {
			lings = LanguageVersionUtils.getLocalVersions();
		} catch (IOException | ParseException e1) {
			lings = new TreeMap<String, Integer>();
		}
		radioLing = new JRadioButtonMenuItem[lings.size()];
		for (String a : lings.keySet()) {
			radioLing[x] = new JRadioButtonMenuItem(a);
			radioLing[x].setBorder(UIConstants.BORDAVAZIA);
			radioLing[x].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setSelectedLing();
				}
			});
			gpL.add(radioLing[x]);
			linguaMenu.add(radioLing[x]);
			x++;
		}
		x = 0;
		gpCom = new ButtonGroup();
		serialMonitor = new JMenuItem("Monitor Serial");
		serialMonitor.setBorder(UIConstants.BORDAVAZIA);
		Action serialAction = new AbstractAction("Monitor Serial") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!PrefManager.getPref("porta")
						.equals("null")) {
					try {
						SerialMonitor serial = new SerialMonitor(
								PrefManager.getPref("porta"));
						serial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						serial.setSize(500, 600);
						if (serial.getConnected())
							serial.setVisible(true);
						else
							serial.dispose();
					} catch (TooManyListenersException e) {
						e.printStackTrace();
					}
				}
			}
		};
		// Menu Rascunho
		sketchMenu = new JMenu("Rascunho");
		sketchMenu.setBorder(UIConstants.BORDAVAZIA);
		verifyItem = new JMenuItem("Compilar/Verificar");
		verifyItem.setBorder(UIConstants.BORDAVAZIA);
		Action verifyAction = new AbstractAction("Compilar/Verificar") {

			public void actionPerformed(ActionEvent event) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextArea());
				}
				FileUtils.saveFile(BrppIDEFrame.getTextArea());
				if (BrppCompiler.compile(FileUtils.getDiretorio()
						.getAbsolutePath()))
					try {
						System.out.println(BrppCompiler.getFile());
						UploaderUtils.compile("\""
								+ BrppCompiler.getFile()
								+ "\"",
								Integer.parseInt(PrefManager.getPref("placa.index")));
					} catch (IOException e) {
						// TODO
						// Auto-generated
						// catch
						// block
						e.printStackTrace();
					}
			}
		};
		verifyAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_R,
						KeyEvent.CTRL_DOWN_MASK));
		verifyItem.setAction(verifyAction);
		loadItem = new JMenuItem("Carregar");
		loadItem.setBorder(UIConstants.BORDAVAZIA);
		Action loadAction = new AbstractAction("Compilar e Carregar") {

			public void actionPerformed(ActionEvent event) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextArea());
				}
				FileUtils.saveFile(BrppIDEFrame.getTextArea());
				if (BrppCompiler.compile(FileUtils.getDiretorio()
						.getAbsolutePath()))
					try {
						UploaderUtils.upload("\""
								+ BrppCompiler.getFile()
								+ "\"",
								PrefManager.getPref("porta"),
								Integer.parseInt(PrefManager.getPref("placa.index")));
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		};
		loadAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_U,
						KeyEvent.CTRL_DOWN_MASK));
		loadItem.setAction(loadAction);
		serialAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_M,
						KeyEvent.CTRL_DOWN_MASK
								| KeyEvent.SHIFT_DOWN_MASK));
		serialMonitor.setAction(serialAction);
		ferrMenu.add(subBoard);
		ferrMenu.add(subCOM);
		ferrMenu.add(linguaMenu);
		ferrMenu.add(separator);
		ferrMenu.add(serialMonitor);
		editMenu.add(comentarItem);
		editMenu.add(separator);
		editMenu.add(findItem);
		editMenu.add(replaceItem);
		editMenu.add(goToItem);
		setComs();
		sketchMenu.add(verifyItem);
		sketchMenu.add(loadItem);
		fileMenu.add(novoItem);
		fileMenu.add(abrirItem);
		fileMenu.add(exemplosMenu);
		fileMenu.add(salvarItem);
		fileMenu.add(salvarComoItem);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setPreferredSize(new Dimension(getWidth(), 25));
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(UIConstants.CINZA);
		g2d.fillRect(0, 0, getWidth(), getHeight() - 2);
		setBackground(Color.BLACK);
		setBorder(UIConstants.BORDAVAZIA);
		add(fileMenu);
		add(editMenu);
		add(ferrMenu);
		add(sketchMenu);
	}

	public static HashMap<String, String> getMap() {
		return listaExemplos;
	}

	public static void setSelectedBoard() {
		for (int i = 0; i < boards.length; i++) {
			if (radioBoards[i].isSelected()) {
				PrefManager.setPref("placa.index",
						String.valueOf(i));
				PrefManager.setPref("placa.nome", boards[i]);
				i = boards.length;
			}
		}
	}

	public static void setSelectedIndexCOM() {
		if (hasCom == false)
			PrefManager.setPref("porta", "null");
		for (int i = 0; i < coms.length; i++) {
			if (radioCOMS[i].isSelected()) {
				PrefManager.setPref("porta",
						radioCOMS[i].getText());
				i = coms.length;
			}
		}
	}

	private void setSelectedLing() {
		for (int i = 0; i < radioLing.length; i++) {
			if (radioLing[i].isSelected()) {
				PrefManager.setPref("lingua",
						radioLing[i].getText());
				try {
					JSONUtils.config(BrppCompilerMain.getPath());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void setComs() {
		ArrayList<String> comList = new ArrayList<String>();
		Enumeration<CommPortIdentifier> comm = CommPortUtils.getComPorts();
		while (comm.hasMoreElements()) {
			CommPortIdentifier port_identifier = (CommPortIdentifier) comm.nextElement();
			if (port_identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				comList.add(port_identifier.getName());
			}
		}
		if (comList.isEmpty()) {
			addCom("Não há portas disponíveis");
			hasCom = false;
		} else {
			addCom(comList);
			hasCom = true;
		}
	}

	public void addCom(ArrayList<String> comsN) {
		int x = 0;
		radioCOMS = new JRadioButtonMenuItem[comsN.size()];
		subCOM.removeAll();
		for (String a : comsN) {
			radioCOMS[x] = new JRadioButtonMenuItem(a);
			gpCom.add(radioCOMS[x]);
			subCOM.add(radioCOMS[x]);
			radioCOMS[x].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setSelectedIndexCOM();
					if (!PrefManager.getPref("porta")
							.equals("null")) {
						SouthPanel.updatePlacaCom(PrefManager.getPref("placa.nome"),
								PrefManager.getPref("porta"));
					}
				}
			});
			coms[x] = a;
			x++;
		}
	}

	public void addCom(String comsN) {
		radioCOMS = new JRadioButtonMenuItem[1];
		subCOM.removeAll();
		radioCOMS[0] = new JRadioButtonMenuItem(comsN);
		gpCom.add(radioCOMS[0]);
		subCOM.add(radioCOMS[0]);
		coms[0] = comsN;
	}

	private class ShowFindDialogAction extends AbstractAction {

		public ShowFindDialogAction() {
			super("Find...");
			int c = getToolkit().getMenuShortcutKeyMask();
			putValue(ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_F, c));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (replaceDialog.isVisible()) {
				replaceDialog.setVisible(false);
			}
			findDialog.setVisible(true);
		}
	}

	private class ShowReplaceDialogAction extends AbstractAction {

		public ShowReplaceDialogAction() {
			super("Replace...");
			int c = getToolkit().getMenuShortcutKeyMask();
			putValue(ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_H, c));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (findDialog.isVisible()) {
				findDialog.setVisible(false);
			}
			replaceDialog.setVisible(true);
		}
	}

	private class GoToLineAction extends AbstractAction {

		public GoToLineAction() {
			super("Go To Line...");
			int c = getToolkit().getMenuShortcutKeyMask();
			putValue(ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_L, c));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (findDialog.isVisible()) {
				findDialog.setVisible(false);
			}
			if (replaceDialog.isVisible()) {
				replaceDialog.setVisible(false);
			}
			GoToDialog dialog = new GoToDialog(
					BrppCompilerMain.getDialog());
			dialog.setMaxLineNumberAllowed(BrppIDEFrame.getTextArea()
					.getLineCount());
			dialog.setVisible(true);
			int line = dialog.getLineNumber();
			if (line > 0) {
				try {
					BrppIDEFrame.getTextArea()
							.setCaretPosition(BrppIDEFrame.getTextArea()
									.getLineStartOffset(line - 1));
				} catch (BadLocationException ble) { // Never
									// happens
					UIManager.getLookAndFeel()
							.provideErrorFeedback(BrppIDEFrame.getTextArea());
					ble.printStackTrace();
				}
			}
		}
	}

	@Override
	public void searchEvent(SearchEvent e) {
		SearchEvent.Type type = e.getType();
		SearchContext context = e.getSearchContext();
		SearchResult result = null;
		switch (type) {
			default: // Prevent FindBugs warning
					// later
			case MARK_ALL:
				result = SearchEngine.markAll(textArea, context);
				break;
			case FIND:
				result = SearchEngine.find(textArea, context);
				if (!result.wasFound()) {
					UIManager.getLookAndFeel()
							.provideErrorFeedback(textArea);
				}
				break;
			case REPLACE:
				result = SearchEngine.replace(textArea, context);
				if (!result.wasFound()) {
					UIManager.getLookAndFeel()
							.provideErrorFeedback(textArea);
				}
				break;
			case REPLACE_ALL:
				result = SearchEngine.replaceAll(textArea,
						context);
				JOptionPane.showMessageDialog(null,
						result.getCount()
								+ " occurrences replaced.");
				break;
		}
		// String text = null;
		// if (result.wasFound()) {
		// text = "Text found; occurrences marked: "
		// + result.getMarkedCount();
		// } else if (type ==
		// SearchEvent.Type.MARK_ALL) {
		// if (result.getMarkedCount() > 0) {
		// text = "Occurrences marked: "
		// + result.getMarkedCount();
		// } else {
		// text = "";
		// }
		// } else {
		// text = "Text not found";
		// }
		// // statusBar.setLabel(text);
	}

	@Override
	public String getSelectedText() {
		return textArea.getSelectedText();
	}

	public void initSearchDialogs() {
		findDialog = new FindDialog(BrppCompilerMain.getDialog(), this);
		replaceDialog = new ReplaceDialog(BrppCompilerMain.getDialog(),
				this);
		// This ties the properties of the two
		// dialogs together (match case,
		// regex, etc.).
		SearchContext context = findDialog.getSearchContext();
		replaceDialog.setSearchContext(context);
		// Create tool bars and tie their search
		// contexts together also.
		findToolBar = new FindToolBar(this);
		findToolBar.setSearchContext(context);
		replaceToolBar = new ReplaceToolBar(this);
		replaceToolBar.setSearchContext(context);
	}

	public static void initUIManager(MenuBar o) {
		final Font font = new Font(o.getFont().getFamily(), Font.PLAIN,
				o.getFont().getSize());
		UIManager.put("Menu.font", font);
		UIManager.put("MenuBar.font", font);
		UIManager.put("MenuItem.font", font);
		UIManager.put("RadioButtonMenuItem.font", font);
		UIManager.put("MenuBar.background", Color.BLACK);
		UIManager.put("MenuItem.background", Color.BLACK);
		UIManager.put("RadioButtonMenuItem.background", Color.BLACK);
		UIManager.put("MenuBar.foreground", Color.WHITE);
		UIManager.put("Menu.foreground", Color.WHITE);
		UIManager.put("MenuItem.foreground", Color.WHITE);
		UIManager.put("RadioButtonMenuItem.foreground", Color.WHITE);
		UIManager.put("MenuItem.acceleratorForeground", Color.green);
		UIManager.put("MenuItem.acceleratorSelectionForeground",
				Color.green);
		UIManager.put("MenuBar.selectionForeground", Color.WHITE);
		UIManager.put("Menu.selectionForeground", Color.WHITE);
		UIManager.put("MenuItem.selectionForeground", Color.WHITE);
		UIManager.put("MenuBar.selectionBackground", Color.BLACK);
		UIManager.put("Menu.selectionBackground", Color.BLACK);
		UIManager.put("MenuItem.selectionBackground", UIConstants.CINZA);
		UIManager.put("RadioMenuItem.selectionBackground",
				UIConstants.CINZA);
		UIManager.put("PopupMenu.border", UIConstants.BORDAVAZIA);
		UIManager.put("PopupMenu.background", Color.BLACK);
		UIManager.put("OptionPane.background", UIConstants.CINZA);
		UIManager.put("OptionPane.messageForeground", Color.WHITE);
		UIManager.put("Panel.background", UIConstants.CINZA);
		UIManager.put("Panel.foreground", Color.WHITE);
		UIManager.put("TextField.background", UIConstants.CINZAESCURO);
		UIManager.put("TextField.foreground", Color.WHITE);
		UIManager.put("TextField.border", new BubbleBorder(
				UIConstants.CINZAESCURO));
<<<<<<< HEAD
		UIManager.put("nimbusBase", Color.DARK_GRAY);
//		UIManager.put("FileChooser.listViewBorder",new BubbleBorder(UIConstants.CINZAESCURO));
//		UIManager.put("FileChooser.listViewBackground",UIConstants.CINZAESCURO);
//		UIManager.put("FileChooser.listViewForeground",Color.WHITE);
		UIManager.put("FileChooser.Foreground",Color.WHITE);
=======
>>>>>>> 62c865e9f507b221d482c61027f383e9b16db0b9
		UIManager.put("Button.border", new BubbleBorder(Color.WHITE));
	}
}