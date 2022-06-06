/*
 * Copyright 2022 Mingchun Zhuang (http://me.mczhuang.cn)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.THE
 * SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * The {@code Service} class loads words from {@code Trimmed.csv} and stores indexes of those words, providing all the
 * functionalities related to word sources.
 *
 * @author Mingchun Zhuang
 * @version 1.0
 */
public class Service {
    /**
     * A static variable storing the only one instance instantiated.
     */
    private static Service instance;

    /**
     * A nested HashMap storing the difficulty of words from the given source, which should be attained by
     * <var>length</var> and then the word itself.
     */
    private HashMap<Integer, HashMap<String, Integer>> indexByLength;

    /**
     * A nested HashMap storing the words from the given source, which should be attained first by
     * <var>length</var> and then <var>difficulty</var>.
     */
    private HashMap<Integer, HashMap<Integer, ArrayList<String>>> wordByLengthThenDifficulty;

    /**
     * A HashMap storing the <var>difficulty</var> of <var>wordSource</var>
     */
    private HashMap<String, Integer> difficultyByWordSource;

    /**
     * Returns an instance of current class, where only one copy of instance will exist.
     *
     * <p>
     * If no instance found, a new one will be generated and stored. Otherwise, the stored one will be return.
     *
     * @return an instance of current class.
     */
    public static Service getInstance() {
        if (Service.instance == null)
            Service.instance = new Service();
        return Service.instance;
    }

    /**
     * Returns a string representation of initialization results.
     *
     * <p>
     * If the returning String is empty, the initialization process is successfully completed. Otherwise, the
     * returning String will contain error details.
     *
     * @param wordSources a String array containing word sources ordered by difficulty increasingly.
     * @param wordLengths a String array containing word lengths ordered increasingly.
     * @return a string representation of initialization results.
     */
    public String initService(String[] wordSources, String[] wordLengths) {
        final int minLength = Integer.parseInt(wordLengths[0]);
        final int maxLength = Integer.parseInt(wordLengths[wordLengths.length - 1]);
        difficultyByWordSource = new HashMap<>();
        for (int i = 0; i < wordSources.length; i++)
            difficultyByWordSource.put(wordSources[i], i + 1);
        indexByLength = new HashMap<>();
        wordByLengthThenDifficulty = new HashMap<>();

        /* Load words from word source. */
        try {
            /* Word source file location may vary due to different running methods including running as a project or as
            a single file. */
            String FilePath = (new File("./Word Sources/Trimmed.csv").exists() ? "./Word Sources/" :
                    "./src/Word Sources/") + "Trimmed.csv";
            FileReader fileReader = new FileReader(FilePath);
            BufferedReader bufReader = new BufferedReader(fileReader);
            for (String curLine = bufReader.readLine(); curLine != null; curLine = bufReader.readLine()) {
                /* The word source is in csv format, where items are separated by a single comma. */
                String[] items = curLine.split(",");
                if (items.length != 2)
                    continue;
                int difficulty = Integer.parseInt(items[1]);
                String word = items[0].toUpperCase();
                int wordLength = word.length();
                /* Ignore the words that are impossible to be requested. */
                if (wordLength < minLength || wordLength > maxLength)
                    continue;
                indexByLength.putIfAbsent(wordLength, new HashMap<>());
                indexByLength.get(wordLength).put(word, difficulty);

                wordByLengthThenDifficulty.putIfAbsent(wordLength, new HashMap<>());
                HashMap<Integer, ArrayList<String>> currentWordByLengthThenDifficulty =
                        wordByLengthThenDifficulty.get(wordLength);
                currentWordByLengthThenDifficulty.putIfAbsent(difficulty, new ArrayList<>());
                currentWordByLengthThenDifficulty.get(difficulty).add(word);
            }
        } catch (Exception e) {
            return e.toString();
        }
        return "";
    }

    /**
     * Returns a string representation of checking results.
     *
     * <p>
     * If the returning String is empty, the word given is valid. Otherwise, the
     * returning String will contain error details.
     *
     * <p>
     * If the word entered is empty, the return will also be empty as it's a representation of a random word from the
     * word source.
     *
     * @param word       an uppercase String to be checked in the difficulty level of <var>wordSource</var>.
     * @param wordSource a String representing the difficulty level of current setting.
     * @return a string representation of checking results.
     */
    public String checkExistence(String word, String wordSource) {
        if (word.length() == 0) return "";
        int difficulty = difficultyByWordSource.get(wordSource);
        int length = word.length();
        if (!indexByLength.containsKey(length) || !indexByLength.get(length).containsKey(word))
            return "Not Found";
        if (indexByLength.get(length).get(word) > difficulty)
            return "The word is too difficult";
        return "";
    }

