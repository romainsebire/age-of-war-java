/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projet_ageofwar;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author romai
 */
public class Donnees {
    //arguments : unite(0 melee, 1 distance, 2 tank), epoque(0, 1, 2)
    private int[][] pointDeVie = new int[3][3];
    private int[][] portee = new int[3][3];
    private int[][] degats = new int[3][3];
    private int[][] cout = new int[3][3];
    private int[][] orLoot = new int[3][3];
    private int[][] expLoot = new int[3][3];
    private int[][] tpsProduction = new int[3][3];
    //arguments : unite(0 melee, 1 distance, 2 tank), epoque(0, 1, 2), camp(0 joueur, 1 adversaire)
    private String[][][] cheminImage = new String[3][3][2];
    private int[][][][] imageBounds = new int[3][3][2][2];
    //arguments : camp(0 joueur, 1 adversaire), epoque(0, 1, 2)
    private int[][] vieInitiale = new int[2][3];
    private int[][] argentInitial = new int[2][3];
    private int[][] coutEvolution = new int[2][3];
    private String[][] cheminImageBase = new String[2][3];
    private int[][][] imageBaseBounds = new int[2][3][2];

    /**
     * Méthode pour créer un objet "données" ayant en attributs toutes les données lues du fichier XML
     */
    public Donnees() {
        LectureDonneesUnite();
        LectureDonneesBase();
    }

    public int getPointDeVie(int unite, int epoque) {
        return pointDeVie[unite-1][epoque-1];
    }

    public int getPortee(int unite, int epoque) {
        return portee[unite-1][epoque-1];
    }

    public int getDegats(int unite, int epoque) {
        return degats[unite-1][epoque-1];
    }

    public int getCout(int unite, int epoque) {
        return cout[unite-1][epoque-1];
    }

    public int getOrLoot(int unite, int epoque) {
        return orLoot[unite-1][epoque-1];
    }

    public int getExpLoot(int unite, int epoque) {
        return expLoot[unite-1][epoque-1];
    }

    public int getTpsProduction(int unite, int epoque) {
        return tpsProduction[unite-1][epoque-1];
    }

    public String getCheminImage(int unite, int epoque, int camp) {
        return cheminImage[unite-1][epoque-1][camp-1];
    }
    
    public int getImageBounds(int unite, int epoque, int camp, int bound) {
        return imageBounds[unite-1][epoque-1][camp-1][bound];
    }

    public int getVieInitiale(int camp, int epoque) {
        return vieInitiale[camp-1][epoque-1];
    }

    public int getArgentInitial(int camp, int epoque) {
        return argentInitial[camp-1][epoque-1];
    }

    public int getCoutEvolution(int camp, int epoque) {
        return coutEvolution[camp-1][epoque-1];
    }

    public String getCheminImageBase(int camp, int epoque) {
        return cheminImageBase[camp-1][epoque-1];
    }
    
    public int getImageBaseBounds(int camp, int epoque, int bound) {
        return imageBaseBounds[camp-1][epoque-1][bound];
    }

