package br.furb.bte.controle.remoto;

import java.util.function.Consumer;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.ProcessadorPadrao;

public class ProcessadorRemoto<T> extends ProcessadorPadrao<T> {

    private Consumer<ComandoAbstrato<T>> funcaoOut;

    public ProcessadorRemoto(Consumer<ComandoAbstrato<T>> funcaoOut) {
	this.funcaoOut = funcaoOut;
    }

    @Override
    public void processaComando(ComandoAbstrato<T> c) {
	funcaoOut.accept(c);
	super.processaComando(c);
    }

    public void processaComandoRemoto(ComandoAbstrato<T> c) {
	super.processaComando(c);
    }

}
