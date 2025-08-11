package hangman;

public class Display {
    
    public void showGameScreen(String word, int lives, String guessedLetters, String category) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("카테고리: " + category);
        System.out.println("=".repeat(40));
        System.out.println("단어: " + word);
        System.out.println("생명: " + LivesDisplay.formatLives(lives, 6));
        System.out.println("시도한 글자: " + guessedLetters);
        System.out.println(HangmanDrawing.getDrawing(6 - lives));
        System.out.println("=".repeat(40));
    }
    
    public void showResult(boolean isWin, String answer) {
        System.out.println("\n" + "".repeat(20));
        if (isWin) {
            System.out.println("\n 축하합니다! 성공입니다! ");
            System.out.println("정답: " + answer);
        } else {
            System.out.println("\n 게임 오버! ");
            System.out.println("정답은 '" + answer + "' 였습니다.");
            System.out.println(HangmanDrawing.getDrawing(6)); // 완성된 행맨
        }
        System.out.println("\n" + "".repeat(20));
    }
    
    // 행맨 그림 내부 클래스
    public static class HangmanDrawing {
        private static final String[] HANGMAN_STAGES = {
            """
              +---+
              |   |
                  |
                  |
                  |
                  |
            =========
            """,
            """
              +---+
              |   |
              O   |
                  |
                  |
                  |
            =========
            """,
            """
              +---+
              |   |
              O   |
              |   |
                  |
                  |
            =========
            """,
            """
              +---+
              |   |
              O   |
             /|   |
                  |
                  |
            =========
            """,
            """
              +---+
              |   |
              O   |
             /|\\  |
                  |
                  |
            =========
            """,
            """
              +---+
              |   |
              O   |
             /|\\  |
             /    |
                  |
            =========
            """,
            """
              +---+
              |   |
              O   |
             /|\\  |
             / \\  |
                  |
            =========
            """
        };
        
        public static String getDrawing(int mistakes) {
            if (mistakes < 0 || mistakes >= HANGMAN_STAGES.length) {
                return HANGMAN_STAGES[HANGMAN_STAGES.length - 1];
            }
            return HANGMAN_STAGES[mistakes];
        }
        
        public static int getMaxStages() {
            return HANGMAN_STAGES.length;
        }
    }
    
    // 단어 표시 내부 클래스
    public static class WordDisplay {
        public static String formatWord(String word, boolean[] revealed) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                if (revealed[i]) {
                    sb.append(word.charAt(i));
                } else {
                    sb.append("_");
                }
                if (i < word.length() - 1) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        }
    }
    
    // 생명 표시 내부 클래스
    public static class LivesDisplay {
        public static String formatLives(int current, int max) {
            return "♥".repeat(current) + "♡".repeat(max - current);
        }
    }
}
