package br.furb.bte.controle.remoto;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConectorRemoto {

    private final ExecutorTarefaRemota executor;
    private final TarefaConectar tarefaConectar;
    private final TarefaEsperar tarefaEspera;

    public ConectorRemoto() {
	executor = new ExecutorTarefaRemota();
	executor.start();
	tarefaConectar = new TarefaConectar();
	tarefaEspera = new TarefaEsperar();
    }

    public void conectar(String host, int porta, ConexaoRemotaListener callback) {
	synchronized (executor) {
	    executor.setTarefaAtual(tarefaConectar);
	    tarefaConectar.setHost(host);
	    tarefaConectar.setPort(porta);
	    executor.setCallback(callback);
	    executor.notify();
	}
    }

    public void aguardarConexao(int porta, ConexaoRemotaListener callback) {
	synchronized (executor) {
	    // verifica se o executor tá livre; lança exceção se não tiver
	    // configura o executor pra abrir ServerSocket
	    executor.setTarefaAtual(tarefaEspera);
	    // altera a porta apenas se a tarefa tiver sido aceita
	    tarefaEspera.setPort(porta);
	    executor.setCallback(callback);
	    executor.notify();
	}
    }

    public String getHostLocal() {
	String hostAddress;
	try {
	    hostAddress = InetAddress.getLocalHost().getHostAddress();
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	    hostAddress = "<desconhecido>";
	}
	return hostAddress + ':' + tarefaEspera.getPort();
    }

    public boolean isAguardando() {
	return executor.getTarefaAtual() == tarefaEspera;
    }

    public void cancelarEspera() {
	cancelar(tarefaEspera);
    }

    public boolean isConectando() {
	return executor.getTarefaAtual() == tarefaConectar;
    }

    public void cancelarConexao() {
	cancelar(tarefaConectar);
    }

    private void cancelar(TarefaConexaoRemota tarefa) {
	if (executor.getTarefaAtual() != tarefa) {
	    return;
	}
	// interrompe a thread, que eventualmente vai disparar evento de cancelamento
	executor.cancelar();
    }

    public String getEnderecoConexao() {
	return tarefaConectar.getEndereco();
    }

    public void encerrarConexao() {
	executor.terminar();
    }

    public ControladorRemoto montarControlador() throws IOException {
	boolean isServer = tarefaEspera.isConcluida();
	Socket s = isServer ? tarefaEspera.getSocket() : tarefaConectar.getSocket();
	return new ControladorRemoto(isServer, s);
    }

}
