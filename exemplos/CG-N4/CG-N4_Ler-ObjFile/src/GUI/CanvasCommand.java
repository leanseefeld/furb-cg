package GUI;

import model.Cell;
import com.sun.opengl.util.GLUT;
import java.awt.Component;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

public class CanvasCommand extends Canvas{    
    //determina os valores do ortho de x e y
    private int orthoX = 256;
    private int orthoY = 128;
    //informa qual célula esta sendo pressionada pelo mouse
    private int selection = 0;
    //informa o valor y do último pressionamento do botão direito do mouse
    private int old_y; 
    //utilizado apenas para sincronizar o primeiro display das telas
//    private boolean[] firstDisplay = {true, false}; //0 = world, 1 = screen
    
    
    public CanvasCommand(Frame frame, PopupMenu popupCommand) {
        this.frame = frame;
        popup = popupCommand;
    }

    public void init(GLAutoDrawable drawable) {
        glDrawable = drawable;
        gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();
        glDrawable.setGL(new DebugGL(gl));

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void display(GLAutoDrawable arg0) {
//        if(displayAll) {
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();

            try {
                glu.gluOrtho2D(orthoX, -orthoX, -orthoY, orthoY);
            } catch (Exception e) {}

            gl.glColor3ub((byte)255, (byte)255, (byte)255);


            //desenha os textos da primeira linha
            if(type == PROJECTION)
                if(mode == PERSPECTIVE) {
                    drawStr((orthoX-180), (orthoY-(perspective[0].getY()-40)), "fovy");
                    drawStr((orthoX-230), (orthoY-(perspective[0].getY()-40)), "aspect");
                    drawStr((orthoX-300), (orthoY-(perspective[0].getY()-40)), "zNear");
                    drawStr((orthoX-360), (orthoY-(perspective[0].getY()-40)), "zFar");
                } else {
                    drawStr((orthoX-120), (orthoY-(perspective[0].getY()-40)), "left");
                    drawStr((orthoX-180), (orthoY-(perspective[0].getY()-40)), "right");
                    drawStr((orthoX-230), (orthoY-(perspective[0].getY()-40)), "bottom");
                    drawStr((orthoX-310), (orthoY-(perspective[0].getY()-40)), "top");
                    drawStr((orthoX-360), (orthoY-(perspective[0].getY()-40)), "near");
                    drawStr((orthoX-420), (orthoY-(perspective[0].getY()-40)), "far");
                }
            else {
                drawStr((orthoX-30), (orthoY-rotation[0].getY()), "glRotatef(");
                drawStr((orthoX-170), (orthoY-rotation[0].getY()), ",");
                drawStr((orthoX-230), (orthoY-rotation[0].getY()), ",");
                drawStr((orthoX-290), (orthoY-rotation[0].getY()), ",");
                drawStr((orthoX-350), (orthoY-rotation[0].getY()), ");");
                
                drawStr((orthoX-10), (orthoY-translation[0].getY()), "glTranslatef(");
                drawStr((orthoX-170), (orthoY-translation[0].getY()), ",");
                drawStr((orthoX-230), (orthoY-translation[0].getY()), ",");
                drawStr((orthoX-290), (orthoY-translation[0].getY()), ");");
                
                drawStr((orthoX-38), (orthoY-120), "glScalef(");
                drawStr((orthoX-170), (orthoY-120), ",");
                drawStr((orthoX-230), (orthoY-120), ",");
                drawStr((orthoX-290), (orthoY-120), ");");
                
                drawStr((orthoX-41), (orthoY-160), "glBegin(  . . .  );");
                drawStr((orthoX-41), (orthoY-200), " . . .");
            }

            //desenha os textos da segunda linha
            if(type == PROJECTION) {
                if(mode == PERSPECTIVE) {
                    drawStr((orthoX- 40), orthoY-perspective[0].getY(), "gluPerspective(");
                    drawStr((orthoX-230), orthoY-perspective[0].getY(), ",");
                    drawStr((orthoX-290), orthoY-perspective[0].getY(), ",");
                    drawStr((orthoX-350), orthoY-perspective[0].getY(), ",");
                    drawStr((orthoX-410), orthoY-perspective[0].getY(), ");");
                } else if(mode == FRUSTUM) {
                    drawStr((orthoX- 20), orthoY-frustum[0].getY(), "glFrustum(");
                    drawStr((orthoX-170), orthoY-frustum[0].getY(), ",");
                    drawStr((orthoX-230), orthoY-frustum[0].getY(), ",");
                    drawStr((orthoX-290), orthoY-frustum[0].getY(), ",");
                    drawStr((orthoX-350), orthoY-frustum[0].getY(), ",");
                    drawStr((orthoX-410), orthoY-frustum[0].getY(), ",");
                    drawStr((orthoX-470), orthoY-frustum[0].getY(), ");");
                } else {
                    drawStr((orthoX- 35), orthoY-ortho[0].getY(), "glOrtho(");
                    drawStr((orthoX-170), orthoY-ortho[0].getY(), ",");
                    drawStr((orthoX-230), orthoY-ortho[0].getY(), ",");
                    drawStr((orthoX-290), orthoY-ortho[0].getY(), ",");
                    drawStr((orthoX-350), orthoY-ortho[0].getY(), ",");
                    drawStr((orthoX-410), orthoY-ortho[0].getY(), ",");
                    drawStr((orthoX-470), orthoY-ortho[0].getY(), ");");
                }

                //desenha os textos do lookat
                drawStr((orthoX- 78), orthoY-lookat[0].getY(), "gluLookAt(");
                drawStr((orthoX-230), orthoY-lookat[0].getY(), ",");
                drawStr((orthoX-290), orthoY-lookat[0].getY(), ",");
                drawStr((orthoX-350), orthoY-lookat[0].getY(), ",");
                drawStr((orthoX-380), orthoY-lookat[0].getY(), "<- eye");
                drawStr((orthoX-230), orthoY-lookat[3].getY(), ",");
                drawStr((orthoX-290), orthoY-lookat[3].getY(), ",");
                drawStr((orthoX-350), orthoY-lookat[3].getY(), ",");
                drawStr((orthoX-380), orthoY-lookat[3].getY(), "<- center");
                drawStr((orthoX-230), orthoY-lookat[6].getY(), ",");
                drawStr((orthoX-290), orthoY-lookat[6].getY(), ",");
                drawStr((orthoX-350), orthoY-lookat[6].getY(), ");");
                drawStr((orthoX-380), orthoY-lookat[6].getY(), "<- up");


                //desenha os parâmetros na segunda linha
                if(mode == PERSPECTIVE) {
                    cellDraw(perspective[0]);
                    cellDraw(perspective[1]);
                    cellDraw(perspective[2]);
                    cellDraw(perspective[3]);
                } else if(mode == FRUSTUM) {
                    cellDraw(frustum[0]);
                    cellDraw(frustum[1]);
                    cellDraw(frustum[2]);
                    cellDraw(frustum[3]);
                    cellDraw(frustum[4]);
                    cellDraw(frustum[5]);
                } else { //mode == ORTHO
                    cellDraw(ortho[0]);
                    cellDraw(ortho[1]);
                    cellDraw(ortho[2]);
                    cellDraw(ortho[3]);
                    cellDraw(ortho[4]);
                    cellDraw(ortho[5]);
                }


                //desenha os parâmetros do lookat
                cellDraw(lookat[0]);
                cellDraw(lookat[1]);
                cellDraw(lookat[2]);
                cellDraw(lookat[3]);
                cellDraw(lookat[4]);
                cellDraw(lookat[5]);
                cellDraw(lookat[6]);
                cellDraw(lookat[7]);
                cellDraw(lookat[8]);
            } else {
                cellDraw(translation[0]);
                cellDraw(translation[1]);
                cellDraw(translation[2]);
                
                cellDraw(rotation[0]);
                cellDraw(rotation[1]);
                cellDraw(rotation[2]);
                cellDraw(rotation[3]);
                
                cellDraw(scale[0]);
                cellDraw(scale[1]);
                cellDraw(scale[2]);
            }

            if(selection == 0) {
                gl.glColor3ub((byte)255, (byte)255, (byte)0);
                drawStr((orthoX-10), (orthoY-240), "Click on the arguments and move the mouse to modify values.");
            }

            gl.glFlush(); 
//        }

    }
    
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
//        this.width = width;
//        this.heigth = height;
//        this.x = x;
//        this.y = y;
//        
//        //verifica se precisa alterar as resoluções        
//        int subWidth = width;
//        int subHeight = height;
//        if(redim) {
//            subWidth = (frame.getWidth() - GAP * 3) / 2;
//            subHeight = (frame.getHeight() - GAP * 3) / 2;
            
//            this.width = subWidth;
//            this.heigth = subHeight;
//            frame.setProperties(3, GAP, GAP+subHeight+GAP, subWidth+GAP+subWidth, subHeight);
            
//            redim = false;
//        }
    }

