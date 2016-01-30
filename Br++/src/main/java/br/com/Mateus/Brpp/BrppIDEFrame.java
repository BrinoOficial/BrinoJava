package br.com.Mateus.Brpp;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class BrppIDEFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	final JTextPane txt;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu ferrMenu;
	private JMenuItem novoItem;
	private JMenuItem salvarItem;
	private JMenuItem abrirItem;
	private JPanel NorthPanel;
	public static JTextArea LOG = new JTextArea(5, 10);
	private JScrollPane SouthPanel = new JScrollPane(LOG);
	private JButton COMP;
	private JButton COMPUP;
	private File diretorio = null;
	private Color GREEN = new Color(11, 125, 73);
	private Color WHITE = new Color(255, 255, 255);
	private String[] coms;
	private String[] boards = { "Uno", "Mega", "Mega2560", "Nano", "Nano 168",
			"Diecimila ou Duemilanove" };
	private String min = "Configuracao() {\n"
			+ "//Coloque aqui seu codigo de Configuracao que sera executado uma vez\n"
			+ "\n" + "}\n" + "Principal(){\n"
			+ "//Coloque aqui seu codigo Principal, para rodar repetidamente\n"
			+ "\n" + "}\n";
	private JMenu subBoard;
	private JRadioButtonMenuItem[] radioBoards;
	private ButtonGroup gp;
	private JRadioButtonMenuItem[] radioCOMS;
	private ButtonGroup gpCom;
	private JMenu subCOM;
	private JButton NOVO;
	private JButton ABR;
	private JButton SAL;

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstCommentChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\")) {
				break;
			}
			index++;
		}
		return index;
	}

	private int findLastCommentChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\n")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	public BrppIDEFrame(String title) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);
		BorderLayout bl = new BorderLayout();
		setLayout(bl);
		// bl.setVgap(10);
		setBackground(GREEN);
		NorthPanel = new JPanel();
		NorthPanel.setBackground(GREEN);
		NorthPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		ImageIcon comp = new ImageIcon("resources/comButton.png");
		COMP = new JButton(comp);
		COMP.setBorderPainted(false);
		COMP.setBorder(emptyBorder);
		COMP.setContentAreaFilled(false);
		COMP.setRolloverIcon(new ImageIcon("resources/comButtonFocus.png"));
		NorthPanel.add(COMP);
		CompHandler handler = new CompHandler();
		COMP.addActionListener(handler);
		ImageIcon compup = new ImageIcon("resources/carrButton.png");
		COMPUP = new JButton(compup);
		COMPUP.setBorderPainted(false);
		COMPUP.setBorder(emptyBorder);
		COMPUP.setContentAreaFilled(false);
		COMPUP.setRolloverIcon(new ImageIcon("resources/carrButtonFocus.png"));
		COMPUP.setRolloverSelectedIcon(new ImageIcon("resources/carrButtonClicked.png"));
		NorthPanel.add(COMPUP);
		UploadHandler uphandler = new UploadHandler();
		COMPUP.addActionListener(uphandler);
		ImageIcon novo = new ImageIcon("resources/novoButton.png");
		NOVO = new JButton(novo);
		NOVO.setBorderPainted(false);
		NOVO.setBorder(emptyBorder);
		NOVO.setContentAreaFilled(false);
		NOVO.setRolloverIcon(new ImageIcon("resources/novoButtonFocus.png"));
		NorthPanel.add(NOVO);
		ImageIcon abr = new ImageIcon("resources/opButton.png");
		ABR = new JButton(abr);
		ABR.setBorderPainted(false);
		ABR.setBorder(emptyBorder);
		ABR.setContentAreaFilled(false);
		ABR.setRolloverIcon(new ImageIcon("resources/opButtonFocus.png"));
		NorthPanel.add(ABR);
		ImageIcon sal = new ImageIcon("resources/saveButton.png");
		SAL = new JButton(sal);
		SAL.setBorderPainted(false);
		SAL.setBorder(emptyBorder);
		SAL.setContentAreaFilled(false);
		SAL.setRolloverIcon(new ImageIcon("resources/opButtonFocus.png"));
		NorthPanel.add(SAL);
		coms = new String[15];
		for (int x = 0; x < coms.length; x++) {
			coms[x] = "COM" + (x + 1);
		}
		add(NorthPanel, BorderLayout.NORTH);
		NorthPanel.setVisible(true);
		add(SouthPanel, BorderLayout.SOUTH);
		SouthPanel.setBackground(WHITE);
		Color azul = new Color(66, 119, 255);
		Color laranja = new Color(255, 56, 0);
		Color vermelho = new Color(252, 145, 20);
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final javax.swing.text.AttributeSet attr = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, azul);
		final javax.swing.text.AttributeSet attri = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, vermelho);
		final javax.swing.text.AttributeSet attrib = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, laranja);
		final javax.swing.text.AttributeSet attrBlack = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
		final javax.swing.text.AttributeSet attrBold = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Bold, true);

		final javax.swing.text.AttributeSet attrNoBold = cont.addAttribute(
				cont.getEmptySet(), StyleConstants.Bold, false);
		DefaultStyledDocument doc = new DefaultStyledDocument() {
			private static final long serialVersionUID = 1L;
			private String KEYWORDS_1 = "(\\W)*(Ligado|Desligado|Numero|NumeroDecimal|Longo|Palavra|Condicao|Modulo|Constante|Verdadeiro|Falso|SemRetorno|Entrada|Saida)";
			private String KEYWORDS_2 = "(\\W)*(Configuracao|Principal|usar|definir|para|se|enquanto|senao|faca|e|ou|responder)";
			private String KEYWORDS_3 = "(\\W)*(soar|pararSoar|esperar|proporcionar|definirModo|usar|conectar|enviar|enviarln|disponivel|ler|escrever|ler|ligar|desligar|tamanho|formatar|posicao|limpar|conectar|escreverAngulo|escreverMicros|frente|tras|parar|transmitir|pararTransitir|solicitar|solicitado|recebido)";
			private String KEYWORDS_4 = "(\\W)*(Memoria|Pino|LCD|USB|I2C|Servo)";

			public void insertString(int offset, String str,
					javax.swing.text.AttributeSet a)
					throws BadLocationException {
				super.insertString(offset, str, a);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offset);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after
							|| String.valueOf(text.charAt(wordR))
									.matches("\\W")) {
						if (text.substring(wordL, wordR).matches(KEYWORDS_1))
							setCharacterAttributes(wordL, wordR - wordL, attr,
									false);
						else if (text.substring(wordL, wordR).matches(
								KEYWORDS_2))
							setCharacterAttributes(wordL, wordR - wordL, attri,
									false);
						else if (text.substring(wordL, wordR).matches(
								KEYWORDS_3))
							setCharacterAttributes(wordL, wordR - wordL,
									attrib, false);
						else if (text.substring(wordL, wordR).matches(
								KEYWORDS_4)) {
							setCharacterAttributes(wordL, wordR - wordL,
									attrib, false);
							setCharacterAttributes(wordL, wordR - wordL,
									attrBold, false);
						} else {
							setCharacterAttributes(wordL, wordR - wordL,
									attrBlack, false);
							setCharacterAttributes(wordL, wordR - wordL,
									attrNoBold, false);
						}
						wordL = wordR;
					}
					wordR++;
				}
			}

			public void remove(int offs, int len) throws BadLocationException {
				super.remove(offs, len);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offs);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offs);

				if (text.substring(before, after).matches(
						"(\\W)*(private|public|protected)")) {
					setCharacterAttributes(before, after - before, attr, false);
				} else {
					setCharacterAttributes(before, after - before, attrBlack,
							false);
				}
			}
		};

		txt = new JTextPane(doc);
		txt.setSize(200, 400);
		txt.setText(min);
		add(new JScrollPane(txt), BorderLayout.CENTER);
		txt.setBackground(WHITE);
		menuBar = new JMenuBar();
		fileMenu = new JMenu("Arquivo");
		fileMenu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(fileMenu);

		novoItem = new JMenuItem("Novo");
		Action novoAction = new AbstractAction("Novo") {
			@Override
			public void actionPerformed(ActionEvent e) {
				createFile();
				txt.setText(min);
			}
		};
		novoAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		novoItem.setAction(novoAction);
		NOVO.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				createFile();
				txt.setText(min);
			}
			
		});
		ABR.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				abrirFile();
				txt.setText(min);
			}
			
		});
		fileMenu.add(novoItem);

		ferrMenu = new JMenu("Ferramentas");
		menuBar.add(ferrMenu);
		subBoard = new JMenu("Placa");
		subCOM = new JMenu("Porta");
		ferrMenu.add(subBoard);
		ferrMenu.add(subCOM);
		int x = 0;
		gp = new ButtonGroup();
		radioBoards = new JRadioButtonMenuItem[boards.length];
		for (String a : boards) {
			radioBoards[x] = new JRadioButtonMenuItem(a);
			gp.add(radioBoards[x]);
			subBoard.add(radioBoards[x]);
			x++;
		}
		gpCom = new ButtonGroup();
		x = 0;
		radioCOMS = new JRadioButtonMenuItem[coms.length];
		for (String a : coms) {
			radioCOMS[x] = new JRadioButtonMenuItem(a);
			gpCom.add(radioCOMS[x]);
			subCOM.add(radioCOMS[x]);
			x++;
		}

		abrirItem = new JMenuItem("Abrir");
		Action abrirAction = new AbstractAction("Abrir") {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirFile();
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
				if (diretorio == null) {
					createFile();
				} else {
					saveFile();
				}

			}
		};
		SAL.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (diretorio == null) {
					createFile();
				} else {
					saveFile();
				}
			}
			
			
		});
		salvarAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		salvarItem.setAction(salvarAction);
		fileMenu.add(salvarItem);
		setJMenuBar(menuBar);

		setVisible(true);
	}

	private class CompHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (diretorio == null) {
				createFile();
				saveFile();
			}
			if (BrppCompiler.compile(diretorio.getAbsolutePath()))
				try {
					if (Uploader.compile(BrppCompiler.getFile()))
						BrppIDEFrame.LOG.append("Compilado");
					else
						BrppIDEFrame.LOG.append("Falha ao compilar...");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
		}

	}

	private class UploadHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (diretorio == null) {
				createFile();
				saveFile();
			}
			if (BrppCompiler.compile(diretorio.getAbsolutePath()))
				try {
					if (Uploader.upload(BrppCompiler.getFile(),
							getSelectedIndexCOM(), getSelectedIndex()))
						BrppIDEFrame.LOG.append("Compilado e Carregado");
					else
						BrppIDEFrame.LOG
								.append("Falha ao compilar e/ou carregar...");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private int getSelectedIndex() {

		for (int i = 0; i < boards.length; i++) {
			if (radioBoards[i].isSelected())
				return i;
		}
		return 0;
	}

	private String getSelectedIndexCOM() {

		for (int i = 0; i < coms.length; i++) {
			if (radioCOMS[i].isSelected())
				return radioCOMS[i].getText();
		}
		return "COM1";
	}

	private void createFile() {
		String name = JOptionPane.showInputDialog("Qual o nome do rascunho?");
		File f = new File("C://Arduino/Brino/" + name + "/" + name + ".brpp");
		if (f.getParentFile().mkdirs()) {

			try {
				f.createNewFile();
				FileWriter fw = new FileWriter(f.getAbsoluteFile(), false);
				txt.write(fw);
				fw.close();

			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		diretorio = f.getAbsoluteFile();
	}

	private void saveFile() {
		FileWriter fw;
		try {
			fw = new FileWriter(diretorio.getAbsoluteFile(), false);
			txt.write(fw);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void abrirFile(){
		txt.setText(null);
		JFileChooser FC = new JFileChooser();
		int res = FC.showOpenDialog(null);

		if (res == JFileChooser.APPROVE_OPTION) {
			diretorio = FC.getSelectedFile();
			try {
				String f = "";
				Scanner in = new Scanner(new File(
						diretorio.getAbsolutePath()));
				while (in.hasNext()) {
					String line = in.nextLine();
					// Highlight
					f += (line + "\n");
				}
				txt.setText(f);
				in.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} else
			JOptionPane.showMessageDialog(null,
					"Voce nao selecionou nenhum diretorio.");
	}
}