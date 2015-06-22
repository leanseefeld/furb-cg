package br.furb.bte.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import br.furb.bte.controle.remoto.ConectorRemoto;
import br.furb.bte.controle.remoto.ConexaoRemotaEvent;
import br.furb.bte.controle.remoto.ConexaoRemotaListener;

@SuppressWarnings("serial")
public class DialogoRede extends JDialog {

    private static final Color ERROR_COLOR = Color.RED;
    private static final Font ERROR_FONT = new Font("Tahoma", Font.BOLD, 13);
    private static final Color STATUS_COLOR = Color.LIGHT_GRAY;
    private static final Font STATUS_FONT = new Font("Tahoma", Font.ITALIC, 13);
    private final JPanel contentPanel = new JPanel();
    private JTextField txtHost;
    private JFormattedTextField txtPortaConectar;
    private JLabel lblHost;
    private JLabel lblPorta;
    private JLabel lblPortSep;
    private JButton btnConectar;
    private JFormattedTextField txtPortaAguardar;
    private JButton btnAguardar;
    private JButton btnVoltar;
    private Launcher launcher;
    private final ConectorRemoto conectorRemoto;
    private JTextPane txtpnStatus;

    private ConexaoRemotaListener callbackPadrao = new ConexaoRemotaListener() {

	@Override
	public void onFinish(ConexaoRemotaEvent event) {
	    switch (event.getResultado()) {
		case FALHA:
		    showError(event.getException().getMessage());
		case CANCELADO:
		    setBloqueado(false);
		    break;
		case SUCESSO:
		    showStatus("Conectado!");
		    setBloqueado(false);
		    try {
			launcher.launch(conectorRemoto.montarControlador());
		    } catch (IOException e) {
			e.printStackTrace();
			showError(e.toString());
		    }
		    break;
		default:
		    setBloqueado(false);
		    break;
	    }
	}

    };

    public DialogoRede() {
	this(null);
    }

    /**
     * Create the dialog.
     * 
     * @param launcher
     */
    public DialogoRede(Launcher launcher) {
	this.launcher = launcher;
	build();
	this.conectorRemoto = new ConectorRemoto();
	assignEvents();
    }

