package br.com.RatosDePC.Brpp.IDEui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import br.com.RatosDePC.Brpp.Utils.FileUtils;
import br.com.RatosDePC.Brpp.Utils.UploaderUtils;
import br.com.RatosDePC.Brpp.compiler.BrppCompiler;

@SuppressWarnings("serial")
public class NorthPanel extends JPanel {

	private static JButton COMP;
	private static JButton COMPUP;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	ImageIcon novo = new ImageIcon("resources/novoButton.png");
	ImageIcon compup = new ImageIcon("resources/carrButton.png");
	ImageIcon comp = new ImageIcon("resources/comButton.png");
	ImageIcon abr = new ImageIcon("resources/opButton.png");
	ImageIcon sal = new ImageIcon("resources/saveButton.png");
	private JButton NOVO;
	private JButton ABR;
	private JButton SAL;

	public NorthPanel() {
		// TODO Auto-generated constructor stub
		// cria e adiciona o botao compilar
		COMP = new JButton(comp);
		COMP.setBorderPainted(false);
		COMP.setBorder(emptyBorder);
		COMP.setContentAreaFilled(false);
		COMP.setRolloverIcon(new ImageIcon("resources/comButtonFocus.png"));
		COMP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextPane());
					FileUtils.saveFile(BrppIDEFrame.getTextPane());
				}
				if (BrppCompiler.compile(FileUtils.getDiretorio()
						.getAbsolutePath()))
					try {
						if (UploaderUtils.compile(BrppCompiler.getFile()))
							BrppIDEFrame.LOG.append("Compilado");
						else
							BrppIDEFrame.LOG.append("Falha ao compilar...");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		add(COMP);
		// cria e adiciona o botao carregar
		COMPUP = new JButton(compup);
		COMPUP.setBorderPainted(false);
		COMPUP.setBorder(emptyBorder);
		COMPUP.setContentAreaFilled(false);
		COMPUP.setRolloverIcon(new ImageIcon("resources/carrButtonFocus.png"));
		COMPUP.setRolloverSelectedIcon(new ImageIcon(
				"resources/carrButtonClicked.png"));
		add(COMPUP);
		COMPUP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextPane());
					FileUtils.saveFile(BrppIDEFrame.getTextPane());
				}
				if (BrppCompiler.compile(FileUtils.getDiretorio()
						.getAbsolutePath()))
					try {
						if (UploaderUtils.upload(BrppCompiler.getFile(),
								MenuBar.getSelectedIndexCOM(),
								MenuBar.getSelectedIndex()))
							BrppIDEFrame.LOG.append("Compilado e Carregado");
						else
							BrppIDEFrame.LOG
									.append("Falha ao compilar e/ou carregar...");
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		});
		// cria e adiciona botao arquivo novo
		NOVO = new JButton(novo);
		NOVO.setBorderPainted(false);
		NOVO.setBorder(emptyBorder);
		NOVO.setContentAreaFilled(false);
		NOVO.setRolloverIcon(new ImageIcon("resources/novoButtonFocus.png"));
		add(NOVO);
		NOVO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				FileUtils.createFile(BrppIDEFrame.getTextPane());
			}
		});
		// cria e adiciona botao abrir arquivo
		ABR = new JButton(abr);
		ABR.setBorderPainted(false);
		ABR.setBorder(emptyBorder);
		ABR.setContentAreaFilled(false);
		ABR.setRolloverIcon(new ImageIcon("resources/opButtonFocus.png"));
		add(ABR);
		ABR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				FileUtils.abrirFile(BrppIDEFrame.getTextPane());
			}
		});
		// cria e adiciona botao salvar aquivo
		SAL = new JButton(sal);
		SAL.setBorderPainted(false);
		SAL.setBorder(emptyBorder);
		SAL.setContentAreaFilled(false);
		SAL.setRolloverIcon(new ImageIcon("resources/saveButtonFocus.png"));
		add(SAL);
		SAL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextPane());
				} else {
					FileUtils.saveFile(BrppIDEFrame.getTextPane());
				}
			}
		});
	}
}