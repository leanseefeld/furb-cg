package br.furb.bte.controle.input;

import java.awt.event.KeyEvent;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.objetos.Moto;

public class Moto1Input extends MotoInput {

    public Moto1Input(Moto moto1, ProcessadorComando<Moto> processador) {
	super(moto1, processador, KeyEvent.VK_D, KeyEvent.VK_A);
    }

}
