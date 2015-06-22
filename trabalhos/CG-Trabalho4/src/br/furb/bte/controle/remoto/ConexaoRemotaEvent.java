package br.furb.bte.controle.remoto;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ConexaoRemotaEvent extends EventObject {

    private Throwable exception;
    private ResultadoConexaoRemota resultado;

    public ConexaoRemotaEvent(Object source, ResultadoConexaoRemota resultado) {
        super(source);
        this.resultado = resultado;
    }

    public ConexaoRemotaEvent(Object source, Throwable ex) {
        this(source, ResultadoConexaoRemota.FALHA);
        this.exception = ex;
    }

    public Throwable getException() {
        return exception;
    }

    public ResultadoConexaoRemota getResultado() {
        return resultado;
    }

}