package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Pause extends ComandoGameplay {

    public Pause(Tela tela) {
	super(TipoComando.PAUSE, tela);
    }

    @Override
    public void executar() {
	alvo.alterarExecucao(false);
    }

}
