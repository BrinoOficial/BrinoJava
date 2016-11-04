package br.com.RatosDePC.Brpp.Utils;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import br.com.RatosDePC.Brpp.IDEui.BrppIDEFrame;
import br.com.RatosDePC.Brpp.IDEui.MenuBar;

public class abrirExemploAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static HashMap listaExemplos = MenuBar.getMap();

	public abrirExemploAction(String replace) {
		// TODO Auto-generated constructor stub
		super(replace);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int choice = JOptionPane.showConfirmDialog(null,
				"Você quer salvar o rascunho antes de abrir um novo?");
		JTextPane txt = BrppIDEFrame.getTextPane();
		switch (choice) {
		case 0:
			FileUtils.saveFile(txt);
		case 1:
			FileUtils.abrirFile(listaExemplos.get(e.getActionCommand())
					.toString(), BrppIDEFrame.getTextPane());
			break;
		case 2:
			break;
		}

	}

}
