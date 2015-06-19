package br.furb.bte.controle.remoto;

import java.io.IOException;
import java.net.ServerSocket;

public class TarefaEsperar extends TarefaConexaoRemota {

    private int port;

    @Override
    public ConexaoRemotaEvent doExecutaTarefa() throws IOException {
	socket = null;
	try (ServerSocket server = new ServerSocket(port)) {
	    socket = server.accept();
	    return new ConexaoRemotaEvent(this, ResultadoConexaoRemota.SUCESSO);
	}
    }

    public int getPort() {
	return port;
    }

    public void setPort(int port) {
	this.port = port;
    }

}
