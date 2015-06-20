package br.furb.bte.controle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import br.furb.bte.GameplayListener;
import br.furb.bte.Tela;
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

    @Override
    public void associarTela(Tela tela) {
	this.tela = tela;
	tela.addGameplayListener(this);
	tela.addKeyListener(keyListener);
    }

    @Override
    public void afterInit() {
	System.out.println("ControladorLocal.afterInit()");
    }

    @Override
    public void onReset() {
	System.out.println("ControladorLocal.onReset()");
	if (tela != null) {
	    Arena arena = tela.getArena();
	    moto1 = arena.getMoto((short) 1);
	    moto2 = arena.getMoto((short) 2);
	}
    }

    @Override
    public void onPause() {
	System.out.println("ControladorLocal.onPause()");
	jogando = false;
    }

    @Override
    public void onResume() {
	System.out.println("ControladorLocal.onResume()");
	jogando = true;
    }

    private boolean trataControleMotos(KeyEvent e) {
	if (!jogando && !e.isControlDown()) {
	    return false;
	}
	int direita = 90;
	int esquerda = -90;
	switch (e.getKeyCode()) {
	//	    case KeyEvent.VK_W:
	//		this.moto1.setAngulo(Moto.CIMA);
	//		break;
	//	    case KeyEvent.VK_UP:
	//		this.moto2.setAngulo(Moto.CIMA);
	//		break;
	//	    case KeyEvent.VK_D:
	//		this.moto1.setAngulo(Moto.DIREITA);
	//		break;
	//	    case KeyEvent.VK_RIGHT:
	//		this.moto2.setAngulo(Moto.DIREITA);
	//		break;
	//	    case KeyEvent.VK_S:
	//		this.moto1.setAngulo(Moto.BAIXO);
	//		break;
	//	    case KeyEvent.VK_DOWN:
	//		this.moto2.setAngulo(Moto.BAIXO);
	//		break;
	//	    case KeyEvent.VK_A:
	//		this.moto1.setAngulo(Moto.ESQUERDA);
	//		break;
	//	    case KeyEvent.VK_LEFT:
	//		this.moto2.setAngulo(Moto.ESQUERDA);
	//		break;
	    case KeyEvent.VK_D:
		moto1.girar(direita);
		break;
	    case KeyEvent.VK_RIGHT:
		moto2.girar(direita);
		break;
	    case KeyEvent.VK_A:
		moto1.girar(esquerda);
		break;
	    case KeyEvent.VK_LEFT:
		moto2.girar(esquerda);
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
		    tela.executarComportamentos();
		} else if (!tela.isGameOver()) {
		    tela.alterarExecucao(!jogando);
		}
		break;
	    case KeyEvent.VK_R:
		tela.reset();
		break;
	    default:
		return false;
	}
	return true;
    }

    private boolean trataControleCenario(KeyEvent e) {
	switch (e.getKeyCode()) {
	    case KeyEvent.VK_1:
		tela.alternarPerspectiva();
		break;
	    default:
		return false;
	}
	if (e.getKeyCode() == KeyEvent.VK_1) {
	}
	return true;
    }
}
