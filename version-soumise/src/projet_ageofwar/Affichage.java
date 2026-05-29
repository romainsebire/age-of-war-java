/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projet_ageofwar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author romai
 */
public class Affichage {
    private Donnees donnees;
    private PlateauDeJeu plateauDeJeu;
    private JFrame fenetre = new JFrame();;
    private Image imageArrierePlan;
    private JButton demarrer;
    private JButton terminer;
    private JButton rejouer;
    private JButton boutonMelee;
    private JButton boutonDistance;
    private JButton boutonTank;
    private JButton boutonEvolution;
    private Clip bandeSon;
    private JLabel argent = new JLabel();
    private JLabel pointsDeVieBaseJoueur = new JLabel();
    private JLabel pointsDeVieBaseAdversaire = new JLabel();
    private JLabel coutMelee;
    private JLabel coutDistance;
    private JLabel coutTank;
    private JLabel coutEvolution;
    private JPanel panelArrierePlan;
    private JLabel finDuJeu = new JLabel();
    private JLabel titre = new JLabel();
    private JLabel signature = new JLabel();
    private JLabel pause = new JLabel();
    private int baseJoueurXSpawn;
    private int baseAdversaireXSpawn;
    private int largeur = 1540;
    private int hauteur = 820;
    private int hauteurAxeX = 220;
    private int ecart = (largeur-4*largeur/6)/7;
    private int variablePause = 0;

