/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projet_ageofwar;

import java.awt.EventQueue;

/**
 *
 * @author romain
 */

public class Projet_AgeOfWar {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            lancerLeJeu();
        });
    }
    
    /**
     * Méthode pour créer un plateau de jeu (utilisée pour lancer et RElancer le jeu à la fin d'une partie)
     */
    public static void lancerLeJeu() {
        PlateauDeJeu plateauDeJeu = new PlateauDeJeu();
    }
}