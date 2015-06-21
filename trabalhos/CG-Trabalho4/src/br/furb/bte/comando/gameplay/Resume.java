package br.furb.bte.comando.gameplay;

import br.furb.bte.Tela;
import br.furb.bte.comando.TipoComando;

public class Resume extends ComandoGameplay {

    public Resume(Tela tela) {
	super(TipoComando.RESUME, tela);
    }

    @Override
    public void executar() {
	alvo.alterarExecucao(true);
    }

}
