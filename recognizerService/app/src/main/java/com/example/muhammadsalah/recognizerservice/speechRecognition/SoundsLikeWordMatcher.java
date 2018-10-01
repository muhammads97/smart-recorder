package com.example.muhammadsalah.recognizerservice.speechRecognition;

import com.example.muhammadsalah.recognizerservice.word.WordMatcher;

import org.apache.commons.codec.language.Soundex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoundsLikeWordMatcher extends WordMatcher {
    protected static Soundex soundex;

    static {
        soundex = new Soundex();
    }

    public SoundsLikeWordMatcher(String... wordsIn) {
        this(Arrays.asList(wordsIn));
    }

    public SoundsLikeWordMatcher(List<String> wordsIn) {
        super(encode(wordsIn));
    }

    @Override
    public boolean isIn(String word) {
        return super.isIn(encode(word));
    }

    /**
     * @param input List of strings or words to encode
     * @return List of encoded words
     */
    protected static List<String> encode(List<String> input) {
        List<String> encoded = new ArrayList<String>();
        for (String in : input) {
            encoded.add(encode(in));
        }
        return encoded;
    }

    /**
     * @param in word to encode
     * @return Soundex encoding of the word in
     */
    private static String encode(String in) {
        String encoded = in;
        try {
            encoded = soundex.encode(in);
        }
        catch (IllegalArgumentException e) {
            //for weird characters that soundex doesn't understand

        }
        return encoded;
    }
}