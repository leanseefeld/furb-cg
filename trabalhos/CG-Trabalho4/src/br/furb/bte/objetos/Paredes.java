package br.furb.bte.objetos;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.swing.JOptionPane;
import com.sun.opengl.util.texture.TextureData;

public class Paredes extends Poligono {

    private int idTexture[];
    private int width, height;
    private BufferedImage image;
    private TextureData td;
    private ByteBuffer buffer;

    @Override
    public boolean renderizar(GL gl) {
	drawWalls(gl);
	return false;
    }

    private void drawWalls(GL gl) {

	gl.glDisable(GL.GL_CULL_FACE);
	aplicaTextura(gl);
	gl.glEnable(GL.GL_TEXTURE_2D);
	{
	    // float[] cor = { 0, 1, 0, 1f };
	    // gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor, 0);

	    // Desenha um cubo no qual a textura é aplicada
	    // gl.glEnable(GL.GL_TEXTURE_2D); // Primeiro habilita uso de
	    // textura
	    // {
	    // gl.glColor3f(0.0f, 1.0f, 0.0f);
	    gl.glPushMatrix();
	    {
		gl.glBegin(GL.GL_QUADS);
		{
		    // face da frente
		    gl.glNormal3f(0.0f, 0.0f, -1.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, +500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 50f, +500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, +500);
		    // face de traz
		    gl.glNormal3f(0.0f, 0.0f, 1.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, +550f);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, +550f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, +550f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, +550f);
		    // face de Baixo
		    gl.glNormal3f(0.0f, -1.0f, 0.0f);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, +500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, +550f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, +550f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, +500);
		    // face de cima
		    gl.glNormal3f(0.0f, 1.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, +500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-500, 50f, +550f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 50f, +550f);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, +500);
		    // face lateral esquerda
		    gl.glNormal3f(-1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, +500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-500, 50f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, +550f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, +550f);
		    // face lateral direita
		    gl.glNormal3f(1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, +500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, +550f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, +550f);
		}
		gl.glEnd();
		gl.glBegin(GL.GL_QUADS);
		{
		    /**
		     * Segundo MURO
		     */
		    // face da frente
		    gl.glNormal3f(0.0f, 0.0f, -1.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, -500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, -500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, -500);
		    // face da traz
		    gl.glNormal3f(0.0f, 0.0f, 1.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, -550f);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, -550f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, -550f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, -550f);
		    // face de baixo
		    gl.glNormal3f(0.0f, 1.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, -550f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, -550f);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 0f, -500);
		    // face de cima
		    gl.glNormal3f(0.0f, 1.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-500, 50f, -550f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 50f, -550f);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, -500);
		    // face lateral esquerda
		    gl.glNormal3f(-1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, -500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-500, 50f, -500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, -550f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, -550f);
		    // face lateral direita
		    gl.glNormal3f(1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, -500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, -500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, -550f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, -550f);
		}
		gl.glEnd();
		gl.glBegin(GL.GL_QUADS);
		{
		    /**
		     * Terceiro Muro
		     */
		    // face da frente
		    gl.glNormal3f(0.0f, 0.0f, -1.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, +500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-500, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, -500);
		    // face da traz
		    gl.glNormal3f(0.0f, 0.0f, 1.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-550f, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-550f, 50f, +500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-550f, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-550f, 0f, -500);
		    // face de baixo
		    gl.glNormal3f(0.0f, 1.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-550f, 0f, -500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-550f, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, -500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 0f, +500);
		    // face de cima
		    gl.glNormal3f(0.0f, 1.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-550, 50f, -500f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-550, 50f, +500f);
		    // face lateral esquerda
		    gl.glNormal3f(-1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, +500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-500, 50f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-550f, 50f, -500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-550f, 0f, -500);
		    // face lateral direita
		    gl.glNormal3f(1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(-500, 0f, -500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(-500, 50f, -500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(-550f, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(-550f, 0f, -500);
		}
		gl.glEnd();
		gl.glBegin(GL.GL_QUADS);
		{
		    /**
		     * Quarto Muro
		     */
		    // face da frente
		    gl.glNormal3f(0.0f, 0.0f, -1.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+500, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, +500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, -500);
		    // face da traz
		    gl.glNormal3f(0.0f, 0.0f, 1.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+550f, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+550f, 50f, +500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+550f, 50f, -500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+550f, 0f, -500);
		    // face de baixo
		    gl.glNormal3f(0.0f, 1.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+500, 0f, +500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+550f, 0f, +500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+550f, 0f, -500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 0f, -500);
		    // face de cima
		    gl.glNormal3f(0.0f, 1.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, +500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+550f, 50f, +500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+550f, 50f, -500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, -500);
		    // face lateral esquerda
		    gl.glNormal3f(-1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, -500);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 50f, -500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+550f, 50f, -500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+550f, 0f, -500);
		    // face lateral direita
		    gl.glNormal3f(1.0f, 0.0f, 0.0f);
		    gl.glTexCoord2f(1.0f, 0.0f);
		    gl.glVertex3f(+500, 0f, +500);
		    gl.glTexCoord2f(1.0f, 1.0f);
		    gl.glVertex3f(+500, 50f, +500);
		    gl.glTexCoord2f(0.0f, 1.0f);
		    gl.glVertex3f(+550f, 50f, +500);
		    gl.glTexCoord2f(0.0f, 0.0f);
		    gl.glVertex3f(+550f, 0f, +500);
		}
		gl.glEnd();
	    }
	    gl.glPopMatrix();
	    // }
	    gl.glDisable(GL.GL_TEXTURE_2D); // Desabilita uso de textura
	    // gl.glColor3f(0.0f, 1.0f, 0.0f);
	    // gl.glPushMatrix();
	    // gl.glBegin(GL.GL_QUADS );
	    // gl.glEnd();
	    // gl.glPopMatrix();
	}
	gl.glEnable(GL.GL_CULL_FACE);
    }

    private void aplicaTextura(GL gl) {
	// Habilita o modelo de colorização de Gouraud
	gl.glShadeModel(GL.GL_SMOOTH);

	// Comandos de inicialização para textura
	// loadImage("madeira.jpg");
	loadImage("data/teste.jpg");

	// Gera identificador de textura
	idTexture = new int[10];
	gl.glGenTextures(1, idTexture, 1);

	// Especifica qual é a textura corrente pelo identificador
	gl.glBindTexture(GL.GL_TEXTURE_2D, idTexture[0]);

	// Envio da textura para OpenGL
	gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, width, height, 0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, buffer);

	// Define os filtros de magnificação e minificação
	gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
	gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
    }

    public void loadImage(String fileName) {
	// Tenta carregar o arquivo
	image = null;
	try {
	    image = ImageIO.read(new File(fileName));
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Erro na leitura do arquivo " + fileName);
	}

	// Obtém largura e altura
	width = image.getWidth();
	height = image.getHeight();
	// Gera uma nova TextureData...
	td = new TextureData(0, 0, false, image);
	// ...e obtém um ByteBuffer a partir dela
	buffer = (ByteBuffer) td.getBuffer();
    }

}
