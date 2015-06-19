package br.furb.bte.controle;

import br.furb.bte.objetos.Moto;

public class Controlador {

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

}
