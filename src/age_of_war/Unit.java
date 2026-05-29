package age_of_war;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Represents a player unit on the game board
 * @author Romain Sebire, Joris Schwarz
 */
public class Unit extends JPanel {
    protected GameData gameData;
    protected Display display;
    protected int hitPoints;
    protected int damage;
    protected int range;
    protected int goldLoot;
    protected int positionX;
    protected int positionY;
    protected int cost;
    protected Image unitImage;
    protected int width;
    protected int height;

    /**
     * Creates a unit with stats loaded from the XML data
     * @param era current era
     * @param unitType unit type (1=melee, 2=ranged, 3=tank)
     * @param gameData game data
     * @param display display manager
     */
    public Unit(int era, int unitType, GameData gameData, Display display) {
        this.display = display;
        this.gameData = gameData;
        this.hitPoints = gameData.getHitPoints(unitType, era);
        this.damage = gameData.getDamage(unitType, era);
        this.range = gameData.getRange(unitType, era);
        this.goldLoot = gameData.getGoldLoot(unitType, era);
        this.cost = gameData.getCost(unitType, era);
        this.width = gameData.getImageBounds(unitType, era, 1, 0);
        this.height = gameData.getImageBounds(unitType, era, 1, 1);
        this.positionX = display.getPlayerBaseXSpawn();
        this.positionY = display.getWindowHeight() - display.getGroundHeight() - height;
        try {
            this.unitImage = ImageIO.read(getClass().getResource(gameData.getImagePath(unitType, era, 1)));
        } catch (IOException e) {
        }
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
        setVisible(true);
        setBounds(positionX, positionY, width, height);
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getCost() {
        return cost;
    }

    public int getGoldLoot() {
        return goldLoot;
    }

    public int getPositionX() {
        return positionX;
    }
    
    /**
     * Returns true if the unit is dead
     */
    public boolean isDead() {
        return this.hitPoints <= 0;
    }

    /**
     * Inflicts damage to this unit
     * @param damage damage amount
     */
    public void takeDamage(int damage) {
        this.hitPoints -= damage;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
    
    /**
     * Moves the unit forward by one pixel
     */
    public void advance(){
        this.positionX += 1;
        this.setLocation(positionX, positionY);
        repaint();
    }
    
    /**
     * Attacks the target unit
     * @param target the unit to attack
     */
    public void attack(Unit target){
        target.takeDamage(this.damage);
        showDamage();
    }
    
    /**
     * Attacks a base
     * @param base the base to attack
     */
    public void attackBase(Base base){
        base.takeDamage(this.damage);
        showDamage();
    }
    
    /**
     * Displays damage numbers when attacking
     */
    public void showDamage(){
        JLabel damageLabel = new JLabel();
        damageLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        damageLabel.setForeground(Color.GREEN);
        display.getBackgroundPanel().add(damageLabel);
        damageLabel.setVisible(false);
        damageLabel.setText("" + this.damage);
        damageLabel.setBounds(this.positionX + this.width -42 -5, display.getWindowHeight() - display.getGroundHeight() - 115, 42, 30);
        damageLabel.setVisible(true);
        Timer damageDisplayTimer = new Timer(800, g -> {
                damageLabel.setVisible(false);
            });
        damageDisplayTimer.setRepeats(false);
        damageDisplayTimer.start();
    }
    
    /**
     * Paints the unit image
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.unitImage != null) {
            g.drawImage(this.unitImage, 0, 0, null);
        }
    }

    /**
     * Draws the unit at its position
     */
    public void drawUnit(Graphics g) {
        if (this.unitImage != null) {
            g.drawImage(this.unitImage, this.positionX, 600, this);
        }
    }
}
