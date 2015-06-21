package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Resume extends ComandoGameplay {

    public Resume() {
	super(TipoComando.RESUME);
    }

    @Override
    public void executar(Tela tela) {
	tela.alterarExecucao(true);
    }

}
