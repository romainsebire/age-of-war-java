/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projet_ageofwar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author romai
 */
public class PlateauDeJeu extends JFrame implements ActionListener {
      //Attributs 
    private final Affichage affichage;
    private final Donnees donnees;
    private Base baseJoueur;
    private int baseJoueurXCombat;
    private Base baseAdversaire;
    private int baseAdversaireXCombat;
    private int epoqueJoueur = 1;
    private int epoqueAdversaire = 1;
    private ArrayList<Unite> listeUnites = new ArrayList<>();
    private ArrayList<UniteAdversaire> listeUnitesAdversaire = new ArrayList<>();
    private ArrayList<Timer> listeTimer = new ArrayList<>();

    /**
     * Constructeur plateau de jeu
     */
    public PlateauDeJeu() {
        this.donnees = new Donnees();
        this.affichage = new Affichage(donnees,this);
        int[] zero = {0,0};
        creerBaseJoueur(zero);
        creerBaseAdversaire(zero);
        affichage.informationsBase();
        affichage.informationsUnites();
        affichage.getFenetre().setVisible(true);
    }

    public Affichage getAffichage() {
        return affichage;
    }

    public int getEpoqueJoueur() {
        return epoqueJoueur;
    }

    public int getEpoqueAdversaire() {
        return epoqueAdversaire;
    }

    public Base getBaseJoueur() {
        return baseJoueur;
    }

    public Base getBaseAdversaire() {
        return baseAdversaire;
    }

    
    private void creerBaseJoueur(int[] infos) {
        int width = donnees.getImageBaseBounds(1,epoqueJoueur,0);
        int height = donnees.getImageBaseBounds(1,epoqueJoueur,1);
        int x = 0;
        int y = affichage.getHauteur() - affichage.getHauteurAxeX() - height + 20;
        this.affichage.setBaseJoueurXSpawn(width - 100);
        this.baseJoueurXCombat = width + 25;
        this.baseJoueur = new Base(1,this,this.donnees);
        this.baseJoueur.setBounds(x, y, width, height);
        this.baseJoueur.setOpaque(false);
        this.baseJoueur.ajouterPointsDeVie(infos[0]);
        this.baseJoueur.ajouterArgent(infos[1]);
        this.affichage.getPanelArrierePlan().add(this.baseJoueur);
    }
    
    private void creerBaseAdversaire(int[] infos) {
        int width = donnees.getImageBaseBounds(2,epoqueAdversaire,0);
        int height = donnees.getImageBaseBounds(2,epoqueAdversaire,1);
        int x = affichage.getLargeur() - width;
        int y = affichage.getHauteur() - affichage.getHauteurAxeX() - height + 20;
        this.affichage.setBaseAdversaireXSpawn(x + 100);
        this.baseAdversaireXCombat = x - 25;
        this.baseAdversaire = new Base(2,this,this.donnees);
        this.baseAdversaire.setBounds(x, y, width, height);
        this.baseAdversaire.setOpaque(false);
        this.baseAdversaire.ajouterPointsDeVie(infos[0]);
        this.affichage.getPanelArrierePlan().add(this.baseAdversaire);
    }

    public int[] supprimerBaseJoueur() {
        int[] infos = {this.baseJoueur.getPointsDeVie(), this.baseJoueur.getArgent()};
        this.baseJoueur.setVisible(false);
        this.affichage.getPanelArrierePlan().remove(this.baseJoueur);
        return infos;
    }

