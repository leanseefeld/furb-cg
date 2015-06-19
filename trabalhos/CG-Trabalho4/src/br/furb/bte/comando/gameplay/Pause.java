package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Pause extends ComandoGameplay {

    public Pause() {
	super(TipoComando.PAUSE);
    }

    @Override
    public void executar(Tela tela) {
	tela.alterarExecucao(tela.isExecutando());
    }

}
