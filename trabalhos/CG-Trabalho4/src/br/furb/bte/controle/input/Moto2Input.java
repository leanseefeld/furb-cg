package br.furb.bte.controle.input;

import java.awt.event.KeyEvent;
import br.furb.bte.comando.ProcessadorComando;
import br.furb.bte.objetos.Moto;

public class Moto2Input extends MotoInput {

    public Moto2Input(Moto moto2, ProcessadorComando<Moto> processador) {
	super(moto2, processador, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
    }

}
