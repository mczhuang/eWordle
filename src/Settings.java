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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.function.Consumer;

/**
 * The {@code Setting} class manages a setting window that enables user to configure preferences and start the game via
 * creating a new {@code Game} instance.
 *
 * @author Mingchun Zhuang
 * @version 1.0
 */
public class Settings {
    /**
     * A static variable storing the only one instance instantiated.
     */
    private static Settings instance;

    /**
     * A static constant holding the width of current window.
     */
    private static final int WINDOW_WIDTH = 600;

    /**
     * A static constant holding the height of current window.
     */
    private static final int WINDOW_HEIGHT = 800;

    /**
     * A static constant holding the height of the vertical interval of contents of current window.
     */
    private static final int BREAK_HEIGHT = 50;

    /**
     * A static constant holding the width of each content box.
     */
    private static final int CONTENT_WIDTH = 500;

    /**
     * A static constant holding the height of each content box.
     */
    private static final int CONTENT_HEIGHT = 100;

    /**
     * A static constant holding the height of the horizontal interval of contents of current window.
     */
    private static final int WIDTH_MARGIN = (WINDOW_WIDTH - CONTENT_WIDTH) / 2;

    /**
     * A static {@code JFrame} holding the instance of current window.
     */
    private static JFrame window;

    /**
     * A static {@code JTextField} holding the instance of text field where users can enter their preferred initial
     * word.
     */
    private static JTextField initWordField;

    /**
     * A static {@code JTextField} holding the instance of text field where error messages will be shown.
     */
    private static JTextField errorMessageField;

    /**
     * A static int holding the word length selected by the user.
     */
    private static int wordLength;

    /**
     * A static String holding the word source selected by the user.
     */
    private static String wordSource;

    /**
     * A static String holding the preferred initial word typed by the user.
     */
    private static String initWord;

    /**
     * A static String array holding all word sources available.
     */
    private static String[] wordSourceOptions;

    /**
     * A static String holding current hashtag.
     */
    private static String currentHashtag;

