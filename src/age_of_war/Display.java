package age_of_war;

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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Manages the game's graphical display (window, buttons, HUD)
 * @author Romain Sebire, Joris Schwarz
 */
public class Display {
    private GameData gameData;
    private GameBoard gameBoard;
    private JFrame window = new JFrame();
    private Image backgroundImage;
    private JButton startButton;
    private JButton quitButton;
    private JButton replayButton;
    private JButton meleeButton;
    private JButton rangedButton;
    private JButton tankButton;
    private JButton evolveButton;
    private Clip soundtrack;
    private JLabel goldLabel = new JLabel();
    private JLabel playerBaseHealthLabel = new JLabel();
    private JLabel enemyBaseHealthLabel = new JLabel();
    private JLabel meleeCostLabel;
    private JLabel rangedCostLabel;
    private JLabel tankCostLabel;
    private JLabel evolveCostLabel;
    private JPanel backgroundPanel;
    private JLabel gameOverLabel = new JLabel();
    private JLabel titleLabel = new JLabel();
    private JLabel signatureLabel = new JLabel();
    private JLabel pauseLabel = new JLabel();
    private int playerBaseXSpawn;
    private int enemyBaseXSpawn;
    private int windowWidth = 1540;
    private int windowHeight = 820;
    private int groundHeight = 220;
    private int spacing = (windowWidth-4*windowWidth/6)/7;
    private int pauseState = 0;

