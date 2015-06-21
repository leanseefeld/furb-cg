package br.furb.bte.controle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import br.furb.bte.GameplayListener;
import br.furb.bte.Tela;
import br.furb.bte.comando.gameplay.Pause;
import br.furb.bte.comando.gameplay.Reset;
import br.furb.bte.comando.gameplay.Resume;
import br.furb.bte.comando.gameplay.Step;
import br.furb.bte.comando.gameplay.TogglePerspective;
import br.furb.bte.comando.moto.VirarDireita;
import br.furb.bte.comando.moto.VirarEsquerda;
import br.furb.bte.objetos.Arena;
import br.furb.bte.objetos.Moto;

public class ControladorLocal extends Controlador implements GameplayListener {

    private KeyListener keyListener = new KeyAdapter() {

	@Override
	public void keyPressed(KeyEvent e) {
	    if (tela == null) {
		return;
	    }
	    if (trataControleMotos(e) || trataControleJogo(e) || trataControleCenario(e)) {
		tela.render();
	    }
	}
    };
    private Tela tela;
    private Moto moto1;
    private boolean jogando;
    private Moto moto2;

    private final VirarDireita virarDireita;
    private final VirarEsquerda virarEsquerda;
    private final Pause pause;
    private final Resume resume;
    private final Step step;
    private final Reset reset;
    private final TogglePerspective togglePerspective;

    public ControladorLocal() {
	virarDireita = new VirarDireita();
	virarEsquerda = new VirarEsquerda();
	pause = new Pause();
	resume = new Resume();
	step = new Step();
	togglePerspective = new TogglePerspective();
	reset = new Reset();
    }

    @Override
    public void associarTela(Tela tela) {
	this.tela = tela;
	tela.addGameplayListener(this);
	tela.addKeyListener(keyListener);
    }

    @Override
    public void afterInit() {
    }

    @Override
    public void onReset() {
	if (tela != null) {
	    Arena arena = tela.getArena();
	    moto1 = arena.getMoto((short) 1);
	    moto2 = arena.getMoto((short) 2);
	}
    }

    @Override
    public void onPause() {
	jogando = false;
    }

    @Override
    public void onResume() {
	jogando = true;
    }

    private boolean trataControleMotos(KeyEvent e) {
	if (!jogando && !e.isControlDown()) {
	    return false;
	}
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_D:
		virarDireita.executar(moto1);
		break;
	    case KeyEvent.VK_RIGHT:
		virarDireita.executar(moto2);
		break;
	    case KeyEvent.VK_A:
		virarEsquerda.executar(moto1);
		break;
	    case KeyEvent.VK_LEFT:
		virarEsquerda.executar(moto2);
		break;
	    default:
		return false;
	}
	return true;
    }

    private boolean trataControleJogo(KeyEvent e) {
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_SPACE:
		if (!jogando && e.isControlDown()) {
		    step.executar(tela);
		} else if (!tela.isGameOver()) {
		    if (jogando) {
			pause.executar(tela);
		    } else {
			resume.executar(tela);
		    }
		}
		break;
	    case KeyEvent.VK_R:
		reset.executar(tela);
		break;
	    default:
		return false;
	}
	return true;
    }

    private boolean trataControleCenario(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_1) {
	    togglePerspective.executar(tela);
	    return true;
	}
	return false;
    }
}
