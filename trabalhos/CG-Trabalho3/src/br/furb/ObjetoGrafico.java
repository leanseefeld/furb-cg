package br.furb;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

public class ObjetoGrafico {
	private List<ObjetoGrafico> objetosGraficos;
	private ObjetoGrafico objetoPai;
	private Transformacao transformacao;
	private List<Ponto> pontos;
	private int cor;
	private int primitiva;
	private BBox bbox;
	private GL gl;
	private Ponto pontoSelecionado;

	public ObjetoGrafico(GL gl) {
		super();
		this.gl = gl;
		this.primitiva = GL.GL_LINE_LOOP;
		this.pontos = new ArrayList<Ponto>();
	}
	
	public void RotacionarX(int graus)
	{
		
	}
	
	public void RotacionarY(int graus)
	{
		
	}
	
	public void Mover(int x, int y)
	{
		
	}
	
	public void Escalonar(int quantidade)
	{
		
	}
	
	public ObjetoGrafico SelecionarObjeto()
	{
		return null;
	}
	
	public Ponto SelecionarPonto(int x, int y)
	{
		pontoSelecionado = null;
		return null;
	}

	public void desenhar() {
		gl.glBegin(primitiva);
		{
			for (Ponto ponto : pontos) {
				gl.glVertex2d(ponto.X, ponto.Y);
			}
		}
		gl.glEnd();

		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			objetoGrafico.desenhar();
		}
	}
}
