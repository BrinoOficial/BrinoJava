package br.com.RatosDePC.Brpp.IDEui;

/**
 * Barra de Menu do IDE
 * 
 * @author Mateus Berardo de Souza Terra e Rafael Mascarenhas Dal Moro
 * @contributors  
 * @version 5/2/2016
 */

//import gnu.io.CommPortIdentifier;

import gnu.io.CommPortIdentifier;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import br.com.RatosDePC.Brpp.Utils.CommPortUtils;
import br.com.RatosDePC.Brpp.Utils.FileUtils;
import br.com.RatosDePC.Brpp.Utils.UploaderUtils;
import br.com.RatosDePC.Brpp.compiler.BrppCompiler;
import br.com.RatosDePC.SerialMonitor.SerialMonitor;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {

	private static String[] coms = new String[1]; // fix
	boolean first = true;
	ArrayList<String> comOldList = new ArrayList<String>();
	private static String[] boards = { "Uno", "Mega 1280", "Mega 2560",
			"Mega ADK", "Nano", "Nano 168", "Diecimila ou Duemilanove 328",
			"Diecimila ou Duemilanove 168", "Leonardo", "Micro", "Esplora",
			"Mini 328", "Mini 168", "Ethernet", "Fio", "BT 328", "BT 168",
			"LilyPad USB", "LilyPad 328", "LilyPad 168",
			"Pro ou Pro Mini 328 5V", "Pro ou Pro Mini 328 3V3",
			"Pro ou Pro Mini 168 5V", "Pro ou Pro Mini 168 3V3", "Gemma" };
	private JMenu subBoard;
	private static JRadioButtonMenuItem[] radioBoards;
	private ButtonGroup gp;
	private static JRadioButtonMenuItem[] radioCOMS;
	private ButtonGroup gpCom;
	private JMenu subCOM;
	private JMenu fileMenu;
	private JMenu ferrMenu;
	private JMenu sketchMenu;
	private JMenuItem novoItem;
	private JMenuItem salvarItem;
	private JMenuItem salvarComoItem;
	private JMenuItem abrirItem;
	private JMenuItem serialMonitor;
	private JMenuItem verifyItem;
	private JMenuItem loadItem;
	public MenuBar() {
		// TODO Auto-generated constructor stub
		coms = new String[15];
		// for (int x = 0; x < coms.length; x++) {
		// coms[x] = "COM" + (x + 1);
		// }
		fileMenu = new JMenu("Arquivo");
		fileMenu.setMnemonic(KeyEvent.VK_A);
		novoItem = new JMenuItem("Novo");
		Action novoAction = new AbstractAction("Novo") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int choice = JOptionPane.showConfirmDialog(null,
						"Você quer salvar o rascunho antes de criar um novo?");
				JTextPane txt = BrppIDEFrame.getTextPane();
				switch (choice) {
				case 0:
					FileUtils.saveFile(txt);
				case 1:
					BrppIDEFrame.getTextPane().setText(BrppIDEFrame.getMin());
					FileUtils.createFile(txt);
					break;
				case 2:
					break;
				}
			}
		};
		novoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		novoItem.setAction(novoAction);
		ferrMenu = new JMenu("Ferramentas");
		ferrMenu.addMenuListener(new MenuListener() {

			public void menuSelected(MenuEvent e) {
				setComs();
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		subBoard = new JMenu("Placa");
		subCOM = new JMenu("Porta");
		int x = 0;
		gp = new ButtonGroup();
		radioBoards = new JRadioButtonMenuItem[boards.length];
		for (String a : boards) {
			radioBoards[x] = new JRadioButtonMenuItem(a);
			radioBoards[x].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SouthPanel.updatePlacaCom(getSelectedIndexBoardName(),
							getSelectedIndexCOM());
				}
			});
			gp.add(radioBoards[x]);
			subBoard.add(radioBoards[x]);
			x++;
		}
		x = 0;
		gpCom = new ButtonGroup();
		serialMonitor = new JMenuItem("Monitor Serial");
		Action serialAction = new AbstractAction("Monitor Serial") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					SerialMonitor serial = new SerialMonitor(
							getSelectedIndexCOM());
					serial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					serial.setSize(500, 600);
					if (serial.getConnected())
						serial.setVisible(true);
					else
						serial.dispose();
				} catch (TooManyListenersException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		sketchMenu = new JMenu("Rascunho");
		verifyItem = new JMenuItem("Compilar/Verificar");
		Action verifyAction = new AbstractAction("Compilar/Verificar"){
			public void actionPerformed(ActionEvent event){
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextPane());
				}
				FileUtils.saveFile(BrppIDEFrame.getTextPane());
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
		verifyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
		verifyItem.setAction(verifyAction);	
		loadItem = new JMenuItem("Carregar");
		Action loadAction = new AbstractAction("Carregar"){
				public void actionPerformed(ActionEvent event){
					if (FileUtils.getDiretorio() == null) {
						FileUtils.createFile(BrppIDEFrame.getTextPane());
					}
					FileUtils.saveFile(BrppIDEFrame.getTextPane());
					if (BrppCompiler.compile(FileUtils.getDiretorio()
							.getAbsolutePath()))
						try {
							UploaderUtils.upload("\"" + BrppCompiler.getFile()
									+ "\"", MenuBar.getSelectedIndexCOM(),
									MenuBar.getSelectedIndex());
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}
		};
		loadAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));
		serialAction.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK
						| KeyEvent.SHIFT_DOWN_MASK));
		serialMonitor.setAction(serialAction);
		abrirItem = new JMenuItem("Abrir");
		Action abrirAction = new AbstractAction("Abrir") {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Você quer salvar o rascunho antes de abrir um novo?");
				JTextPane txt = BrppIDEFrame.getTextPane();
				switch (choice) {
				case 0:
					FileUtils.saveFile(txt);
				case 1:
					FileUtils.abrirFile(BrppIDEFrame.getTextPane());
					break;
				case 2:
					break;
				}
			}
		};
		abrirAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		abrirItem.setAction(abrirAction);
		salvarItem = new JMenuItem("Salvar");
		Action salvarAction = new AbstractAction("Salvar") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextPane());
				} else {
					FileUtils.saveFile(BrppIDEFrame.getTextPane());
				}
			}
		};
		salvarAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		salvarItem.setAction(salvarAction);
		salvarComoItem = new JMenuItem("Salvar como");
		Action salvarComoAction = new AbstractAction("SalvarComo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileUtils.createFile(BrppIDEFrame.getTextPane());
			}
		};
		salvarComoAction.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK
						| KeyEvent.SHIFT_DOWN_MASK));
		salvarComoItem.setAction(salvarComoAction);
		ferrMenu.add(subBoard);
		ferrMenu.add(subCOM);
		ferrMenu.addSeparator();
		ferrMenu.add(serialMonitor);
		setComs();
		sketchMenu.add(verifyItem);
		sketchMenu.add(loadItem);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		add(fileMenu);
		fileMenu.add(novoItem);
		add(ferrMenu);
		add(sketchMenu);
		fileMenu.add(abrirItem);
		fileMenu.add(salvarItem);
		fileMenu.add(salvarComoItem);
	}

	public static int getSelectedIndex() {
		for (int i = 0; i < boards.length; i++) {
			if (radioBoards[i].isSelected())
				return i;
		}
		return 0;

	}

	public static String getSelectedIndexBoardName() {
		int a = getSelectedIndex();
		return boards[a];
	}

	public static String getSelectedIndexCOM() {
		System.out.println("chamado");
		for (int i = 0; i < coms.length; i++) {
			if (radioCOMS[i].isSelected())
				return radioCOMS[i].getText();
		}
		return "COM1";
	}

	// public void setComs() {
	// Enumeration comm = CommPortUtils.getComPorts();
	// ArrayList<String> comList = new ArrayList<String>();
	// for (String a : coms) {
	// comOldList.add(a);
	// }
	// while (comm.hasMoreElements()) {
	// CommPortIdentifier port_identifier = (CommPortIdentifier) comm
	// .nextElement();
	// if (port_identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	// if (!comOldList.contains(port_identifier.getName()))
	// comList.add(port_identifier.getName());
	// }
	// }
	// if (comList.size() > 0) {
	// String[] temp = new String[coms.length];
	// int size = temp.length;
	// int cont = 0;
	// if (!first) if (temp[0].equals("Não há portas disponíveis")) size-=1;
	// coms = new String[comList.size() + size];
	// for (String c : temp) {
	// if (!first) if(temp[0].equals("Não há portas disponíveis")) cont--;
	// if(cont>=0) coms[cont] = c;
	// cont++;
	// }
	// for (String c : comList) {
	// this.coms[cont] = c;
	// cont++;
	// }
	// } else if (first){
	// coms[0] = "Não há portas disponíveis";
	// comList.add("Não há portas disponíveis");
	// first = false;
	// }
	// radioCOMS = new JRadioButtonMenuItem[coms.length];
	// addCom(comList);
	//
	// }

	public void setComs() {
		ArrayList<String> comList = new ArrayList<String>();
		Enumeration comm = CommPortUtils.getComPorts();
		while (comm.hasMoreElements()) {
			CommPortIdentifier port_identifier = (CommPortIdentifier) comm
					.nextElement();
			if (port_identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				// if (!comOldList.contains(port_identifier.getName()))
				comList.add(port_identifier.getName());
			}
		}
		if (comList.isEmpty()){
			addCom("Não há portas disponíveis");
		} else {
			addCom(comList);
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
					SouthPanel.updatePlacaCom(getSelectedIndexBoardName(),
							getSelectedIndexCOM());
				}
			});
			coms[x]=a;
			x++;

		}
	}

	public void addCom(String comsN) {
		int x = 0;
		radioCOMS = new JRadioButtonMenuItem[1];
		subCOM.removeAll();
		radioCOMS[0] = new JRadioButtonMenuItem(comsN);
		gpCom.add(radioCOMS[0]);
		subCOM.add(radioCOMS[0]);
		coms[0] = comsN;
		x++;

	}
}