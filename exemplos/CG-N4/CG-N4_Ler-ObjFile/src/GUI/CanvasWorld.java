package GUI;

import com.sun.opengl.util.GLUT;
import java.awt.PopupMenu;
import java.awt.event.KeyEvent;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import model.Cell;
import object.OBJModel;


public class CanvasWorld extends Canvas {
    
    //armazena a proporção da janela
    private float aspectRatio = 1.0f;
    //informa se é para desenhar ou não o modelo
    private boolean world_draw = true;
    
    private float xe = 0.0f, ye = 0.0f, ze = 0.0f;

    public CanvasWorld(Frame frame, PopupMenu popupWorld) {
        this.frame = frame;
        popup = popupWorld;        
    }

    public void init(GLAutoDrawable drawable) {
        glDrawable = drawable;
        gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();
        glDrawable.setGL(new DebugGL(gl));
        reshape(glDrawable, 0, 0, 256, 256);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  
        
        //carrega os objetos
        loader[0] = new OBJModel("data/soccerball", 1.5f, gl, true);
        loader[1] = new OBJModel("data/al", 1.5f, gl, true);
        loader[2] = new OBJModel("data/f-16", 1.5f, gl, true);
        loader[3] = new OBJModel("data/dolphins", 1.5f, gl, true);
        loader[4] = new OBJModel("data/flowers", 1.5f, gl, true);
        loader[5] = new OBJModel("data/porsche", 1.5f, gl, true);
        loader[6] = new OBJModel("data/rose+vase", 1.5f, gl, true);
        
        //altera flag informando que jah carregou tudo que precisava
//        frame.getCommand().setFirstDisplay(0);
    }

    public void display(GLAutoDrawable arg0) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();  
        
        gl.glPushMatrix();
        
        /* draw the viewing frustum */
        Cell aux[] = frame.getCommand().getCellArray(frame.getCommand().getMode());
        
        gl.glColor3f(1.0f, 0.2f, 0.2f);
        gl.glBegin(GL.GL_QUADS);
            gl.glVertex3i(1, 1, (int)aux[aux.length-2].getValue());
            gl.glVertex3i(-1, 1, (int)aux[aux.length-2].getValue());
            gl.glVertex3i(-1, -1, (int)aux[aux.length-2].getValue());
            gl.glVertex3i(1, -1, (int)aux[aux.length-2].getValue());
        gl.glEnd();
        
        gl.glColor3ub((byte)128, (byte)196, (byte)128);
        gl.glBegin(GL.GL_LINES);
            gl.glVertex3i(1, 1, (int)aux[aux.length-2].getValue());
            gl.glVertex3i(1, 1, (int)aux[aux.length-1].getValue());
            gl.glVertex3i(-1, 1, (int)aux[aux.length-2].getValue());
            gl.glVertex3i(-1, 1, (int)aux[aux.length-1].getValue());
            gl.glVertex3i(-1, -1, (int)aux[aux.length-2].getValue());
            gl.glVertex3i(-1, -1, (int)aux[aux.length-1].getValue());
            gl.glVertex3i(1, -1, (int)aux[aux.length-2].getValue());
            gl.glVertex3i(1, -1, (int)aux[aux.length-1].getValue());
        gl.glEnd();
        
        gl.glEnable(GL.GL_BLEND);
            gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            gl.glColor4f(0.2f, 0.2f, 0.4f, 0.5f);
            gl.glBegin(GL.GL_QUADS);
                gl.glVertex3i(2, 2, (int)aux[aux.length-1].getValue());
                gl.glVertex3i(-2, 2, (int)aux[aux.length-1].getValue());
                gl.glVertex3i(-2, -2, (int)aux[aux.length-1].getValue());
                gl.glVertex3i(2, -2, (int)aux[aux.length-1].getValue());
            gl.glEnd();
        gl.glDisable(GL.GL_BLEND);


        
        gl.glPopMatrix();
        
