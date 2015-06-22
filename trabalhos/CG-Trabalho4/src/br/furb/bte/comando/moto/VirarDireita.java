package br.furb.bte.comando.moto;

import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.TipoComando;
import br.furb.bte.objetos.Moto;

public class VirarDireita extends ComandoAbstrato<Moto> {

    private static final TipoComando TIPO_COMANDO = TipoComando.DIREITA;

    public VirarDireita(Moto moto) {
	super(TIPO_COMANDO, moto);
    }

    @Override
    public void executar() {
	alvo.girar(90);
    }

}
