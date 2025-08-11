package hangman;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WordProvider implements WordInterface {
    private Map<Integer, List<String>> wordsByCategory;
    private Random random;
    
    public WordProvider() {
        this.wordsByCategory = new HashMap<>();
        this.random = new Random();
        initializeCategoryWords();
    }
    
    private void initializeCategoryWords() {
        // 카테고리 1: 음식
        wordsByCategory.put(1, Arrays.asList(
            "BREAD", "APPLE", "PIZZA", "GRAPE", "STEAK",
            "MANGO", "SALAD", "BACON", "LEMON", "SUSHI",
            "BURGER", "COFFEE", "BANANA", "ORANGE", "CHEESE",
            "CHICKEN", "NOODLES", "SANDWICH", "CHOCOLATE", "PASTA"
        ));
        
        // 카테고리 2: 동물
        wordsByCategory.put(2, Arrays.asList(
            "TIGER", "ZEBRA", "MOUSE", "HORSE", "SNAKE",
            "PANDA", "WHALE", "EAGLE", "SHEEP", "RABBIT",
            "MONKEY", "TURTLE", "DOLPHIN", "PENGUIN", "GIRAFFE",
            "ELEPHANT", "KANGAROO", "BUTTERFLY", "CROCODILE", "OCTOPUS"
        ));
        
        // 카테고리 3: 사물
        wordsByCategory.put(3, Arrays.asList(
            "TABLE", "CHAIR", "CLOCK", "PHONE", "WATCH",
            "WINDOW", "PENCIL", "MIRROR", "BOTTLE", "CAMERA",
            "LAPTOP", "WALLET", "GUITAR", "UMBRELLA", "BACKPACK",
            "KEYBOARD", "HEADPHONE", "TELESCOPE", "MICROWAVE", "CALENDAR"
        ));
        
        // 카테고리 4: 동작
        wordsByCategory.put(4, Arrays.asList(
            "WRITE", "DANCE", "SLEEP", "THINK", "LAUGH",
            "LISTEN", "CREATE", "TRAVEL", "SEARCH", "SMILE",
            "EXPLORE", "IMAGINE", "DISCOVER", "REMEMBER", "CELEBRATE",
            "WHISPER", "EXERCISE", "MEDITATE", "CALCULATE", "NAVIGATE"
        ));
    }
    
    @Override
    public String getWord(int category) {
        if (!hasWord(category)) {
            // 잘못된 카테고리면 랜덤 카테고리에서 선택
            category = random.nextInt(4) + 1;
        }
        
        List<String> words = wordsByCategory.get(category);
        return words.get(random.nextInt(words.size()));
    }
    
    @Override
    public boolean hasWord(int category) {
        return wordsByCategory.containsKey(category);
    }
    
    // 카테고리의 단어 개수 반환
    public int getCategoryWordCount(int category) {
        if (!hasWord(category)) {
            return 0;
        }
        return wordsByCategory.get(category).size();
    }
}