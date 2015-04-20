package br.furb;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Canvas implements GLEventListener, MouseMotionListener,
		MouseListener {
	private float ortho2D_minX = -400.0f, ortho2D_maxX = 400.0f,
			ortho2D_minY = -400.0f, ortho2D_maxY = 400.0f;
	private GL gl;
	private GLU glu;
	private GLAutoDrawable glDrawable;
	private Mundo mundo;
	private ObjetoGrafico objetoSelecionado;
	private Ponto mouseReal;

	@Override
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(ortho2D_minX, ortho2D_maxX, ortho2D_minY, ortho2D_maxY);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		SRU();

		mundo.desenhar();
		
		this.desenhaPonteiro();
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

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		System.out.println("Espaço de desenho com tamanho: "
				+ drawable.getWidth() + " x " + drawable.getHeight());
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		mundo = new Mundo(gl, (int) ortho2D_maxX, (int) ortho2D_minX,
				(int) ortho2D_maxY, (int) ortho2D_minY);
		
		mouseReal = new Ponto(0, 0);

		mundo.setSelected(true);
		objetoSelecionado = mundo;
		objetoSelecionado.setSelected(false);

		Poligono po = new Poligono(gl);
		po.cor = new Cor(0, 1, 0);
		mundo.addFilho(po);
		po.addPonto(new Ponto(-200, 200));
		po.addPonto(new Ponto(-200, -200));
		po.addPonto(new Ponto(200, -200));
		po.addPonto(new Ponto(200, 200));
		po.concluir();
		objetoSelecionado = po;
	}
	
	public void desenhaPonteiro()
	{
		gl.glPointSize(5);
		gl.glColor3f(0f, 0f, 1f);
		gl.glBegin(GL.GL_POINTS);{
			gl.glVertex2d(mouseReal.X, -mouseReal.Y);
		}
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		objetoSelecionado.setSelected(false);
		Ponto pontoSelecionado = new Ponto(arg0.getX(), arg0.getY());
		ajustaPonto(pontoSelecionado);
		objetoSelecionado = mundo.selecionarObjeto(pontoSelecionado);
		glDrawable.display();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		this.mouseReal.X = arg0.getX();
		this.mouseReal.Y = arg0.getY();
		ajustaPonto(this.mouseReal);
		glDrawable.display();
		System.out.println(mouseReal.toString());
	}

	private void ajustaPonto(Ponto ponto)
	{
		ponto.X = ponto.X * 2 - 400;
		ponto.Y = ponto.Y * 2 - 400;
	}
}
