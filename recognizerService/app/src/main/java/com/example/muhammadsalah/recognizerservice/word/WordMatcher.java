package com.example.muhammadsalah.recognizerservice.word;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WordMatcher {
    /**
     * Target words
     */
    private Set<String> words;

    public WordMatcher(String... wordsIn)
    {
        this(Arrays.asList(wordsIn));
    }

    public WordMatcher(List<String> wordsIn)
    {
        //care about order so we can execute isInAt
        words = new LinkedHashSet<String>(wordsIn);
    }

    public Set<String> getWords()
    {
        return words;
    }

    /**
     * @param word word to match
     * @return boolean to indicate the word was matched with the target words or not
     */
    public boolean isIn(String word)
    {
        return words.contains(word);
    }

    /**
     * @param wordsIn Array of words to match
     * @return boolean to indicate if one element of the array was matched
     */
    public boolean isIn(String [] wordsIn)
    {
        boolean wordIn = false;
        for (String word : wordsIn)
        {
            if (isIn(word))
            {
                wordIn = true;
                break;
            }
        }
        return wordIn;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (String word : getWords())
        {
            sb.append(word).append(" ");
        }
        return sb.toString().trim();
    }
}
