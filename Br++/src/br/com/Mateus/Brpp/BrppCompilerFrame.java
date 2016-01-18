package br.com.Mateus.Brpp;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class BrppCompilerFrame extends JFrame {
	public static JTextArea LOG = new JTextArea();
	private JButton COMP;
	private JButton CANCEL;
	public JFileChooser FC;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private JTextField IDE = new JTextField(400);

	public BrppCompilerFrame(String title) {
		super(title);
		layout = new GridBagLayout();
		setLayout(layout);
		constraints = new GridBagConstraints();
		COMP = new JButton("Compilar");
		addComponent(COMP, 3, 1, 1, 1);
		// addComponent(IDE, 0,0,3,3);
		ButtonHandler handler = new ButtonHandler();
		COMP.addActionListener(handler);
		CANCEL = new JButton("Abort");
		addComponent(CANCEL, 3, 2, 1, 1);
		CanButtonHandler CanHandler = new CanButtonHandler();
		CANCEL.addActionListener(CanHandler);
		addComponent(LOG, 5, 0, 6, 4);
		// addComponent(FC,0,0,1,2);

	}

	public static void setText(String line) {
		LOG.setText(line + "\n");
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
						Uploader.upload(BrppCompiler.getFile());
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

	private class CanButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);

		}

	}
}
