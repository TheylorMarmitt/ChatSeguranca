package br.edu.unoesc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ArquivoUtil {

	
	private OutputStream out;

	public void baixar(File file) throws IOException {
		 out = null;
         FileInputStream in = new FileInputStream(file);
         byte[] buffer = new byte[4096];
         int length;
         while ((length = in.read(buffer)) > 0){
            out.write(buffer, 0, length);
         }
         in.close();
         out.flush();
	}
	
	public void baixarURL(String path, String localArquivo, String nomeArquivo) {
		try{
		    URL url = new URL(path);
		    URLConnection urlConn = url.openConnection();
		    BufferedInputStream is = new BufferedInputStream(urlConn.getInputStream());
		    File out = new File(localArquivo, nomeArquivo);
		    BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(out));
		    byte[] b = new byte[8 *1024];
		    int read = 0;
		    while((read = is.read(b)) > -1){
		        bout.write(b,0, read);
		    }
		    bout.flush();
		    bout.close();
		    is.close();
		     
		}
		catch(IOException mfu){
		    mfu.printStackTrace();
		}
	}
	
}
