package age_of_war;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Represents an enemy unit (moves in opposite direction)
 * @author Romain Sebire, Joris Schwarz
 */
public class EnemyUnit extends Unit {
    /**
     * Creates an enemy unit
     * @param era current era
     * @param unitType unit type (1=melee, 2=ranged, 3=tank)
     * @param gameData game data
     * @param display display manager
     */
    EnemyUnit(int era, int unitType, GameData gameData, Display display) {
        super(era, unitType, gameData, display);
        this.width = gameData.getImageBounds(unitType, era, 1, 0);
        this.height = gameData.getImageBounds(unitType, era, 1, 1);
        this.positionX = display.getEnemyBaseXSpawn() - width;
        this.positionY = display.getWindowHeight() - display.getGroundHeight() - height;
        try {
            this.unitImage = ImageIO.read(getClass().getResource(gameData.getImagePath(unitType, era, 2)));
        } catch (IOException ex) {
            Logger.getLogger(EnemyUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
        setVisible(true);
        setBounds(positionX, positionY, width, height);
    }
    
    /**
     * Moves the enemy unit forward (towards the player, i.e. left)
     */
    @Override
    public void advance(){
        this.positionX -= 1;
        this.setLocation(positionX, positionY);
        repaint();
    }
    
    /**
     * Displays damage numbers in red when the enemy attacks
     */
    @Override
    public void showDamage(){
        JLabel damageLabel = new JLabel();
        damageLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        damageLabel.setForeground(Color.RED);
        display.getBackgroundPanel().add(damageLabel);
        damageLabel.setVisible(false);
        damageLabel.setText("" + this.damage);
        damageLabel.setBounds(this.positionX + 5, display.getWindowHeight() - display.getGroundHeight() - 115, 42, 30);
        damageLabel.setVisible(true);
        Timer damageDisplayTimer = new Timer(800, g -> {
                damageLabel.setVisible(false);
            });
        damageDisplayTimer.setRepeats(false);
        damageDisplayTimer.start();
    }
}
