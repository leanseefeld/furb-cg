package br.furb.bte.objetos;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import object.OBJModel;
import object.Tuple3;

public class Moto extends Poligono {

	private int angulo;
	private Rastro rastro;
	private final Cor corColisao = new Cor(0, 0, 1);
	private final Cor corNormal;
	private static final int VELOCIDADE = 10;
	public static final int CIMA = -90;
	public static final int DIREITA = 0;
	public static final int BAIXO = 90;
	public static final int ESQUERDA = 180;

	private OBJModel moto;

	public Moto(int x, int z, Cor cor, GL gl) {
		this.primitiva = GL.GL_QUADS;
		this.cor = cor;
		this.corNormal = cor;
		this.rastro = new Rastro(cor);
		moto = new OBJModel("data/porsche", 1.5f, gl, true);
		setPosicao(x, z);
		setPontos(criarPontos());
	}

	public float getAngulo() {
		return angulo;
	}

	public void setColidido(boolean isColidido) {
		this.cor = isColidido ? this.corColisao : this.corNormal;
	}

	private void setPosicao(int x, int z) {
		Transformacao transTranslacao = new Transformacao();
		transTranslacao.atribuirTranslacao(x, 0, z);
		this.addMovimentacao(transTranslacao);
	}

	public void setRastro(Rastro rastro) {
		this.rastro = rastro;
	}

	public Rastro getRastro() {
		return rastro;
	}

	@Override
	protected List<Ponto> criarPontos() {
		List<Ponto> pontos = new ArrayList<>();

		for(Tuple3 tup : moto.getVerts()){
			Ponto p = Ponto.Tuple3toPoint(tup);
			pontos.add(p);
		}
		
//		// Cima
//		pontos.add(new Ponto(+17, +10, +4));
//		pontos.add(new Ponto(+17, +10, -4));
//		pontos.add(new Ponto(-19, +10, -6));
//		pontos.add(new Ponto(-19, +10, +6));
//
//		// Baixo
//		pontos.add(new Ponto(-19, +0, +6));
//		pontos.add(new Ponto(-19, +0, -6));
//		pontos.add(new Ponto(+17, +0, -4));
//		pontos.add(new Ponto(+17, +0, +4));
//
//		// Frente
//		pontos.add(new Ponto(-19, +10, -6));
//		pontos.add(new Ponto(-19, +0, -6));
//		pontos.add(new Ponto(-19, +0, +6));
//		pontos.add(new Ponto(-19, +10, +6));
//
//		// Traz
//		pontos.add(new Ponto(+17, +10, +4));
//		pontos.add(new Ponto(+17, +0, +4));
//		pontos.add(new Ponto(+17, +0, -4));
//		pontos.add(new Ponto(+17, +10, -4));
//
//		// Lateral Esquerda
//		pontos.add(new Ponto(+17, +10, +4));
//		pontos.add(new Ponto(-19, +10, +6));
//		pontos.add(new Ponto(-19, +0, +6));
//		pontos.add(new Ponto(+17, +0, +4));
//
//		// Lateral Direita
//		pontos.add(new Ponto(+17, +0, -4));
//		pontos.add(new Ponto(-19, +0, -6));
//		pontos.add(new Ponto(-19, +10, -6));
//		pontos.add(new Ponto(+17, +10, -4));

		return pontos;
	}

	public void mover() {
		int moverX = (int) Math.cos(Math.toRadians(this.angulo)) * VELOCIDADE;
		int moverZ = (int) Math.sin(Math.toRadians(this.angulo)) * VELOCIDADE;
		Transformacao trans = new Transformacao();
		trans.atribuirTranslacao(moverX, 0, moverZ);
		this.addMovimentacao(trans);
		this.rastro.arrastar(this.transformacao.transformPoint(this.bbox
				.getCentro()));
	}

	public boolean estaColidindo(Poligono objeto) {
		BBox bbox = this.getBBoxTransformada();
		BBox bbox2 = objeto.getBBoxTransformada();
		return bbox.estaColidindo(bbox2);
	}

	public boolean estaColidindo(Rastro rastro) {
		BBox bboxTransformado = getBBoxTransformada();

		boolean existeColisao = false;
		int qtdIgnorar = rastro == this.rastro ? 3 : 0;

		List<BBox> bboxes = rastro.getBBoxes();
		for (int i = 0; i < bboxes.size() - qtdIgnorar; i++) {
			if (bboxTransformado.estaColidindo(bboxes.get(i))) {
				existeColisao = true;
				System.out.println("BBox nº " + i);
				break;
			}
		}

		if (existeColisao)
			this.cor = this.corColisao;
		else
			this.cor = this.corNormal;
		return existeColisao;
	}

	public boolean estaSobre(Poligono objeto) {
		BBox bbox = this.getBBoxTransformada();
		BBox bbox2 = objeto.getBBoxTransformada();
		return bbox.estaSob(bbox2);
	}

	public void girar(int graus) {
		this.angulo += graus;
		Transformacao trans = new Transformacao();
		trans.atribuirRotacaoY(Math.toRadians(graus));
		this.addRotacao(trans);
	}

	public boolean renderizar(GL gl) {
		float[] cor2 = { cor.r, cor.g, cor.b, 1f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor2, 0);

		// gl.glColor3f(cor.R, cor.G, cor.B);

		gl.glPushMatrix();
		{
			gl.glMultMatrixd(transformacao.getMatriz(), 0);

			gl.glBegin(primitiva);
			{
				for (Ponto ponto : getPontos()) {
					gl.glVertex3d(ponto.x, ponto.y, ponto.z);
				}
			}
			gl.glEnd();
		}
		gl.glPopMatrix();
		// return super.renderizar(gl);
		return true;
	}

	@Override
	public BBox getBBoxTransformada() {
		// TODO: Ver forma mais performática de fazer a colisão
		List<Ponto> pontosTrans = new ArrayList<Ponto>();
		for (Ponto ponto : getPontos()) {
			pontosTrans.add(this.transformacao.transformPoint(ponto));
		}
		BBox bbox = new BBox(pontosTrans);

		// return this.bbox.transformar(this.transformacao);
		return bbox;
	}

}
