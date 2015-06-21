package br.furb.bte.comando;

public abstract class ComandoAbstrato<T> {

    private final TipoComando tipoComando;
    protected T alvo;

    public ComandoAbstrato(TipoComando tipoComando, T alvo) {
	this.tipoComando = tipoComando;
	this.alvo = alvo;
    }

    public TipoComando getTipoComando() {
	return tipoComando;
    }

    public T getAlvo() {
	return alvo;
    }

    public void setAlvo(T alvo) {
	this.alvo = alvo;
    }

    /**
     * Executa o comportamento com o alvo configurado no construtor ou no método {@link #setAlvo(T)}
     * .
     */
    public abstract void executar();

}
