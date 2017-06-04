package cc.brino.Brpp.IDEui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.json.simple.parser.ParseException;
import cc.brino.Brpp.Utils.LanguageVersionUtils;


public class SelecionadorDeLinguaFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable linguasTable;
	private JPanel center, north, south;
	private Map<String, Integer> linguas;
	private JScrollPane scrollPane;
	private JButton selecionar;
	private JButton cancelar;
	private JLabel texto;
	private String[] colunas = { "Língua", "Versão" };
	private Object[][] valores;

	public SelecionadorDeLinguaFrame() {
		super("Selecionador de Língua");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			linguas = LanguageVersionUtils.getRemoteVersions();
			valores = new Object[linguas.size()][2];
			int i = 0;
			// itera pelas linguas e printa as
			// linguas
			for (String key : linguas.keySet()) {
				// Captura o valor a
				// partir da chave
				Integer value = linguas.get(key);
				valores[i][0] = key;
				valores[i][1] = value;
				i++;
			}
			center = new JPanel();
			center.setBackground(UIConstants.CINZA);
			linguasTable = new JTable(valores, colunas);
			scrollPane = new JScrollPane(linguasTable);
			scrollPane.setBackground(UIConstants.CINZA);
			scrollPane.setViewportBorder(UIConstants.BORDAVAZIA);
			scrollPane.setBorder(UIConstants.BORDAVAZIA);
			center.add(scrollPane, BorderLayout.CENTER);
			add(center, BorderLayout.CENTER);
			texto = new JLabel(
					"Selecione a língua desejada abaixo e clique em baixar:");
			north = new JPanel();
			north.setBackground(UIConstants.CINZA);
			north.setLayout(new FlowLayout(FlowLayout.LEFT));
			north.add(texto);
			texto.setForeground(Color.white);
			add(north, BorderLayout.NORTH);
			south = new JPanel();
			south.setBackground(UIConstants.CINZA);
			south.setLayout(new FlowLayout(FlowLayout.RIGHT));
			selecionar = new JButton("Baixar");
			selecionar.setBackground(Color.green);
			selecionar.setBorder(new BubbleBorder(Color.green));
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
			cancelar.setBackground(Color.red);
			cancelar.setBorder(new BubbleBorder(Color.red));
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