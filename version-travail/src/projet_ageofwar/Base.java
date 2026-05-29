package projet_ageofwar;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author romai
 */
public class Base extends JPanel{
    private PlateauDeJeu plateauDeJeu;
    private int camp;
    private int pointsDeVie;
    private int argent;
    private Image imageBase;
    private Donnees donnees;

    /**
     * Constructeur de base en fonction du camp
     * @param camp
     * @param plateauDeJeu
     * @param donnees
    */
    public Base(int camp, PlateauDeJeu plateauDeJeu, Donnees donnees) {
        this.plateauDeJeu = plateauDeJeu;
        this.donnees = donnees;
        this.camp = camp;
        if(camp == 1){
            this.pointsDeVie = donnees.getVieInitiale(camp, plateauDeJeu.getEpoqueJoueur());
            this.argent = donnees.getArgentInitial(camp, plateauDeJeu.getEpoqueJoueur());
            try {
                imageBase = ImageIO.read(getClass().getResource(donnees.getCheminImageBase(camp, plateauDeJeu.getEpoqueJoueur())));
            } catch (IOException ex) {
                Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.pointsDeVie = donnees.getVieInitiale(camp, plateauDeJeu.getEpoqueAdversaire());
            this.argent = donnees.getArgentInitial(camp, plateauDeJeu.getEpoqueAdversaire());
            try {
                imageBase = ImageIO.read(getClass().getResource(donnees.getCheminImageBase(camp, plateauDeJeu.getEpoqueAdversaire())));
            } catch (IOException ex) {
                Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public int getArgent() {
        return argent;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageBase != null) {
            g.drawImage(imageBase, 0, 0, this);
        }
    }
    
    public void ajouterPointsDeVie(int vie) {
        this.pointsDeVie += vie;
        if(this.camp == 1) { 
            plateauDeJeu.getAffichage().getPointsDeVieBaseJoueur().setText("Vie de la base : " + this.pointsDeVie);
        } else {
            plateauDeJeu.getAffichage().getPointsDeVieBaseAdversaire().setText("Vie de la base : " + this.pointsDeVie);
        }
    }
    
    public void diminuerPointsDeVie(int degats) {
        this.pointsDeVie -= degats;
        if(this.camp == 1) { 
            plateauDeJeu.getAffichage().getPointsDeVieBaseJoueur().setText("Vie de la base : " + this.pointsDeVie);
        } else {
            plateauDeJeu.getAffichage().getPointsDeVieBaseAdversaire().setText("Vie de la base : " + this.pointsDeVie);
        }
    }

    public void diminuerArgent(int cout) {
        this.argent -= cout;
        plateauDeJeu.getAffichage().getArgent().setText("Argent : " + this.argent);
    }

    public void ajouterArgent(int gain) {
        this.argent += gain;
        plateauDeJeu.getAffichage().getArgent().setText("Argent : " + this.argent);
    }
}