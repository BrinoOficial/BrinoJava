package cc.brino.Brpp;

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
 * 
 * Classe principal do programa
 * 
 * @author Mateus Berardo de Souza Terra
 * @contributors
 * @version 18/11/2016
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.json.simple.parser.ParseException;
import cc.brino.Brpp.IDEui.BrppIDEFrame;
import cc.brino.Brpp.Pref.PrefManager;
import cc.brino.Brpp.Utils.FileUtils;
import cc.brino.Brpp.Utils.JSONUtils;
import cc.brino.Brpp.Utils.KeywordManagerUtils;
import cc.brino.Brpp.Utils.LanguageVersionUtils;
import cc.brino.Brpp.compiler.BrppCompiler;


public class BrppCompilerMain {

	private static String path;
	private static final String currentRelativePath = Paths.get("")
			.toAbsolutePath()
			.toString();
	private static final File destDir = Paths.get(currentRelativePath,
			"Arduino",
			"libraries").toFile();
	private static final Logger logger = Logger.getLogger(BrppCompilerMain.class.getName());
	private static Logger l;
	private static Logger l2;
	private static FileHandler fh = null;
	private static FileHandler fh2 = null;
	private static BrppIDEFrame frame;

	public static void init() {
		try {
			fh = new FileHandler("brino.log", false);
			fh2 = new FileHandler("lib/detailedLog.log", false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		l = Logger.getLogger("");
		l2 = Logger.getLogger("");
		fh.setFormatter(new SimpleFormatter());
		fh2.setFormatter(new SimpleFormatter());
		l.addHandler(fh);
		l2.addHandler(fh2);
		l2.setLevel(Level.CONFIG);
		l.setLevel(Level.WARNING);
	}

	public static void main(String[] args) {
		BrppCompilerMain.init();
		final String[] getArgs = args;
		// cria o diretorio do Brino
		logger.log(Level.CONFIG, "Criando o diretorio do Brino");
		File dir = new File(FileUtils.getBrinodirectory());
		dir.mkdirs();
		// cria o diretorio do Brino
		logger.log(Level.CONFIG,
				"Criando o diretorio de bibliotecas do Brino");
		File libDir = new File(Paths.get(FileUtils.getBrinodirectory(),
				"bibliotecas").toAbsolutePath().toString());
		libDir.mkdirs();
		// salva o diretorio do brino
		logger.log(Level.INFO, "Path set to: " + currentRelativePath);
		setPath(currentRelativePath);
		try {
			logger.log(Level.CONFIG, "Configurando preferências");
			PrefManager.setPrefs();
			logger.log(Level.CONFIG, "Configurando lingua");
			JSONUtils.config(currentRelativePath);
			logger.log(Level.CONFIG, "Configurando bibliotecas");
			FileUtils.copyFolder(libDir, destDir);
			logger.log(Level.CONFIG,
					"Processando bibliotecas para highlight");
			KeywordManagerUtils.processLibraries();
			logger.log(Level.CONFIG, "Atualizando lingua");
			LanguageVersionUtils.updateLanguages();
		} catch (FileNotFoundException fnfe) {
			logger.log(Level.SEVERE,
					"Erro ao configurar o programa! Arquivo não encontrado : \n",
					fnfe);
		} catch (UnknownHostException uhe) {
			logger.log(Level.CONFIG,
					"Nao ha internet. Nao foi possivel atualizar a lingua!\n",
					uhe);
		} catch (MalformedURLException mue) {
			logger.log(Level.SEVERE,
					"Erro ao acessar a URL da lingua!\n",
					mue);
		} catch (IOException ioe) {
			logger.log(Level.SEVERE,
					"Erro ao configurar o programa!\n",
					ioe);
		} catch (ParseException pe) {
			logger.log(Level.SEVERE,
					"Erro ao fazer o parse da lingua!\n",
					pe);
		} catch (NullPointerException npe) {
			logger.log(Level.SEVERE,
					"Erro ao mover bibliotecas!\n",
					npe);
		}
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				frame = new BrppIDEFrame("Brino "
						+ BrppCompiler.version);
				frame.setSize(500, 600);
				frame.setLocation(100, 30);
				logger.log(Level.CONFIG, "Inicializando frame");
				frame.setVisible(true);
				if (getArgs.length > 0) {
					String filePath = getArgs[0];
					FileUtils.abrirFile(filePath,
							BrppIDEFrame.getTextArea(),
							true);
				}
			}
		});
	}

	public static String getPath() {
		return path;
	}

	public static JFrame getDialog() {
		// TODO Auto-generated method stub
		return frame;
	}

	public static void setPath(String path) {
		BrppCompilerMain.path = path;
	}
}