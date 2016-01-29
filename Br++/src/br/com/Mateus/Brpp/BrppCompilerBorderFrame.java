package br.com.Mateus.Brpp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class BrppCompilerBorderFrame extends JFrame {
	public static JTextArea LOG = new JTextArea(5, 10);
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem novoItem;
	private JMenuItem salvarItem;
	private JMenuItem abrirItem;
	private JButton COMP;
	private JButton COMPUP;
	private JButton CANCEL;
	private JPanel NorthPanel = new JPanel();
	private JScrollPane SouthPanel = new JScrollPane(LOG);
	public JFileChooser FC;
	private JScrollPane IDEScroll;
	private BorderLayout layout;
	private JComboBox<String> COM;
	private String[] coms;

	private JTextArea IDE;
	File diretorio = null;

	public BrppCompilerBorderFrame(String title) {
		super(title);
		coms=new String[50];
		for (int x = 0; x<50; x++){
			coms[x]="COM"+(x+1);
		}
		menuBar = new JMenuBar();
		fileMenu = new JMenu("Arquivo");
		fileMenu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(fileMenu);

		novoItem = new JMenuItem("Novo");
		Action novoAction = new AbstractAction("Novo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane
						.showInputDialog("Qual o nome do rascunho?");
				File f = new File("C://Arduino/Brino/" + name + "/" + name
						+ ".brpp");
				if (f.getParentFile().mkdirs()) {

					try {
						f.createNewFile();
						IDE.setText(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				diretorio = f.getAbsoluteFile();

			}
		};
		novoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		novoItem.setAction(novoAction);
		fileMenu.add(novoItem);

		abrirItem = new JMenuItem("Abrir");
		Action abrirAction = new AbstractAction("Abrir") {
			@Override
			public void actionPerformed(ActionEvent e) {
				IDE.setText(null);
				JFileChooser FC = new JFileChooser();
				int res = FC.showOpenDialog(null);

				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = FC.getSelectedFile();
					try {
						Scanner in = new Scanner(new File(
								diretorio.getAbsolutePath()));
						while (in.hasNext()) {
							String line = in.nextLine();
							// Highlight
							IDE.append(line + "\n");
						}
						in.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else
					JOptionPane.showMessageDialog(null,
							"Voce nao selecionou nenhum diretorio.");
			}
		};
		abrirAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		abrirItem.setAction(abrirAction);
		fileMenu.add(abrirItem);

		salvarItem = new JMenuItem("Salvar");
		Action salvarAction = new AbstractAction("Salvar") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (diretorio != null) {
					FileWriter fw;
					try {
						fw = new FileWriter(diretorio.getAbsoluteFile(), false);
						IDE.write(fw);
						fw.close();
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
				} else {
					String name = JOptionPane
							.showInputDialog("Qual o nome do rascunho?");
					File f = new File("C://Arduino/Brino/" + name + "/" + name
							+ ".brpp");
					if (f.getParentFile().mkdirs()) {

						try {
							f.createNewFile();
							FileWriter fw = new FileWriter(f.getAbsoluteFile(),
									false);
							IDE.write(fw);
							fw.close();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
				}
			}
		};
		salvarAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		salvarItem.setAction(salvarAction);
		fileMenu.add(salvarItem);
		setJMenuBar(menuBar);
		
		layout = new BorderLayout();
		setLayout(layout);
		NorthPanel.setLayout(new FlowLayout());
		COMP = new JButton("Compilar");
		NorthPanel.add(COMP);
		CompHandler handler = new CompHandler();
		COMP.addActionListener(handler);
		COMPUP = new JButton("Compilar e Carregar");
		NorthPanel.add(COMPUP);
//		UploadHandler uphandler = new UploadHandler();
//		COMPUP.addActionListener(uphandler);
		COM = new JComboBox<String>(coms);
		NorthPanel.add(COM);
		CANCEL = new JButton("Abort");
		NorthPanel.add(CANCEL);
		CanButtonHandler CanHandler = new CanButtonHandler();
		CANCEL.addActionListener(CanHandler);
		add(NorthPanel, BorderLayout.NORTH);
		NorthPanel.setVisible(true);
		IDE = new JTextArea(200, 400);
		IDEScroll = new JScrollPane(IDE);
		IDEScroll.setVisible(true);
		IDEScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		IDEScroll
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		IDEHigh = new Highlight2();
//		IDEHigh.setSize(200,400);
		add(IDEScroll, BorderLayout.CENTER);
//		add(IDEHigh, BorderLayout.CENTER);
		add(SouthPanel, BorderLayout.SOUTH);
		SouthPanel.setVisible(true);
		SouthPanel
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		SouthPanel
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		LOG.setEditable(false);

	}

	public static void setText(String line) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// LOG.append(line);
			}
		});
	}

	private class CompHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (diretorio == null) {
				JFileChooser FC = new JFileChooser();
				int res = FC.showOpenDialog(null);
				File diretorio = null;
				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = FC.getSelectedFile();
					if (BrppCompiler.compile(diretorio.getAbsolutePath()))
						try {
							if (Uploader.compile(BrppCompiler.getFile()))
								BrppCompilerBorderFrame.setText("Compilado");
							else
								BrppCompilerBorderFrame
										.setText("Falha ao compilar...");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					// JOptionPane.showMessageDialog(null, diretorio.getName()
					// + " compilado");
				} else
					JOptionPane.showMessageDialog(null,
							"Voce nao selecionou nenhum diretorio.");
			} else {
				FileWriter fw;
				try {
					fw = new FileWriter(diretorio.getAbsoluteFile(), false);
					IDE.write(fw);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (BrppCompiler.compile(diretorio.getAbsolutePath()))
					try {
						if (Uploader.compile(BrppCompiler.getFile()))
							BrppCompilerBorderFrame.setText("Compilado");
						else
							BrppCompilerBorderFrame
									.setText("Falha ao compilar...");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
			}

		}

	}

//	private class UploadHandler implements ActionListener {
//		public void actionPerformed(ActionEvent event) {
//			if (diretorio != null) {
//				FileWriter fw;
//				try {
//					fw = new FileWriter(diretorio.getAbsoluteFile(), false);
//					IDE.write(fw);
//					fw.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (BrppCompiler.compile(diretorio.getAbsolutePath()))
//					try {
//						if (Uploader.upload(BrppCompiler.getFile(), COM
//								.getSelectedItem().toString()))
//							BrppCompilerBorderFrame
//									.setText("Compilado e Carregado");
//						else
//							BrppCompilerBorderFrame
//									.setText("Falha ao compilar e/ou carregar...");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//			} else {
//				JFileChooser FC = new JFileChooser();
//				int res = FC.showOpenDialog(null);
//				File diretorio = null;
//				if (res == JFileChooser.APPROVE_OPTION) {
//					diretorio = FC.getSelectedFile();
//					if (BrppCompiler.compile(diretorio.getAbsolutePath()))
//						try {
//							if (Uploader.upload(BrppCompiler.getFile(), COM
//									.getSelectedItem().toString()))
//								BrppCompilerBorderFrame
//										.setText("Compilado e Carregado");
//							else
//								BrppCompilerBorderFrame
//										.setText("Falha ao compilar e/ou carregar...");
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//				} else
//					JOptionPane.showMessageDialog(null,
//							"Voce nao selecionou nenhum diretorio.");
//			}
//		}
//	}

	private class CanButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);

		}

	}
}
