package age_of_war;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Core game logic - manages units, combat, movement, and game state
 * @author Romain Sebire, Joris Schwarz
 */
public class GameBoard extends JFrame implements ActionListener {
    private final Display display;
    private final GameData gameData;
    private Base playerBase;
    private int playerBaseCombatX;
    private Base enemyBase;
    private int enemyBaseCombatX;
    private int playerEra = 1;
    private int enemyEra = 1;
    private ArrayList<Unit> playerUnits = new ArrayList<>();
    private ArrayList<EnemyUnit> enemyUnits = new ArrayList<>();
    private ArrayList<Timer> timers = new ArrayList<>();

    /**
     * Constructor - initializes the game board
     */
    public GameBoard() {
        this.gameData = new GameData();
        this.display = new Display(gameData, this);
        int[] zero = {0, 0};
        createPlayerBase(zero);
        createEnemyBase(zero);
        display.showBaseInfo();
        display.showUnitInfo();
        display.getWindow().setVisible(true);
    }

    public Display getDisplay() {
        return display;
    }

    public int getPlayerEra() {
        return playerEra;
    }

    public int getEnemyEra() {
        return enemyEra;
    }

    public Base getPlayerBase() {
        return playerBase;
    }

    public Base getEnemyBase() {
        return enemyBase;
    }

    /**
     * Creates the player base for the current era with given stats (health and gold)
     * @param stats array [health, gold] to add
     */
    private void createPlayerBase(int[] stats) {
        int width = gameData.getBaseImageBounds(1, playerEra, 0);
        int height = gameData.getBaseImageBounds(1, playerEra, 1);
        int x = 0;
        int y = display.getWindowHeight() - display.getGroundHeight() - height + 20;
        this.display.setPlayerBaseXSpawn(width - 100);
        this.playerBaseCombatX = width + 25;
        this.playerBase = new Base(1, this, this.gameData);
        this.playerBase.setBounds(x, y, width, height);
        this.playerBase.setOpaque(false);
        this.playerBase.addHealth(stats[0]);
        this.playerBase.addGold(stats[1]);
        this.display.getBackgroundPanel().add(this.playerBase);
    }
    
    /**
     * Creates the enemy base for the current era with given stats (health and gold)
     * @param stats array [health, gold] to add
     */
    private void createEnemyBase(int[] stats) {
        int width = gameData.getBaseImageBounds(2, enemyEra, 0);
        int height = gameData.getBaseImageBounds(2, enemyEra, 1);
        int x = display.getWindowWidth() - width;
        int y = display.getWindowHeight() - display.getGroundHeight() - height + 20;
        this.display.setEnemyBaseXSpawn(x + 100);
        this.enemyBaseCombatX = x - 25;
        this.enemyBase = new Base(2, this, this.gameData);
        this.enemyBase.setBounds(x, y, width, height);
        this.enemyBase.setOpaque(false);
        this.enemyBase.addHealth(stats[0]);
        this.display.getBackgroundPanel().add(this.enemyBase);
    }

    /**
     * Removes the player base and returns its current stats [health, gold]
     */
    public int[] removePlayerBase() {
        int[] stats = {this.playerBase.getHealth(), this.playerBase.getGold()};
        this.playerBase.setVisible(false);
        this.display.getBackgroundPanel().remove(this.playerBase);
        return stats;
    }

    /**
     * Removes the enemy base and returns its current stats [health, gold]
     */
    public int[] removeEnemyBase() {
        int[] stats = {this.enemyBase.getHealth(), this.enemyBase.getGold()};
        this.enemyBase.setVisible(false);
        this.display.getBackgroundPanel().remove(this.enemyBase);
        return stats;
    }

