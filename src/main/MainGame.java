package main;

import gameworld.Entity;
import gameworld.Projectile;
import gameworld.entitys.Player;
import gameworld.entitys.Player2;
import input.KeyHandler;
import input.MotionHandler;
import input.MouseHandler;

import javax.swing.*;
import java.awt.*;


public class MainGame extends JPanel implements Runnable {

    //Screen setting
    public static final double FRAMES_PER_SECOND = 120;
    public static final int SCREEN_WIDTH = 1400;
    public static final int SCREEN_HEIGHT = 900;


    //Variables
    public int globalLogicTicks;
    public String player2Information;

    //Game thread
    Thread gameThread;

    //Instances of important classes
    public MotionHandler motionHandler = new MotionHandler();
    public MouseHandler mouseHandler = new MouseHandler(motionHandler);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    KeyHandler keyHandler = new KeyHandler();
    WorldRender wRender = new WorldRender(this);
    Entity entity = new Entity(this);
    public Player player = new Player(this, keyHandler);
    public Player2 player2 = new Player2(this);
    Projectile projectile = new Projectile(this, motionHandler, mouseHandler, entity,keyHandler);
    Multiplayer multiplayer = new Multiplayer(this, player2, entity);

    /**
     * Main game loop class
     */
    public MainGame() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);
        this.addMouseMotionListener(motionHandler);
        this.setOpaque(true);


    }


    /**
     * Main game loop
     */
    @Override
    public void run() {

        double delta = 0;
        long firstTimeGate;
        double timer = 0;
        int fps = 0;
        int logic_ticks = 0;
        double fpsCounter = 0;
        long lastTime = System.nanoTime();
        double interval = 1000000000 / FRAMES_PER_SECOND;
        while (gameThread != null) {
            firstTimeGate = System.nanoTime();
            delta += (firstTimeGate - lastTime) / interval;
            fpsCounter += (firstTimeGate - lastTime);
            timer += (firstTimeGate - lastTime) / interval;
            lastTime = firstTimeGate;
            //12677853 fps with optimized render
            //18491828 fps with "old" render
            //
            if (timer >= 2) {
                update();
                timer = 0;
                logic_ticks++;

                globalLogicTicks = logic_ticks;
            }
            if (delta >= 1) {
                repaint();
                fps++;
                delta--;
            }

            if (fpsCounter >= 1000000000) {
                //System.out.println(fps + " " + logic_ticks + " ");
                fpsCounter = 0;
                fps = 0;
                logic_ticks = 0;
            }
        }
    }


    /**
     * Main updateMultiInput method
     */

    public void update() {
        if (keyHandler.debugFps && keyHandler.multiplayer) {
            multiplayer.startMultiplayer();
        }
        if (keyHandler.multiplayer) {

            multiplayer.updateMultiInput();
        }
        projectile.update();
        player.update();
        entity.update();


        if (keyHandler.multiplayer) {

            multiplayer.updateOutput();
        }
    }

    /**
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Debug
        long drawStart = System.nanoTime();

        //RENDER START

        wRender.draw(g2);
        projectile.draw(g2);
        entity.draw(g2);
        player2.draw(g2);
        player.draw(g2);

        //RENDER END

        long drawEnd = System.nanoTime();
        long difference = drawEnd - drawStart;
        if (keyHandler.debugFps) {
            g2.setColor(Color.white);
            g2.drawString(("Draw Time" + difference), 500, 600);
        }
        g2.dispose();

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }


}
