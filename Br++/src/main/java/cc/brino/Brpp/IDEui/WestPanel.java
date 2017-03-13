package cc.brino.Brpp.IDEui;

/*
 * Copyright (c) 2016 StarFruitBrasil
 * 
 * Permission is hereby granted, free of charge, to any
 * person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without
 * limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice
 * shall be included in all copies or substantial portions
 * of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
/**
 * Painel localizado ao norte da IDE
 * 
 * @author Mateus Berardo de Souza Terra e Rafael
 *         Mascarenhas Dal Moro
 * @contributors
 * @version 5/2/2016
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import cc.brino.Brpp.BrppCompilerMain;
import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.Utils.FileUtils;
import cc.brino.Brpp.Utils.UploaderUtils;
import cc.brino.Brpp.compiler.BrppCompiler;
import cc.brino.SerialMonitor.SerialMonitor;


@SuppressWarnings("serial")
public class WestPanel extends JPanel {

	private static final Logger logger = Logger.getLogger(BrppCompilerMain.class.getName());
	private JButton COMP;
	private JButton COMPUP;
	private JButton NOVO;
	private JButton ABR;
	private JButton SAL;
	private JButton SERIAL;
	Border emptyBorder = BorderFactory.createEmptyBorder();
	private final ImageIcon novo = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/novoButton.png"));
	private final ImageIcon novoFocus = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/novoButtonFocus.png"));
	private final ImageIcon compup = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/carrButton.png"));
	private final ImageIcon compupFocus = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/carrButtonFocus.png"));
	private final ImageIcon comp = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/comButton.png"));
	private final ImageIcon compFocus = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/comButtonFocus.png"));
	private final ImageIcon abr = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/opButton.png"));
	private final ImageIcon abrFocus = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/opButtonFocus.png"));
	private final ImageIcon sal = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/saveButton.png"));
	private final ImageIcon salFocus = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/saveButtonFocus.png"));
	private final ImageIcon ser = new ImageIcon(getClass().getClassLoader()
			.getResource("resources/serialButton.png"));
	private final ImageIcon serFocus = new ImageIcon(
			getClass().getClassLoader()
					.getResource("resources/serialButtonFocus.png"));

	public WestPanel() {
		// cria o botao compilar
		COMP = new JButton(comp);
		COMP.setToolTipText("Compilar");
		COMP.setBorderPainted(false);
		COMP.setBorder(emptyBorder);
		COMP.setContentAreaFilled(false);
		COMP.setRolloverIcon(compFocus);
		COMP.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextArea());
				}
				FileUtils.saveFile(BrppIDEFrame.getTextArea());
				if (BrppCompiler.compile(FileUtils.getDiretorio()
						.getAbsolutePath()))
					try {
						UploaderUtils.compile("\""
								+ BrppCompiler.getFile()
								+ "\"",Integer.parseInt(PrefManager.getPref("placa.index")));
					} catch (IOException e) {
						logger.log(Level.SEVERE,
								"Erro ao compilar no Arduino",
								e);
					}
			}
		});
		// cria o botao carregar
		COMPUP = new JButton(compup);
		COMPUP.setToolTipText("Compilar e carregar");
		COMPUP.setBorderPainted(false);
		COMPUP.setBorder(emptyBorder);
		COMPUP.setContentAreaFilled(false);
		COMPUP.setRolloverIcon(compupFocus);
		COMPUP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextArea());
				}
				FileUtils.saveFile(BrppIDEFrame.getTextArea());
				if (BrppCompiler.compile(FileUtils.getDiretorio()
						.getAbsolutePath()))
					try {
						UploaderUtils.upload("\""
								+ BrppCompiler.getFile()
								+ "\"",
								PrefManager.getPref("porta"),
								Integer.parseInt(PrefManager.getPref("placa.index")));
					} catch (IOException e) {
						logger.log(Level.SEVERE,
								"Erro ao compilar e carregar no Arduino",
								e);
					}
			}
		});
		// cria botao arquivo novo
		NOVO = new JButton(novo);
		NOVO.setToolTipText("Novo");
		NOVO.setBorderPainted(false);
		NOVO.setBorder(emptyBorder);
		NOVO.setContentAreaFilled(false);
		NOVO.setRolloverIcon(novoFocus);
		NOVO.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Você quer salvar o rascunho antes de criar um novo?");
				RSyntaxTextArea txt = BrppIDEFrame.getTextArea();
				switch (choice) {
					case 0:
						FileUtils.saveFile(txt);
					case 1:
						BrppIDEFrame.getTextArea()
								.setText(BrppIDEFrame.getMin());
						FileUtils.createFile(txt);
						break;
					case 2:
						break;
				}
			}
		});
		// cria botao abrir arquivo
		ABR = new JButton(abr);
		ABR.setToolTipText("Abrir");
		ABR.setBorderPainted(false);
		ABR.setBorder(emptyBorder);
		ABR.setContentAreaFilled(false);
		ABR.setRolloverIcon(abrFocus);
		ABR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showConfirmDialog(null,
						"Você quer salvar o rascunho antes de abrir um novo?");
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
		});
		// cria botao salvar aquivo
		SAL = new JButton(sal);
		SAL.setToolTipText("Salvar");
		SAL.setBorderPainted(false);
		SAL.setBorder(emptyBorder);
		SAL.setContentAreaFilled(false);
		SAL.setRolloverIcon(salFocus);
		SAL.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (FileUtils.getDiretorio() == null) {
					FileUtils.createFile(BrppIDEFrame.getTextArea());
				} else {
					FileUtils.saveFile(BrppIDEFrame.getTextArea());
				}
			}
		});
		// cria o botao do serial monitor
		SERIAL = new JButton(ser);
		SERIAL.setToolTipText("Monitor Serial");
		SERIAL.setBorderPainted(false);
		SERIAL.setBorder(emptyBorder);
		SERIAL.setContentAreaFilled(false);
		SERIAL.setRolloverIcon(serFocus);
		SERIAL.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (!PrefManager.getPref("porta")
						.equals("null")) {
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
		});
		Box box = Box.createVerticalBox();
		COMP.setAlignmentX(Component.CENTER_ALIGNMENT);
		COMPUP.setAlignmentX(Component.CENTER_ALIGNMENT);
		NOVO.setAlignmentX(Component.CENTER_ALIGNMENT);
		ABR.setAlignmentX(Component.CENTER_ALIGNMENT);
		SAL.setAlignmentX(Component.CENTER_ALIGNMENT);
		SERIAL.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// adiciona os botoes
		box.add(COMP);
		box.add(Box.createVerticalStrut(5));
		box.add(COMPUP);
		box.add(Box.createVerticalStrut(5));
		box.add(NOVO);
		box.add(Box.createVerticalStrut(5));
		box.add(ABR);
		box.add(Box.createVerticalStrut(5));
		box.add(SAL);
		box.add(Box.createVerticalStrut(5));
		box.add(SERIAL);
		add(box, BorderLayout.NORTH);
	}
}