    /**
     * Button event listener - handles all game actions
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == display.getStartButton()) {
            display.showGameButtons();

            // Enemy transitions to era 2 between 40 and 45 seconds after game start
            Random rand = new Random();
            int timeBeforeEra2 = rand.nextInt(5000) + 40000;
            
            Timer enemyEra2Timer = new Timer(timeBeforeEra2, g -> {
                this.enemyEra = 2;
                createEnemyBase(removeEnemyBase());
            });
            timers.add(enemyEra2Timer);
            enemyEra2Timer.setRepeats(false);
            enemyEra2Timer.start();
            
            // Enemy transitions to era 3 between 100 and 130 seconds after game start
            int timeBeforeEra3 = rand.nextInt(30000) + 100000;

            Timer enemyEra3Timer = new Timer(timeBeforeEra3, g -> {
                this.enemyEra = 3;
                createEnemyBase(removeEnemyBase());
            });
            timers.add(enemyEra3Timer);
            enemyEra3Timer.setRepeats(false);
            enemyEra3Timer.start();
            
            // Main game timer - enemy troop spawning
            Timer enemySpawnTimer = new Timer(1000, (ActionEvent g) -> {
                if(this.enemyUnits.size() < 5){
                    enemyActions();
                }
            });
            timers.add(enemySpawnTimer);
            enemySpawnTimer.start();

            // Gold income timer
            Timer goldTimer = new Timer(5000, g -> {
                this.playerBase.addGold(100);
            });
            timers.add(goldTimer);
            goldTimer.start();
            
            // Unit movement timer
            Timer movementTimer = new Timer(10, (ActionEvent e1) -> {
                moveAllUnits();
            });
            timers.add(movementTimer);
            movementTimer.start();
            
            // Unit attack timer
            Timer attackTimer = new Timer(1500, (ActionEvent e1) -> {
                processAllAttacks();
            });
            timers.add(attackTimer);
            attackTimer.start();

        } else if (source == display.getQuitButton()) {
            display.getWindow().dispose();
            System.exit(0);
        } else if (source == display.getReplayButton()) {
            display.getWindow().dispose();
            AgeOfWar.startGame();
        } else if (source == display.getMeleeButton()) {
                onUnitButtonPressed(1);
        } else if (source == display.getRangedButton()) {
                onUnitButtonPressed(2);
        } else if (source == display.getTankButton()) {
                onUnitButtonPressed(3);
        } else if (source == display.getEvolveButton()) {
            int cost = gameData.getEvolutionCost(1, this.playerEra);
            if ( playerBase.getGold() >= cost ) {
                playerBase.spendGold(cost);
                this.playerEra += 1;
                if(this.playerEra == 3){
                    display.getEvolveCostLabel().setVisible(false);
                    display.getEvolveButton().setVisible(false);
                }
                createPlayerBase(removePlayerBase());
                display.refreshUnitInfo();
            }
        }
    }

    /**
     * Processes all unit attacks for one tick (enemy units attack first)
     */
    public void processAllAttacks() {
        // Enemy units attack first
        if(!playerUnits.isEmpty()){
            Unit frontPlayerUnit = playerUnits.get(0);
            for(EnemyUnit enemyUnit : enemyUnits) {
                // If the front player unit is in range
                if( frontPlayerUnit.getPositionX() + frontPlayerUnit.getWidth() + enemyUnit.getRange() >= enemyUnit.getPositionX()) {
                    enemyUnit.attack(frontPlayerUnit);
                    if(frontPlayerUnit.isDead()){
                        frontPlayerUnit.setLocation(-500, -500);
                        frontPlayerUnit.repaint();
                        playerUnits.remove(frontPlayerUnit);
                        display.getBackgroundPanel().remove(frontPlayerUnit);
                        frontPlayerUnit.repaint();
                        if(!playerUnits.isEmpty()) {
                            frontPlayerUnit = playerUnits.get(0);
                        }
                    }
                // If the player base is in range instead
                } else if( playerBaseCombatX + enemyUnit.getRange() >= enemyUnit.getPositionX()) {
                    enemyUnit.attackBase(playerBase);
                    checkGameOver();
                }
            }
        } else if(!enemyUnits.isEmpty()) {
            for(EnemyUnit enemyUnit : enemyUnits) {
                if( playerBaseCombatX + enemyUnit.getRange() >= enemyUnit.getPositionX()) {
                    enemyUnit.attackBase(playerBase);
                    checkGameOver();
                }
            }
        }
        
        // Player units attack
        if(!enemyUnits.isEmpty()){
            EnemyUnit frontEnemyUnit = enemyUnits.get(0);
            for(Unit unit : playerUnits) {
                if( frontEnemyUnit.getPositionX() <= unit.getPositionX() + unit.getWidth() + unit.getRange()) {
                    unit.attack(frontEnemyUnit);
                    if(frontEnemyUnit.isDead()){
                        this.playerBase.addGold(frontEnemyUnit.getGoldLoot());
                        frontEnemyUnit.setLocation(-500, -500);
                        frontEnemyUnit.repaint();
                        enemyUnits.remove(frontEnemyUnit);
                        display.getBackgroundPanel().remove(frontEnemyUnit);
                        frontEnemyUnit.repaint();
                        if(!enemyUnits.isEmpty()) {
                            frontEnemyUnit = enemyUnits.get(0);
                        }
                    }
                } else if( enemyBaseCombatX <= unit.getPositionX() + unit.getWidth() + unit.getRange()) {
                    unit.attackBase(enemyBase);
                    checkGameOver();
                }
            }
        } else if(!playerUnits.isEmpty()) {
            for(Unit unit : playerUnits) {
                if( enemyBaseCombatX <= unit.getPositionX() + unit.getWidth() + unit.getRange()) {
                    unit.attackBase(enemyBase);
                    checkGameOver();
                }
            }
        }  
    }
    
