package br.furb.bte.comando.moto;

import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.TipoComando;
import br.furb.bte.objetos.Moto;

public class VirarDireita extends ComandoAbstrato<Moto> {

    public VirarDireita() {
	super(TipoComando.DIREITA);
    }

    @Override
    public void executar(Moto moto) {
	moto.girar(90);
    }

}
