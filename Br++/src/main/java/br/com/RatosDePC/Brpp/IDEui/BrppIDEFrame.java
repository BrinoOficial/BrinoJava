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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import br.com.RatosDePC.Brpp.Utils.FileUtils;

public class BrppIDEFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static JTextPane code;
	private ImageIcon logo = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/logo.png"));
	private JMenuBar menuBar;
	private NorthPanel NorthPanel;
	private SouthPanel SouthPanel;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	private Color verde = new Color(72, 155, 0);//11, 125, 73
	private static final String min = "Configuracao() {\r\n"
			+ "//Coloque aqui seu codigo de Configuracao que sera executado uma vez\r\n"
			+ "\r\n"
			+ "}\r\n"
			+ "Principal(){\r\n"
			+ "//Coloque aqui seu codigo Principal, para rodar repetidamente\r\n"
			+ "\r\n" + "}\r\n";

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
		code = new JTextPane(new CodeDocument());
		code.setSize(200, 400);
		code.setText(getMin());
		JScrollPane CentralPane = new JScrollPane(code);
		code.setBorder(emptyBorder);
		add(CentralPane, BorderLayout.CENTER);
		// txt.setBackground(WHITE);
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		setVisible(true);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				int dialogResult = JOptionPane.showConfirmDialog(BrppIDEFrame.this,
						"Você deseja salvar seu rascunho antes de sair?",
						"Salvar", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if(dialogResult == JOptionPane.YES_OPTION){
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
		return code;
	}

	public static String getMin() {
		return min;
	}
}