package br.furb.bte.controle.ia;

import java.util.Arrays;
import java.util.Collection;
import br.furb.bte.Mapa;
import br.furb.bte.Tela;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.comando.ProcessadorPadrao;
import br.furb.bte.controle.Controlador;
import br.furb.bte.controle.input.CenarioInput;
import br.furb.bte.controle.input.GameplayInput;
import br.furb.bte.controle.input.KeyboardInput;
import br.furb.bte.controle.input.Moto1Input;
import br.furb.bte.ia.Direction;
import br.furb.bte.ia.MyTronBot;
import br.furb.bte.objetos.Arena;
import br.furb.bte.objetos.Moto;

public class ControladorIA extends Controlador {

    private Tela tela;

    private ProcessadorPadrao<Tela> processadorGameplay;
    private ProcessadorPadrao<Tela> processadorCenario;
    private ProcessadorComando<Moto> processadorMotos;
    private final CenarioInput cenarioInput;
    private final GameplayInput gameplayInput;
    private final MotoIAInput motoIAInput;
    private final Moto1Input motoPlayerInput;
    private Direction direcaoAtual;

    private final Collection<KeyboardInput<?>> allInputs;

    private final BotLoop botLoop;
    private Mapa mapa;

    public ControladorIA() {
	criarProcessadores();
	cenarioInput = new CenarioInput(processadorCenario);
	gameplayInput = new GameplayInput(processadorGameplay);
	motoIAInput = new MotoIAInput(null, processadorMotos);
	motoPlayerInput = new Moto1Input(null, processadorMotos);

	allInputs = Arrays.asList(cenarioInput, gameplayInput, motoPlayerInput);
	botLoop = new BotLoop();
	botLoop.start();
    }

    private void criarProcessadores() {
	processadorCenario = new ProcessadorPadrao<>();
	processadorGameplay = new ProcessadorPadrao<>();
	processadorMotos = new ProcessadorPadrao<>();
    }

    @Override
    public void associarTela(Tela tela) {
	if (this.tela != null) {
	    allInputs.forEach(i -> i.desassociarTela());
	}
	this.tela = tela;
	//	processadorCenario.setTela(tela);
	//	processadorGameplay.setTela(tela);
	if (tela != null) {
	    allInputs.forEach(i -> i.associarTela(tela));
	    tela.addGameplayListener(this);
	}
    }

    @Override
    public void onReset() {
	if (tela != null) {
	    Arena arena = tela.getArena();
	    motoPlayerInput.associarMoto(arena.getMoto((short) 1));
	    motoIAInput.associarMoto(arena.getMoto((short) 2));
	    this.direcaoAtual = Direction.North;
	}
    }

    @Override
    public void beforePlay() {
	if (tela != null) {
	    System.out.println("ControladorIA.beforePlay()");
	    botLoop.setFreeToPlay();
	}
    }

    private Direction deParaDirecao(Direction processMove) {
	switch (processMove) {
	    case West:
		return Direction.East;
	    case East:
		return Direction.West;
	    default:
		return processMove;
	}
    }

    @Override
    public void onFinish() {
	System.out.println("################################ FIM DE JOGO ####################################");
	System.out.println("Dados do jogo:");
	System.out.println("Direção atual: " + this.direcaoAtual.getNameInPT());
	System.out.println("Mapa gerado pela IA - Map");
	MyTronBot.imprimirMapa();
	System.out.println("Mapa gerado pela IA - GameState");
	System.out.println(mapa.toString());
    }

    /**
     * Retorna o movimento necessário para girar para a nova direção
     * 
     * @param direcaoDestino
     * @return
     */
    @SuppressWarnings("incomplete-switch")
    private Lado movimentoParaGirarPara(Direction direcaoDestino) {
	Lado ladoGirar = null;
	switch (this.direcaoAtual) {
	    case North:
		switch (direcaoDestino) {
		    case East:
			ladoGirar = Lado.Direita;
			break;
		    case West:
			ladoGirar = Lado.Esquerda;
			break;
		}
		break;
	    case South:
		switch (direcaoDestino) {
		    case East:
			ladoGirar = Lado.Esquerda;
			break;
		    case West:
			ladoGirar = Lado.Direita;
			break;
		}
		break;
	    case East:
		switch (direcaoDestino) {
		    case North:
			ladoGirar = Lado.Esquerda;
			break;
		    case South:
			ladoGirar = Lado.Direita;
			break;
		}
		break;
	    case West:
		switch (direcaoDestino) {
		    case North:
			ladoGirar = Lado.Direita;
			break;
		    case South:
			ladoGirar = Lado.Esquerda;
			break;
		}
		break;
	}
	return ladoGirar;
    }

    private class BotLoop extends Thread {

	private volatile boolean play;

	public BotLoop() {
	    super("BotLoop");
	}

	public synchronized void setFreeToPlay() {
	    play = true;
	    notify();
	}

	@Override
	public void run() {
	    while (true) {
		if (play) {
		    play = false;
		    System.out.println("ControladorIA.BotLoop.run(): playing");
		    mapa = tela.getMapa();
		    int[][] matriz = mapa.getMatriz();
		    Direction direcaoDestino = deParaDirecao(MyTronBot.processMove(matriz));

		    System.out.println(direcaoDestino.getNameInPT());
		    Lado lado = movimentoParaGirarPara(direcaoDestino);
		    if (lado != null) {

			//		MyTronBot.imprimirMapa();
			System.out.println("De " + direcaoAtual.getNameInPT() + " para " + direcaoDestino.getNameInPT());
			System.out.println("Girou para " + lado.name());

			motoIAInput.girar(lado);
			direcaoAtual = direcaoDestino;
		    }
		}
		synchronized (this) {
		    try {
			wait();
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
    }

}
