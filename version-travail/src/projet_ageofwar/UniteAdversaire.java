/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projet_ageofwar;

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
 *
 * @author romai
 */
public class UniteAdversaire extends Unite {
    UniteAdversaire(int epoque, int unite, Donnees donnees, Affichage affichage) {
        super(epoque, unite, donnees, affichage);
        this.width = donnees.getImageBounds(unite, epoque, 1, 0);
        this.height = donnees.getImageBounds(unite, epoque, 1, 1);
        this.positionX = affichage.getBaseAdversaireXSpawn() - width;
        this.positionY = affichage.getHauteur() - affichage.getHauteurAxeX() - height;
        try {
            this.imageUnite = ImageIO.read(getClass().getResource(donnees.getCheminImage(unite, epoque, 2)));
        } catch (IOException ex) {
            Logger.getLogger(UniteAdversaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
        setVisible(true);
        setBounds(positionX, positionY, width, height);
    }
    
    @Override
    public void avancer(){
        this.positionX -= 1;
        this.setLocation(positionX, positionY);
        repaint();
    }
    
    @Override
    public void affichageDegats(){
        JLabel labelDegats = new JLabel();
        labelDegats.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        labelDegats.setForeground(Color.RED);
        affichage.getPanelArrierePlan().add(labelDegats);
        labelDegats.setVisible(false);
        labelDegats.setText("" + this.degats);
        labelDegats.setBounds(this.positionX + 5, affichage.getHauteur() - affichage.getHauteurAxeX() - 115, 42, 30);
        labelDegats.setVisible(true);
        Timer timerAffichageDegats = new Timer(800, g -> {
                labelDegats.setVisible(false);
            });
        timerAffichageDegats.setRepeats(false);
        timerAffichageDegats.start();
    }
}