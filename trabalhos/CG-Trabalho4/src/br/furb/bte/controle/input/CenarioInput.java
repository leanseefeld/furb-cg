package br.furb.bte.controle.input;

import java.awt.event.KeyEvent;
import br.furb.bte.Tela;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.comando.cenario.TogglePerspective;

public class CenarioInput extends KeyboardInput<Tela> {

    private final TogglePerspective togglePerspective;

    public CenarioInput(ProcessadorComando<Tela> processador) {
	super(processador);
	togglePerspective = new TogglePerspective(tela);
    }

    @Override
    public void associarTela(Tela tela) {
	super.associarTela(tela);
	togglePerspective.setAlvo(tela);
    }

    @Override
    public ComandoAbstrato<Tela> tratarTecla(KeyEvent event) {
	if (event.getKeyCode() == KeyEvent.VK_1) {
	    return togglePerspective;
	}
	return null;
    }

}
