package br.furb.bte.controle;

import java.util.Arrays;
import java.util.Collection;
import br.furb.bte.Tela;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.comando.ProcessadorPadrao;
import br.furb.bte.controle.input.CenarioInput;
import br.furb.bte.controle.input.GameplayInput;
import br.furb.bte.controle.input.KeyboardInput;
import br.furb.bte.controle.input.Moto1Input;
import br.furb.bte.controle.input.Moto2Input;
import br.furb.bte.objetos.Arena;
import br.furb.bte.objetos.Moto;

public class ControladorLocal extends Controlador {

    private Tela tela;

    private ProcessadorPadrao<Tela> processadorCenario;
    private ProcessadorPadrao<Tela> processadorGameplay;
    private ProcessadorComando<Moto> processadorMotos;
    private final CenarioInput cenarioInput;
    private final GameplayInput gameplayInput;
    private final Moto1Input moto1Input;
    private final Moto2Input moto2Input;

    private final Collection<KeyboardInput<?>> allInputs;

    public ControladorLocal() {
	criarProcessadores();
	cenarioInput = new CenarioInput(processadorCenario);
	gameplayInput = new GameplayInput(processadorGameplay);
	moto1Input = new Moto1Input(null, processadorMotos);
	moto2Input = new Moto2Input(null, processadorMotos);

	allInputs = Arrays.asList(cenarioInput, gameplayInput, moto1Input, moto2Input);
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
	processadorCenario.setTela(tela);
	processadorGameplay.setTela(tela);
	if (tela != null) {
	    allInputs.forEach(i -> i.associarTela(tela));
	    tela.addGameplayListener(this);
	}
    }

    @Override
    public void onReset() {
	if (tela != null) {
	    Arena arena = tela.getArena();
	    moto1Input.associarMoto(arena.getMoto((short) 1));
	    moto2Input.associarMoto(arena.getMoto((short) 2));
	}
    }

}
