package cc.brino.Brpp.IDEui;

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

/**
* Barra de menu da IDE
* 
* @author Mateus Berardo de Souza Terra e Rafael Mascarenhas Dal Moro
* @contributors 
* @version 5/2/2016
*/

import gnu.io.CommPortIdentifier;

import java.awt.Graphics;
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
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.json.simple.parser.ParseException;

import cc.brino.Brpp.BrppCompilerMain;
import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.Utils.CommPortUtils;
import cc.brino.Brpp.Utils.FileUtils;
import cc.brino.Brpp.Utils.JSONUtils;
import cc.brino.Brpp.Utils.LanguageVersionUtils;
import cc.brino.Brpp.Utils.UploaderUtils;
import cc.brino.Brpp.Utils.abrirExemploAction;
import cc.brino.Brpp.compiler.BrppCompiler;
import cc.brino.SerialMonitor.SerialMonitor;
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
/**
 * Barra de Menu do IDE
 * 
 * @author Mateus Berardo de Souza Terra e Rafael Mascarenhas Dal Moro
 * @contributors  
 * @version 5/2/2016
 */
//import gnu.io.CommPortIdentifier;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	private String fileSeparator = System.getProperty("file.separator");
	private static HashMap<String, String> listaExemplos = new HashMap<String, String>();
	private static String[] coms = new String[1]; // fix
	boolean first = true;
	static boolean hasCom = false;
	ArrayList<String> comOldList = new ArrayList<String>();
	private static String[] boards = { "Uno", "Mega 1280", "Mega 2560",
			"Mega ADK", "Nano", "Nano 168", "Diecimila ou Duemilanove 328",
			"Diecimila ou Duemilanove 168", "Leonardo", "Micro", "Esplora",
			"Mini 328", "Mini 168", "Ethernet", "Fio", "BT 328", "BT 168",
			"LilyPad USB", "LilyPad 328", "LilyPad 168",
			"Pro ou Pro Mini 328 5V", "Pro ou Pro Mini 328 3V3",
			"Pro ou Pro Mini 168 5V", "Pro ou Pro Mini 168 3V3", "Gemma",
			"One Dollar Board" };
	private JMenu subBoard;
	private static JRadioButtonMenuItem[] radioBoards;
	private ButtonGroup gp;
	private static JRadioButtonMenuItem[] radioLing;
	private ButtonGroup gpL;
	private static JRadioButtonMenuItem[] radioCOMS;
	private ButtonGroup gpCom;
	private JMenu subCOM;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu ferrMenu;
	private JMenu exemplosMenu;
	private JMenu sketchMenu;
	private JMenu linguaMenu;
	private JMenuItem novoItem;
	private JMenuItem salvarItem;
	private JMenuItem salvarComoItem;
	private JMenuItem abrirItem;
	private JMenuItem serialMonitor;
	private JMenuItem verifyItem;
	private JMenuItem loadItem;
	private JMenuItem comentarItem;
	private JMenuItem gerenciadorLingItem;

	public MenuBar() {
		coms = new String[15];

		// Menu Arquivo
		fileMenu = new JMenu("Arquivo");
		// Adiciona A como atalho
		fileMenu.setMnemonic(KeyEvent.VK_A);
		// Adicona o item "novo"
		novoItem = new JMenuItem("Novo");
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
					BrppIDEFrame.getTextArea().setText(BrppIDEFrame.getMin());
					FileUtils.createFile(txt);
					break;
				case 2:
					break;
				}
			}
		};
		// Adiciona o atalho CTRL+N
		novoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		// Adiciona a acao ao item
		novoItem.setAction(novoAction);
		// Adiciona o item abrir
		abrirItem = new JMenuItem("Abrir");
		// Cria a acao do item
		Action abrirAction = new AbstractAction("Abrir") {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Voc� quer salvar o rascunho antes de abrir um novo?");
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
				KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		// Adiciona a acao ao item
		abrirItem.setAction(abrirAction);
		// Cria o submenu exemplos
		exemplosMenu = new JMenu("Exemplos");
		// Percorre a pasta exemplos
		try (Stream<Path> paths = Files.walk(
				Paths.get("." + fileSeparator + "exemplos" + fileSeparator), 1)) {
			Iterator<Path> files = paths.iterator();
			files.next();
			while (files.hasNext()) {
				File f = new File(files.next().toString());
				Stream<Path> path = Files.walk(Paths.get(f.getCanonicalPath()),
						2);
				JMenu tipoDeExemplo = new JMenu(f.getName());
				Iterator<Path> exemplos = path.iterator();
				while (exemplos.hasNext()) {
					File exemplo = new File(exemplos.next().toString());
					if (!exemplo.isDirectory()) {
						JMenuItem exemploItem = new JMenuItem(exemplo.getName()
								.replace(".brpp", ""));
						listaExemplos.put(exemplo.getName()
								.replace(".brpp", ""), exemplo
								.getAbsolutePath());
						exemploItem.setAction(new abrirExemploAction(exemplo
								.getName().replace(".brpp", "")));
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
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		// Adiciona a acao ao item
		salvarItem.setAction(salvarAction);
		// Adiciona o item salvar como
		salvarComoItem = new JMenuItem("Salvar como");
		// Cria a acao do item
		Action salvarComoAction = new AbstractAction("Salvar como") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileUtils.createFile(BrppIDEFrame.getTextArea());
			}
		};
		// Adiciona o atalho CTRL+SHIFT+S
		salvarComoAction.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK
						| KeyEvent.SHIFT_DOWN_MASK));
		// Adiciona a acao ao item
		salvarComoItem.setAction(salvarComoAction);

		// Menu Editar
		editMenu = new JMenu("Editar");
		comentarItem = new JMenuItem("Comentar/Descomentar");
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
		commentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_SLASH, KeyEvent.CTRL_DOWN_MASK));
		comentarItem.setAction(commentAction);
		// menu Ferramentas
		ferrMenu = new JMenu("Ferramentas");
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
		subBoard = new JMenu("Placa");
		subCOM = new JMenu("Porta");
		linguaMenu = new JMenu("Língua");
		
		int x = 0;
		gp = new ButtonGroup();
		radioBoards = new JRadioButtonMenuItem[boards.length];
		for (String a : boards) {
			radioBoards[x] = new JRadioButtonMenuItem(a);
			radioBoards[x].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setSelectedBoard();
					SouthPanel.updatePlacaCom(
							PrefManager.getPref("placa.nome"), PrefManager
									.getPref("porta").equals("null") ? "--"
									: PrefManager.getPref("porta"));
				}
			});
			gp.add(radioBoards[x]);
			subBoard.add(radioBoards[x]);
			x++;
		}
		
		gerenciadorLingItem = new JMenuItem("Gerenciador de Línguas");
		linguaMenu.add(gerenciadorLingItem);
		Action lingGeren = new AbstractAction("Gerenciador de Línguas") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelecionadorDeLinguaFrame selecionador = new SelecionadorDeLinguaFrame();
				selecionador.setVisible(true);
			}
		};
		lingGeren.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		gerenciadorLingItem.setAction(lingGeren);
		linguaMenu.addSeparator();
		x = 0;
		gpL = new ButtonGroup();
		Map<String,Integer> lings;
		try {
			lings = LanguageVersionUtils.getLocalVersions();
		} catch (IOException | ParseException e1) {
			lings=new TreeMap<String, Integer>();
		}
		radioLing = new JRadioButtonMenuItem[lings.size()];
		for (String a : lings.keySet()) {
			radioLing[x] = new JRadioButtonMenuItem(a);
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
		Action serialAction = new AbstractAction("Monitor Serial") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!PrefManager.getPref("porta").equals("null")) {
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
		verifyItem = new JMenuItem("Compilar/Verificar");
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
						UploaderUtils.compile("\"" + BrppCompiler.getFile()
								+ "\"");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		};
		verifyAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
		verifyItem.setAction(verifyAction);
		loadItem = new JMenuItem("Carregar");
		Action loadAction = new AbstractAction("Compilar e Carregar") {
			public void actionPerformed(ActionEvent event) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextArea());
				}
				FileUtils.saveFile(BrppIDEFrame.getTextArea());
				if (BrppCompiler.compile(FileUtils.getDiretorio()
						.getAbsolutePath()))
					try {
						UploaderUtils.upload("\"" + BrppCompiler.getFile()
								+ "\"", PrefManager.getPref("porta"), Integer
								.parseInt(PrefManager.getPref("placa.index")));
					} catch (IOException e) {
						e.printStackTrace();
					}

			}
		};
		loadAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));
		loadItem.setAction(loadAction);
		serialAction.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK
						| KeyEvent.SHIFT_DOWN_MASK));
		serialMonitor.setAction(serialAction);

		ferrMenu.add(subBoard);
		ferrMenu.add(subCOM);
		ferrMenu.add(linguaMenu);
		ferrMenu.addSeparator();
		ferrMenu.add(serialMonitor);
		editMenu.add(comentarItem);
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
		add(fileMenu);
		add(editMenu);
		add(ferrMenu);
		add(sketchMenu);

	}

	public static HashMap<String, String> getMap() {
		return listaExemplos;
	}

	public static void setSelectedBoard() {
		for (int i = 0; i < boards.length;i++) {
			if (radioBoards[i].isSelected()){
				PrefManager.setPref("placa.index", String.valueOf(i));
				PrefManager.setPref("placa.nome", boards[i]);
				i=boards.length;
			}
		}
	}

	public static void setSelectedIndexCOM() {
		if (hasCom == false)
			PrefManager.setPref("porta", "null");
		for (int i = 0; i < coms.length; i++) {
			if (radioCOMS[i].isSelected()) {
				PrefManager.setPref("porta", radioCOMS[i].getText());
				i=coms.length;
			}
		}
	}
	

	private void setSelectedLing() {
		for (int i = 0; i < radioLing.length; i++) {
			if (radioLing[i].isSelected()) {
				PrefManager.setPref("lingua", radioLing[i].getText());
				try {
					JSONUtils.config(BrppCompilerMain.getPath());
				} catch (IOException
						| ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void setComs() {
		ArrayList<String> comList = new ArrayList<String>();
		Enumeration<CommPortIdentifier> comm = CommPortUtils.getComPorts();
		while (comm.hasMoreElements()) {
			CommPortIdentifier port_identifier = (CommPortIdentifier) comm
					.nextElement();
			if (port_identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// if (!comOldList.contains(port_identifier.getName()))
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
					if (!PrefManager.getPref("porta").equals("null")) {
						SouthPanel.updatePlacaCom(
								PrefManager.getPref("placa.nome"),
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
}