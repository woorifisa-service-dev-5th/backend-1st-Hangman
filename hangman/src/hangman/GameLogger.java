package hangman;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameLogger {
    private static GameLogger instance;
    private List<String> logs;
    private SimpleDateFormat dateFormat;
    
    private GameLogger() {
        this.logs = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    // 싱글톤 패턴
    public static GameLogger getInstance() {
        if (instance == null) {
            synchronized (GameLogger.class) {
                if (instance == null) {
                    instance = new GameLogger();
                }
            }
        }
        return instance;
    }
    
    public void log(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = String.format("[%s] %s", timestamp, message);
        logs.add(logMessage);
        
        // 콘솔에도 출력 (디버깅용 - 필요시 주석처리)
        // System.out.println(logMessage);
    }
    
    public void saveLogsToFile(String filename) {
        if (logs.isEmpty()) {
            return;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println("\n" + "=".repeat(50));
            writer.println("게임 세션 로그 - " + dateFormat.format(new Date()));
            writer.println("=".repeat(50));
            
            for (String log : logs) {
                writer.println(log);
            }
            
            writer.println("=".repeat(50) + "\n");
            
            // 저장 후 로그 클리어 (다음 게임을 위해)
            logs.clear();
            
            System.out.println("📁 로그가 " + filename + "에 저장되었습니다.");
            
        } catch (IOException e) {
            System.err.println("⚠️ 로그 저장 실패: " + e.getMessage());
        }
    }
    
    public void clearLogs() {
        logs.clear();
    }
    
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
}