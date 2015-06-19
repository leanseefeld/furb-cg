package br.furb.bte.comando.moto;

import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.TipoComando;
import br.furb.bte.objetos.Moto;

public class VirarEsquerda extends ComandoAbstrato<Moto> {

    public VirarEsquerda() {
	super(TipoComando.ESQUERDA);
    }

    @Override
    public void executar(Moto alvo) {
	alvo.girar(-90);
    }

}
