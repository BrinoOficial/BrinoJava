package install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Installer {
	static File jar = null;
	static File placeJar = null;
	static File resources = null;
	static File placeRes = null;

	public static void main(String args[]) {
		try {
			JFileChooser FC = new JFileChooser("Localize o jar");
			int res = FC.showOpenDialog(null);

			if (res == JFileChooser.APPROVE_OPTION) {
				jar = FC.getSelectedFile();
				String installLoc = JOptionPane.showInputDialog("Selecione o local de instalação");
				placeJar = new File(installLoc+"/BrppCompiler.jar");
				resources = new File/* FileInputStream */("resources/");
				placeRes = new File/* OutputStream */(
						installLoc+"/resources/");
				copyFolder(jar, placeJar);
				copyFolder(resources, placeRes);
			}

		}
		// if (!placeJar.)
		// byte[] buffer = new byte[1024];
		// int length;
		// while ((length = jar.read(buffer)) > 0) {
		// placeJar.write(buffer, 0, length);
		// }
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// try {
			// jar.close();
			// placeJar.close();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
		}
	}

	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdirs();
				System.out.println("Directory copied from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			dest.getParentFile().mkdirs();
			dest.createNewFile();
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}
}
