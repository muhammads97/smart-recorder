package com.example.muhammadsalah.recognizerservice.word;

public class WordList {
    private String [] words;
    private String source;

    public WordList(String source){
        this.source = source.toLowerCase();
        words = this.source.split("\\s");
    }

    /**
     * @param wordIndex index
     * @return String after index
     */
    public String getStringAfter(int wordIndex) {
        int startAt = wordIndex + 1;
        if (startAt >= words.length) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = startAt; i < words.length; i++) {
            sb.append(words[i]).append(" ");
        }
        return sb.toString();
    }

    /**
     * @param indexToRemove index
     * @return string without word at index
     */
    public String getStringWithout(int indexToRemove) {
        if (indexToRemove >= words.length) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i != indexToRemove) {
                sb.append(words[i]).append(" ");
            }
        }
        return sb.toString();
    }

    public String[] getWords() {
        return words;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return source;
    }
}