package br.furb.bte.controle.remoto;

import java.io.IOException;
import java.net.Socket;

public class TarefaConectar extends TarefaConexaoRemota {

    private int porta;
    private String host;

    @Override
    public ConexaoRemotaEvent doExecutaTarefa() throws IOException, InterruptedException {
	this.socket = null;
	try (Socket socket = new Socket(host, porta)) {
	    this.socket = socket;
	    return new ConexaoRemotaEvent(this, ResultadoConexaoRemota.SUCESSO);
	}
    }

    public void setHost(String host) {
	this.host = host;

    }

    public void setPort(int porta) {
	this.porta = porta;

    }

    public String getEndereco() {
	return getHost() + ":" + getPort();
    }

    private String getHost() {
	return host;
    }

    private int getPort() {
	return porta;
    }

}
