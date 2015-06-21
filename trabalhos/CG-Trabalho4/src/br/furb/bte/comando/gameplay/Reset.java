package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Reset extends ComandoGameplay {

    public Reset(Tela tela) {
	super(TipoComando.RESET, tela);
    }

    @Override
    public void executar() {
	alvo.reset();
    }

}
