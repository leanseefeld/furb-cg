package br.furb.bte.controle.remoto;

import java.io.IOException;
import java.net.Socket;

public abstract class TarefaConexaoRemota {

    protected Socket socket;

    public final void executaTarefa(ExecutorTarefaRemota executor) {
	try {
	    executor.disparaEvento(doExecutaTarefa());
	} catch (InterruptedException ie) {
	    executor.disparaEvento(new ConexaoRemotaEvent(this, ResultadoConexaoRemota.CANCELADO));
	} catch (Exception ex) {
	    executor.disparaEvento(new ConexaoRemotaEvent(this, ex));
	}
    }

    public abstract ConexaoRemotaEvent doExecutaTarefa() throws IOException, InterruptedException;

    public Socket getSocket() {
	return socket;
    }

}