        gl.glFlush();    
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {        
        //verifica se precisa alterar as resoluções        
        int subWidth = width;
        int subHeight = height;
        
        
            gl.glViewport(0, 0, subWidth, subHeight);
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();
            aspectRatio = (float) subWidth / subHeight;
            try{
                glu.gluPerspective(60.0f, aspectRatio, 1.0f, 256.0f);
            }catch (Exception e) {};

//            gl.glMatrixMode(GL.GL_MODELVIEW);
//            gl.glLoadIdentity();
//            glu.gluLookAt(2.0f, 0.0f, 5.0f,
//                          0.0f, 0.0f, 0.0f,
//                          0.0f, 1.0f, 0.0f);
            gl.glTranslatef(0.0f, 0.0f, -5.0f);
            gl.glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);        
            gl.glEnable(GL.GL_DEPTH_TEST);
            gl.glEnable(GL.GL_LIGHTING);
            gl.glEnable(GL.GL_LIGHT0);
    }

    private void drawAxes() {
        gl.glBegin(gl.GL_LINE_STRIP);
            gl.glVertex3f(0.0f, 0.0f, 0.0f);
            gl.glVertex3f(1.0f, 0.0f, 0.0f);
            gl.glVertex3f(0.75f, 0.25f, 0.0f);
            gl.glVertex3f(0.75f, -0.25f, 0.0f);
            gl.glVertex3f(1.0f, 0.0f, 0.0f);
            gl.glVertex3f(0.75f, 0.0f, 0.25f);
            gl.glVertex3f(0.75f, 0.0f, -0.25f);
            gl.glVertex3f(1.0f, 0.0f, 0.0f);
        gl.glEnd();


        gl.glBegin(gl.GL_LINE_STRIP);
            gl.glVertex3f(0.0f, 0.0f, 0.0f);
            gl.glVertex3f(0.0f, 1.0f, 0.0f);
            gl.glVertex3f(0.0f, 0.75f, 0.25f);
            gl.glVertex3f(0.0f, 0.75f, -0.25f);
            gl.glVertex3f(0.0f, 1.0f, 0.0f);
            gl.glVertex3f(0.25f, 0.75f, 0.0f);
            gl.glVertex3f(-0.25f, 0.75f, 0.0f);
            gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glEnd();


        gl.glBegin(gl.GL_LINE_STRIP);
            gl.glVertex3f(0.0f, 0.0f, 0.0f);
            gl.glVertex3f(0.0f, 0.0f, 1.0f);
            gl.glVertex3f(0.25f, 0.0f, 0.75f);
            gl.glVertex3f(-0.25f, 0.0f, 0.75f);
            gl.glVertex3f(0.0f, 0.0f, 1.0f);
            gl.glVertex3f(0.0f, 0.25f, 0.75f);
            gl.glVertex3f(0.0f, -0.25f, 0.75f);
            gl.glVertex3f(0.0f, 0.0f, 1.0f);
        gl.glEnd();


        gl.glColor3ub((byte) 255, (byte) 255, (byte) 0);
        gl.glRasterPos3f(1.1f, 0.0f, 0.0f);
        glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_12, 'x');
        gl.glRasterPos3f(0.0f, 1.1f, 0.0f);
        glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_12, 'y');
        gl.glRasterPos3f(0.0f, 0.0f, 1.1f);
        glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_12, 'z');
    }

    private float normalize(float[] v) {
        float length;

        length = (float) Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);

        v[0] /= length;
        v[1] /= length;
        v[2] /= length;

        return length;
    }
    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_UP == e.getKeyCode())
            ye += 0.1f;
        else if(KeyEvent.VK_DOWN == e.getKeyCode())
            ye -= 0.1f;
        else if(KeyEvent.VK_LEFT == e.getKeyCode())
            xe -= 0.1f;
        else if(KeyEvent.VK_RIGHT == e.getKeyCode())
            xe -= 0.1f;
        
        else if(KeyEvent.VK_W == e.getKeyCode())
            ze += 1.0f;
        else if(KeyEvent.VK_S == e.getKeyCode())
            ze -= 1.0f;
//        else if(KeyEvent.VK_A == e.getKeyCode())
//            zr += 1.0f;
//        else if(KeyEvent.VK_D == e.getKeyCode())
//            zr -= 1.0f;
//        else if(KeyEvent.VK_PAGE_UP == e.getKeyCode())
//            yr += 1.0f;
//        else
//            yr -= 1.0f;
        
        
//            reshape(glDrawable, 0, 0, 256, 256);
            glDrawable.display();    
//        }            
    }    

    
    /**
     * Alterar para desenhar ou nao o modelo.
     */
    public void setWorldDraw() {
        world_draw = !world_draw;
        
        glDrawable.display();
    }
}