    private void assignEvents() {
	addWindowListener(new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
		onClose(e);
	    }
	});
	btnVoltar.addActionListener(this::onClose);

	btnAguardar.addActionListener(event -> {
	    clearStatus();
	    try {
		conectorRemoto.aguardarConexao(getPortaAguardar(), callbackPadrao);
		setBloqueado(true);
		showStatus("Aguardando conexão em " + conectorRemoto.getHostLocal() + "...");
	    } catch (Exception ex) {
		ex.printStackTrace();
		showError(ex.getMessage());
	    }
	});

	btnConectar.addActionListener(event -> {
	    clearStatus();
	    try {
		conectorRemoto.conectar(getHost(), getPortaConectar(), callbackPadrao);
		setBloqueado(true);
		showStatus("Conectando à " + conectorRemoto.getEnderecoConexao() + "...");
	    } catch (Exception ex) {
		ex.printStackTrace();
		showError(ex.getMessage());
	    }
	});
    }

    private void onClose(EventObject event) {
	if (cancelOperations()) {
	    setVisible(false);
	    launcher.setVisible(true);
	}
    }

    private int getPortaConectar() {
	Object value = txtPortaConectar.getValue();
	if (value == null) {
	    throw new IllegalStateException("Informe uma porta");
	}
	return ((Number) value).intValue();
    }

    private int getPortaAguardar() {
	Object value = txtPortaAguardar.getValue();
	if (value == null) {
	    throw new IllegalStateException("Informe uma porta");
	}
	return ((Number) value).intValue();
    }

    private String getHost() {
	return txtHost.getText().replace(" ", "");
    }

    private boolean cancelOperations() {
	if (conectorRemoto == null) {
	    return true;
	}
	if (conectorRemoto.isAguardando()) {
	    if (JOptionPane.showConfirmDialog(this, "Isso irá cancelar a espera. Tem certeza?", "Aguardando conexão",
		    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		conectorRemoto.cancelarEspera();
		return true;
	    }
	    return false;
	} else if (conectorRemoto.isConectando()) {
	    if (JOptionPane.showConfirmDialog(this, "Isso irá cancelar a conexão. Tem certeza?", "Tentando conexão",
		    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
		conectorRemoto.cancelarConexao();
		return true;
	    }
	    return false;
	}
	return true;
    }

    private void build() {
	setMaximumSize(new Dimension(400, 240));
	setMinimumSize(new Dimension(220, 240));
	setTitle("Jogo em rede");
	getContentPane().setBackground(Color.LIGHT_GRAY);
	setBackground(Color.LIGHT_GRAY);
	setBounds(100, 100, 284, 218);
	BorderLayout borderLayout = new BorderLayout();
	getContentPane().setLayout(borderLayout);
	contentPanel.setBackground(Color.LIGHT_GRAY);
	contentPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
	getContentPane().add(contentPanel, BorderLayout.CENTER);
	GridBagLayout gbl_contentPanel = new GridBagLayout();
	gbl_contentPanel.columnWidths = new int[] { 0, 0 };
	gbl_contentPanel.rowHeights = new int[] { 30, 50, 0 };
	gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
	gbl_contentPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
	contentPanel.setLayout(gbl_contentPanel);
	{
	    JPanel panelConectar = new JPanel();
	    panelConectar.setToolTipText("Informar um endereço para se conectar e jogar com outra máquina.");
	    panelConectar.setBackground(Color.LIGHT_GRAY);
	    panelConectar.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Conectar",
		    TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
	    GridBagConstraints gbc_panelConectar = new GridBagConstraints();
	    gbc_panelConectar.insets = new Insets(0, 0, 5, 0);
	    gbc_panelConectar.fill = GridBagConstraints.BOTH;
	    gbc_panelConectar.gridx = 0;
	    gbc_panelConectar.gridy = 0;
	    contentPanel.add(panelConectar, gbc_panelConectar);
	    GridBagLayout gbl_panelConectar = new GridBagLayout();
	    gbl_panelConectar.columnWidths = new int[] { 50, 2, 50, 10, 0 };
	    gbl_panelConectar.rowHeights = new int[] { 0, 0, 0 };
	    gbl_panelConectar.columnWeights = new double[] { 3.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
	    gbl_panelConectar.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
	    panelConectar.setLayout(gbl_panelConectar);
	    {
		lblHost = new JLabel("Host:");
		GridBagConstraints gbc_lblHost = new GridBagConstraints();
		gbc_lblHost.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblHost.gridx = 0;
		gbc_lblHost.gridy = 0;
		panelConectar.add(lblHost, gbc_lblHost);
		lblHost.setFont(new Font("Tahoma", Font.PLAIN, 10));
	    }
	    {
		lblPorta = new JLabel("Porta:");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblPorta.insets = new Insets(0, 0, 0, 5);
		gbc_lblPorta.gridx = 2;
		gbc_lblPorta.gridy = 0;
		panelConectar.add(lblPorta, gbc_lblPorta);
		lblPorta.setFont(new Font("Tahoma", Font.PLAIN, 10));
	    }
	    {
		txtHost = new JTextField();
		txtHost.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_txtHost = new GridBagConstraints();
		gbc_txtHost.insets = new Insets(0, 0, 5, 0);
		gbc_txtHost.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHost.gridx = 0;
		gbc_txtHost.gridy = 1;
		panelConectar.add(txtHost, gbc_txtHost);
		txtHost.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtHost.setBackground(new Color(220, 220, 220));
		txtHost.setText("192.168.0.2");
		txtHost.setColumns(10);
	    }
	    {
		lblPortSep = new JLabel(":");
		GridBagConstraints gbc_lblPortSep = new GridBagConstraints();
		gbc_lblPortSep.anchor = GridBagConstraints.EAST;
		gbc_lblPortSep.gridx = 1;
		gbc_lblPortSep.gridy = 1;
		panelConectar.add(lblPortSep, gbc_lblPortSep);
	    }
	    {
		txtPortaConectar = new JFormattedTextField(NumberFormat.getIntegerInstance());
		GridBagConstraints gbc_txtPortaConectar = new GridBagConstraints();
		gbc_txtPortaConectar.insets = new Insets(0, 0, 5, 5);
		gbc_txtPortaConectar.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPortaConectar.gridx = 2;
		gbc_txtPortaConectar.gridy = 1;
		panelConectar.add(txtPortaConectar, gbc_txtPortaConectar);
		txtPortaConectar.setBackground(new Color(220, 220, 220));
		txtPortaConectar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtPortaConectar.setValue(42158);
		txtPortaConectar.setColumns(5);
	    }
	    {
		btnConectar = new JButton("»");
		btnConectar.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnConectar.setBackground(Color.GRAY);
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.fill = GridBagConstraints.VERTICAL;
		gbc_btnConectar.gridheight = 2;
		gbc_btnConectar.gridx = 3;
		gbc_btnConectar.gridy = 0;
		panelConectar.add(btnConectar, gbc_btnConectar);
	    }
	}
	{
	    JPanel panelAguardar = new JPanel();
	    panelAguardar.setToolTipText("Aguardar que alguém se conecte a esta máquina na porta informada.");
	    panelAguardar.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Aguardar",
		    TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
	    panelAguardar.setBackground(Color.LIGHT_GRAY);
	    GridBagConstraints gbc_panelAguardar = new GridBagConstraints();
	    gbc_panelAguardar.fill = GridBagConstraints.BOTH;
	    gbc_panelAguardar.gridx = 0;
	    gbc_panelAguardar.gridy = 1;
	    contentPanel.add(panelAguardar, gbc_panelAguardar);
	    GridBagLayout gbl_panelAguardar = new GridBagLayout();
	    gbl_panelAguardar.columnWidths = new int[] { 5, 50, 0, 5, 0 };
	    gbl_panelAguardar.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
	    gbl_panelAguardar.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
	    gbl_panelAguardar.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
	    panelAguardar.setLayout(gbl_panelAguardar);
	    {
		JLabel label = new JLabel("Porta:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 10));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.SOUTHWEST;
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 2;
		panelAguardar.add(label, gbc_label);
	    }
	    {
		txtPortaAguardar = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtPortaAguardar.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortaAguardar.setValue(42158);
		txtPortaAguardar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtPortaAguardar.setColumns(5);
		txtPortaAguardar.setBackground(new Color(220, 220, 220));
		GridBagConstraints gbc_txtPortaAguardar = new GridBagConstraints();
		gbc_txtPortaAguardar.insets = new Insets(0, 0, 5, 5);
		gbc_txtPortaAguardar.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPortaAguardar.gridx = 1;
		gbc_txtPortaAguardar.gridy = 3;
		panelAguardar.add(txtPortaAguardar, gbc_txtPortaAguardar);
	    }
	    {
		btnAguardar = new JButton("Aguardar");
		btnAguardar.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnAguardar.setBackground(Color.GRAY);
		GridBagConstraints gbc_btnAguardar = new GridBagConstraints();
		gbc_btnAguardar.fill = GridBagConstraints.VERTICAL;
		gbc_btnAguardar.gridheight = 2;
		gbc_btnAguardar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAguardar.gridx = 2;
		gbc_btnAguardar.gridy = 2;
		panelAguardar.add(btnAguardar, gbc_btnAguardar);
	    }
	    getContentPane().setFocusTraversalPolicy(
		    new FocusTraversalOnArray(new Component[] { txtHost, txtPortaConectar, btnConectar,
			    txtPortaAguardar, btnAguardar, btnVoltar }));
	}
	{
	    JPanel buttonPane = new JPanel();
	    buttonPane.setBackground(Color.DARK_GRAY);
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    GridBagLayout gbl_buttonPane = new GridBagLayout();
	    gbl_buttonPane.columnWidths = new int[] { 0, 0, 0 };
	    gbl_buttonPane.rowHeights = new int[] { 27, 0 };
	    gbl_buttonPane.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
	    gbl_buttonPane.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
	    buttonPane.setLayout(gbl_buttonPane);
	    {
		txtpnStatus = new JTextPane();
		txtpnStatus.setForeground(STATUS_COLOR);
		txtpnStatus.setFont(STATUS_FONT);
		txtpnStatus.setEditable(false);
		txtpnStatus.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_txtpnStatus = new GridBagConstraints();
		gbc_txtpnStatus.insets = new Insets(5, 5, 5, 0);
		gbc_txtpnStatus.fill = GridBagConstraints.BOTH;
		gbc_txtpnStatus.gridx = 0;
		gbc_txtpnStatus.gridy = 0;
		buttonPane.add(txtpnStatus, gbc_txtpnStatus);
	    }
	    {
		btnVoltar = new JButton("Voltar");
		btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnVoltar.setBackground(Color.GRAY);
		btnVoltar.setActionCommand("OK");
		GridBagConstraints gbc_btnVoltar = new GridBagConstraints();
		gbc_btnVoltar.insets = new Insets(5, 5, 5, 5);
		gbc_btnVoltar.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnVoltar.gridx = 1;
		gbc_btnVoltar.gridy = 0;
		buttonPane.add(btnVoltar, gbc_btnVoltar);
		getRootPane().setDefaultButton(btnVoltar);
	    }
	}
    }

    @Override
    public void setVisible(boolean b) {
	super.setVisible(b);
	if (b) {
	    txtHost.requestFocus();
	    txtHost.selectAll();
	}
	clearStatus();
    }

    protected void setBloqueado(boolean bloqueado) {
	txtHost.setEnabled(!bloqueado);
	txtPortaAguardar.setEnabled(!bloqueado);
	txtPortaConectar.setEnabled(!bloqueado);
	btnAguardar.setEnabled(!bloqueado);
	btnConectar.setEnabled(!bloqueado);
    }

    private void showStatus(String message) {
	txtpnStatus.setForeground(STATUS_COLOR);
	txtpnStatus.setFont(STATUS_FONT);
	txtpnStatus.setText(message);
    }

    private void showError(String message) {
	txtpnStatus.setForeground(ERROR_COLOR);
	txtpnStatus.setFont(ERROR_FONT);
	txtpnStatus.setText(message);
    }

    private void clearStatus() {
	showStatus("");
    }

    @Override
    public void dispose() {
	conectorRemoto.encerrarConexao();
	super.dispose();
    }

}
