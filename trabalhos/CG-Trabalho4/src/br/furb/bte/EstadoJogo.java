package br.furb.bte;

public enum EstadoJogo {

    DERROTADO("Perdeu"),
    VITORIOSO("Venceu"),
    EMPATADO("Empatou");

    private final String mensagemFimDeJogo;

    private EstadoJogo(String mensagemFimDeJogo) {
	this.mensagemFimDeJogo = mensagemFimDeJogo;
    }

    public String getMensagemFimDeJogo() {
	return mensagemFimDeJogo;
    }

}
