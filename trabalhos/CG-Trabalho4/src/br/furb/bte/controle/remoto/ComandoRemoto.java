package br.furb.bte.controle.remoto;

import br.furb.bte.comando.TipoComando;

public class ComandoRemoto {

    private final TipoComando tipoComando;

    public ComandoRemoto(TipoComando tipoComando) {
	this.tipoComando = tipoComando;
    }

    public TipoComando getTipoComando() {
	return tipoComando;
    }

}
