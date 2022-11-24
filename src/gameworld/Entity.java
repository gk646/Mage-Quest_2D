package gameworld;

import gameworld.entitys.Enemy;
import main.MainGame;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Main inheritable class for all game world entity's
 */
public class Entity {
    public int worldY, worldX, entityWidth, entityHeight;
    public Enemy[] entities;
    public int movementSpeed, health;
    public MainGame mainGame;
    public BufferedImage up1;
    
    public String direction;
    public Rectangle collisionBox;
    private boolean initializeEnemies = true;
    public boolean collisionUp, collisionDown, collisionLeft, collisionRight, dead;

    public Entity(MainGame mainGame) {
        this.entities = new Enemy[100];
        this.mainGame = mainGame;

    }

    public void update() {
        if (initializeEnemies) {
            spawnEnemies();
            initializeEnemies = false;

        }
        for (Entity entity : entities) {
            if (entity != null) {
                entity.update();

            }
        }
    }

    public void draw(Graphics2D g2) {
        for (Enemy entity1 : entities) {
            if (entity1 != null) {
                entity1.draw(g2);
                g2.setColor(Color.black);
                g2.drawString(entity1.health+"",entity1.screenX+12,entity1.screenY);
            }
        }
    }

    public void spawnEnemies() {
        entities[0] = new Enemy(mainGame, 2300, 2400, 50, new Point(2400, 2400));

        entities[1] = new Enemy(mainGame, 2550, 2400, 99, new Point(2400, 2400));

        entities[2] = new Enemy(mainGame, 2650, 2400, 99, new Point(2400, 2400));
        entities[3] = new Enemy(mainGame, 2760, 2500, 99, new Point(2400, 2400));
        entities[4] = new Enemy(mainGame, 2800, 2550, 99, new Point(2400, 2400));
        entities[5] = new Enemy(mainGame, 2900, 2600, 99, new Point(2400, 2400));



    }

}
