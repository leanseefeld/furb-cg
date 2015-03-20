package GUI;

import java.awt.MenuItem;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.PopupMenu;
import java.awt.event.ActionListener;
import javax.swing.WindowConstants;

public class Frame extends JFrame{

	private static final long serialVersionUID = 1L;
        private PopupMenu popupCommand  = new PopupMenu();
        private PopupMenu popupScreen  = new PopupMenu();
        //private PopupMenu popupWorld  = new PopupMenu();
	//private CanvasWorld world = new CanvasWorld(this, popupWorld);
        private CanvasScreen screen= new CanvasScreen("soccerball", this, popupScreen);
        private CanvasCommand command = new CanvasCommand(this, popupCommand);
        private JLabel jlcommand = new JLabel();
        //private JLabel jlworld = new JLabel();
        private JLabel jlscreen = new JLabel();
        //informa se esta carregando o form agora
//        private boolean first = true;
        
        
        //cria os canvas
        private GLCanvas canvasScreen;
        //private GLCanvas canvasWorld;
        private GLCanvas canvasCommand;
        
	
	public Frame() {		
		// Cria o frame.
		super("Projection");   
		setBounds(50,50,586,612); 
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
                setResizable(false);                
                

		/* Cria um objeto GLCapabilities para especificar 
		 * o número de bits por pixel para RGBA
		 */
		GLCapabilities c = new GLCapabilities();
		c.setRedBits(8);
		c.setBlueBits(8);
		c.setGreenBits(8);
		c.setAlphaBits(8);

		/* Cria 2 canvas, adiciona ao frame e objeto "ouvinte" 
		 * para os eventos Gl, de mouse e teclado
		 */                
                
                //GLCanvas canvasScreen = new GLCanvas(c);                
                canvasScreen = new GLCanvas(c);
		add(canvasScreen);     
                canvasScreen.addGLEventListener(screen);
                canvasScreen.addMouseListener(screen);
                canvasScreen.addMouseMotionListener(screen);
		//canvasScreen.requestFocus();
                canvasScreen.setBounds(22, 22, 534, 256);
                
//                canvasWorld = new GLCanvas(c);                
//		add(canvasWorld);
//		canvasWorld.addGLEventListener(world);        
//                canvasWorld.addMouseListener(world);
//                canvasWorld.addMouseMotionListener(world);
//                canvasWorld.addKeyListener(world);
//		canvasWorld.requestFocus();
//                canvasWorld.setBounds(22, 22, 256, 256);
                        
                /*
                 *Cria o painél de commando. 
                 */
                canvasCommand = new GLCanvas(c);
                add(canvasCommand);
                canvasCommand.addGLEventListener(command);
                canvasCommand.addMouseListener(command);
                canvasCommand.addMouseMotionListener(command);
                canvasCommand.setBounds(22, 300, 534, 256);
                
                
                /*
                 *Cria as mensagens fixas da tela
                 */
                jlcommand.setFont(new java.awt.Font("Times New Roman", 0, 12));
                jlcommand.setText("Command manipulation window");
                add(jlcommand);
                jlcommand.setBounds(22, 281, 170, 20);

//                jlworld.setFont(new java.awt.Font("Times New Roman", 0, 12));
//                jlworld.setText("World-Space View");
//                add(jlworld);
//                jlworld.setBounds(22, 3, 100, 20);

                jlscreen.setFont(new java.awt.Font("Times New Roman", 0, 12));
                jlscreen.setText("Screen-Space View");
                add(jlscreen);
                jlscreen.setBounds(22, 3, 100, 20);
                
                
                /*
                 *Cria os popups nos paineis
                 */
                 //popup do canvas command
                final MenuItem transformation = new MenuItem("Transformation");                
                final MenuItem projection = new MenuItem("Projection");
                final MenuItem space1 = new MenuItem("");
                final MenuItem ortho = new MenuItem("[o] glOrtho");
                final MenuItem frustum = new MenuItem("[f] glFrustum");
                final MenuItem perspective = new MenuItem("[p] gluPerspective");
                final MenuItem space2 = new MenuItem("");
                MenuItem reset = new MenuItem("[r] Reset parameters");
                final MenuItem space3 = new MenuItem("");
                MenuItem quit = new MenuItem("Quit");
                
                projection.setEnabled(false);
                
                popupCommand.add(transformation);
                popupCommand.addSeparator();
                popupCommand.add(projection);
                popupCommand.add(space1);
                popupCommand.add(ortho);
                popupCommand.add(frustum);
                popupCommand.add(perspective);
                popupCommand.add(space2);
                popupCommand.add(reset);
                popupCommand.add(space3);
                popupCommand.add(quit);
                canvasCommand.add(popupCommand);
                
                
                //popup do canvas screen
                MenuItem models = new MenuItem("Models");
                MenuItem space4 = new MenuItem("");
                MenuItem soccer = new MenuItem("Soccerball");
                MenuItem al = new MenuItem("Al Capone");
                MenuItem jet = new MenuItem("F-16 Jet");
                MenuItem dolphis = new MenuItem("Dolphins");
                MenuItem flower = new MenuItem("Flowers");
                MenuItem porsche = new MenuItem("Porsche");
                MenuItem rose = new MenuItem("Rose");
                popupScreen.add(models);
                popupScreen.add(space4);
                popupScreen.add(soccer);
                popupScreen.add(al);
                popupScreen.add(jet);
                popupScreen.add(dolphis);
                popupScreen.add(flower);
                popupScreen.add(porsche);
                popupScreen.add(rose);
                canvasScreen.add(popupScreen);
                
                
                //popup do canvas world
//                MenuItem toggle = new MenuItem("Toggle model");
//                popupWorld.add(toggle);
//                canvasWorld.add(popupWorld);
                
                
                
                /*
                 *Cria os eventos para os menus dos popus
                 */
                
                //cria os eventos para o popup do canvas de comando
                transformation.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                         transformation.setEnabled(false);
                         projection.setEnabled(true);
                         space1.setEnabled(false);
                         ortho.setEnabled(false);
                         frustum.setEnabled(false);
                         perspective.setEnabled(false);
                         space2.setEnabled(false);
                         space3.setEnabled(false);
                         
                         command.changeType();
                    }            
                });
                
                projection.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                         transformation.setEnabled(true);
                         projection.setEnabled(false);
                         space1.setEnabled(true);
                         ortho.setEnabled(true);
                         frustum.setEnabled(true);
                         perspective.setEnabled(true);
                         space2.setEnabled(true);
                         space3.setEnabled(true);
                         
                         command.changeType();                         
                    }            
                });
                
                ortho.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        command.setMode(command.ORTHO);
                    }            
                });
                
                frustum.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        command.setMode(command.FRUSTUM);
                    }            
                });
                
                perspective.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        command.setMode(command.PERSPECTIVE);
                    }            
                });
                
                 reset.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        command.reset();
                    }            
                });
                
                
                quit.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        System.exit(0);
                    }            
                });
                
                
                
                
                
                //cria os eventos para o popup do canvas de comando
                soccer.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        screen.loadModel(0);//soccerball
                    }            
                });
                
                al.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        screen.loadModel(1);//al capone
                    }            
                });
                
                jet.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        screen.loadModel(2);//f-16
                    }            
                });
                
                dolphis.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        screen.loadModel(3);//dolphins
                    }            
                });
                
                flower.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        screen.loadModel(4);//flowers
                    }            
                });
                
                porsche.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        screen.loadModel(5);//porsche
                    }            
                });
                
                rose.addActionListener(new ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        screen.loadModel(6);//rose
                    }            
                });
                
                
                
                //cria os eventos para o popup do canvas de mundo
//                toggle.addActionListener(new ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        world.setWorldDraw();
//                    }            
//                });              
                
                
//                first = false;
	}
        
        /**
         * Retorna o screenCanvas
         */
        public CanvasScreen getScreen() {
            return screen;
        }
        
        /**
         * Retorna o worldCanvas
         
        public CanvasWorld getWorld() {
            return world;
        }*/
        
        /**
         * Retorna o commandCanvas.
         */
        public CanvasCommand getCommand() {
            return command;
        }
        
        /**
         * Altera as propriedas das janelas.
         * @param window - a janela que deseja atualizar. 1 = world, 2 = screen e 3 = command
         * @param x - posiçao x da janela
         * @param y - posição y da janela
         * @param width - comprimento da janela
         * @param height - altura da janela
         */
        public void setProperties(int window, int x, int y, int width, int height) {
            GLCanvas aux = null;
            
            switch(window) {
//                case 1: {aux = canvasWorld; break;}
                case 2: {aux = canvasScreen; break;}
                case 3: {aux = canvasCommand; break;}
            }
            
            aux.setBounds(x, y, width, height);
        }
	
	public static void main(String[] args) {
		new Frame().setVisible(true);
	}

	
}
