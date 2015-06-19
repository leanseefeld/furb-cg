package br.furb.bte.comando;

public abstract class ComandoAbstrato<T> {

    private final TipoComando tipoComando;

    public ComandoAbstrato(TipoComando tipoComando) {
	this.tipoComando = tipoComando;
    }

    public TipoComando getTipoComando() {
	return tipoComando;
    }

    /**
     * Executa este comando no alvo informado.
     * 
     * @param alvo
     *            alvo onde o comando vai ter efeito.
     */
    public abstract void executar(T alvo);

}