    /**
     * Constructor - initializes the graphical interface with buttons and layout
     * @param gameData game data
     * @param gameBoard the game board
     */
    public Display(GameData gameData, GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.gameData = gameData;
        
        window.setTitle("Age of War");
        window.setSize(windowWidth,windowHeight);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
                
        // Create buttons
        this.startButton = new JButton("Start Game");
        this.startButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 22));
        this.replayButton = new JButton("Replay");
        this.replayButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 22));
        this.quitButton = new JButton("Quit Game");
        this.quitButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 22));
        this.meleeButton = new JButton("Melee Warrior");
        this.meleeButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        this.rangedButton = new JButton("Ranged Warrior");
        this.rangedButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        this.tankButton = new JButton("Tank");
        this.tankButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        this.evolveButton = new JButton("Evolve");
        this.evolveButton.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 20));
        
        // Size buttons
        this.startButton.setBounds(windowWidth/2 - windowWidth/12, windowHeight/2 - windowHeight/8 - windowHeight/50, windowWidth/6, windowHeight/8);
        this.replayButton.setBounds(windowWidth/2 - windowWidth/12, windowHeight/2 - windowHeight/8 - windowHeight/50, windowWidth/6, windowHeight/8);
        this.quitButton.setBounds(windowWidth/2 - windowWidth/12, windowHeight/2 + windowHeight/50, windowWidth/6, windowHeight/8);
        this.meleeButton.setBounds(2*spacing, windowHeight/16, windowWidth/6, windowHeight/8);
        this.rangedButton.setBounds(3*spacing + windowWidth/6 , windowHeight/16, windowWidth/6, windowHeight/8);
        this.tankButton.setBounds(4*spacing + 2*windowWidth/6, windowHeight/16, windowWidth/6, windowHeight/8);
        this.evolveButton.setBounds(5*spacing + 3*windowWidth/6, windowHeight/16, windowWidth/6, windowHeight/8);
        
        // Hide buttons that appear later
        this.meleeButton.setVisible(false);
        this.rangedButton.setVisible(false);
        this.tankButton.setVisible(false);
        this.evolveButton.setVisible(false);
        this.replayButton.setVisible(false);
        
        // Add buttons to window
        window.add(this.meleeButton);
        window.add(this.rangedButton);
        window.add(this.tankButton);
        window.add(this.evolveButton);
        window.add(this.startButton);
        window.add(this.replayButton);
        window.add(this.quitButton);
        
        // Make buttons clickable
        this.startButton.addActionListener(gameBoard);
        this.replayButton.addActionListener(gameBoard);
        this.quitButton.addActionListener(gameBoard);
        this.meleeButton.addActionListener(gameBoard);
        this.rangedButton.addActionListener(gameBoard);
        this.tankButton.addActionListener(gameBoard);
        this.evolveButton.addActionListener(gameBoard);
        
        // Load and draw background image
        try {
            this.backgroundImage = ImageIO.read(getClass().getResource("assets/background.jpg"));
        } catch (IOException e) {
        }
        this.backgroundPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, null);
            }
        }; 
        this.backgroundPanel.setLayout(null);
        window.add(this.backgroundPanel);
        
        // Load and play soundtrack
        try {
            URL soundtrackUrl = getClass().getResource("assets/soundtrack.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundtrackUrl);
            this.soundtrack = AudioSystem.getClip();
            this.soundtrack.open(audioStream);
            this.soundtrack.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            // Audio may not be available (headless server, no sound card, etc.)
        }
        
        // Title and signature setup
        this.pauseLabel.setText("Pause");
        this.pauseLabel.setBounds(windowWidth/2-250, windowHeight/2 - 80, 500, 80);
        this.pauseLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 70));
        this.titleLabel.setText("Age of War");
        this.titleLabel.setBounds(windowWidth/2-200, 60, 500, 80);
        this.signatureLabel.setText("Joris & Romain");
        this.signatureLabel.setBounds(windowWidth-375, windowHeight-150, 500, 80);
        this.titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 70));
        this.signatureLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        titleLabel.setForeground(Color.DARK_GRAY);
        signatureLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setVisible(true);
        signatureLabel.setVisible(true);
        pauseLabel.setVisible(false);
        backgroundPanel.add(signatureLabel);
        backgroundPanel.add(pauseLabel);
        backgroundPanel.add(titleLabel);
    }

    public JFrame getWindow() {
        return window;
    }

    public JPanel getBackgroundPanel() {
        return backgroundPanel;
    }

    public JLabel getPlayerBaseHealthLabel() {
        return playerBaseHealthLabel;
    }

    public JLabel getEnemyBaseHealthLabel() {
        return enemyBaseHealthLabel;
    }

    public JLabel getGoldLabel() {
        return goldLabel;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getGroundHeight() {
        return groundHeight;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public JButton getReplayButton() {
        return replayButton;
    }

    public JButton getMeleeButton() {
        return meleeButton;
    }

    public JButton getRangedButton() {
        return rangedButton;
    }

    public JButton getTankButton() {
        return tankButton;
    }

    public JButton getEvolveButton() {
        return evolveButton;
    }

    public JLabel getPauseLabel() {
        return pauseLabel;
    }

    public JLabel getGameOverLabel() {
        return gameOverLabel;
    }

    public JLabel getEvolveCostLabel() {
        return evolveCostLabel;
    }

    public int getPlayerBaseXSpawn() {
        return playerBaseXSpawn;
    }

    public int getPauseState() {
        return pauseState;
    }

    public void setPauseState(int pauseState) {
        this.pauseState = pauseState;
    }

    public void setPlayerBaseXSpawn(int playerBaseXSpawn) {
        this.playerBaseXSpawn = playerBaseXSpawn;
    }

    public int getEnemyBaseXSpawn() {
        return enemyBaseXSpawn;
    }

    public void setEnemyBaseXSpawn(int enemyBaseXSpawn) {
        this.enemyBaseXSpawn = enemyBaseXSpawn;
    }
    
    /**
     * Shows only the in-game buttons and hides menu buttons
     */
    public void showGameButtons() {
        this.startButton.setVisible(false);
        this.quitButton.setVisible(false);
        this.gameOverLabel.setVisible(false);
        titleLabel.setVisible(false);
        signatureLabel.setVisible(false);

        this.meleeButton.setVisible(true);
        this.rangedButton.setVisible(true);
        this.tankButton.setVisible(true);
        this.evolveButton.setVisible(true);
        this.playerBaseHealthLabel.setVisible(true);
        this.enemyBaseHealthLabel.setVisible(true);
        this.goldLabel.setVisible(true);
        this.meleeCostLabel.setVisible(true);
        this.rangedCostLabel.setVisible(true);
        this.tankCostLabel.setVisible(true);
        this.evolveCostLabel.setVisible(true);
    }
    
    /**
     * Displays base information (health, gold) on screen
     */
    public void showBaseInfo() {
        int textHeight = 50;
        int textWidth = 350;

        this.goldLabel.setBounds(windowWidth/80, windowHeight - groundHeight - windowHeight/2,textWidth,textHeight);
        this.goldLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.backgroundPanel.add(goldLabel);
        goldLabel.setVisible(false);

        this.playerBaseHealthLabel.setBounds(windowWidth/80, windowHeight - groundHeight - windowHeight/2 + textHeight,textWidth,textHeight);
        this.playerBaseHealthLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.backgroundPanel.add(playerBaseHealthLabel);
        this.playerBaseHealthLabel.setVisible(false);
        
        this.enemyBaseHealthLabel.setBounds(windowWidth - windowWidth/80 - textWidth, windowHeight - groundHeight - windowHeight/2 + textHeight,textWidth,textHeight);
        this.enemyBaseHealthLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.backgroundPanel.add(enemyBaseHealthLabel);
        this.enemyBaseHealthLabel.setVisible(false);
    }
    
    /**
     * Displays unit cost information
     */
    public void showUnitInfo() {
        this.meleeCostLabel = new JLabel("Cost: " + gameData.getCost(1,1));
        this.meleeCostLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.meleeCostLabel.setBounds(2*spacing + 50, 0, 200, windowHeight/12);
        this.backgroundPanel.add(this.meleeCostLabel);
        meleeCostLabel.setVisible(false);

        this.rangedCostLabel = new JLabel("Cost: " + gameData.getCost(2,1));
        this.rangedCostLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.rangedCostLabel.setBounds(3*spacing + windowWidth/6 + 50, 0, 200, windowHeight/12);
        this.backgroundPanel.add(this.rangedCostLabel);
        this.rangedCostLabel.setVisible(false);

        this.tankCostLabel = new JLabel("Cost: " + gameData.getCost(3,1));
        this.tankCostLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.tankCostLabel.setBounds(4*spacing + 2*windowWidth/6 + 50, 0, 200, windowHeight/12);
        this.backgroundPanel.add(this.tankCostLabel);
        this.tankCostLabel.setVisible(false);

        this.evolveCostLabel = new JLabel("Cost: " + gameData.getEvolutionCost(1, 1));
        this.evolveCostLabel.setFont(new Font(Font.MONOSPACED, Font.TYPE1_FONT, 25));
        this.evolveCostLabel.setBounds(5*spacing + 3*windowWidth/6 + 50, 0, 200, windowHeight/12);
        this.backgroundPanel.add(this.evolveCostLabel);
        this.evolveCostLabel.setVisible(false);
    }
    
    /**
     * Updates unit cost display for the current era
     */
    public void refreshUnitInfo() {
        tankCostLabel.setText("Cost: " + gameData.getCost(3, gameBoard.getPlayerEra()));
        rangedCostLabel.setText("Cost: " + gameData.getCost(2, gameBoard.getPlayerEra()));
        meleeCostLabel.setText("Cost: " + gameData.getCost(1, gameBoard.getPlayerEra()));
        evolveCostLabel.setText("Cost: " + gameData.getEvolutionCost(1, gameBoard.getPlayerEra()));
    }
    
    /**
     * Shows the game over screen
     * @param victory 1 if player won, 0 if player lost
     */
    public void showGameOver(int victory){
        // Hide game elements
        goldLabel.setVisible(false);
        enemyBaseHealthLabel.setVisible(false);
        playerBaseHealthLabel.setVisible(false);
        rangedButton.setVisible(false);
        meleeButton.setVisible(false);
        tankButton.setVisible(false);
        evolveButton.setVisible(false);
        meleeCostLabel.setVisible(false);
        rangedCostLabel.setVisible(false);
        tankCostLabel.setVisible(false);
        evolveCostLabel.setVisible(false);

        // Show game over elements
        titleLabel.setVisible(true);
        signatureLabel.setVisible(true);
        replayButton.setVisible(true);
        quitButton.setVisible(true);
        if(victory == 1){
            gameOverLabel.setText("Game Over — VICTORY!");
        } else {
            gameOverLabel.setText("Game Over — DEFEAT!");
        }
        gameOverLabel.setFont(new Font("Serif", Font.BOLD, windowWidth/50));
        gameOverLabel.setBounds(windowWidth/2-windowWidth/8, windowHeight/10, windowWidth/3, windowWidth/8);
        backgroundPanel.add(gameOverLabel);
        gameOverLabel.setVisible(true);
    }
}
