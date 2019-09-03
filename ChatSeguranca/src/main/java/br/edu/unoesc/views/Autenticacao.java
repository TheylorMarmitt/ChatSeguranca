package br.edu.unoesc.views;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.FileUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

@StyleSheet("frontend://styles/styles.css")
@Route("autenticacao")
public class Autenticacao extends VerticalLayout {

	private static final long serialVersionUID = -8274922127183851458L;
	private int[] vets;

	public Autenticacao() {
		MemoryBuffer buffer = new MemoryBuffer();
		Upload upload = new Upload(buffer);

		Button b = new Button("enviar");

		add(upload, b);

		b.addClickListener(e -> {
			buffer.getFileData();
			String path = System.getProperty("user.home") + File.separator + "Documents" + File.separator
					+ "certificado";
			File novoArquivo = new File(path + File.separator + buffer.getFileName());
			boolean criado;

			try {
				criado = novoArquivo.createNewFile();
				FileUtils.writeByteArrayToFile(novoArquivo, buffer.getFileData().getMimeType().getBytes());
				System.out.println("arquivo criado =========" + criado);
			} catch (IOException erro) {
				System.out.println("erro ========" + erro);
			}

		});

		getStyle().set("align", "center");
		getStyle().set("margin-top", "30px");

	}

}
