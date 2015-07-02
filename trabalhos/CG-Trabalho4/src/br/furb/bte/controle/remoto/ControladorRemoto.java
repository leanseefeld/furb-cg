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
import java.util.Arrays;
import java.util.Collection;
import javax.swing.JFrame;
import br.furb.bte.Tela;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.ProcessadorPadrao;
import br.furb.bte.comando.TipoComando;
import br.furb.bte.comando.gameplay.Pause;
import br.furb.bte.comando.gameplay.Reset;
import br.furb.bte.comando.gameplay.Resume;
import br.furb.bte.comando.moto.VirarDireita;
import br.furb.bte.comando.moto.VirarEsquerda;
import br.furb.bte.controle.Controlador;
import br.furb.bte.controle.input.CenarioInput;
import br.furb.bte.controle.input.GameplayInput;
import br.furb.bte.controle.input.KeyboardInput;
import br.furb.bte.controle.input.Moto1Input;
import br.furb.bte.objetos.Arena;
import br.furb.bte.objetos.Moto;

public class ControladorRemoto extends Controlador {

    private class RemoteListener extends Thread {

	private boolean running = true;
	private boolean handShaked = false;
	private TipoComando lastReceivedCommand;

	// comandos
	private final VirarDireita virarDireita;
	private final VirarEsquerda virarEsquerda;
	private final Resume resume;
	private final Pause pause;
	private final Reset reset;

	public RemoteListener(boolean isServer) {
	    super("RemoteListener-" + (isServer ? "server" : "client"));
	    virarDireita = new VirarDireita(null);
	    virarEsquerda = new VirarEsquerda(null);
	    resume = new Resume(null);
	    pause = new Pause(null);
	    reset = new Reset(null);
	}

	public void associarMoto(Moto moto) {
	    virarDireita.setAlvo(moto);
	    virarEsquerda.setAlvo(moto);
	}

	public void associarTela(Tela tela) {
	    resume.setAlvo(tela);
	    pause.setAlvo(tela);
	    reset.setAlvo(tela);
	}

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
		    // TODO: notificar perda de conexão
		    throw new RuntimeException(e);
		}
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }
	}

	private void processCommand(String receivedCommand) throws IOException {
	    System.out.println("ControladorRemoto.RemoteListener.processCommand: " + receivedCommand);
	    String[] splitCommand = receivedCommand.split("\\s+");
	    TipoComando tipoComando = TipoComando.valueOf(splitCommand[0]);
	    try {
		if (!handShaked) {
		    if (tipoComando != TipoComando.HELLO) {
			reply(TipoComando.BYE);
			throw new RuntimeException("A conexão remota não soube cumprimentar; recebido: "
				+ receivedCommand);
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
			    processadorGameplay.processaComandoRemoto(resume);
			    break;
			case PAUSE:
			    // TODO
			    processadorGameplay.processaComandoRemoto(pause);
			    break;
			case RESET:
			    // TODO
			    processadorGameplay.processaComandoRemoto(reset);
			    break;
			case DIREITA:
			    processadorMotoRemota.processaComandoRemoto(virarDireita);
			    break;
			case ESQUERDA:
			    processadorMotoRemota.processaComandoRemoto(virarEsquerda);
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
	    } finally {
		lastReceivedCommand = tipoComando;
	    }
	}

    }

    private final boolean isServer;
    private final Socket socket;
    private final RemoteListener remoteListener;
    private final BufferedWriter writer;

    private ProcessadorPadrao<Tela> processadorCenario;
    private ProcessadorRemoto<Tela> processadorGameplay;
    private ProcessadorRemoto<Moto> processadorMotoRemota;
    private ProcessadorRemoto<Moto> processadorMotoLocal;

    private final CenarioInput cenarioInput;
    private final GameplayInput gameplayInput;
    private final Moto1Input moto1Input;
    private final Collection<KeyboardInput<?>> allInputs;

    public ControladorRemoto(boolean isServer, Socket socket) throws IOException {
	System.out.println("ControladorRemoto: " + (isServer ? "server" : "client"));
	this.isServer = isServer;
	this.socket = socket;

	criarProcessadores();
	cenarioInput = new CenarioInput(processadorCenario);
	gameplayInput = new GameplayInput(processadorGameplay);
	moto1Input = new Moto1Input(null, processadorMotoLocal);
	allInputs = Arrays.asList(cenarioInput, gameplayInput, moto1Input);

	OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
	this.writer = new BufferedWriter(osw);
	this.remoteListener = new RemoteListener(isServer);
	this.remoteListener.start();

	reply(TipoComando.HELLO);
    }

    private void criarProcessadores() {
	class ReplyAction<T> {

	    void action(ComandoAbstrato<T> comando) {
		try {
		    reply(comando);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

	processadorCenario = new ProcessadorPadrao<>();
	processadorGameplay = new ProcessadorRemoto<Tela>(new ReplyAction<Tela>()::action);
	processadorMotoLocal = new ProcessadorRemoto<Moto>(new ReplyAction<Moto>()::action);
	processadorMotoRemota = new ProcessadorRemoto<Moto>(new ReplyAction<Moto>()::action);
    }

    protected void reply(TipoComando comando) throws IOException {
	reply(comando.toString());
    }

    protected void reply(String comando) throws IOException {
	System.out.println("ControladorRemoto.reply: " + comando);
	writer.write(comando + '\n');
	writer.flush();
    }

    protected void reply(ComandoAbstrato<?> comando) throws IOException {
	// TODO
	reply(comando.getTipoComando());
    }

    @Override
    public void associarTela(Tela tela) {
	JFrame frame = ((JFrame) tela.getParent().getParent().getParent().getParent());
	frame.setTitle(frame.getTitle() + (isServer ? " (server)" : " (client)"));

	if (this.tela != null) {
	    allInputs.forEach(i -> i.desassociarTela());
	}
	this.tela = tela;
	processadorCenario.setTela(tela);
	processadorGameplay.setTela(tela);
	processadorMotoRemota.setTela(tela);
	remoteListener.associarTela(tela);
	if (tela != null) {
	    allInputs.forEach(i -> i.associarTela(tela));
	    tela.addGameplayListener(this);
	}

    }

    @Override
    public void onReset() {
	if (tela != null) {
	    Arena arena = tela.getArena();
	    Moto motoLocal;
	    Moto motoRemota;
	    if (isServer) {
		motoLocal = arena.getMoto((short) 1);
		motoRemota = arena.getMoto((short) 2);
	    } else {
		motoLocal = arena.getMoto((short) 2);
		motoRemota = arena.getMoto((short) 1);
	    }
	    moto1Input.associarMoto(motoLocal);
	    remoteListener.associarMoto(motoRemota);
	    tela.setMotoJogador(motoLocal);
	}
    }

}
