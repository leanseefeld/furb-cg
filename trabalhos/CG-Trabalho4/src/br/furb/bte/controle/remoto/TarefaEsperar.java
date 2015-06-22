package br.furb.bte.controle.remoto;

import java.io.IOException;
import java.net.ServerSocket;

public class TarefaEsperar extends TarefaConexaoRemota {

    private int port;
    private ServerSocket server;

    @Override
    public ConexaoRemotaEvent doExecutaTarefa() throws IOException {
	socket = null;
	try (ServerSocket server = new ServerSocket(port)) {
	    this.server = server;
	    socket = server.accept();
	    this.server = null;
	    return new ConexaoRemotaEvent(this, ResultadoConexaoRemota.SUCESSO);
	}
    }

    public int getPort() {
	return port;
    }

    public void setPort(int port) {
	this.port = port;
    }

    @Override
    public void cancelar() {
	if (server != null) {
	    try {
		server.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

}
