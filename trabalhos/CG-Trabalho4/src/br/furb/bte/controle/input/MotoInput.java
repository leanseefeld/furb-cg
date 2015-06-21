package br.furb.bte.controle.input;

import java.awt.event.KeyEvent;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.comando.moto.VirarDireita;
import br.furb.bte.comando.moto.VirarEsquerda;
import br.furb.bte.objetos.Moto;

public class MotoInput extends KeyboardInput<Moto> {

    private final VirarDireita virarDireita;
    private final VirarEsquerda virarEsquerda;
    private final int teclaDireita, teclaEsquerda;

    public MotoInput(Moto moto, ProcessadorComando<Moto> processador, int teclaDireita, int teclaEsquerda) {
	super(processador);
	this.teclaDireita = teclaDireita;
	this.teclaEsquerda = teclaEsquerda;
	virarDireita = new VirarDireita(moto);
	virarEsquerda = new VirarEsquerda(moto);
    }
    
    public void associarMoto(Moto moto) {
	virarDireita.setAlvo(moto);
	virarEsquerda.setAlvo(moto);
    }

    @Override
    public ComandoAbstrato<Moto> tratarTecla(KeyEvent event) {
	if (!tela.isExecutando() && !event.isControlDown()) {
	    return null;
	}
	ComandoAbstrato<Moto> comando = null;
	int keyCode = event.getKeyCode();
	if (keyCode == teclaDireita) {
	    comando = virarDireita;
	} else if (keyCode == teclaEsquerda) {
	    comando = virarEsquerda;
	}
	return comando;
    }

}
