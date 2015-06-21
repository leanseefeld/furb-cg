package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Step extends ComandoGameplay {

    public Step() {
	super(TipoComando.STEP);
    }

    @Override
    public void executar(Tela tela) {
	tela.executarComportamentos();
    }

}
