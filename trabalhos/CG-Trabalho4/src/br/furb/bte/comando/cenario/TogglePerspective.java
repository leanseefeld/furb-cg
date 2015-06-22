package br.furb.bte.comando.cenario;

import br.furb.bte.Tela;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.TipoComando;

public class TogglePerspective extends ComandoAbstrato<Tela> {

    public TogglePerspective(Tela tela) {
	super(TipoComando.TOGGLE_PERSPECTIVE, tela);
    }

    @Override
    public void executar() {
	alvo.alternarPerspectiva();
    }

}
