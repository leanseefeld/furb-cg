package br.furb;

import java.util.List;

public class Mundo {
	private List<ObjetoGrafico> objetosGraficos;

	public void desenhar() {
		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			objetoGrafico.desenhar();
		}
	}
}
