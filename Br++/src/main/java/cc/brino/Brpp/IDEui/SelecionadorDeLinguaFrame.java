package cc.brino.Brpp.IDEui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import org.json.simple.parser.ParseException;

import cc.brino.Brpp.Utils.LanguageVersionUtils;


public class SelecionadorDeLinguaFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable linguasTable;
	private Map<String, Integer> linguas;
	private JScrollPane scrollPane;
	private JButton selecionar;
	private JButton cancelar;
	private JLabel texto;
	private String[] colunas = { "Língua", "Versão" };
	private Object[][] valores;
	Border emptyBorder = BorderFactory.createEmptyBorder();

	public SelecionadorDeLinguaFrame() {
		super("Selecionador de Língua");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		try {
			linguas = LanguageVersionUtils.getRemoteVersions();
			valores = new Object[linguas.size()][2];
			int i = 0;
			// itera pelas linguas e printa as
			// linguas
			for (String key : linguas.keySet()) {
				// Capturamos o valor a
				// partir da chave
				Integer value = linguas.get(key);
				valores[i][0] = key;
				valores[i][1] = value;
				i++;
			}
			JPanel center = new JPanel();
			linguasTable = new JTable(valores, colunas);
			scrollPane = new JScrollPane(linguasTable);
			scrollPane.setBorder(emptyBorder);
			// linguasTable.setFillsViewportHeight(true);
			center.add(scrollPane, BorderLayout.CENTER);
			add(center, BorderLayout.CENTER);
			texto = new JLabel(
					"Selecione a língua desejada abaixo e clique em instalar ou atualizar:");
			JPanel north = new JPanel();
			north.setLayout(new FlowLayout(FlowLayout.LEFT));
			north.add(texto);
			add(north, BorderLayout.NORTH);
			JPanel south = new JPanel();
			south.setLayout(new FlowLayout(FlowLayout.RIGHT));
			selecionar = new JButton("Baixar");
			selecionar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					int row = linguasTable.getSelectedRow();
					String ling = (String) valores[row][0];
					if (!LanguageVersionUtils.downloadLanguage(ling))
						fail();
					else {
						JOptionPane.showMessageDialog(null,
								ling
										+ " instalado com sucesso!",
								"Sucesso",
								JOptionPane.PLAIN_MESSAGE);
						dispose();
					}
				}
			});
			cancelar = new JButton("Cancelar");
			cancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					dispose();
				}
			});
			south.add(cancelar);
			south.add(selecionar);
			add(south, BorderLayout.SOUTH);
			setSize(500, 200);
			setLocation(100, 100);
			setVisible(true);
		} catch (UnknownHostException e) {
			fail();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

	public void fail() {
		JOptionPane.showMessageDialog(null,
				"Não foi possível recuperar as línguas disponíveis.\nTente novamente mais tarde",
				"ERRO",
				JOptionPane.ERROR_MESSAGE);
		dispose();
	}
}