    /**
     * Constructeur de l'interface graphique avec initialisation des boutons
     * @param donnees
     * @param plateaudeJeu 
     */
    public Affichage(Donnees donnees, PlateauDeJeu plateaudeJeu) {
        this.plateauDeJeu = plateaudeJeu;
        this.donnees = donnees;
        
        fenetre.setTitle("Age of War");
        fenetre.setSize(largeur,hauteur);
        fenetre.setResizable(false);
        fenetre.setLocationRelativeTo(null);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                
        //Création Boutons
        this.demarrer = new JButton("Démarrer le jeu");
        this.demarrer.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 22));
        this.rejouer = new JButton("Rejouer");
        this.rejouer.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 22));
        this.terminer = new JButton("Terminer le jeu");
        this.terminer.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 22));
        this.boutonMelee = new JButton("Guerrier melee");
        this.boutonMelee.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        this.boutonDistance = new JButton("Guerrier distance");
        this.boutonDistance.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        this.boutonTank = new JButton("Tank");
        this.boutonTank.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        this.boutonEvolution = new JButton("Evoluer");
        this.boutonEvolution.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        
        //Dimensionnement des boutons
        this.demarrer.setBounds(largeur/2 - largeur/12, hauteur/2 - hauteur/8 - hauteur/50, largeur/6, hauteur/8);
        this.rejouer.setBounds(largeur/2 - largeur/12, hauteur/2 - hauteur/8 - hauteur/50, largeur/6, hauteur/8);
        this.terminer.setBounds(largeur/2 - largeur/12, hauteur/2 + hauteur/50, largeur/6, hauteur/8);
        this.boutonMelee.setBounds(2*ecart, hauteur/16, largeur/6, hauteur/8);
        this.boutonDistance.setBounds(3*ecart + largeur/6 , hauteur/16, largeur/6, hauteur/8);
        this.boutonTank.setBounds(4*ecart + 2*largeur/6, hauteur/16, largeur/6, hauteur/8);
        this.boutonEvolution.setBounds(5*ecart + 3*largeur/6, hauteur/16, largeur/6, hauteur/8);
        
        //Masquage des boutons intervenants plus tard
        this.boutonMelee.setVisible(false);
        this.boutonDistance.setVisible(false);
        this.boutonTank.setVisible(false);
        this.boutonEvolution.setVisible(false);
        this.rejouer.setVisible(false);
        
        //Ajout des boutons à la fenetre
        fenetre.add(this.boutonMelee);
        fenetre.add(this.boutonDistance);
        fenetre.add(this.boutonTank);
        fenetre.add(this.boutonEvolution);
        fenetre.add(this.demarrer);
        fenetre.add(this.rejouer);
        fenetre.add(this.terminer);
        
        //On rend les boutons cliquables
        this.demarrer.addActionListener(plateaudeJeu);
        this.rejouer.addActionListener(plateaudeJeu);
        this.terminer.addActionListener(plateaudeJeu);
        this.boutonMelee.addActionListener(plateaudeJeu);
        this.boutonDistance.addActionListener(plateaudeJeu);
        this.boutonTank.addActionListener(plateaudeJeu);
        this.boutonEvolution.addActionListener(plateaudeJeu);
        
        //On importe et on dessine l'image d'arriere plan
        try {
            this.imageArrierePlan = ImageIO.read(getClass().getResource("graphiques/background.jpg"));
        } catch (IOException e) {
        }
        this.panelArrierePlan = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imageArrierePlan, 0, 0, null);
            }
        }; 
        this.panelArrierePlan.setLayout(null);
        fenetre.add(this.panelArrierePlan);
        
        //On importe et on lance la bande son
        try {
            URL urlBandeSon = getClass().getResource("graphiques/soundtrack.wav");
            AudioInputStream fluxAudio = AudioSystem.getAudioInputStream(urlBandeSon);
            this.bandeSon = AudioSystem.getClip();
            this.bandeSon.open(fluxAudio);
            this.bandeSon.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
        
        //Réglages titre et signature
        this.pause.setText("Pause");
        this.pause.setBounds(largeur/2-250, hauteur/2 - 80, 500, 80);
        this.pause.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 70));
        this.titre.setText("Age of War");
        this.titre.setBounds(largeur/2-200, 60, 500, 80);
        this.signature.setText("Joris et Romain");
        this.signature.setBounds(largeur-375, hauteur-150, 500, 80);
        this.titre.setFont(new Font(Font.MONOSPACED, Font.BOLD, 70));
        this.signature.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        titre.setForeground(Color.DARK_GRAY);
        signature.setForeground(Color.DARK_GRAY);
        titre.setVisible(true);
        signature.setVisible(true);
        pause.setVisible(false);
        panelArrierePlan.add(signature);
        panelArrierePlan.add(pause);
        panelArrierePlan.add(titre);
    }

    public JFrame getFenetre() {
        return fenetre;
    }

    public JPanel getPanelArrierePlan() {
        return panelArrierePlan;
    }

    public JLabel getPointsDeVieBaseJoueur() {
        return pointsDeVieBaseJoueur;
    }

    public JLabel getPointsDeVieBaseAdversaire() {
        return pointsDeVieBaseAdversaire;
    }

    public JLabel getArgent() {
        return argent;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getHauteurAxeX() {
        return hauteurAxeX;
    }

    public JButton getDemarrer() {
        return demarrer;
    }

    public JButton getTerminer() {
        return terminer;
    }

    public JButton getRejouer() {
        return rejouer;
    }

    public JButton getBoutonMelee() {
        return boutonMelee;
    }

    public JButton getBoutonDistance() {
        return boutonDistance;
    }

    public JButton getBoutonTank() {
        return boutonTank;
    }

    public JButton getBoutonEvolution() {
        return boutonEvolution;
    }

    public JLabel getPause() {
        return pause;
    }

    public JLabel getFinDuJeu() {
        return finDuJeu;
    }

    public JLabel getCoutEvolution() {
        return coutEvolution;
    }

    public int getBaseJoueurXSpawn() {
        return baseJoueurXSpawn;
    }

    public int getVariablePause() {
        return variablePause;
    }

    public void setVariablePause(int variablePause) {
        this.variablePause = variablePause;
    }

    public void setBaseJoueurXSpawn(int baseJoueurXSpawn) {
        this.baseJoueurXSpawn = baseJoueurXSpawn;
    }

    public int getBaseAdversaireXSpawn() {
        return baseAdversaireXSpawn;
    }

    public void setBaseAdversaireXSpawn(int baseAdversaireXSpawn) {
        this.baseAdversaireXSpawn = baseAdversaireXSpawn;
    }
    
    /**
     * Méthode pour n'afficher que les boutons utiles lors du jeu et cacher les autres
     */
    public void boutonsJeu() {
        this.demarrer.setVisible(false);
        this.terminer.setVisible(false);
        this.finDuJeu.setVisible(false);
        titre.setVisible(false);
        signature.setVisible(false);

        this.boutonMelee.setVisible(true);
        this.boutonDistance.setVisible(true);
        this.boutonTank.setVisible(true);
        this.boutonEvolution.setVisible(true);
        this.pointsDeVieBaseJoueur.setVisible(true);
        this.pointsDeVieBaseAdversaire.setVisible(true);
        this.argent.setVisible(true);
        this.coutMelee.setVisible(true);
        this.coutDistance.setVisible(true);
        this.coutTank.setVisible(true);
        this.coutEvolution.setVisible(true);
    }
    
    /**
     * Méthode pour afficher toutes les informations des 2 bases à l'écran
     */
    public void informationsBase() {
        int hauteurTexte = 50;
        int largeurTexte = 350;

        this.argent.setBounds(largeur/80, hauteur - hauteurAxeX - hauteur/2,largeurTexte,hauteurTexte);
        this.argent.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.panelArrierePlan.add(argent);
        argent.setVisible(false);

        this.pointsDeVieBaseJoueur.setBounds(largeur/80, hauteur - hauteurAxeX - hauteur/2 + hauteurTexte,largeurTexte,hauteurTexte);
        this.pointsDeVieBaseJoueur.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.panelArrierePlan.add(pointsDeVieBaseJoueur);
        this.pointsDeVieBaseJoueur.setVisible(false);
        
        this.pointsDeVieBaseAdversaire.setBounds(largeur - largeur/80 - largeurTexte, hauteur - hauteurAxeX - hauteur/2 + hauteurTexte,largeurTexte,hauteurTexte);
        this.pointsDeVieBaseAdversaire.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.panelArrierePlan.add(pointsDeVieBaseAdversaire);
        this.pointsDeVieBaseAdversaire.setVisible(false);
    }
    
    /**
     * Méthode pour afficher les informations des unités
     */
    public void informationsUnites() {
        this.coutMelee = new JLabel("Coût : " + donnees.getCout(1,1));
        this.coutMelee.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.coutMelee.setBounds(2*ecart + 50, 0, 200, hauteur/12);
        this.panelArrierePlan.add(this.coutMelee);
        coutMelee.setVisible(false);

        this.coutDistance = new JLabel("Coût : " + donnees.getCout(2,1));
        this.coutDistance.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.coutDistance.setBounds(3*ecart + largeur/6 + 50, 0, 200, hauteur/12);
        this.panelArrierePlan.add(this.coutDistance);
        this.coutDistance.setVisible(false);

        this.coutTank = new JLabel("Coût : " + donnees.getCout(3,1));
        this.coutTank.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.coutTank.setBounds(4*ecart + 2*largeur/6 + 50, 0, 200, hauteur/12);
        this.panelArrierePlan.add(this.coutTank);
        this.coutTank.setVisible(false);

        this.coutEvolution = new JLabel("Coût : " + donnees.getCoutEvolution(1, 1));
        this.coutEvolution.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.coutEvolution.setBounds(5*ecart + 3*largeur/6 + 50, 0, 200, hauteur/12);
        this.panelArrierePlan.add(this.coutEvolution);
        this.coutEvolution.setVisible(false);
    }
    
    /**
     * Méthode pour mettre à jour l'affichage des informations des unités
     */
    public void actualiserInformationsUnites() {
        coutTank.setText("Coût : " + donnees.getCout(3, plateauDeJeu.getEpoqueJoueur()));
        coutDistance.setText("Coût : " + donnees.getCout(2, plateauDeJeu.getEpoqueJoueur()));
        coutMelee.setText("Coût : " + donnees.getCout(1, plateauDeJeu.getEpoqueJoueur()));
        coutEvolution.setText("Coût : " + donnees.getCoutEvolution(1, plateauDeJeu.getEpoqueJoueur()));
    }
    
    /**
     * Méthode pour basculer sur l'affichage de fin de partie si l'un des camps a gagné
     * @param victoire 
     */
    public void finDePartie(int victoire){
        // Masquer les éléments
        argent.setVisible(false);
        pointsDeVieBaseAdversaire.setVisible(false);
        pointsDeVieBaseJoueur.setVisible(false);
        boutonDistance.setVisible(false);
        boutonMelee.setVisible(false);
        boutonTank.setVisible(false);
        boutonEvolution.setVisible(false);
        coutMelee.setVisible(false);
        coutDistance.setVisible(false);
        coutTank.setVisible(false);
        coutEvolution.setVisible(false);

        // Afficher les éléments de fin de partie
        titre.setVisible(true);
        signature.setVisible(true);
        rejouer.setVisible(true);
        terminer.setVisible(true);
        if(victoire == 1){
            finDuJeu.setText("Fin de la partie, VICTOIRE !");
        } else {
            finDuJeu.setText("Fin de la partie, DEFAITE !");
        }
        finDuJeu.setFont(new Font("Serif", Font.BOLD, largeur/50));
        finDuJeu.setBounds(largeur/2-largeur/8, hauteur/10, largeur/3, largeur/8);
        panelArrierePlan.add(finDuJeu);
        finDuJeu.setVisible(true);
    }
}