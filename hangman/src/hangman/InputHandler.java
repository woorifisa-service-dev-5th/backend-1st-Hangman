package hangman;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InputHandler extends Thread {
    private volatile String input = null;
    private volatile boolean inputReceived = false;
    private Scanner scanner;
    private CountDownLatch latch;
    
    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
        this.latch = new CountDownLatch(1);
    }
    
    @Override
    public void run() {
        try {
            // 사용자 입력 대기
            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
                inputReceived = true;
            }
            latch.countDown();  // 입력 완료 신호
        } catch (Exception e) {
            inputReceived = false;
            latch.countDown();
        }
    }
    
    // 타임아웃과 함께 입력 대기
    public String getInputWithTimeout(int seconds) {
        this.start();  // 입력 스레드 시작
        
        // 타이머 표시 스레드
        TimerDisplay timer = new TimerDisplay(seconds);
        timer.start();
        
        try {
            // 지정된 시간만큼 대기
            boolean completed = latch.await(seconds, TimeUnit.SECONDS);
            
            timer.stopTimer();  // 타이머 중지
            
            if (!completed) {
                // 타임아웃 발생
                this.interrupt();  // 입력 스레드 중단
                return null;  // null 반환 = 타임아웃
            }
            
            return inputReceived ? input : null;
            
        } catch (InterruptedException e) {
            timer.stopTimer();
            return null;
        }
    }
    
    // 입력 검증 (정적 메서드)
    public static boolean validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        input = input.trim();
        if (input.length() != 1) {
            return false;
        }
        
        char c = input.charAt(0);
        // 알파벳(a-z, A-Z)인지 체크
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    
    // 검증된 문자 반환
    public static char getValidatedChar(String input) {
        if (validateInput(input)) {
            return Character.toUpperCase(input.trim().charAt(0));
        }
        return '\0';
    }
    
    // 타이머 표시 내부 클래스
    private static class TimerDisplay extends Thread {
        private int seconds;
        private volatile boolean running = true;
        
        public TimerDisplay(int seconds) {
            this.seconds = seconds;
            this.setDaemon(true);  // 메인 스레드 종료시 자동 종료
        }
        
        @Override
        public void run() {
            // 타이머는 화면을 어지럽히지 않도록 간단히 구현
            // 실제 카운트다운은 내부적으로만 진행
            while (running && seconds > 0) {
                try {
                    Thread.sleep(1000);
                    seconds--;
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        
        public void stopTimer() {
            running = false;
        }
    }
}