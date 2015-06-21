package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Reset extends ComandoGameplay {

    public Reset() {
	super(TipoComando.RESET);
    }

    @Override
    public void executar(Tela tela) {
	tela.reset();
    }

}