    /**
     * This method configs the setting window at the very beginning and should be called before being set visible.
     *
     * @param wordLength        an int describing the length of words to be guessed.
     * @param wordSource        a String describing the specific source type, included in <var>wordSourceOptions</var>.
     * @param wordLengthOptions a String array containing the word lengths to be chosen.
     * @param wordSourceOptions a String array containing the word sources to be chosen.
     */
    public void configSettings(int wordLength, String wordSource, String[] wordLengthOptions,
                               String[] wordSourceOptions) {
        Settings.instance = this;
        Settings.wordLength = wordLength;
        Settings.wordSource = wordSource;
        Settings.wordSourceOptions = wordSourceOptions;

        // Configure window settings.
        window = new JFrame("Welcome - eWordle");
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel windowPanel = new JPanel();
        windowPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.add(windowPanel);
        window.pack();
        windowPanel.setBackground(new Color(238, 238, 238));
        windowPanel.setLayout(null);

        windowPanel.add(Settings.textInit("Preferences", "Comic Sans MS", JTextField.CENTER,
                Font.BOLD, WIDTH_MARGIN, BREAK_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT, 60, false,
                false));

        // two following Combos initialized with identical Event Consumer, which is distinguished in Consumer via char
        // at hint[5]: 'L' or 'S'.
        Consumer<ItemEvent> comboEventConsumer = event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String selectedItem = (String) event.getItem();
                char c = selectedItem.charAt(5);
                if (c == 'L')
                    Settings.wordLength = Integer.parseInt(selectedItem.substring(13));
                else if (c == 'S')
                    Settings.wordSource = selectedItem.substring(13);
            }
        };
        // Add two combos.
        int currentHeight = BREAK_HEIGHT + CONTENT_HEIGHT;
        windowPanel.add(Settings.textInit("Word Length", "", JTextField.LEFT, Font.PLAIN,
                WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 30, false,
                false));
        windowPanel.add(initCombo("Word Length: ", wordLengthOptions, currentHeight + BREAK_HEIGHT,
                comboEventConsumer, "Word Length (Default: " + wordLength + " or last round preference)",
                wordLength + ""));

        currentHeight += BREAK_HEIGHT + CONTENT_HEIGHT;
        windowPanel.add(Settings.textInit("Word Source", "", JTextField.LEFT, Font.PLAIN,
                WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 30, false,
                false));
        windowPanel.add(initCombo("Word Source: ", wordSourceOptions, currentHeight + BREAK_HEIGHT,
                comboEventConsumer, "Word Source (Default: " + wordSource + " or last round preference)",
                wordSource));

        // Add text field for the user to enter preferred Wordle word.
        currentHeight += BREAK_HEIGHT + CONTENT_HEIGHT;
        windowPanel.add(Settings.textInit("Wordle Word or Hashtag", "", JTextField.LEFT, Font.PLAIN,
                WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 30, false,
                false));
        initWordField = Settings.textInit("", "", JTextField.LEFT, Font.PLAIN, WIDTH_MARGIN,
                currentHeight + BREAK_HEIGHT, CONTENT_WIDTH, BREAK_HEIGHT, 30, true,
                true);
        windowPanel.add(initWordField);
        currentHeight += BREAK_HEIGHT + CONTENT_HEIGHT / 3;
        windowPanel.add(Settings.textInit("Hint: Leave empty to guess a random word.", "",
                JTextField.LEFT, Font.PLAIN, WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 15,
                false, false));

        // Add error message field to display error message.
        currentHeight = WINDOW_HEIGHT - BREAK_HEIGHT * 2 - CONTENT_HEIGHT;
        errorMessageField = Settings.textInit("", "", JTextField.CENTER, Font.BOLD, WIDTH_MARGIN,
                currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 15, false, false);
        errorMessageField.setForeground(Color.RED);
        windowPanel.add(errorMessageField);
        currentHeight += BREAK_HEIGHT;
        JButton startButton = initButton("Start", WINDOW_WIDTH / 2 - CONTENT_WIDTH / 4, currentHeight,
                CONTENT_WIDTH / 2, CONTENT_HEIGHT, 70, event -> start());
        windowPanel.add(startButton);
    }

    /**
     * Returns an instance of current class, where only one copy of instance will exist.
     *
     * <p>
     * If no instance found, a new one will be generated and stored. Otherwise, the stored one will be return.
     *
     * @return an instance of current class.
     */
    public static Settings getInstance() {
        if (Settings.instance == null)
            Settings.instance = new Settings();
        return Settings.instance;
    }

    /**
     * Returns the word source selected by the user.
     *
     * @return a String describing the word source selected by the user.
     */
    public static String getWordSource() {
        return Settings.wordSource;
    }

    /**
     * Returns the initial word typed by the user.
     *
     * @return a String describing the initial word typed by the user.
     */
    public static String getInitWord() {
        return Settings.initWord;
    }

    /**
     * Returns current hashtag.
     *
     * @return a String describing current hashtag.
     */
    public static String getCurrentHashtag() {
        return Settings.currentHashtag;
    }

    /**
     * This method sets the window to the center and makes it change its visible status
     *
     * @param status a boolean describing the intended visible status of the window.
     */
    public void setVisibleStatus(Boolean status) {
        window.setLocationRelativeTo(null);
        window.setVisible(status);
    }

    /**
     * This static method returns a configured {@code JTextField}.
     *
     * @param content      a String describing the name of the text field.
     * @param fontName     a String describing the name of the font.
     * @param alignment    an int describing the alignment of the words of the text field.
     * @param fontStyle    an int describing the font style of the text field.
     * @param x            an int describing the new horizontal or {@code x}-coordinate of the text field.
     * @param y            an int describing the new vertical or {@code y}-coordinate of the text field.
     * @param width        an int describing the horizontal size of the text field.
     * @param height       an int describing the vertical size of the text field.
     * @param fontSize     an int describing the font size of the text field.
     * @param opaqueStatus a boolean describing the opaque status of the text field.
     * @param editable     a boolean describing the editable status of the text field.
     * @return a configured JTextField.
     */
    public static JTextField textInit(String content, String fontName, int alignment, int fontStyle, int x, int y,
                                      int width, int height, int fontSize, boolean opaqueStatus, boolean editable) {
        JTextField textField = new JTextField(content);
        textField.setHorizontalAlignment(alignment);
        textField.setBounds(x, y, width, height);
        textField.setEditable(editable);
        textField.setOpaque(opaqueStatus);
        textField.setBorder(null);
        textField.setFont(new Font(fontName, fontStyle, fontSize));
        return textField;
    }

    /**
     * This static method returns a configured {@code JButton}.
     *
     * @param content  a String describing the content displayed on the button.
     * @param x        an int describing the new horizontal or {@code x}-coordinate of the button.
     * @param y        an int describing the new vertical or {@code y}-coordinate of the button.
     * @param xSize    an int describing the horizontal size of the button.
     * @param ySize    an int describing the vertical size of the button.
     * @param fontSize an int describing the font size of the button.
     * @param event    an {@code ActionListener} that will be called if the button is pressed.
     * @return a configured JButton.
     */
    public static JButton initButton(String content, int x, int y, int xSize, int ySize, int fontSize,
                                     ActionListener event) {
        JButton button = new JButton(content);
        button.setBounds(x, y, xSize, ySize);
        button.setFont(new Font("Comic Sans MS", Font.PLAIN, fontSize));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(event);
        button.setFocusable(false);
        return button;
    }

    /**
     * This method returns a configured combo to the window.
     *
     * @param hint         a String describing the hint to add before each content.
     * @param contents     a String array describing contents to be displayed in the combo.
     * @param height       an int describing the new vertical or {@code y}-coordinate of the combo.
     * @param consumer     a {@code Consumer<ItemEvent>} that consumes events related to the combo.
     * @param toolTip      a String describing the tooltip displayed if the mouse is placed onto the combo.
     * @param selectedItem a String describing the default selected item of the combo.
     * @return a configured {@code JComboBox<String>}.
     */
    private JComboBox<String> initCombo(String hint, String[] contents, int height, Consumer<ItemEvent> consumer,
                                        String toolTip, String selectedItem) {
        contents = contents.clone();
        for (int i = 0; i < contents.length; i++)
            contents[i] = hint + contents[i];
        JComboBox<String> result = new JComboBox<>(contents);
        result.setBounds(WIDTH_MARGIN, height, CONTENT_WIDTH, CONTENT_HEIGHT / 3);
        result.setToolTipText(toolTip);
        result.setCursor(new Cursor(Cursor.HAND_CURSOR));
        result.addItemListener(consumer::accept);
        result.setSelectedItem(hint + selectedItem);
        return result;
    }

    /**
     * This method checks the word typed by the user and create a new {@code Game} instance to start the game if the
     * check is passed. Otherwise, this method will display error message in the <var>errorMessageField</var>
     */
    private void start() {
        // All internal letters are stored and processed in uppercase.
        String text = initWordField.getText().toUpperCase();
        // Hashtag handler
        if (text.length() > 0 && text.charAt(0) == '#') {
            String[] decodeResult = Settings.hashtagDecoder(text).split("\\$");
            if (decodeResult[0].length() != 0) {
                errorMessageField.setText(decodeResult[0]);
                return;
            }
            errorMessageField.setText("");
            this.setVisibleStatus(false);
            Settings.initWord = decodeResult[1];
            currentHashtag = text;
            Game.createInstance().playGame(Settings.wordSourceOptions[Integer.parseInt(decodeResult[2]) - 1],
                    decodeResult[1], currentHashtag);
        }
        // Not hashtag
        else if (text.length() == wordLength || text.length() == 0) {
            String checkResult = Service.getInstance().checkExistence(text, wordSource);
            if (checkResult.length() == 0) {
                if (text.length() == 0) {
                    text = Service.getInstance().generateRandomWord(wordLength, wordSource);
                    initWordField.setText(text);
                }
                if (!text.equals("Not Found")) {
                    errorMessageField.setText("");
                    this.setVisibleStatus(false);
                    Settings.initWord = text;
                    currentHashtag = Settings.hashtagEncoder(wordSource, text);
                    Game.createInstance().playGame(wordSource, text, currentHashtag);
                } else
                    errorMessageField.setText(text);
            } else
                errorMessageField.setText(checkResult);
        } else
            errorMessageField.setText("Error: The length of Wordle Word is too " +
                    (text.length() < wordLength ? "small" : "large") + "!");
    }

    /**
     * This static method encodes current settings and return the hashtag.
     *
     * <p>
     * The hashtag is generated from three parameters: <var>hashtagWordSource</var>, <var>hashtagWord</var>, and
     * <var>hashtagWordLength</var>(calculated from <var>hashtagWord</var>).
     *
     * <p>
     * The first step is to generate a base-29 integer decoded three parameters mentioned above, where the order from
     * the lower digit of the integer is <var>hashtagWordLength</var>, <var>hashtagWordSource</var>, and then
     * <var>hashtagWord</var>, whose alphabetic letters are converted to integer counting from 0 to 25 inclusion. For
     * example, when <var>hashtagWordLength=5</var>, <var>hashtagWordSource=3</var>, and <var>hashtagWord="APPLE"</var>,
     * the integer generated equals to (29^0)*5 + (29^1)*3 + (29^2)*0 + (29^3)*15 + (29^4)*15 + (29^5)*11 + (29^6)*4 =
     * 2615891065.
     *
     * <p>
     * The second step is to convert the number system to base-36 (26+10), representing by number and alphabet letter,
     * where number counts from 0 to 9 and alphabet letter counts from 10 to 35. For example, the sample shown above
     * will become #179FMGP.
     *
     * @param hashtagWordSource a String describing the word source selected.
     * @param hashtagWord       a String describing the Wordle word selected.
     * @return a String describing the decoded hashtag result.
     */
    static private String hashtagEncoder(String hashtagWordSource, String hashtagWord) {
//        System.out.println("Encoding:"+hashtagWordSource+" "+hashtagWord);
        final long hashtagLetterCount = 26 + 10;
        final long radix = 29;
        long integer = 0;
        for (int i = hashtagWord.length() - 1; i >= 0; i--) {
            integer *= radix;
            integer += hashtagWord.charAt(i) - 'A';
        }
        for (int i = 0; i < Settings.wordSourceOptions.length; i++)
            // Found word source in wordSourceOptions with index to be later decoded.
            if (Settings.wordSourceOptions[i].equals(hashtagWordSource)) {
                // Encode hashtag word source, which counts from 1.
                integer *= radix;
                integer += i + 1;
                // Encode hashtag word length.
                integer *= radix;
                integer += hashtagWord.length();
                // Convert integer to base-36 hashtag representation.
                StringBuilder reverseHashtag = new StringBuilder();
                do {
                    int currentDigit = (int) (integer % hashtagLetterCount);
                    reverseHashtag.append(currentDigit < 10 ? (char) (currentDigit + (int) '0') :
                            (char) (currentDigit - 10 + (int) 'A'));
                    integer /= hashtagLetterCount;
                } while (integer > 0);
                return "#" + reverseHashtag.reverse();
            }
        return "Hashtag Error: word source not found";
    }

    /**
     * This static method decodes hashtag and return the results.
     *
     * @param hashtag a String describing the hint to add before each content, maximum length (excluded '#') 12
     *                supported using current decoder base on {@code long} (possible longer support under using
     *                BigInteger but not necessary ).
     * @return a String describing the results, whose format is "errorMessage$word$difficulty", typed
     * "String$String$int", where the latter two will be not null when {@code errorMessage} is empty, representing
     * successfully decoded.
     * Note: difficulty counts from 1 to total word sources available.
     * Sample: error: "Invalid hashtag input$$", successfully decoded: "$apple$1".
     */
    static private String hashtagDecoder(String hashtag) {
        final long hashtagLetterCount = 26 + 10;
        final long radix = 29;
        long encoded = 0;
        if (hashtag.length() > 13)
            return "Invalid hashtag input: length too large$$";
        // Decode raw base-hashtagLetterCount String to long.
        for (int i = 1; i < hashtag.length(); i++) {
            char ch = hashtag.charAt(i);
            if (Character.isDigit(ch)) {
                encoded *= hashtagLetterCount;
                encoded += Character.digit(ch, 10);
            } else if (Character.isAlphabetic(ch)) {
                encoded *= hashtagLetterCount;
                encoded += ((int) ch) - ((int) 'A') + 10;
            } else // illegal letter
                return "Invalid hashtag input: illegal letter$$";
        }
        /* Retrieve details from decoded base-radix(29) integer. */
        // Retrieve word length.
        long hashtagWordLength = encoded % radix;
        encoded /= radix;
        // Retrieve word source.
        int hashtagWordSource = (int) (encoded % radix);
        if (!(0 < hashtagWordSource && hashtagWordSource <= Settings.wordSourceOptions.length))
            return "Invalid hashtag input: illegal word source option$$";
        String hashtagWordSourceStr = Settings.wordSourceOptions[hashtagWordSource - 1];
        encoded /= radix;
        // Retrieve Wordle word.
        StringBuilder hashtagWord = new StringBuilder();
        for (int i = 0; i < hashtagWordLength; i++) {
            long currentDigit = encoded % radix;
            encoded /= radix;
            if (0 <= currentDigit && currentDigit < 26)
                hashtagWord.append((char) (currentDigit + (int) 'A'));
            else
                return "Invalid hashtag input: illegal word letter$$";
        }
        // Check decoded result in Service.
        String hashtagCheckResult = Service.getInstance().checkExistence(hashtagWord.toString(), hashtagWordSourceStr);
        if (hashtagCheckResult.length() == 0) {
            return "$" + hashtagWord + "$" + hashtagWordSource;
        }
        return "Invalid hashtag input: " + hashtagCheckResult + "$$";
    }
}
