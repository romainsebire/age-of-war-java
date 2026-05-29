package age_of_war;

import java.awt.EventQueue;

/**
 * Main entry point for the Age of War game
 * @author Romain Sebire, Joris Schwarz
 */
public class AgeOfWar {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            startGame();
        });
    }
    
    /**
     * Creates a new game board (used to start and restart the game)
     */
    public static void startGame() {
        GameBoard gameBoard = new GameBoard();
    }
}
