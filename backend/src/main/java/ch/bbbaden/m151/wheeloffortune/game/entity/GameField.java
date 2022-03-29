package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class GameField {
    public static final List<Character> PUNCTUATIONS = List.of( '.', ',', '!', '?', ' ', '-', '"', '\'', '/' );
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
                visibleCharacters[i] = ' ';
        }

        return visibleCharacters;
    }
}