    public void mousePressed(MouseEvent e) {
        if(MouseEvent.BUTTON1 == e.getButton()) {
            //inicializa a seleçao da célula
            selection = 0;

            //seleciona a célula que o mouse esta clicando
            if(type == PROJECTION) {
                if(mode == PERSPECTIVE) {
                    selection += perspective[0].hit(e.getX(), e.getY());
                    selection += perspective[1].hit(e.getX(), e.getY());
                    selection += perspective[2].hit(e.getX(), e.getY());
                    selection += perspective[3].hit(e.getX(), e.getY());
                } else if(mode == FRUSTUM) {
                    selection += frustum[0].hit(e.getX(), e.getY());
                    selection += frustum[1].hit(e.getX(), e.getY());
                    selection += frustum[2].hit(e.getX(), e.getY());
                    selection += frustum[3].hit(e.getX(), e.getY());
                    selection += frustum[4].hit(e.getX(), e.getY());
                    selection += frustum[5].hit(e.getX(), e.getY());
                } else {
                    selection += ortho[0].hit(e.getX(), e.getY());
                    selection += ortho[1].hit(e.getX(), e.getY());
                    selection += ortho[2].hit(e.getX(), e.getY());
                    selection += ortho[3].hit(e.getX(), e.getY());
                    selection += ortho[4].hit(e.getX(), e.getY());
                    selection += ortho[5].hit(e.getX(), e.getY());
                }


                selection += lookat[0].hit(e.getX(), e.getY());
                selection += lookat[1].hit(e.getX(), e.getY());
                selection += lookat[2].hit(e.getX(), e.getY());
                selection += lookat[3].hit(e.getX(), e.getY());
                selection += lookat[4].hit(e.getX(), e.getY());
                selection += lookat[5].hit(e.getX(), e.getY());
                selection += lookat[6].hit(e.getX(), e.getY());
                selection += lookat[7].hit(e.getX(), e.getY());
                selection += lookat[8].hit(e.getX(), e.getY());
            } else {
                selection += translation[0].hit(e.getX(), e.getY());
                selection += translation[1].hit(e.getX(), e.getY());
                selection += translation[2].hit(e.getX(), e.getY());
                
                selection += rotation[0].hit(e.getX(), e.getY());
                selection += rotation[1].hit(e.getX(), e.getY());
                selection += rotation[2].hit(e.getX(), e.getY());
                selection += rotation[3].hit(e.getX(), e.getY());
                
                selection += scale[0].hit(e.getX(), e.getY());
                selection += scale[1].hit(e.getX(), e.getY());
                selection += scale[2].hit(e.getX(), e.getY());
            }

            //armazena o valor y do mouse para ser usado depois no deslocamento do mouse
            old_y = e.getY();

            glDrawable.display();
        } else if(MouseEvent.BUTTON3 == e.getButton()) {
            popup.show((Component)popup.getParent(), e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        //reseta o valor da célula selecionada para deixar de pintar ela de vermelho
        selection = 0;
        
        glDrawable.display();
    }

    public void mouseDragged(MouseEvent e) {
            //altera o valor das células de acordo com o movimento do mouse
            if(type == PROJECTION) {
                if(mode == PERSPECTIVE) {
                    perspective[0].update(old_y-e.getY(), selection);
                    perspective[1].update(old_y-e.getY(), selection);
                    perspective[2].update(old_y-e.getY(), selection);
                    perspective[3].update(old_y-e.getY(), selection);
                } else if(mode == FRUSTUM) {
                    frustum[0].update(old_y-e.getY(), selection);
                    frustum[1].update(old_y-e.getY(), selection);
                    frustum[2].update(old_y-e.getY(), selection);
                    frustum[3].update(old_y-e.getY(), selection);
                    frustum[4].update(old_y-e.getY(), selection);
                    frustum[5].update(old_y-e.getY(), selection);
                } else {
                    ortho[0].update(old_y-e.getY(), selection);
                    ortho[1].update(old_y-e.getY(), selection);
                    ortho[2].update(old_y-e.getY(), selection);
                    ortho[3].update(old_y-e.getY(), selection);
                    ortho[4].update(old_y-e.getY(), selection);
                    ortho[5].update(old_y-e.getY(), selection);
                }


                //atualiza os valores da câmera caso esteja sendo alterado
                lookat[0].update(old_y-e.getY(), selection);
                lookat[1].update(old_y-e.getY(), selection);
                lookat[2].update(old_y-e.getY(), selection);
                lookat[3].update(old_y-e.getY(), selection);
                lookat[4].update(old_y-e.getY(), selection);
                lookat[5].update(old_y-e.getY(), selection);
                lookat[6].update(old_y-e.getY(), selection);
                lookat[7].update(old_y-e.getY(), selection);
                lookat[8].update(old_y-e.getY(), selection);
            } else {
                translation[0].update(old_y-e.getY(), selection);
                translation[1].update(old_y-e.getY(), selection);
                translation[2].update(old_y-e.getY(), selection);
                
                rotation[0].update(old_y-e.getY(), selection);
                rotation[1].update(old_y-e.getY(), selection);
                rotation[2].update(old_y-e.getY(), selection);
                rotation[3].update(old_y-e.getY(), selection);
                
                scale[0].update(old_y-e.getY(), selection);
                scale[1].update(old_y-e.getY(), selection);
                scale[2].update(old_y-e.getY(), selection);                
            }

            //atualiza a posição do mouse no eixo y
            old_y = e.getY();

            glDrawable.display();
            //atualiza o screen windows e world windows
            frame.getScreen().getGLDrawable().display();
//            frame.getWorld().getGLDrawable().display();
    }

    
    /**
     * Desenha o texto no canvas.
     */
    private void drawStr(int x, int y, String texto) {
        gl.glRasterPos2i(x, y);
        //desenha letra por letra na tela
        try {
            for(int i=0; i < texto.length(); i++)
                glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, texto.charAt(i));
        } catch(Exception e) {}
    }
    
    /**
     * Desenha o valor da célula passado pelo parâmetro.
     */
    private void cellDraw(Cell cell) {
        gl.glColor3ub((byte)0, (byte)255, (byte)128);
        //verifica se esta célula esta sendo pressionada pelo mouse
        if(selection == cell.getId()) {
            gl.glColor3ub((byte)255, (byte)255, (byte)0);
            drawStr((orthoX-10), (orthoY-240), cell.getInfo());
            gl.glColor3ub((byte)255, (byte)0, (byte)0);
        }
        
        
        //formata o valor da célula
        String value = Float.toString(cell.getValue());
        //valor < 10 com mais de 2 casas após o ponto
        if(value.indexOf(".") == 1 && value.length() > 4)
            value = value.substring(0, 4);
        //valor < 100 e maior que 9 com mais de 2 casas após o ponto
        else if(value.indexOf(".") == 2 && value.length() > 5)
            value = value.substring(0, 5);
        //valor < 1000 e maior que 99 com mais de 2 casas após o ponto
        else if(value.indexOf(".") == 3 && value.length() > 6)
            value = value.substring(0, 6);
        
        
        //desenha o valor da célula na tela
        drawStr((orthoX-cell.getX()), (orthoY-cell.getY()), value);
    }
    
    /**
     * Retorna o array de células.
     */
    public Cell[] getCellArray(int tipo) {
        if(tipo == PERSPECTIVE)
            return perspective;
        else if(tipo == FRUSTUM)
            return frustum;
        else if(tipo == ORTHO)
            return ortho;
        else if(tipo == LOOKAT)
            return lookat;    
        else if(tipo == TRANSLATION)
            return translation;
        else if(tipo == ROTATION)
            return rotation;
        else
            return scale;
    }
    
    /**
     * Atribui uma nova visão de camera.
     */
    public void setMode(int type) {
        if(type >= PERSPECTIVE && type <= LOOKAT ) //>=0 & <=4
            mode = type;
        
            glDrawable.display();
            frame.getScreen().getGLDrawable().display();
//            frame.getWorld().getGLDrawable().display();
    }
    
    
    /**
     * Reseta os valores de visão.
     */
    public void reset() {
        if(type == PROJECTION) {
            perspective[0].setValue(60.0f);
            perspective[1].setValue(2.0f);
            perspective[2].setValue(1.0f);
            perspective[3].setValue(10.0f);
            ortho[0].setValue(-2.0f);
            ortho[1].setValue(2.0f);
            ortho[2].setValue(-1.0f);
            ortho[3].setValue(1.0f);
            ortho[4].setValue(1.0f);
            ortho[5].setValue(3.5f);
            frustum[0].setValue(-2.0f);
            frustum[1].setValue(2.0f);
            frustum[2].setValue(-1.0f);
            frustum[3].setValue(1.0f);
            frustum[4].setValue(1.0f);
            frustum[5].setValue(3.5f);
            lookat[0].setValue(0.0f);
            lookat[1].setValue(0.0f);
            lookat[2].setValue(2.0f);
            lookat[3].setValue(0.0f);
            lookat[4].setValue(0.0f);
            lookat[5].setValue(0.0f);
            lookat[6].setValue(0.0f);
            lookat[7].setValue(1.0f);
            lookat[8].setValue(0.0f);
        } else {
            translation[0].setValue(0.0f);
            translation[1].setValue(0.0f);
            translation[2].setValue(0.0f);
            
            rotation[0].setValue(0.0f);
            rotation[1].setValue(0.0f);
            rotation[2].setValue(1.0f);
            rotation[3].setValue(0.0f);
            
            scale[0].setValue(1.0f);
            scale[1].setValue(1.0f);
            scale[2].setValue(1.0f);
        }
        
        
        glDrawable.display();
        frame.getScreen().getGLDrawable().display();
//        frame.getWorld().getGLDrawable().display();
    }
    
    /**
     * Controla a primeira exibição.
     
    public void setFirstDisplay(int pos) {
        firstDisplay[pos] = true;
        
        if(firstDisplay[0] && firstDisplay[1]) {
            displayAll = true;
//            frame.getWorld().activeDisplay();
            frame.getScreen().activeDisplay();
            
            if(glDrawable != null)
                glDrawable.display();
        }
    }*/
    
}
