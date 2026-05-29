package age_of_war;

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
 * Loads and stores all game data from the XML configuration file
 * @author Romain Sebire, Joris Schwarz
 */
public class GameData {
    // Indexed by: unit(0 melee, 1 ranged, 2 tank), era(0, 1, 2)
    private int[][] hitPoints = new int[3][3];
    private int[][] range = new int[3][3];
    private int[][] damage = new int[3][3];
    private int[][] cost = new int[3][3];
    private int[][] goldLoot = new int[3][3];
    private int[][] expLoot = new int[3][3];
    private int[][] productionTime = new int[3][3];
    // Indexed by: unit(0 melee, 1 ranged, 2 tank), era(0, 1, 2), side(0 player, 1 enemy)
    private String[][][] imagePath = new String[3][3][2];
    private int[][][][] imageBounds = new int[3][3][2][2];
    // Indexed by: side(0 player, 1 enemy), era(0, 1, 2)
    private int[][] initialHealth = new int[2][3];
    private int[][] initialGold = new int[2][3];
    private int[][] evolutionCost = new int[2][3];
    private String[][] baseImagePath = new String[2][3];
    private int[][][] baseImageBounds = new int[2][3][2];

    /**
     * Constructor - loads all data from the XML file
     */
    public GameData() {
        loadUnitData();
        loadBaseData();
    }

    public int getHitPoints(int unit, int era) {
        return hitPoints[unit-1][era-1];
    }

    public int getRange(int unit, int era) {
        return range[unit-1][era-1];
    }

    public int getDamage(int unit, int era) {
        return damage[unit-1][era-1];
    }

    public int getCost(int unit, int era) {
        return cost[unit-1][era-1];
    }

    public int getGoldLoot(int unit, int era) {
        return goldLoot[unit-1][era-1];
    }

    public int getExpLoot(int unit, int era) {
        return expLoot[unit-1][era-1];
    }

    public int getProductionTime(int unit, int era) {
        return productionTime[unit-1][era-1];
    }

    public String getImagePath(int unit, int era, int side) {
        return imagePath[unit-1][era-1][side-1];
    }
    
    public int getImageBounds(int unit, int era, int side, int bound) {
        return imageBounds[unit-1][era-1][side-1][bound];
    }

    public int getInitialHealth(int side, int era) {
        return initialHealth[side-1][era-1];
    }

    public int getInitialGold(int side, int era) {
        return initialGold[side-1][era-1];
    }

    public int getEvolutionCost(int side, int era) {
        return evolutionCost[side-1][era-1];
    }

    public String getBaseImagePath(int side, int era) {
        return baseImagePath[side-1][era-1];
    }
    
    public int getBaseImageBounds(int side, int era, int bound) {
        return baseImageBounds[side-1][era-1][bound];
    }

    /**
     * Parses unit data from the XML file
     */
    private void loadUnitData(){
        try {
            File file = new File("characters.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();

            for( int unit = 0 ; unit <= 2 ; unit++ ){
                for( int era = 0 ; era <= 2 ; era++ ){
                    String unitTag = "";
                    switch (unit) {
                        case 0 -> { unitTag = "melee"; }
                        case 1 -> { unitTag = "ranged"; }
                        case 2 -> { unitTag = "tank"; }
                        default -> {}
                    }
                    NodeList nListUnit = document.getElementsByTagName(unitTag);
                    Node nNode = nListUnit.item(era);
                    Element eElement = (Element) nNode;
                    hitPoints[unit][era] = Integer.parseInt(eElement.getElementsByTagName("hitPoints").item(0).getTextContent());
                    range[unit][era] = Integer.parseInt(eElement.getElementsByTagName("range").item(0).getTextContent());
                    damage[unit][era] = Integer.parseInt(eElement.getElementsByTagName("damage").item(0).getTextContent());
                    cost[unit][era] = Integer.parseInt(eElement.getElementsByTagName("cost").item(0).getTextContent());
                    goldLoot[unit][era] = Integer.parseInt(eElement.getElementsByTagName("goldLoot").item(0).getTextContent());
                    expLoot[unit][era] = Integer.parseInt(eElement.getElementsByTagName("expLoot").item(0).getTextContent());
                    productionTime[unit][era] = Integer.parseInt(eElement.getElementsByTagName("productionTime").item(0).getTextContent());
                    
                    Node nPlayerImage = eElement.getElementsByTagName("playerImage").item(0);
                    Element ePlayerImage = (Element) nPlayerImage;
                    imagePath[unit][era][0] = ePlayerImage.getElementsByTagName("path").item(0).getTextContent();
                    imageBounds[unit][era][0][0] = Integer.parseInt(ePlayerImage.getElementsByTagName("width").item(0).getTextContent());
                    imageBounds[unit][era][0][1] = Integer.parseInt(ePlayerImage.getElementsByTagName("height").item(0).getTextContent());
                    
                    Node nEnemyImage = eElement.getElementsByTagName("enemyImage").item(0);
                    Element eEnemyImage = (Element) nEnemyImage;
                    imagePath[unit][era][1] = eEnemyImage.getElementsByTagName("path").item(0).getTextContent();
                    imageBounds[unit][era][1][0] = Integer.parseInt(eEnemyImage.getElementsByTagName("width").item(0).getTextContent());
                    imageBounds[unit][era][1][1] = Integer.parseInt(eEnemyImage.getElementsByTagName("height").item(0).getTextContent());
                }
            }        
        } catch (IOException e) {
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(GameData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Parses base data from the XML file
     */
    private void loadBaseData(){
        try {
            File file = new File("characters.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();

            for( int side = 0 ; side <= 1 ; side++ ){
                for( int era = 0 ; era <= 2 ; era++ ){
                    String sideTag = "";
                    String eraTag = "";
                    switch (side) {
                        case 0 -> { sideTag = "playerBase"; }
                        case 1 -> { sideTag = "enemyBase"; }
                        default -> {}
                    }
                    switch (era) {
                        case 0 -> { eraTag = "era1"; }
                        case 1 -> { eraTag = "era2"; }
                        case 2 -> { eraTag = "era3"; }
                        default -> {}
                    }
                    NodeList nList = document.getElementsByTagName(sideTag);
                    Node nNode = nList.item(0);
                    Element base = (Element) nNode;
                    Node nEra = base.getElementsByTagName(eraTag).item(0);
                    Element eEra = (Element) nEra;
                    initialHealth[side][era] = Integer.parseInt(eEra.getElementsByTagName("initialHealth").item(0).getTextContent());
                    initialGold[side][era] = Integer.parseInt(eEra.getElementsByTagName("initialGold").item(0).getTextContent());
                    evolutionCost[side][era] = Integer.parseInt(eEra.getElementsByTagName("evolutionCost").item(0).getTextContent());
                    Node nImage = eEra.getElementsByTagName("image").item(0);
                    Element eImage = (Element) nImage;
                    baseImagePath[side][era] = eImage.getElementsByTagName("path").item(0).getTextContent();
                    baseImageBounds[side][era][0] = Integer.parseInt(eImage.getElementsByTagName("width").item(0).getTextContent());
                    baseImageBounds[side][era][1] = Integer.parseInt(eImage.getElementsByTagName("height").item(0).getTextContent());
                }
            }         
        } catch (IOException e) {
        } catch (SAXException | ParserConfigurationException ex) {
            Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