    /**
     * Méthode pour extraire les informations des unités du fichier XML
     */
    private void LectureDonneesUnite(){
        try {
              //Ouverture du document
            File file = new File("caracteristiquesPersonnages.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
              //Lecture du document
            for( int unite = 0 ; unite <= 2 ; unite++ ){
                for( int epoque = 0 ; epoque <= 2 ; epoque++ ){
                    String sUnite = "";
                    switch (unite) {
                        case 0 -> { sUnite = "melee"; }
                        case 1 -> { sUnite = "distance"; }
                        case 2 -> { sUnite = "tank"; }
                        default -> {}
                    }
                    NodeList nListUnite = document.getElementsByTagName(sUnite);
                    Node nNode = nListUnite.item(epoque);
                    Element eElement = (Element) nNode;
                    pointDeVie[unite][epoque] = Integer.parseInt(eElement.getElementsByTagName("pointDeVie").item(0).getTextContent());
                    portee[unite][epoque] = Integer.parseInt(eElement.getElementsByTagName("portee").item(0).getTextContent());
                    degats[unite][epoque] = Integer.parseInt(eElement.getElementsByTagName("degats").item(0).getTextContent());
                    cout[unite][epoque] = Integer.parseInt(eElement.getElementsByTagName("cout").item(0).getTextContent());
                    orLoot[unite][epoque] = Integer.parseInt(eElement.getElementsByTagName("orLoot").item(0).getTextContent());
                    expLoot[unite][epoque] = Integer.parseInt(eElement.getElementsByTagName("expLoot").item(0).getTextContent());
                    tpsProduction[unite][epoque] = Integer.parseInt(eElement.getElementsByTagName("tpsProduction").item(0).getTextContent());
                    
                    Node nImageJoueur = eElement.getElementsByTagName("image").item(0);
                    Element eImageJoueur = (Element) nImageJoueur;
                    cheminImage[unite][epoque][0] = eImageJoueur.getElementsByTagName("cheminImage").item(0).getTextContent();
                    imageBounds[unite][epoque][0][0] = Integer.parseInt(eImageJoueur.getElementsByTagName("width").item(0).getTextContent());
                    imageBounds[unite][epoque][0][1] = Integer.parseInt(eImageJoueur.getElementsByTagName("height").item(0).getTextContent());
                    
                    Node nImageAdversaire = eElement.getElementsByTagName("imageAdversaire").item(0);
                    Element eImageAdversaire = (Element) nImageAdversaire;
                    cheminImage[unite][epoque][1] = eImageAdversaire.getElementsByTagName("cheminImageAdversaire").item(0).getTextContent();
                    imageBounds[unite][epoque][1][0] = Integer.parseInt(eImageAdversaire.getElementsByTagName("width").item(0).getTextContent());
                    imageBounds[unite][epoque][1][1] = Integer.parseInt(eImageAdversaire.getElementsByTagName("height").item(0).getTextContent());
                }
            }        
        } catch (IOException e) {
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(Donnees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Méthode pour extraire les informations des bases du fichier XML
     */
    private void LectureDonneesBase(){
        try {
              //Ouverture du document
            File file = new File("caracteristiquesPersonnages.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
              //Lecture du document
            for( int camp = 0 ; camp <= 1 ; camp++ ){
                for( int epoque = 0 ; epoque <= 2 ; epoque++ ){
                    String sCamp = "";
                    String sEpoque = "";
                    switch (camp) {
                        case 0 -> { sCamp = "baseJoueur"; }
                        case 1 -> { sCamp = "baseAdversaire"; }
                        default -> {}
                    }
                    switch (epoque) {
                        case 0 -> { sEpoque = "bEpoque1"; }
                        case 1 -> { sEpoque = "bEpoque2"; }
                        case 2 -> { sEpoque = "bEpoque3"; }
                        default -> {}
                    }
                    NodeList nList = document.getElementsByTagName(sCamp);
                    Node nNode = nList.item(0);
                    Element base = (Element) nNode;
                    Node nEpoque = base.getElementsByTagName(sEpoque).item(0);
                    Element eEpoque = (Element) nEpoque;
                    vieInitiale[camp][epoque] = Integer.parseInt(eEpoque.getElementsByTagName("vieInitiale").item(0).getTextContent());
                    argentInitial[camp][epoque] = Integer.parseInt(eEpoque.getElementsByTagName("argentInitial").item(0).getTextContent());
                    coutEvolution[camp][epoque] = Integer.parseInt(eEpoque.getElementsByTagName("coutEvolution").item(0).getTextContent());
                    Node nImage = eEpoque.getElementsByTagName("image").item(0);
                    Element eImage = (Element) nImage;
                    cheminImageBase[camp][epoque] = eImage.getElementsByTagName("cheminImage").item(0).getTextContent();
                    imageBaseBounds[camp][epoque][0] = Integer.parseInt(eImage.getElementsByTagName("width").item(0).getTextContent());
                    imageBaseBounds[camp][epoque][1] = Integer.parseInt(eImage.getElementsByTagName("height").item(0).getTextContent());
                }
            }         
        } catch (IOException e) {
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(PlateauDeJeu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}