package br.com.RatosDePC.Brpp.IDEui;

/**
 * Painel localizado ao norte da IDE
 * 
 * @author Mateus Berardo de Souza Terra e Rafael Mascarenhas Dal Moro
 * @contributors 
 * @version 5/2/2016
 */

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import br.com.RatosDePC.Brpp.Utils.FileUtils;
import br.com.RatosDePC.Brpp.Utils.UploaderUtils;
import br.com.RatosDePC.Brpp.compiler.BrppCompiler;
import br.com.RatosDePC.SerialMonitor.SerialMonitor;

@SuppressWarnings("serial")
public class NorthPanel extends JPanel {

	private JButton COMP;
	private JButton COMPUP;
	private JButton NOVO;
	private JButton ABR;
	private JButton SAL;
	private JButton SERIAL;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	ImageIcon novo = new ImageIcon(getClass().getClassLoader().getResource(
			"resources/novoButton.png"));
	ImageIcon compup = new ImageIcon(getClass().getClassLoader().getResource(
			"resources/carrButton.png"));
	ImageIcon comp = new ImageIcon(getClass().getClassLoader().getResource(
			"resources/comButton.png"));
	ImageIcon abr = new ImageIcon(getClass().getClassLoader().getResource(
			"resources/opButton.png"));
	ImageIcon sal = new ImageIcon(getClass().getClassLoader().getResource(
			"resources/saveButton.png"));
	ImageIcon ser = new ImageIcon(getClass().getClassLoader().getResource(
			"resources/serialButton.png"));
	
	public NorthPanel() {
		// TODO Auto-generated constructor stub
		// cria e adiciona o botao compilar
		COMP = new JButton(comp);
		COMP.setToolTipText("Compilar");
		COMP.setBorderPainted(false);
		COMP.setBorder(emptyBorder);
		COMP.setContentAreaFilled(false);
		COMP.setRolloverIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("resources/comButtonFocus.png")));
		COMP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
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
		});
		// cria e adiciona o botao carregar
		COMPUP = new JButton(compup);
		COMPUP.setToolTipText("Compilar e carregar");
		COMPUP.setBorderPainted(false);
		COMPUP.setBorder(emptyBorder);
		COMPUP.setContentAreaFilled(false);
		COMPUP.setRolloverIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("resources/carrButtonFocus.png")));
//		COMPUP.setRolloverSelectedIcon(new ImageIcon(getClass()
//				.getClassLoader()
//				.getResource("resources/carrButtonClicked.png")));
		COMPUP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
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
		});
		// cria e adiciona botao arquivo novo
		NOVO = new JButton(novo);
		NOVO.setToolTipText("Novo");
		NOVO.setBorderPainted(false);
		NOVO.setBorder(emptyBorder);
		NOVO.setContentAreaFilled(false);
		NOVO.setRolloverIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("resources/novoButtonFocus.png")));
		NOVO.addActionListener(new ActionListener() {
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
		});
		// cria e adiciona botao abrir arquivo
		ABR = new JButton(abr);
		ABR.setToolTipText("Abrir");
		ABR.setBorderPainted(false);
		ABR.setBorder(emptyBorder);
		ABR.setContentAreaFilled(false);
		ABR.setRolloverIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("resources/opButtonFocus.png")));
		ABR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
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
		});
		// cria e adiciona botao salvar aquivo
		SAL = new JButton(sal);
		SAL.setToolTipText("Salvar");
		SAL.setBorderPainted(false);
		SAL.setBorder(emptyBorder);
		SAL.setContentAreaFilled(false);
		SAL.setRolloverIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("resources/saveButtonFocus.png")));
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
		//cria e adiciona o botao do serial monitor
		SERIAL = new JButton(ser);
		SERIAL.setToolTipText("Monitor Serial");
		SERIAL.setBorderPainted(false);
		SERIAL.setBorder(emptyBorder);
		SERIAL.setContentAreaFilled(false);
		SERIAL.setRolloverIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("resources/serialButtonFocus.png")));
		SERIAL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					SerialMonitor serial = new SerialMonitor(
							MenuBar.getSelectedIndexCOM());
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
		});
		add(COMP);
		add(COMPUP);
		add(NOVO);
		add(ABR);
		add(SAL);
		add(SERIAL);
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		add(COMP);
//		add(COMPUP);
//		add(NOVO);
//		add(ABR);
//		add(SAL);
//		add(SERIAL);
		SERIAL.setLocation(getWidth()-42, 5);
	}
	
}