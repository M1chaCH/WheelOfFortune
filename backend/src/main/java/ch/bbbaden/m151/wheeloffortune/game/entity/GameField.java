package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class GameField {
    public static final char HIDDEN_CHAR = '*';
    public static final List<Character> PUNCTUATIONS = List.of( '.', ',', '!', '?', ' ', '-', '"', '\'', '/', '#' );
    private final int sentenceLength;
    private char[] revealedCharacters;

    public GameField(String currentSentence){
        this.sentenceLength = currentSentence.length();
        this.revealedCharacters = revealPunctuations(currentSentence);
    }

    private char[] revealPunctuations(String sentence){
        char[] sentenceArray = sentence.toCharArray();
        char[] visibleCharacters = new char[sentenceArray.length];

        for (int i = 0; i < sentenceArray.length; i++) {
            if(PUNCTUATIONS.contains(sentenceArray[i]))
                visibleCharacters[i] = sentenceArray[i];
            else
                visibleCharacters[i] = HIDDEN_CHAR;
        }

        return visibleCharacters;
    }

    public int revealCharacter(char toReveal, char[] sentence){
        int countConsonants = 0;
        for (int i = 0; i < sentence.length; i++) {
            if(Character.toLowerCase(sentence[i]) == Character.toLowerCase(toReveal)){
                countConsonants++;
                revealedCharacters[i] = sentence[i];
            }
        }
        return countConsonants;
    }
}
