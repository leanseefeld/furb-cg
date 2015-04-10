package br.furb;

import java.util.List;

public class ObjetoGrafico {
	private List<ObjetoGrafico> objetosGraficos;
	private List<Transformação> transformacao;
	private List<Ponto> pontos;
	private int cor;
	
	public void desenhar()
	{
		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			objetoGrafico.desenhar();
		}
	}
}
