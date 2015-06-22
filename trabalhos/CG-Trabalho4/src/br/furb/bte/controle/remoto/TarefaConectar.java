package br.furb.bte.controle.remoto;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TarefaConectar extends TarefaConexaoRemota {

    private int porta;
    private String host;

    @Override
    public ConexaoRemotaEvent doExecutaTarefa() throws IOException, InterruptedException {
	socket = new Socket();
	socket.connect(new InetSocketAddress(host, porta));
	return new ConexaoRemotaEvent(this, ResultadoConexaoRemota.SUCESSO);
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

    @Override
    public void cancelar() {
	if (socket != null) {
	    try {
		socket.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}
