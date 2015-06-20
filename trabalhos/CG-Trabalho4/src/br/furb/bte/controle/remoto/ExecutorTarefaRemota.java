package br.furb.bte.controle.remoto;

public class ExecutorTarefaRemota extends Thread {

    private TarefaConexaoRemota tarefaAtual;
    private boolean terminado;
    private ConexaoRemotaListener callback;

    public ExecutorTarefaRemota() {
	super("ExecutorRemoto");
	setDaemon(true);
    }

    @Override
    public final void run() {
	synchronized (this) {
	    while (!terminado) {
		if (tarefaAtual != null) {
		    tarefaAtual.executaTarefa(this);
		    tarefaAtual = null;
		}
		try {
		    wait();
		} catch (InterruptedException e) {
		}
	    }
	}
    }

    public void terminar() {
	terminado = true;
	tarefaAtual = null;
	interrupt();
    }

    void disparaEvento(ConexaoRemotaEvent event) {
	if (callback != null) {
	    callback.onFinish(event);
	}
    }

    public TarefaConexaoRemota getTarefaAtual() {
	return tarefaAtual;
    }

    public void setTarefaAtual(TarefaConexaoRemota tarefa) {
	if (tarefaAtual != null) {
	    throw new IllegalStateException("Já há uma tarefa remota em andamento");
	}
	this.tarefaAtual = tarefa;
    }

    public void setCallback(ConexaoRemotaListener callback) {
	this.callback = callback;
    }

    public void cancelar() {
	if (tarefaAtual != null) {
	    tarefaAtual.cancelar();
	}
    }
}
