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
     * A static int holding the word length selected by the user,
     */
    private static int wordLength;

    /**
     * A static String holding the word source selected by the user,
     */
    private static String wordSource;

    /**
     * A static String holding the preferred initial word typed by the user,
     */
    private static String initWord;

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

        // Configure window settings.
        window = new JFrame("Welcome - Wordle");
        window.setLocationRelativeTo(null);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setBackground(Color.WHITE);
        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(Settings.textInit("Preferences", "Comic Sans MS", JTextField.CENTER, Font.BOLD,
                WIDTH_MARGIN, BREAK_HEIGHT, CONTENT_WIDTH, CONTENT_HEIGHT, 60, false,
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
        window.add(Settings.textInit("Word Length Preference", "", JTextField.LEFT, Font.PLAIN,
                WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 30, false,
                false));
        initCombo("Word Length: ", wordLengthOptions, currentHeight + BREAK_HEIGHT, comboEventConsumer,
                "Word Length (Default: " + wordLength + " or last round preference)",
                wordLength + "");

        currentHeight += BREAK_HEIGHT + CONTENT_HEIGHT;
        window.add(Settings.textInit("Word Source Preference", "", JTextField.LEFT, Font.PLAIN,
                WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 30, false,
                false));
        initCombo("Word Source: ", wordSourceOptions, currentHeight + BREAK_HEIGHT, comboEventConsumer,
                "Word Source (Default: " + wordSource + " or last round preference)", wordSource);

        // Add text field for the user to enter preferred Wordle word.
        currentHeight += BREAK_HEIGHT + CONTENT_HEIGHT;
        window.add(Settings.textInit("Wordle Word Preference", "", JTextField.LEFT, Font.PLAIN,
                WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 30, false,
                false));
        initWordField = Settings.textInit("", "", JTextField.LEFT, Font.PLAIN, WIDTH_MARGIN,
                currentHeight + BREAK_HEIGHT, CONTENT_WIDTH, BREAK_HEIGHT, 30, true,
                true);
        window.add(initWordField);
        currentHeight += BREAK_HEIGHT + CONTENT_HEIGHT / 3;
        window.add(Settings.textInit("Hint: Leave empty to guess a random word", "", JTextField.LEFT,
                Font.PLAIN, WIDTH_MARGIN, currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 15, false,
                false));

        // Add error message field to display error message.
        currentHeight = WINDOW_HEIGHT - BREAK_HEIGHT * 2 - CONTENT_HEIGHT;
        errorMessageField = Settings.textInit("", "", JTextField.CENTER, Font.BOLD, WIDTH_MARGIN,
                currentHeight, CONTENT_WIDTH, BREAK_HEIGHT, 15, false, false);
        errorMessageField.setForeground(Color.RED);
        window.add(errorMessageField);
        currentHeight += BREAK_HEIGHT;
        JButton startButton = initButton("Start", WINDOW_WIDTH / 2 - CONTENT_WIDTH / 4, currentHeight,
                CONTENT_WIDTH / 2, CONTENT_HEIGHT, 70, event -> start());
        window.add(startButton);
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
     * This method adds a configured combo to the window.
     *
     * @param hint         a String describing the hint to add before each content.
     * @param contents     a String array describing contents to be displayed in the combo.
     * @param height       an int describing the new vertical or {@code y}-coordinate of the combo.
     * @param consumer     a {@code Consumer<ItemEvent>} that consumes events related to the combo.
     * @param toolTip      a String describing the tooltip displayed if the mouse is placed onto the combo.
     * @param selectedItem a String describing the default selected item of the combo.
     */
    private void initCombo(String hint, String[] contents, int height, Consumer<ItemEvent> consumer, String toolTip,
                           String selectedItem) {
        contents = contents.clone();
        for (int i = 0; i < contents.length; i++)
            contents[i] = hint + contents[i];
        JComboBox<String> result = new JComboBox<>(contents);
        result.setBounds(WIDTH_MARGIN, height, CONTENT_WIDTH, CONTENT_HEIGHT / 3);
        result.setToolTipText(toolTip);
        result.setCursor(new Cursor(Cursor.HAND_CURSOR));
        result.addItemListener(consumer::accept);
        result.setSelectedItem(hint + selectedItem);
        window.add(result);
    }

    /**
     * This method checks the word typed by the user and create a new {@code Game} instance to start the game if the
     * check is passed. Otherwise, this method will display error message in the <var>errorMessageField</var>
     */
    private void start() {
        String text = initWordField.getText();
        if (text.length() == wordLength || text.length() == 0) {
            // All internal letters are stored and processed in uppercase.
            text = text.toUpperCase();
            String checkResult = Service.getInstance().checkExistence(text, wordSource);
            if (checkResult.length() == 0) {
                if (text.length() == 0)
                    text = Service.getInstance().generateRandomWord(wordLength, wordSource);
                if (!text.equals("Not Found")) {
                    System.out.println("word to be guessed:" + text);
                    errorMessageField.setText("");
                    this.setVisibleStatus(false);
                    Settings.initWord = text;
                    Game.createInstance().playGame(wordSource, text);
                } else
                    errorMessageField.setText(text);
            } else
                errorMessageField.setText(checkResult);
        } else
            errorMessageField.setText("Error: The length of Wordle Word is too " +
                    (text.length() < wordLength ? "small" : "large") + "!");
    }
}
