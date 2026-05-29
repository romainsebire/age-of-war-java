/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projet_ageofwar;

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
 *
 * @author romai
 */
public class Unite extends JPanel {
    protected Donnees donnees;
    protected Affichage affichage;
    protected int pointDeVie;
    protected int degats;
    protected int portee;
    protected int orLoot;
    protected int positionX;
    protected int positionY;
    protected int cout;
    protected Image imageUnite;
    protected int width;
    protected int height;


    /**
     * Constructeur d'unite avec les statistiques du fichier XML
     * @param epoque
     * @param unite
     * @param donnees
     * @param affichage
     */
    public Unite(int epoque, int unite, Donnees donnees, Affichage affichage) {
        this.affichage = affichage;
        this.donnees = donnees;
        this.pointDeVie = donnees.getPointDeVie(unite, epoque);
        this.degats = donnees.getDegats(unite, epoque);
        this.portee = donnees.getPortee(unite, epoque);
        this.orLoot = donnees.getOrLoot(unite, epoque);
        this.cout = donnees.getCout(unite, epoque);
        this.width = donnees.getImageBounds(unite, epoque, 1, 0);
        this.height = donnees.getImageBounds(unite, epoque, 1, 1);
        this.positionX = affichage.getBaseJoueurXSpawn();
        this.positionY = affichage.getHauteur() - affichage.getHauteurAxeX() - height;
        try {
            this.imageUnite = ImageIO.read(getClass().getResource(donnees.getCheminImage(unite, epoque, 1)));
        } catch (IOException e) {
        }
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
        setVisible(true);
        setBounds(positionX, positionY, width, height);
    }

    public int getDegats() {
        return degats;
    }

    public int getPortee() {
        return portee;
    }

    public int getCout() {
        return cout;
    }

    public int getOrLoot() {
        return orLoot;
    }

    public int getPositionX() {
        return positionX;
    }
    
    public boolean estMort() {
        return this.pointDeVie <= 0;
    }

    public void retirerPointDeVie(int degats) {
        this.pointDeVie -= degats;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
    
    public void avancer(){
        this.positionX += 1;
        this.setLocation(positionX, positionY);
        repaint();
    }
    
    public void attaquer(Unite unite){
        unite.retirerPointDeVie(this.degats);
        affichageDegats();
    }
    
    public void attaquerBase(Base base){
        base.diminuerPointsDeVie(this.degats);
        affichageDegats();
    }
    
    public void affichageDegats(){
        JLabel labelDegats = new JLabel();
        labelDegats.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        labelDegats.setForeground(Color.GREEN);
        affichage.getPanelArrierePlan().add(labelDegats);
        labelDegats.setVisible(false);
        labelDegats.setText("" + this.degats);
        labelDegats.setBounds(this.positionX + this.width -42 -5, affichage.getHauteur() - affichage.getHauteurAxeX() - 115, 42, 30);
        labelDegats.setVisible(true);
        Timer timerAffichageDegats = new Timer(800, g -> {
                labelDegats.setVisible(false);
            });
        timerAffichageDegats.setRepeats(false);
        timerAffichageDegats.start();
    }
    
    //METHODES GRAPHIQUES
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.imageUnite != null) {
            g.drawImage(this.imageUnite, 0, 0, null);
        }
    }

    public void dessinerGuerrier(Graphics g) {
        if (this.imageUnite != null) {
            g.drawImage(this.imageUnite, this.positionX, 600, this);
        }
    }
}