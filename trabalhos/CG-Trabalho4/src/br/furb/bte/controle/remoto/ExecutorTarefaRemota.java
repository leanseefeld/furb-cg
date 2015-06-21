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
		    tarefaAtual.setConcluida(false);
		    tarefaAtual.executaTarefa(this);
		    // pode ter sido cancelada
		    if (tarefaAtual != null) {
			tarefaAtual.setConcluida(true);
			tarefaAtual = null;
		    }
		}
		try {
		    if (!terminado) {
			wait();
		    }
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
	    new Thread("ExecutorRemoto-dispatcher") {

		@Override
		public void run() {
		    // executa o callback apenas depois de dormir ou sair do while
		    synchronized (ExecutorTarefaRemota.this) {
			callback.onFinish(event);
		    }
		}
	    }.start();
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
	    tarefaAtual = null;
	}
    }
}
