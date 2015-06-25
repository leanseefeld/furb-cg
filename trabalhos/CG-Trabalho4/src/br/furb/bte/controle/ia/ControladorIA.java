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
    private Direcao direcaoAtual;

    private final Collection<KeyboardInput<?>> allInputs;

    private Mapa mapa;

    public ControladorIA() {
	criarProcessadores();
	cenarioInput = new CenarioInput(processadorCenario);
	gameplayInput = new GameplayInput(processadorGameplay);
	motoIAInput = new MotoIAInput(null, processadorMotos);
	motoPlayerInput = new Moto1Input(null, processadorMotos);

	allInputs = Arrays.asList(cenarioInput, gameplayInput, motoPlayerInput);
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
	    this.direcaoAtual = Direcao.Norte;
	}
    }

    @Override
    public void beforePlay() {
	if (tela != null) {
	    mapa = tela.getMapa();
	    int[][] matriz = mapa.getMatriz();
	    String moveString = MyTronBot.processMove(matriz);
	    Direcao direcaoDestino = toDirecao(moveString);

	    System.out.println(moveString);
	    Lado lado = movimentoParaGirarPara(direcaoDestino);
	    if (lado != null) {
		
		MyTronBot.imprimirMapa();
		System.out.println("De " + this.direcaoAtual.name() + " para " + direcaoDestino.name());
		System.out.println("Girou para " + lado.name());

		motoIAInput.girar(lado);
		this.direcaoAtual = direcaoDestino;
	    }

	}
    }
    
    @Override
    public void onFinish() {
	System.out.println("################################ FIM DE JOGO ####################################");
	System.out.println("Dados do jogo:");
	System.out.println("Direção atual: " + this.direcaoAtual.name());
	System.out.println("Mapa gerado pela IA - Map");
	MyTronBot.imprimirMapa();
	System.out.println("Mapa gerado pela IA - GameState");
	MyTronBot.imprimirMapaGameState();
	System.out.println("Mapa gerado baseado na arena");
	System.out.println(mapa.toString());
    }

    /**
     * Retorna o movimento necessário para girar para a nova direção
     * 
     * @param direcaoDestino
     * @return
     */
    @SuppressWarnings("incomplete-switch")
    private Lado movimentoParaGirarPara(Direcao direcaoDestino) {
	Lado ladoGirar = null;
	switch (this.direcaoAtual) {
	    case Norte:
		switch (direcaoDestino) {
		    case Leste:
			ladoGirar = Lado.Direita;
			break;
		    case Oeste:
			ladoGirar = Lado.Esquerda;
			break;
		}
		break;
	    case Sul:
		switch (direcaoDestino) {
		    case Leste:
			ladoGirar = Lado.Esquerda;
			break;
		    case Oeste:
			ladoGirar = Lado.Direita;
			break;
		}
		break;
	    case Leste:
		switch (direcaoDestino) {
		    case Norte:
			ladoGirar = Lado.Esquerda;
			break;
		    case Sul:
			ladoGirar = Lado.Direita;
			break;
		}
		break;
	    case Oeste:
		switch (direcaoDestino) {
		    case Norte:
			ladoGirar = Lado.Direita;
			break;
		    case Sul:
			ladoGirar = Lado.Esquerda;
			break;
		}
		break;
	}
	return ladoGirar;
    }

    private Direcao toDirecao(String processMove) {
	char move = processMove.toUpperCase().charAt(0);
	switch (move) {
	    case 'N':
		return Direcao.Norte;
	    case 'S':
		return Direcao.Sul;
	    case 'E':
		return Direcao.Leste;
	    case 'W':
		return Direcao.Oeste;
	}
	System.out.println("Direção inválida: " + processMove);
	return null;
    }

}