    public int[] supprimerBaseAdversaire() {
        int[] infos = {this.baseAdversaire.getPointsDeVie(), this.baseAdversaire.getArgent()};
        this.baseAdversaire.setVisible(false);
        this.affichage.getPanelArrierePlan().remove(this.baseAdversaire);
        return infos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == affichage.getDemarrer()) {
            affichage.boutonsJeu();

            // Transition de l'adversaire vers l'epoque 2 entre 40 et 45 secondes après le début du jeu
            Random rand = new Random();
            int tempsAvantEpoque2 = rand.nextInt(5000) + 40000;
            
            Timer timerAdversaireEpoque2 = new Timer(tempsAvantEpoque2, g -> {
                this.epoqueAdversaire = 2;
                creerBaseAdversaire(supprimerBaseAdversaire());
            });
            listeTimer.add(timerAdversaireEpoque2);
            timerAdversaireEpoque2.setRepeats(false);
            timerAdversaireEpoque2.start();
            
            // Transition de l'adversaire vers une nouvelle ère entre 100 et 130 secondes après le début du jeu
            int tempsAvantEpoque3 = rand.nextInt(30000) + 100000;

            Timer timerAdversaireEpoque3 = new Timer(tempsAvantEpoque3, g -> {
                this.epoqueAdversaire = 3;
                creerBaseAdversaire(supprimerBaseAdversaire());
            });
            listeTimer.add(timerAdversaireEpoque3);
            timerAdversaireEpoque3.setRepeats(false);
            timerAdversaireEpoque3.start();
            
            //Timer principal JEU
            Timer timerEnvoieTroupesAdversaires = new Timer(1000, (ActionEvent g) -> {
                if(this.listeUnitesAdversaire.size() < 5){
                    operationsAdversaire();
                }
            });
            listeTimer.add(timerEnvoieTroupesAdversaires);
            timerEnvoieTroupesAdversaires.start();

            //Timer gain argent
            Timer timerArgent = new Timer(5000, g -> {
                this.baseJoueur.ajouterArgent(100);
            });
            listeTimer.add(timerArgent);
            timerArgent.start();
            
            // Déplacement des unités
            Timer timerDeplacements = new Timer(10, (ActionEvent e1) -> {
                deplacementDesUnites();
            });
            listeTimer.add(timerDeplacements);
            timerDeplacements.start();
            
            // Attaque des unités
            Timer timerAttaques = new Timer(1500, (ActionEvent e1) -> {
                attaqueDesUnites();
            });
            listeTimer.add(timerAttaques);
            timerAttaques.start();

        } else if (source == affichage.getTerminer()) {
            affichage.getFenetre().dispose();
            System.exit(0);
        } else if (source == affichage.getRejouer()) {
            affichage.getFenetre().dispose();
            Projet_AgeOfWar.lancerLeJeu();
        } else if (source == affichage.getBoutonMelee()) {
                boutonUnitePresse(1);
        } else if (source == affichage.getBoutonDistance()) {
                boutonUnitePresse(2);
        } else if (source == affichage.getBoutonTank()) {
                boutonUnitePresse(3);
        } else if (source == affichage.getBoutonEvolution()) {
            int cout = donnees.getCoutEvolution(1, this.epoqueJoueur);
            if ( baseJoueur.getArgent() >= cout ) {
                baseJoueur.diminuerArgent(cout);
                this.epoqueJoueur += 1;
                if(this.epoqueJoueur == 3){
                    affichage.getCoutEvolution().setVisible(false);
                    affichage.getBoutonEvolution().setVisible(false);
                }
                creerBaseJoueur(supprimerBaseJoueur());
                affichage.actualiserInformationsUnites();
            }
        }
    }
        

    public void attaqueDesUnites() {
        //On commence par faire attaquer toutes les unités adversaires
        //Si la file d'unité du joueur est non vide
        if(!listeUnites.isEmpty()){
            //On recupère la première unité de la file du joueur
            Unite premiereUnite = listeUnites.get(0);
            //Pour toutes les unités dans la file adversaire
            for(UniteAdversaire uniteAdversaire : listeUnitesAdversaire) {
                //Si la premiere unité du joueur est à portée
                if( premiereUnite.getPositionX() + premiereUnite.getWidth() + uniteAdversaire.getPortee() >= uniteAdversaire.getPositionX()) {
                    //On l'attaque
                    uniteAdversaire.attaquer(premiereUnite);
                    //Si la première unité du joueur meurt
                    if(premiereUnite.estMort()){
                        //On retire l'unité morte de la file du joueur et de l'affichage
                        premiereUnite.setLocation(-500, -500);
                        premiereUnite.repaint();
                        listeUnites.remove(premiereUnite);
                        affichage.getPanelArrierePlan().remove(premiereUnite);
                        premiereUnite.repaint();
                        //Si il y a une seconde unité dans la file du joueur
                        if(!listeUnites.isEmpty()) {
                            //On définit la seconde unité comme la nouvelle première
                            premiereUnite = listeUnites.get(0);
                        }
                    }
                //Si la première unité n'est pas a portee, on regarde si la base est a portée
                } else if( baseJoueurXCombat + uniteAdversaire.getPortee() >= uniteAdversaire.getPositionX()) {
                    //Si oui, l'unité attaque la base
                    uniteAdversaire.attaquerBase(baseJoueur);
                    jeuFini();
                }
            }
        //Si par contre la file d'unité du joueur est vide mais pas celle de l'adversaire
        } else if(!listeUnitesAdversaire.isEmpty()) {
            //Pour toutes les unités de la file adversaire
            for(UniteAdversaire uniteAdversaire : listeUnitesAdversaire) {
                //On regarde seulement si la base est à portee
                if( baseJoueurXCombat + uniteAdversaire.getPortee() >= uniteAdversaire.getPositionX()) {
                    //On attaque la base du joueur
                    uniteAdversaire.attaquerBase(baseJoueur);
                    jeuFini();
                }
            }
        }
        
        //On fait ensuite attaquer les unités du joueur
        //Si la file d'unité adversaire est non vide
        if(!listeUnitesAdversaire.isEmpty()){
            //On récupère la première unité adversaire
            UniteAdversaire premiereUniteAdversaire = listeUnitesAdversaire.get(0);
            //Puis pour chaque unité du joueur
            for(Unite unite : listeUnites) {
                //Si la première unité de la file adversaire et a portee
                if( premiereUniteAdversaire.getPositionX() <= unite.getPositionX() + unite.getWidth() + unite.getPortee()) {
                    //L'unité du joueur l'attaque
                    unite.attaquer(premiereUniteAdversaire);
                    //Si elle meurt
                    if(premiereUniteAdversaire.estMort()){
                        //On ajoute l'or gagné au joueur
                        this.baseJoueur.ajouterArgent(premiereUniteAdversaire.getOrLoot());
                        //On la retire de la file et de l'affichage
                        premiereUniteAdversaire.setLocation(-500, -500);
                        premiereUniteAdversaire.repaint();
                        listeUnitesAdversaire.remove(premiereUniteAdversaire);
                        affichage.getPanelArrierePlan().remove(premiereUniteAdversaire);
                        premiereUniteAdversaire.repaint();
                        //Si il y a une seconde unité dans la file
                        if(!listeUnitesAdversaire.isEmpty()) {
                            //On la définit comme la nouvelle première unité
                            premiereUniteAdversaire = listeUnitesAdversaire.get(0);
                        }
                    }
                //Sinon, si la première unité adversaire n'est pas à portée on regarde si la base adversaire l'est
                } else if( baseAdversaireXCombat <= unite.getPositionX() + unite.getWidth() + unite.getPortee()) {
                    //L'unité attaque la base adversaire
                    unite.attaquerBase(baseAdversaire);
                    jeuFini();
                }
            }
        //Sinon (file unité adversaire vide), mais si la liste unité du joueur est non vide
        } else if(!listeUnites.isEmpty()) {
            //Pour chaque unité du joueur
            for(Unite unite : listeUnites) {
                //Si la base adversaire est à portée
                if( baseAdversaireXCombat <= unite.getPositionX() + unite.getWidth() + unite.getPortee()) {
                    //L'unité l'attaque
                    unite.attaquerBase(baseAdversaire);
                    jeuFini();
                }
            }
        }  
    }
    
    
    public void deplacementDesUnites(){
        //On commence par le déplacement des unités adversaires
        //Si la file d'unité du joueur est non vide
        if(!listeUnites.isEmpty()){
            //On définit la première unité de la file du joueur comme unité de blocage pour les unités adversaires
            Unite premiereUnite = listeUnites.get(0);
            Unite objetBlocage = premiereUnite;
            //Pour chaque unité adversaire
            for(UniteAdversaire uniteAdversaire : listeUnitesAdversaire) {
                //Si l'élément de blocage et suffisamment loin et que l'unité n'a pas atteint la base du joueur
                if( objetBlocage.getPositionX() + objetBlocage.getWidth() < uniteAdversaire.getPositionX() && baseJoueurXCombat < uniteAdversaire.getPositionX()) {
                    //On fait avancer l'unité adversaire
                    uniteAdversaire.avancer();
                }
                //Puis on redéfinit l'élément de blocage pour l'unité suivante dans la file à cette unité la
                objetBlocage = uniteAdversaire;
            }
        //Sinon, si la file d'unité du joueur est vide mais pas celle de l'adversaire
        } else if(!listeUnitesAdversaire.isEmpty()) {
            //Si la premiere unité de la file d'unité de l'adversaire n'a pas atteint la base
            if(baseJoueurXCombat < listeUnitesAdversaire.get(0).getPositionX()) {
                //On la fait avancer
                listeUnitesAdversaire.get(0).avancer();
            }
            //Puis on définit l'élément de blocage pour l'unité suivante à cette première unité
            Unite objetBlocage = listeUnitesAdversaire.get(0);
            //Pour toute les autres unités de la file adversaire
            for(int i = 1 ; i < listeUnitesAdversaire.size() ; i++){
                UniteAdversaire uniteAdversaire = listeUnitesAdversaire.get(i);
                //Si l'unité n'est pas bloquée et n'a pas atteint la base
                if( objetBlocage.getPositionX() + objetBlocage.getWidth() < uniteAdversaire.getPositionX() && baseJoueurXCombat < uniteAdversaire.getPositionX()) {
                    //On la fait avancer
                    uniteAdversaire.avancer();
                }
                //Puis on redéfinit l'élément de blocage comme étant cette unité pour l'unité suivante dans la file
                objetBlocage = uniteAdversaire;
            }
        }
        //Maintenant on fait de même pour les unités du joueur
        //Si la file des unités de l'adversaire est non vide
        if(!listeUnitesAdversaire.isEmpty()){
            //On définit l'élément de blocage comme la première des unités adversaires
            UniteAdversaire premiereUniteAdversaire = listeUnitesAdversaire.get(0);
            Unite objetBlocage2 = premiereUniteAdversaire;
            //Puis pour chaque unité du joueur
            for(Unite unite : listeUnites) {
                //Si l'élément blocage n'est pas atteint et la base adversaire non plus
                if( objetBlocage2.getPositionX() > unite.getPositionX() + unite.getWidth() && baseAdversaireXCombat > unite.getPositionX() + unite.getWidth()) {
                    //On avance l'unité du joueur
                    unite.avancer();
                }
                //Puis on redéfinit l'élément de blocage à cette unité pour l'unité suivante dans la file
                objetBlocage2 = unite;
            }
        //Sinon, si la file d'unité adversaire est vide mais pas celle du joueur
        } else if(!listeUnites.isEmpty()) {
            //Si la premiere unité de la file du joueur n'a pas atteint la base adversaire
            if(baseAdversaireXCombat > listeUnites.get(0).getPositionX() + listeUnites.get(0).getWidth()) {
                //On la fait avancer
                listeUnites.get(0).avancer();
            }
            //On définit l'élément blocage à cette premiere unité de file
            Unite objetBlocage2 = listeUnites.get(0);
            //Puis pour chaque unité suivante dans la file
            for(int i = 1 ; i < listeUnites.size() ; i++){
                Unite unite = listeUnites.get(i);
                //Si l'unité n'a pas atteint l'élément blocage ou la base adversaire
                if( objetBlocage2.getPositionX() > unite.getPositionX() + unite.getWidth() && baseAdversaireXCombat > unite.getPositionX() + unite.getWidth()) {
                    //On l'avance
                    unite.avancer();
                }
                //Puis on redéfinit l'élément blocage à celle ci
                objetBlocage2 = unite;
            }
        }
    }
    
    // Actions de l'ennemi       
    public void operationsAdversaire() {
        Random rand = new Random();
        //On tire un nombre aléatoire entre 0 et 19
        int nombreAleatoire = rand.nextInt(100);
        //Si le nombre aléatoire est 0 ou 1 (15% de chance)
        if( nombreAleatoire < 15){
            //On tire un type d'unité aléatoire 
            int typeUnite = rand.nextInt(3) + 1;
            if(this.epoqueAdversaire==3 && typeUnite==3 && rand.nextInt(3)<2){
                typeUnite = 1;
            }
            //On ajoute une unité de ce type à la file adversaire et on l'affiche
            UniteAdversaire uniteAdversaire = new UniteAdversaire(this.epoqueAdversaire,typeUnite,this.donnees,affichage);
            listeUnitesAdversaire.add(uniteAdversaire);
            int width = donnees.getImageBounds(typeUnite, this.epoqueAdversaire, 2, 0);
            int height = donnees.getImageBounds(typeUnite, this.epoqueAdversaire, 2, 1);
            int x = affichage.getLargeur() - width - 130;
            int y = affichage.getHauteur() - affichage.getHauteurAxeX() - height;
            uniteAdversaire.setBounds(x, y, width, height);
            uniteAdversaire.setOpaque(false);
            uniteAdversaire.setVisible(true);
            affichage.getPanelArrierePlan().add(uniteAdversaire);
        }
    }

    public void jeuFini(){
        if(baseJoueur.getPointsDeVie() <= 0 ){
            affichage.finDePartie(0);
            arreterTousLesTimers();
        }
        else if(baseAdversaire.getPointsDeVie() <= 0){
            affichage.finDePartie(1);
            arreterTousLesTimers();
        }
    }
    
    public void boutonUnitePresse(int numUnite) {
        //On regarde si le joueur a plus ou autant d'argent que le coût de l'unité
        if ( this.baseJoueur.getArgent() >= donnees.getCout(numUnite, this.epoqueJoueur)) {
            //On ajoute l'unité acheté à la file du joueur et on diminue l'argent
            Unite unite = new Unite(epoqueJoueur,numUnite,donnees,affichage);
            listeUnites.add(unite);
            baseJoueur.diminuerArgent(unite.getCout());
            affichage.getPanelArrierePlan().add(unite);
        }
    }
    
    public void arreterTousLesTimers() {
       for(Timer timer : listeTimer) {
           timer.stop();
       }
    }
}