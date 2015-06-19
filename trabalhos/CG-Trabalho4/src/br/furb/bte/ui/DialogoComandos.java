package br.furb.bte.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class DialogoComandos extends JDialog {

    private final JPanel contentPanel = new JPanel();

    /**
     * Create the dialog.
     */
    public DialogoComandos(Launcher launcher) {
	super(launcher, launcher != null);
	setTitle("Comandos");
	setBounds(100, 100, 450, 300);
	getContentPane().setLayout(new BorderLayout());
	contentPanel.setBackground(Color.GRAY);
	contentPanel.setLayout(new FlowLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

}
