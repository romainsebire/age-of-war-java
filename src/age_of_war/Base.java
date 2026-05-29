package age_of_war;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Represents a base (player or enemy) on the game board
 * @author Romain Sebire, Joris Schwarz
 */
public class Base extends JPanel{
    private GameBoard gameBoard;
    private int side;
    private int health;
    private int gold;
    private Image baseImage;

    /**
     * Creates a base for the given side
     * @param side 1 for player, 2 for enemy
     * @param gameBoard the game board
     * @param gameData the game data
     */
    public Base(int side, GameBoard gameBoard, GameData gameData) {
        this.gameBoard = gameBoard;
        this.side = side;
        if(side == 1){
            this.health = gameData.getInitialHealth(side, gameBoard.getPlayerEra());
            this.gold = gameData.getInitialGold(side, gameBoard.getPlayerEra());
            try {
                baseImage = ImageIO.read(getClass().getResource(gameData.getBaseImagePath(side, gameBoard.getPlayerEra())));
            } catch (IOException ex) {
                Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.health = gameData.getInitialHealth(side, gameBoard.getEnemyEra());
            this.gold = gameData.getInitialGold(side, gameBoard.getEnemyEra());
            try {
                baseImage = ImageIO.read(getClass().getResource(gameData.getBaseImagePath(side, gameBoard.getEnemyEra())));
            } catch (IOException ex) {
                Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public int getGold() {
        return gold;
    }
    
    /**
     * Adds health points to this base
     * @param hp health to add
     */
    public void addHealth(int hp) {
        this.health += hp;
        if(this.side == 1) { 
            gameBoard.getDisplay().getPlayerBaseHealthLabel().setText("Base HP: " + this.health);
        } else {
            gameBoard.getDisplay().getEnemyBaseHealthLabel().setText("Base HP: " + this.health);
        }
    }
    
    /**
     * Inflicts damage to this base
     * @param damage damage to deal
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if(this.side == 1) { 
            gameBoard.getDisplay().getPlayerBaseHealthLabel().setText("Base HP: " + this.health);
        } else {
            gameBoard.getDisplay().getEnemyBaseHealthLabel().setText("Base HP: " + this.health);
        }
    }

    /**
     * Spends gold from this base
     * @param amount amount to spend
     */
    public void spendGold(int amount) {
        this.gold -= amount;
        gameBoard.getDisplay().getGoldLabel().setText("Gold: " + this.gold);
    }

    /**
     * Adds gold to this base
     * @param amount amount to add
     */
    public void addGold(int amount) {
        this.gold += amount;
        gameBoard.getDisplay().getGoldLabel().setText("Gold: " + this.gold);
    }
    
    /**
     * Paints the base image
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (baseImage != null) {
            g.drawImage(baseImage, 0, 0, this);
        }
    }
}
