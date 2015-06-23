package br.furb.bte.controle.ia;

import java.awt.event.KeyEvent;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.comando.moto.VirarDireita;
import br.furb.bte.comando.moto.VirarEsquerda;
import br.furb.bte.controle.input.Lado;
import br.furb.bte.objetos.Moto;

public class MotoIAInput {
    
    private final VirarDireita virarDireita;
    private final VirarEsquerda virarEsquerda;
    private ProcessadorComando<Moto> processador ;

    public MotoInput(Moto moto, ProcessadorComando<Moto> processador) {
	this.processador = processador; 
	virarDireita = new VirarDireita(moto);
	virarEsquerda = new VirarEsquerda(moto);
    }
    
    public void associarMoto(Moto moto) {
	virarDireita.setAlvo(moto);
	virarEsquerda.setAlvo(moto);
    }

    @Override
    public girar(Lado lado) {
	ComandoAbstrato<Moto> comando = null;
	if (lado == Lado.Direita) {
	    comando = virarDireita;
	} else {
	    comando = virarEsquerda;
	}
	processador.processaComando(comando);
    }
}
