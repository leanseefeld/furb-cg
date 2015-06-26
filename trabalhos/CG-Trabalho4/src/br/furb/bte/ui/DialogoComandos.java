package br.furb.bte.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Font;

@SuppressWarnings("serial")
public class DialogoComandos extends JDialog {

    private JTextPane txtpnComandos;

    /**
     * Create the dialog.
     */
    public DialogoComandos(Launcher launcher) {
	super(launcher, launcher != null);
	setTitle("Comandos");
	setBounds(100, 100, 450, 300);
	BorderLayout borderLayout = new BorderLayout();
	borderLayout.setVgap(10);
	borderLayout.setHgap(10);
	getContentPane().setLayout(borderLayout);

	JScrollPane scrollPane = new JScrollPane();
	getContentPane().add(scrollPane, BorderLayout.CENTER);

	txtpnComandos = new JTextPane();
	txtpnComandos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
	txtpnComandos.setContentType("text/html");
	txtpnComandos.setEditable(false);
	txtpnComandos.setBackground(Color.GRAY);
	scrollPane.setViewportView(txtpnComandos);
	
	carregarInstrucoes();
    }

    private void carregarInstrucoes() {
	StringBuilder sb = new StringBuilder();
	try (Scanner sc = new Scanner(new File("res/comandos.html"))) {
	    while (sc.hasNext()) {
		sb.append(sc.nextLine()).append('\n');
	    }
	    txtpnComandos.setText(sb.toString());
	} catch (FileNotFoundException e) {
	    new RuntimeException("Não foi possível mostrar as instruções de uso", e).printStackTrace();
	}

    }

}
