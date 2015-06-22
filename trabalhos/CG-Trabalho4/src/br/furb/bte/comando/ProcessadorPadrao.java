package br.furb.bte.comando;

import br.furb.bte.Tela;

public class ProcessadorPadrao<T> implements ProcessadorComando<T> {

    private Tela tela;

    public ProcessadorPadrao() {
	this(null);
    }

    public ProcessadorPadrao(Tela tela) {
	this.tela = tela;
    }

    public void setTela(Tela tela) {
	this.tela = tela;
    }

    @Override
    public void processaComando(ComandoAbstrato<T> c) {
	c.executar();
	if (tela != null) {
	    tela.render();
	}
    }

}
