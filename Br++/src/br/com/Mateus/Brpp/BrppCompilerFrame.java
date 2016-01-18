package br.com.Mateus.Brpp;

import java.awt.Component;
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
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class BrppCompilerFrame extends JFrame {
	public static JTextArea LOG = new JTextArea();
	private JButton COMP;
	private JButton COMPUP;
	private JButton CANCEL;
	public JFileChooser FC;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JComboBox<String> COM;
	private String[] coms = { "COM1", "COM2", "COM3", "COM4", "COM5", "COM6",
			"COM7", "COM8", "COM9" };

	// private JTextArea IDE = new JTextArea(null,"Código-Fonte",
	// getDefaultCloseOperation(), getDefaultCloseOperation());

	public BrppCompilerFrame(String title) {
		super(title);
		layout = new GridBagLayout();
		setLayout(layout);
		constraints = new GridBagConstraints();
		COM = new JComboBox<String>(coms);
		COMP = new JButton("Compilar");
		COMPUP = new JButton("Compilar e Carregar");
		// addComponent(IDE, 1, 1, 6, 2);
		// Dimension a = new Dimension(200,200);
		// IDE.setPreferredSize(a);
		addComponent(COMP, 4, 1, 1, 1);
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
		// addComponent(FC,0,0,1,2);

	}

	public static void setText(String line) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LOG.append(line);
			}
		});
	}

	private void addComponent(Component component, int row, int column,
			int width, int height) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		 constraints.fill = constraints.BOTH;
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
			if (BrppCompiler.getFile() != null) {
				try {
					Uploader.upload(BrppCompiler.getFile(), COM
							.getSelectedItem().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				JFileChooser FC = new JFileChooser();
				int res = FC.showOpenDialog(null);
				File diretorio = null;
				if (res == JFileChooser.APPROVE_OPTION) {
					diretorio = FC.getSelectedFile();
					if (BrppCompiler.compile(diretorio.getAbsolutePath()))
						try {
							if (Uploader.upload(BrppCompiler.getFile(), COM
									.getSelectedItem().toString()))
								BrppCompilerFrame
										.setText("Compilado e Carregado");
							else
								BrppCompilerFrame
										.setText("Falha ao compilar e/ou carregar...");
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
	}

	private class CanButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);

		}

	}
}
