package br.furb.bte.controle;

import br.furb.bte.GameplayListener;
import br.furb.bte.Tela;

public abstract class Controlador implements GameplayListener {

    protected Tela tela;

    /**
     * Vincula este controlador a tela.
     * <p>
     * A partir deste momento os comandos tratados por este controlador serão aplicados na tela
     * configurada.
     * </p>
     * 
     * @param tela
     *            tela a ser controlada.
     */
    public abstract void associarTela(Tela tela);

}
