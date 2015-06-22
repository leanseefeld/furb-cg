package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Step extends ComandoGameplay {

    public Step(Tela tela) {
	super(TipoComando.STEP, tela);
    }

    @Override
    public void executar() {
	alvo.executarComportamentos();
    }

}
