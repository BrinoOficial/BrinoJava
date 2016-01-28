package br.com.Mateus.Brpp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class BrppCompilerFrame extends JFrame {
	public static JTextArea LOG = new JTextArea();
	private JButton COMP;
	private JButton COMPUP;
	private JButton CANCEL;
	public JFileChooser FC;
	private JScrollPane IDEScroll;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JComboBox<String> COM;
	private String[] coms = { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6",
			"COM7", "COM8", "COM9", "COM10", "COM11", "COM12", "COM13","COM14", "COM15"};

	private JTextArea IDE = new JTextArea(null, "Código-Fonte",
			getDefaultCloseOperation(), getDefaultCloseOperation());

	public BrppCompilerFrame(String title) {
		super(title);
		layout = new GridBagLayout();
		setLayout(layout);
		constraints = new GridBagConstraints();
		IDEScroll=new JScrollPane();
		COM = new JComboBox<String>(coms);
		COMP = new JButton("Compilar");
		COMPUP = new JButton("Compilar e Carregar");
		addComponent(IDEScroll, 1, 1, 6, 2);
		IDEScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		IDEScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ScrollPaneLayout b = new ScrollPaneLayout();
		IDEScroll.setLayout(b);
		Dimension a = new Dimension(200, 200);
		IDE.setMinimumSize(a);
		IDEScroll.setPreferredSize(a);
		addComponent(COMP, 4, 1, 1, 1);
		IDEScroll.add(IDE);
		IDE.setVisible(true);
		addComponent(COMPUP, 4, 2, 1, 1);
		ButtonHandler handler = new ButtonHandler();
		UploadHandler uphandler = new UploadHandler();
		COMPUP.addActionListener(uphandler);
		COMP.addActionListener(handler);
		CANCEL = new JButton("Abort");
		addComponent(COM, 4, 3, 1, 1);
		addComponent(CANCEL, 4, 4, 1, 1);
		CanButtonHandler CanHandler = new CanButtonHandler();
		CANCEL.addActionListener(CanHandler);
		addComponent(LOG, 6, 0, 6, 4);
		LOG.setEditable(false);

	}

	public static void setText(String line) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LOG.setText(line);
			}
		});
	}

	private void addComponent(Component component, int row, int column,
			int width, int height) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		//constraints.fill = constraints.BOTH;
		layout.setConstraints(component, constraints);
		add(component);
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFileChooser FC = new JFileChooser();
			int res = FC.showOpenDialog(null);
			File diretorio = null;
			if (res == JFileChooser.APPROVE_OPTION) {
				diretorio = FC.getSelectedFile();
				if (BrppCompiler.compile(diretorio.getAbsolutePath()))
					try {
						if (Uploader.compile(BrppCompiler.getFile()))
							BrppCompilerFrame.setText("Compilado");
						else
							BrppCompilerFrame.setText("Falha ao compilar...");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				// JOptionPane.showMessageDialog(null, diretorio.getName()
				// + " compilado");
			} else
				JOptionPane.showMessageDialog(null,
						"Voce nao selecionou nenhum diretorio.");

		}

	}

	private class UploadHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			JFileChooser FC = new JFileChooser();
			int res = FC.showOpenDialog(null);
			File diretorio = null;
			if (res == JFileChooser.APPROVE_OPTION) {
				diretorio = FC.getSelectedFile();
				if (BrppCompiler.compile(diretorio.getAbsolutePath()))
					try {
						if (Uploader.upload(BrppCompiler.getFile(), COM
								.getSelectedItem().toString()))
							BrppCompilerFrame.setText("Compilado e Carregado");
						else
							BrppCompilerFrame
									.setText("Falha ao compilar e/ou carregar...");
					} catch (IOException e) {
						e.printStackTrace();
					}
			} else
				JOptionPane.showMessageDialog(null,
						"Voce nao selecionou nenhum diretorio.");

		}
	}

	private class CanButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);

		}

	}
}
