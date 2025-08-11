package hangman;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String playAgain;
        
        do {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("         HANGMAN GAME ");
            System.out.println("=".repeat(40));
            
            HangmanGame game = new HangmanGame();
            game.startGame();
            
            System.out.print("\n다시 플레이하시겠습니까? (y/n): ");
            playAgain = scanner.nextLine();
            
        } while (playAgain.equalsIgnoreCase("y"));
        
        System.out.println("\n게임을 종료합니다. 감사합니다! ");
        scanner.close();
    }
}