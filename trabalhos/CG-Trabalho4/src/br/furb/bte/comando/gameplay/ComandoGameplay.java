package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.TipoComando;

public abstract class ComandoGameplay extends ComandoAbstrato<Tela> {

    public ComandoGameplay(TipoComando tipoComando, Tela tela) {
	super(tipoComando, tela);
    }

}
