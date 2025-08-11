package hangman;

import java.util.HashSet;
import java.util.Set;

public class Word {
    private String word;
    private boolean[] revealed;
    private Set<Character> guessedLetters;
    private int correctGuessCount;
    
    public Word(String word) {
        this.word = word.toUpperCase();
        this.revealed = new boolean[word.length()];
        this.guessedLetters = new HashSet<>();
        this.correctGuessCount = 0;
    }
    
    public boolean guessLetter(char letter) {
        letter = Character.toUpperCase(letter);
        guessedLetters.add(letter);
        
        boolean found = false;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                if (!revealed[i]) {
                    revealed[i] = true;
                    correctGuessCount++;
                }
                found = true;
            }
        }
        return found;
    }
    
    public boolean isComplete() {
        for (boolean r : revealed) {
            if (!r) return false;
        }
        return true;
    }
    
    public String getDisplayWord() {
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
    
    public String getAnswer() {
        return word;
    }
    
    public Set<Character> getGuessedLetters() {
        return new HashSet<>(guessedLetters);
    }
    
    public boolean isAlreadyGuessed(char letter) {
        return guessedLetters.contains(Character.toUpperCase(letter));
    }
    
    public boolean[] getRevealedArray() {
        return revealed.clone();
    }
    
    public int getCorrectGuessCount() {
        return correctGuessCount;
    }
    
    public int getWordLength() {
        return word.length();
    }
}