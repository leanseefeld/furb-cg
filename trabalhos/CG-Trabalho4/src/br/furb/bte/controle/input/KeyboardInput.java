package br.furb.bte.controle.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;
import br.furb.bte.Tela;
import br.furb.bte.comando.ComandoAbstrato;
import br.furb.bte.comando.ProcessadorComando;

public abstract class KeyboardInput<T> extends KeyAdapter {

    protected Tela tela;
    private ProcessadorComando<T> processador;

    public KeyboardInput(ProcessadorComando<T> processador) {
	Objects.requireNonNull(processador, "Alguém tem que processar os comandos");
	this.processador = processador;
    }

    public void associarTela(Tela tela) {
	this.tela = tela;
	tela.addKeyListener(this);
    }

    public void desassociarTela() {
	tela.removeKeyListener(this);
	tela = null;
    }

    @Override
    public final void keyPressed(KeyEvent e) {
	ComandoAbstrato<T> comando = tratarTecla(e);
	if (comando != null) {
	    processador.processaComando(comando);
	}
    }

    public abstract ComandoAbstrato<T> tratarTecla(KeyEvent event);

}
