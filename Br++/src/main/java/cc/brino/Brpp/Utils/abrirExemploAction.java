package cc.brino.Brpp.Utils;

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
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import cc.brino.Brpp.IDEui.BrppIDEFrame;
import cc.brino.Brpp.IDEui.MenuBar;


public class abrirExemploAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static HashMap<String, String> listaExemplos = MenuBar.getMap();

	public abrirExemploAction(String replace) {
		super(replace);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int choice = JOptionPane.showConfirmDialog(null,
				"Voce quer salvar o rascunho antes de abrir um novo?");
		RSyntaxTextArea txt = BrppIDEFrame.getTextArea();
		switch (choice) {
			case 0:
				FileUtils.saveFile(txt);
			case 1:
				FileUtils.abrirFile(listaExemplos.get(e.getActionCommand())
						.toString(),
						BrppIDEFrame.getTextArea(),
						false);
				break;
			case 2:
				break;
		}
	}
}
