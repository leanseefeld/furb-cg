package br.furb.bte.controle.remoto;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JFrame;
import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;
import br.furb.bte.controle.Controlador;

public class ControladorRemoto extends Controlador {

    private class RemoteListener extends Thread {

	private boolean running = true;
	private boolean handShaked = false;

	@Override
	public void run() {
	    try {
		InputStream is = socket.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		InputStreamReader isr = new InputStreamReader(bis);
		BufferedReader reader = new BufferedReader(isr);
		while (running) {
		    String command = reader.readLine();
		    processCommand(command);
		}
	    } catch (SocketException e) {
		if (e.getMessage().contains("reset")) {
		    // TODO: perda de conexão
		}
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }
	}

	private void processCommand(String receivedCommand) throws IOException {
	    String[] splitCommand = receivedCommand.split("\\s+");
	    TipoComando tipoComando = TipoComando.valueOf(splitCommand[0]);
	    if (!handShaked) {
		if (tipoComando != TipoComando.HELLO) {
		    reply(TipoComando.BYE);
		    throw new RuntimeException("A conexão remota não soube cumprimentar; recebido: " + receivedCommand);
		} else {
		    handShaked = true;
		}
	    } else {
		switch (tipoComando) {
		    case CONFIG_MOTO:
			// TODO
			break;
		    case READY:
			// TODO
			break;
		    case RESUME:
			// TODO
			break;
		    case PAUSE:
			// TODO
			break;
		    case RESET:
			// TODO
			break;
		    case DIREITA:
			// TODO
			break;
		    case ESQUERDA:
			// TODO
			break;
		    case COLISAO:
			// TODO
			break;
		    case ESTADO_MOTO:
			// TODO
			break;
		    case BYE:
			// TODO
			running = false;
			break;
		    default:
			reply(TipoComando.BYE);
			throw new RuntimeException("Comando inesperado: " + receivedCommand);
		}
	    }
	}

    }

    private final boolean isServer;
    private final Socket socket;
    private final RemoteListener remoteListener;
    private final BufferedWriter writer;

    public ControladorRemoto(boolean isServer, Socket socket) throws IOException {
	this.isServer = isServer;
	this.socket = socket;
	OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
	this.writer = new BufferedWriter(osw);
	this.remoteListener = new RemoteListener();
	this.remoteListener.start();
	reply(TipoComando.HELLO);
    }

    protected void reply(TipoComando comando) throws IOException {
	reply(comando.toString());
    }

    protected void reply(String comando) throws IOException {
	writer.write(comando);
	writer.flush();
    }

    @Override
    public void associarTela(Tela tela) {
	JFrame frame = ((JFrame) tela.getParent().getParent().getParent().getParent());
	frame.setTitle(frame.getTitle() + (isServer ? " (server)" : " (client)"));
	// TODO Auto-generated method stub

    }

    @Override
    public void afterInit() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onReset() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onPause() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onResume() {
	// TODO Auto-generated method stub

    }

}
