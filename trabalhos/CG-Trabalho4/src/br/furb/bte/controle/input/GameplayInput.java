package br.furb.bte.controle.input;

import java.awt.event.KeyEvent;
import br.furb.bte.Tela;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.comando.gameplay.ComandoGameplay;
import br.furb.bte.comando.gameplay.Pause;
import br.furb.bte.comando.gameplay.Reset;
import br.furb.bte.comando.gameplay.Resume;
import br.furb.bte.comando.gameplay.Step;

public class GameplayInput extends KeyboardInput<Tela> {

    private final Pause pause;
    private final Resume resume;
    private final Step step;
    private final Reset reset;

    public GameplayInput(ProcessadorComando<Tela> processador) {
	super(processador);
	pause = new Pause(tela);
	resume = new Resume(tela);
	step = new Step(tela);
	reset = new Reset(tela);
    }

    @Override
    public void associarTela(Tela tela) {
	super.associarTela(tela);
	pause.setAlvo(tela);
	resume.setAlvo(tela);
	step.setAlvo(tela);
	reset.setAlvo(tela);
    }

    @Override
    public ComandoAbstrato<Tela> tratarTecla(KeyEvent event) {
	ComandoGameplay comando = null;
	boolean jogando = tela.isExecutando();
	switch (event.getKeyCode()) {
	    case KeyEvent.VK_SPACE:
		if (!jogando && event.isControlDown()) {
		    comando = step;
		} else if (!tela.isGameOver()) {
		    comando = jogando ? pause : resume;
		}
		break;
	    case KeyEvent.VK_R:
		comando = reset;
		break;
	    default:
		comando = null;
	}
	return comando;
    }

}
