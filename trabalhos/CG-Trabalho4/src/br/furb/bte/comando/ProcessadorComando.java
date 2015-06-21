package br.furb.bte.comando;

public interface ProcessadorComando<T> {

    void processaComando(ComandoAbstrato<T> c);

}