    /**
     * Moves all units on the board (enemy units move first)
     */
    public void moveAllUnits(){
        // Move enemy units
        if(!playerUnits.isEmpty()){
            Unit frontPlayerUnit = playerUnits.get(0);
            Unit blockingElement = frontPlayerUnit;
            for(EnemyUnit enemyUnit : enemyUnits) {
                if( blockingElement.getPositionX() + blockingElement.getWidth() < enemyUnit.getPositionX() && playerBaseCombatX < enemyUnit.getPositionX()) {
                    enemyUnit.advance();
                }
                blockingElement = enemyUnit;
            }
        } else if(!enemyUnits.isEmpty()) {
            if(playerBaseCombatX < enemyUnits.get(0).getPositionX()) {
                enemyUnits.get(0).advance();
            }
            Unit blockingElement = enemyUnits.get(0);
            for(int i = 1 ; i < enemyUnits.size() ; i++){
                EnemyUnit enemyUnit = enemyUnits.get(i);
                if( blockingElement.getPositionX() + blockingElement.getWidth() < enemyUnit.getPositionX() && playerBaseCombatX < enemyUnit.getPositionX()) {
                    enemyUnit.advance();
                }
                blockingElement = enemyUnit;
            }
        }

        // Move player units
        if(!enemyUnits.isEmpty()){
            EnemyUnit frontEnemyUnit = enemyUnits.get(0);
            Unit blockingElement2 = frontEnemyUnit;
            for(Unit unit : playerUnits) {
                if( blockingElement2.getPositionX() > unit.getPositionX() + unit.getWidth() && enemyBaseCombatX > unit.getPositionX() + unit.getWidth()) {
                    unit.advance();
                }
                blockingElement2 = unit;
            }
        } else if(!playerUnits.isEmpty()) {
            if(enemyBaseCombatX > playerUnits.get(0).getPositionX() + playerUnits.get(0).getWidth()) {
                playerUnits.get(0).advance();
            }
            Unit blockingElement2 = playerUnits.get(0);
            for(int i = 1 ; i < playerUnits.size() ; i++){
                Unit unit = playerUnits.get(i);
                if( blockingElement2.getPositionX() > unit.getPositionX() + unit.getWidth() && enemyBaseCombatX > unit.getPositionX() + unit.getWidth()) {
                    unit.advance();
                }
                blockingElement2 = unit;
            }
        }
    }
    
    /**
     * Enemy AI - randomly spawns enemy units with a 15% chance per tick
     */
    public void enemyActions() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(100);
        if( randomNumber < 15){
            int unitType = rand.nextInt(3) + 1;
            if(this.enemyEra==3 && unitType==3 && rand.nextInt(3)<2){
                unitType = 1;
            }
            EnemyUnit enemyUnit = new EnemyUnit(this.enemyEra, unitType, this.gameData, display);
            enemyUnits.add(enemyUnit);
            int width = gameData.getImageBounds(unitType, this.enemyEra, 2, 0);
            int height = gameData.getImageBounds(unitType, this.enemyEra, 2, 1);
            int x = display.getWindowWidth() - width - 130;
            int y = display.getWindowHeight() - display.getGroundHeight() - height;
            enemyUnit.setBounds(x, y, width, height);
            enemyUnit.setOpaque(false);
            enemyUnit.setVisible(true);
            display.getBackgroundPanel().add(enemyUnit);
        }
    }

    /**
     * Checks if the game is over (either base destroyed)
     */
    public void checkGameOver(){
        if(playerBase.getHealth() <= 0 ){
            display.showGameOver(0);
            stopAllTimers();
        }
        else if(enemyBase.getHealth() <= 0){
            display.showGameOver(1);
            stopAllTimers();
        }
    }
    
    /**
     * Spawns a player unit when a unit button is pressed
     * @param unitType unit type (1=melee, 2=ranged, 3=tank)
     */
    public void onUnitButtonPressed(int unitType) {
        if ( this.playerBase.getGold() >= gameData.getCost(unitType, this.playerEra)) {
            Unit unit = new Unit(playerEra, unitType, gameData, display);
            playerUnits.add(unit);
            playerBase.spendGold(unit.getCost());
            display.getBackgroundPanel().add(unit);
        }
    }
    
    /**
     * Stops all game timers
     */
    public void stopAllTimers() {
       for(Timer timer : timers) {
           timer.stop();
       }
    }
}
