package GUI;


import com.sun.opengl.util.GLUT;
import java.awt.PopupMenu;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import model.Cell;
import object.OBJModel;

public class CanvasScreen extends Canvas{   
    
    //armazena o caminho do arquivo a ser carregado
    private String path;
    private float rotY = 0.0f;
    
    
    public CanvasScreen(String file, Frame frame, PopupMenu popupScreen) {       
        path = file;    
        this.frame = frame;
        popup = popupScreen;       
    }
    
    public void init(GLAutoDrawable drawable) {
        glDrawable = drawable;
        gl = drawable.getGL();
        glu = new GLU();
        glut = new GLUT();
        glDrawable.setGL(new DebugGL(gl));
        
        reshape(glDrawable, 0, 0, 534, 256 );

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable( GL.GL_DEPTH_TEST );
	gl.glShadeModel( GL.GL_SMOOTH );
        
        //carrega os objetos
        loader[0] = new OBJModel("data/soccerball", 1.5f, gl, true);
        loader[1] = new OBJModel("data/al", 1.5f, gl, true);
        loader[2] = new OBJModel("data/f-16", 1.5f, gl, true);
        loader[3] = new OBJModel("data/dolphins", 1.5f, gl, true);
        loader[4] = new OBJModel("data/flowers", 1.5f, gl, true);
        loader[5] = new OBJModel("data/porsche", 1.5f, gl, true);
        loader[6] = new OBJModel("data/rose+vase", 1.5f, gl, true);
        loader[7] = new OBJModel("data/moto2", 1.5f, gl, true);
        
        //altera flag informando que jah carregou tudo que precisava
//        frame.getCommand().setFirstDisplay(1);
    }
    
    
    public void display(GLAutoDrawable arg0) { 
        Cell aux[] = perspective, look[] = lookat,
             trans[] = translation, rot[] = rotation, sca[] = scale;
//        if(displayAll) {     
            
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();        


                //define visão da camera
                try {
                    if(frame.getCommand().getMode() == PERSPECTIVE) {
                        
//                        if(frame.getCommand().getType() == PROJECTION) {
                            aux = frame.getCommand().getCellArray(PERSPECTIVE);
                            look = frame.getCommand().getCellArray(LOOKAT);
//                        }
                        
                        glu.gluPerspective(aux[0].getValue(), aux[1].getValue(), 
                                           aux[2].getValue(), aux[3].getValue());
                    }


                     else if(frame.getCommand().getMode() == ORTHO) {
                         ortho = frame.getCommand().getCellArray(ORTHO);
                         gl.glOrtho(ortho[0].getValue(), ortho[1].getValue(), ortho[2].getValue(),
                                    ortho[3].getValue(), ortho[4].getValue(), ortho[5].getValue());
                     }


                     else {
                         frustum = frame.getCommand().getCellArray(FRUSTUM);
                         gl.glFrustum(frustum[0].getValue(), frustum[1].getValue(), frustum[2].getValue(),
                                      frustum[3].getValue(), frustum[4].getValue(), frustum[5].getValue());
                    }
            } catch (Exception e) {e.printStackTrace();};


            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();
            
            lookat = frame.getCommand().getCellArray(LOOKAT);

            //atualiza visão da camera
            try {
                glu.gluLookAt(lookat[0].getValue(), lookat[1].getValue(), lookat[2].getValue(),
                              lookat[3].getValue(), lookat[4].getValue(), lookat[5].getValue(),
                              lookat[6].getValue(), lookat[7].getValue(), lookat[8].getValue());
            } catch (Exception e) {};

            //desenha o modelo       
//            if(frame.getCommand().getType() == TRANSFORMATION) {
                trans = frame.getCommand().getCellArray(TRANSLATION);
                rot = frame.getCommand().getCellArray(ROTATION);
                sca = frame.getCommand().getCellArray(SCALE);
//            }
                
            gl.glTranslatef(trans[0].getValue(), trans[1].getValue(), trans[2].getValue());                
            gl.glRotatef(rot[0].getValue(), rot[1].getValue(), rot[2].getValue(), rot[3].getValue());
            gl.glScalef(sca[0].getValue(), sca[1].getValue(), sca[2].getValue());
            
            loader[indexOBJ].draw(gl);


            gl.glFlush();
//        }
    }
    
    
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
//        this.width = width;
//        this.heigth = height;
//        this.x = x;
//        this.y = y;
//        
        int subWidth = width;
        int subHeight = height;
//        if(redim) {
//            subWidth = (frame.getWidth() - GAP * 3) / 2;
//            subHeight = (frame.getHeight() - GAP * 3) / 2;
//            
//            this.width = subWidth;
//            this.heigth = subHeight;
//            frame.setProperties(2, GAP+subWidth+GAP , GAP, subWidth, subHeight);
//            
//            redim = false;
//        }
        
        
        if(gl != null) {
            gl.glViewport(0, 0, subWidth, subHeight);
            gl.glMatrixMode(GL.GL_PROJECTION);
            gl.glLoadIdentity();

            //define visão da camera
            if(frame.getCommand().getMode() == PERSPECTIVE)
                glu.gluPerspective(perspective[0].getValue(), perspective[1].getValue(), 
                                   perspective[2].getValue(), perspective[3].getValue());


             else if(frame.getCommand().getMode() == ORTHO)
                 gl.glOrtho(ortho[0].getValue(), ortho[1].getValue(), ortho[2].getValue(),
                            ortho[3].getValue(), ortho[4].getValue(), ortho[5].getValue());


             else
                 gl.glFrustum(frustum[0].getValue(), frustum[1].getValue(), frustum[2].getValue(),
                              frustum[3].getValue(), frustum[4].getValue(), frustum[5].getValue());


            gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, new double[16], 1);
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glLoadIdentity();

            //atualiza visão da camera
            glu.gluLookAt(lookat[0].getValue(), lookat[1].getValue(), lookat[2].getValue(),
                          lookat[3].getValue(), lookat[4].getValue(), lookat[5].getValue(),
                          lookat[6].getValue(), lookat[7].getValue(), lookat[8].getValue());


            gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, new double[16], 1);
            gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
            gl.glEnable(GL.GL_DEPTH_TEST);
            gl.glEnable(GL.GL_LIGHTING);
            gl.glEnable(GL.GL_LIGHT0);    
        }
    }
    
    /**
     * Retorna o modelo.
     */
    public OBJModel getModel() {        
        return loader[indexOBJ];
    }
    
    /**
     * Carrega os modelos na tela.
     */
    public void loadModel(int pos) {
        //indica qual o modelo que deve ser desenhado na tela
        indexOBJ = pos;        
//        frame.getWorld().setIndex(pos);
        
        glDrawable.display();
    }
    
    public GL getGL() {
        return gl;
    }
    
    
    public void mouseReleased(MouseEvent e) {
        if(MouseEvent.BUTTON1 == e.getButton())
            rotY+= 10.0f;
        else if(MouseEvent.BUTTON2 == e.getButton())
            rotY-= 10.0f;
        
        glDrawable.display();
        
    }

}
