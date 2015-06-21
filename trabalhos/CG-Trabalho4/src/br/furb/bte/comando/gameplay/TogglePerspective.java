package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class TogglePerspective extends ComandoGameplay {

    public TogglePerspective() {
	super(TipoComando.TOGGLE_PERSPECTIVE);
    }

    @Override
    public void executar(Tela tela) {
	tela.alternarPerspectiva();
    }

}