    /**
     * Returns a random word with {@code O(1)} time complexity under given restrictions.
     *
     * <p>
     * If the returning String is not {@code "Not Found"}, the word returns is valid.
     *
     * @param wordLength an int describing the length restriction.
     * @param wordSource a String representing the difficulty level of current setting.
     * @return a random word or {@code "Not Found"} under given conditions.
     */
    public String generateRandomWord(int wordLength, String wordSource) {
        int difficulty = difficultyByWordSource.get(wordSource);
        HashMap<Integer, ArrayList<String>> wordByDifficulty = wordByLengthThenDifficulty.get(wordLength);
        int total = 0;
        for (int i = 1; i <= difficulty; i++)
            if (wordByDifficulty.containsKey(i)) {
                total += wordByDifficulty.get(i).size();
            }
        int randomIndex = new Random().nextInt(total);
        for (int i = 1; i <= difficulty; i++)
            if (wordByDifficulty.containsKey(i)) {
                int size = wordByDifficulty.get(i).size();
                if (randomIndex < size)
                    return wordByDifficulty.get(i).get(randomIndex);
                randomIndex -= size;
            }
        return "Not Found";
    }

    /**
     * This method returns the result of helper input checking and matched results. Word length and word source
     * configuration is attained directly from {@code Settings}.
     *
     * @param helperInput a String describing the input from the helper input text field.
     * @return a String containing error reason, which will be empty if no error found, and matched results. The error
     * reason and matched results are separated by "$".
     */
    public String validateHelperInput(String helperInput) {
        // Initialize variables.
        helperInput = helperInput.toUpperCase();
        boolean isInsideRoundBracket = false;
        boolean isInsideSquareBracket = false;
        boolean isContainedRoundBracket = false;
        HashMap<Character, Integer> mustExistCount = new HashMap<>();
        HashSet<Character> mustNotExist = new HashSet<>();
        boolean eligibilityMatchAll = false;
        StringBuilder patternString = new StringBuilder();
        // Scan and check the input string.
        for (int i = 0; i < helperInput.length(); i++) {
            char ch = helperInput.charAt(i);
            if (ch == '(') {
                isContainedRoundBracket = true;
                if (isInsideRoundBracket || isInsideSquareBracket)
                    return "Nested Brackets Not Supported$";
                else
                    isInsideRoundBracket = true;
            } else if (ch == ')') {
                if (isInsideRoundBracket)
                    isInsideRoundBracket = false;
                else
                    return "Unpair Bracket Found$";
            } else if (ch == '[') {
                if (isInsideSquareBracket || isInsideRoundBracket)
                    return "Nested Brackets Not Supported$";
                else
                    isInsideSquareBracket = true;
            } else if (ch == ']') {
                if (isInsideSquareBracket)
                    isInsideSquareBracket = false;
                else
                    return "Unpair Bracket Found$";
            } else if (Character.isAlphabetic(ch)) {
                if (isInsideRoundBracket)
                    mustExistCount.put(ch, mustExistCount.getOrDefault(ch, 0) + 1);
                else if (isInsideSquareBracket)
                    mustNotExist.add(ch);
                else
                    patternString.append(ch);
            } else if (ch == '*') {
                if (isInsideRoundBracket)
                    eligibilityMatchAll = true;
                else if (isInsideSquareBracket)
                    return "* Inside [] Not Allowed$";
                else
                    patternString.append(ch);
            } else
                return "Illegal Input$";
        }
        final int initWordLength = Settings.getInitWord().length();
        if (patternString.length() != initWordLength)
            return "Word Length too " + (patternString.length() < initWordLength ? "small" : "large") + "$";
        if (isInsideRoundBracket || isInsideSquareBracket)
            return "Unpair Bracket Found$";
        // Scan the database to filter out valid candidate words.
        if (!isContainedRoundBracket)
            eligibilityMatchAll = true;
        HashMap<Integer, ArrayList<String>> wordByDifficulty = wordByLengthThenDifficulty.get(initWordLength);
        int difficultyLevel = difficultyByWordSource.get(Settings.getWordSource());
        StringBuilder results = new StringBuilder();
        int candidateCount = 0;
        for (int currentDifficulty = 1; currentDifficulty <= difficultyLevel; currentDifficulty++) {
            ArrayList<String> currentWordList = wordByDifficulty.get(currentDifficulty);
            for (String word : currentWordList) {
                boolean ok = true;
                HashMap<Character, Integer> existCount = new HashMap<>();
                for (int i = 0; i < initWordLength; i++) {
                    char ch = word.charAt(i);
                    if (ch != patternString.charAt(i)) {
                        if (patternString.charAt(i) != '*') { // Pattern not match.
                            ok = false;
                            break;
                        }
                        // Pattern string here is *.
                        else if (mustNotExist.contains(ch)) {
                            ok = false;
                            break;
                        } else if (existCount.getOrDefault(ch, 0) <
                                mustExistCount.getOrDefault(ch, 0)) { // Check must exist condition first.
                            existCount.put(ch, existCount.getOrDefault(ch, 0) + 1);
                        } else if (!eligibilityMatchAll) {
                            ok = false;
                            break;
                        }
                    }
                }
                // Check must exist characters validity.
                for (Map.Entry<Character, Integer> pair : mustExistCount.entrySet()) {
                    if (existCount.getOrDefault(pair.getKey(), 0) < pair.getValue()) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    ++candidateCount;
                    results.append(word).append("\n");
                }
            }
        }
        return "$" + ("Found " + candidateCount + " result(s)" + (candidateCount > 0 ? ":" : ".")) + "\n" + results;
    }
}
