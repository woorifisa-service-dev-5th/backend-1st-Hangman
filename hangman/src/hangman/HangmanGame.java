package hangman;

import java.util.Scanner;
import java.io.IOException;

public class HangmanGame extends Game {
    private static final int MAX_LIVES = 6;
    private static final int TIME_LIMIT = 10; // 10초 제한
    
    private Word currentWord;
    private WordProvider wordProvider;
    private int lives;
    private String categoryName;
    private GameLogger logger;
    private Scanner scanner;
    private Display display;
    
    public HangmanGame() {
        super();
        this.wordProvider = new WordProvider();
        this.logger = GameLogger.getInstance();
        this.scanner = new Scanner(System.in);
        this.display = new Display();
    }
    
    @Override
    public void startGame() {
        logger.log("게임 시작!");
        
        // 환영 메시지
        System.out.println("""
                
                +---+
                |   |
             Welcome|
               to   |
            Hangman |
              Game  |
          ===============
          
        """);
        
        // 카테고리 선택
        int category = getWordCategory();
        
        // 카테고리 이름 저장
        switch(category) {
            case 1: categoryName = "음식"; break;
            case 2: categoryName = "동물"; break;
            case 3: categoryName = "사물"; break;
            case 4: categoryName = "동작"; break;
            default: categoryName = "기타";
        }
        
        // 단어 초기화
        String selectedWord = wordProvider.getWord(category);
        currentWord = new Word(selectedWord);
        lives = MAX_LIVES;
        
        logger.log("카테고리: " + categoryName + ", 단어: " + selectedWord);
        System.out.println("\n선택된 카테고리: " + categoryName);
        System.out.println("단어가 준비되었습니다! (" + selectedWord.length() + "글자)\n");
        
        // 게임 루프
        do {
            // 게임 화면 표시 (카테고리 포함)
            display.showGameScreen(
                currentWord.getDisplayWord(),
                lives,
                currentWord.getGuessedLetters().toString(),
                categoryName
            );
            
            if (!gameOver) {
                System.out.print("\n 글자를 입력하세요 (a~z) - " + TIME_LIMIT + "초 제한: ");
                System.out.flush();  // 출력 버퍼 플러시
                
                // 타이머 스레드로 입력 처리
                InputHandler inputHandler = new InputHandler(scanner);
                String input = inputHandler.getInputWithTimeout(TIME_LIMIT);
                
                if (input == null) {
                    // 타임아웃 발생
                    System.out.println("\n\n 시간 초과! 목숨이 1개 감소합니다.");
                    lives--;
                    logger.log("시간 초과 - 남은 목숨: " + lives);
                    
                    if (lives <= 0) {
                        gameOver = true;
                    } else {
                        // 게임이 계속되는 경우에만 버퍼 정리
                        System.out.println("계속하려면 Enter를 누르세요...");
                        
                        // Scanner 버퍼 비우기
                        try {
                            // 타임아웃 후 잔여 입력 제거
                            while (System.in.available() > 0) {
                                System.in.read();
                            }
                            Thread.sleep(1000);  // 1초 대기
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    
                } else {
                    // 정상 입력 처리
                    processInput(input);
                }
            }
        } while (!gameOver);
        
        endGame();
    }
    
    @Override
    public void processInput(String input) {
        // 입력 검증
        if (!InputHandler.validateInput(input)) {
            System.out.println(" 올바르지 않은 입력! a~z 중 한 글자를 입력하세요.");
            return;
        }
        
        char letter = InputHandler.getValidatedChar(input);
        
        // 이미 입력한 글자인지 체크
        if (currentWord.isAlreadyGuessed(letter)) {
            System.out.println(" '" + letter + "'는 이미 입력한 글자입니다.");
            return;
        }
        
        logger.log("입력된 글자: " + letter);
        
        // 글자 맞추기
        if (currentWord.guessLetter(letter)) {
            System.out.println(" 정답! '" + letter + "'가 단어에 있습니다.");
            
            if (currentWord.isComplete()) {
                gameOver = true;
            }
        } else {
            lives--;
            System.out.println(" 틀렸습니다! '" + letter + "'는 단어에 없습니다.");
            
            if (lives <= 0) {
                gameOver = true;
            }
        }
    }
    
    private int getWordCategory() {
        int category = 0;
        
        while (category < 1 || category > 4) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("      카테고리를 선택하세요");
            System.out.println("=".repeat(30));
            System.out.println("  1. 음식");
            System.out.println("  2. 동물");
            System.out.println("  3. 사물");
            System.out.println("  4. 동작");
            System.out.println("=".repeat(30));
            System.out.print("선택 (1-4): ");
            
            try {
                category = Integer.parseInt(scanner.nextLine());
                if (category < 1 || category > 4) {
                    System.out.println(" 1~4 사이의 숫자를 입력해주세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" 올바른 숫자를 입력해주세요.");
            }
        }
        
        return category;
    }
    
    @Override
    public void endGame() {
        System.out.println("\n" + "=".repeat(40));
        
        // 결과 표시
        boolean isWin = currentWord.isComplete();
        display.showResult(isWin, currentWord.getAnswer());
        
        // 점수 계산 및 표시
        if (isWin) {
            score = lives * 20;
            System.out.println(" 최종 점수: " + score + "점");
            System.out.println("   (남은 목숨 " + lives + " × 20)");
            logger.log("게임 승리 - 점수: " + score);
        } else {
            System.out.println(" 아쉽네요! 다시 도전해보세요!");
            logger.log("게임 패배 - 정답: " + currentWord.getAnswer());
        }
        
        // 통계 표시
        System.out.println("\n 게임 통계:");
        System.out.println("   시도한 글자: " + currentWord.getGuessedLetters());
        System.out.println("   맞춘 글자 수: " + currentWord.getCorrectGuessCount());
        
        logger.log("게임 종료 - 카테고리: " + categoryName);
        logger.saveLogsToFile("hangman_log.txt");
        
        System.out.println("=".repeat(40));
    }
}