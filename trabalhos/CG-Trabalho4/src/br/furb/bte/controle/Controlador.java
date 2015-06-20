package br.furb.bte.controle;

import br.furb.bte.Tela;
import br.furb.bte.objetos.Moto;

public abstract class Controlador {

    private Moto moto;

    /**
     * Define a moto a ser controlada por este controlador.
     * 
     * @param moto
     *            moto a ser controlada por este controlador.
     */
    public void associarMoto(Moto moto) {
	this.moto = moto;
    }

    /**
     * Retorna a moto sendo controlada por este controlador.
     * 
     * @return moto sendo controlada por este controlador.
     */
    public Moto getMotoAssociada() {
	return moto;
    }

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
