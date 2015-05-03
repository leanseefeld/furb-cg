package br.furb;

import javax.media.opengl.GL;

public class Mundo extends ObjetoGrafico {
	private ObjetoGrafico objetoSelecionado;
	public static Estado EstadoAtual;

	public Mundo(GL gl, int maxX, int minX, int maxY, int minY) {
		super(gl);
		super.bbox = new BBox(maxX, minX, maxY, minY);
	}
	
	@Override
	public void desenhar() {
		super.desenhar();
	}

	@Override
	public boolean malhaSelecionada(Ponto pontoBusca) {
		//Não possui malha, então retorna true sempre
		return true;
	}
	
	@Override
	public ObjetoGrafico selecionarObjeto(Ponto ponto) {
		if(this.objetoSelecionado != null)
			this.objetoSelecionado.setSelected(false);
		this.objetoSelecionado = super.selecionarObjeto(ponto);
		return this.objetoSelecionado;
	}
	
	public void setObjetoSelecionado(ObjetoGrafico objetoGrafico)
	{
		if(this.objetoSelecionado != null)
			this.objetoSelecionado.setSelected(false);
		this.objetoSelecionado = objetoGrafico;
		this.objetoSelecionado.setSelected(true);
	}
	
	public ObjetoGrafico getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void removeObjetoSelecionado() {
		this.objetoSelecionado.getParent().RemoveFilho(objetoSelecionado);
	}
}
