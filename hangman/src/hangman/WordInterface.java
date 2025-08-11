package hangman;

public interface WordInterface {
    String getWord(int category);
    boolean hasWord(int category);
}