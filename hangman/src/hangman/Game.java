package hangman;

public abstract class Game {
    protected boolean gameOver;
    protected int score;
    
    public Game() {
        this.gameOver = false;
        this.score = 0;
    }
    
    // 추상 메서드들
    public abstract void startGame();
    public abstract void endGame();
    public abstract void processInput(String input);
    
    // 구현된 메서드
    public boolean isGameOver() {
        return gameOver;
    }
    
    public int getScore() {
        return score;
    }
    
    protected void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
