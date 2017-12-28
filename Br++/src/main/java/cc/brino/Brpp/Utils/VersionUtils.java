package cc.brino.Brpp.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import cc.brino.Brpp.compiler.BrppCompiler;


public class VersionUtils {

	public static void checkVersion() throws MalformedURLException,
			IOException,
			URISyntaxException {
		String URL = "http://brino.cc/brino/versao.php";
		// pega a URL e conecta ao servidor
		URL website = new URL(URL);
		URLConnection connection = website.openConnection();
		// abre a stream para ler a tesposta do
		// servidor
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		// le linha a linha da resposta do servidor
		String inputLine;
		inputLine = in.readLine();
		response.append(inputLine);
		// fecha a stream
		in.close();
		Object[] options = { "Sim, por favor!", "Agora não, obrigado." };
		if (!BrppCompiler.version.equals(inputLine)) {
			int n = JOptionPane.showOptionDialog(null,
					"Há uma nova versão do Br.ino, você gostaria de atualizar?",
					"Atualização Disponível",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					1);
			if (n == 0)
				java.awt.Desktop.getDesktop()
						.browse(new URI(
								"http://brino.cc/download#downloadA"));
		}
	}
}
