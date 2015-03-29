package br.furb.n6;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener, MouseMotionListener,
		MouseListener {
	private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f,
			ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Ponto2D pontos[];
	private Ponto2D pontoSelecionado;
	private int numeroPontos;
	private int antigoX;
	private int antigoY;
	private final static double AJUSTE_MOUSE = 1.1d;

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		pontos = new Ponto2D[] {
		/*    */new Ponto2D(-100, -100), //
				new Ponto2D(-100, +100), //
				new Ponto2D(+100, +100), //
				new Ponto2D(+100, -100)};
		pontoSelecionado = pontos[0];
		numeroPontos = 10;
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espa�o de desenho com tamanho: "
				+ drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void SRU() {
		// eixo x
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glLineWidth(1.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(-200.0f, 0.0f);
		gl.glVertex2f(200.0f, 0.0f);
		gl.glEnd();
		// eixo y
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2f(0.0f, -200.0f);
		gl.glVertex2f(0.0f, 200.0f);
		gl.glEnd();
	}

	// exibicaoPrincipal
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(ortho2D_minX, ortho2D_maxX, ortho2D_minY, ortho2D_maxY);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		SRU();

		// spline
		gl.glColor3f(1f, 1f, 0f);
		gl.glLineWidth(2f);
		gl.glBegin(GL.GL_LINE_STRIP);
		for (double i = 0; i <= 1; i += (1d / numeroPontos)) {
			
			//Dessa forma fica limitado ao 4 pontos
		  /*Ponto2D ponto1 = GetPontoIntermediario(pontos[0], pontos[1], i);
			Ponto2D ponto2 = GetPontoIntermediario(pontos[1], pontos[2], i);
			Ponto2D ponto3 = GetPontoIntermediario(pontos[2], pontos[3], i);
			Ponto2D ponto12 = GetPontoIntermediario(ponto1, ponto2, i);
			Ponto2D ponto23 = GetPontoIntermediario(ponto2, ponto3, i);
			Ponto2D ponto13 = GetPontoIntermediario(ponto12, ponto23, i);
			gl.glVertex2d(ponto13.getX(), ponto13.getY());*/
			
			//Dessa forma fica mais flexível alterar a quantidade de pontos para fazer 
			//as interpolações. Esse metodo aceita quantos pontos forem precisos
			Ponto2D pontoSpline = GetPontoIntermediario(pontos, i);
			gl.glVertex2d(pontoSpline.getX(), pontoSpline.getY());
		}

		gl.glEnd();

		gl.glColor3f(0f, 0f, 0f);
		gl.glPointSize(6f);
		gl.glBegin(GL.GL_POINTS);
		{
			gl.glVertex2d(pontoSelecionado.getX(), pontoSelecionado.getY());
		}
		gl.glEnd();

		// poliedro
		gl.glColor3f(0f, 1f, 1f);
		gl.glLineWidth(2f);
		gl.glBegin(GL.GL_LINE_STRIP);
		for (Ponto2D point : pontos) {
			gl.glVertex2d(point.getX(), point.getY());
		}
		gl.glEnd();

		gl.glFlush();
	}

	private Ponto2D GetPontoIntermediario(Ponto2D[] pontosX, double peso) {
		Ponto2D pontoFinal = new Ponto2D();

		Ponto2D[] pontosIntermediarios = new Ponto2D[pontosX.length - 1];
		for (int i = 0; i < pontosX.length - 1; i++) {
			pontosIntermediarios[i] = GetPontoIntermediario(pontosX[i],
					pontosX[i + 1], peso);
		}

		if (pontosIntermediarios.length > 1) {
			pontoFinal = GetPontoIntermediario(pontosIntermediarios, peso);
		} else {
			pontoFinal = pontosIntermediarios[0];
		}

		return pontoFinal;
	}

	private Ponto2D GetPontoIntermediario(Ponto2D pontoInicial,
			Ponto2D pontoFinal, double peso) {
		Ponto2D novoPonto = new Ponto2D();
		novoPonto.setX(pontoInicial.getX()
				+ (pontoFinal.getX() - pontoInicial.getX()) * peso);
		novoPonto.setY(pontoInicial.getY()
				+ (pontoFinal.getY() - pontoInicial.getY()) * peso);
		return novoPonto;
	}

	public double RetornaX(double angulo, double raio) {
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}

	public double RetornaY(double angulo, double raio) {
		return (raio * Math.sin(Math.PI * angulo / 180.0));
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		boolean reconheceu = true;
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_1:
			this.pontoSelecionado = pontos[0];
			break;
		case KeyEvent.VK_2:
			this.pontoSelecionado = pontos[1];
			break;
		case KeyEvent.VK_3:
			this.pontoSelecionado = pontos[2];
			break;
		case KeyEvent.VK_4:
			this.pontoSelecionado = pontos[3];
			break;
		case KeyEvent.VK_5:
			this.pontoSelecionado = pontos[4];
			break;
		case KeyEvent.VK_ADD:
			this.numeroPontos += 1;
			break;
		case KeyEvent.VK_SUBTRACT:
			this.numeroPontos -= 1;
			if (this.numeroPontos < 1)
				this.numeroPontos = 1;
			break;
		default:
			reconheceu = false;
			break;
		}
		if (reconheceu) {
			glDrawable.display();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

		double movtoX = e.getX() - antigoX;
		double movtoY = e.getY() - antigoY;

		pontoSelecionado.MoverX((movtoX + (movtoX * AJUSTE_MOUSE)));
		pontoSelecionado.MoverY(-(movtoY + (movtoY * AJUSTE_MOUSE)));

		antigoX = e.getX();
		antigoY = e.getY();

		glDrawable.display();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		antigoX = e.getX();
		antigoY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
