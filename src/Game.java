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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The {@code Game} class manages a game window that enables user to play the game and shows the result window after
 * the game ends.
 *
 * <p>
 * If the user click the helper icon, the helper window will be shown. The helper window is managed by current
 * {@code Game} instance and will be disposed when the game window closes (to show the result window).
 *
 * <p>
 * Multiple instances of this class may be instantiated for various settings of preferred word length of the word to be
 * guessed. But only one instance will exist or be held by {@link Game#instance} at any time.
 *
 * @author Mingchun Zhuang
 * @version 1.0
 */
public class Game {
    /**
     * A static variable storing the most recent instance instantiated, where older ones will be eligible for garbage
     * collection.
     */
    private static Game instance;

    /**
     * A static constant holding the width of current window.
     */
    private static final int WINDOW_WIDTH = 600;

    /**
     * A static constant holding the height of current window.
     */
    private static final int WINDOW_HEIGHT = 850;

    /**
     * A static constant holding the width of each content box.
     */
    private static final int CONTENT_WIDTH = 500;

    /**
     * A static constant holding the height of each content box.
     */
    private static final int CONTENT_HEIGHT = 100;

    /**
     * A static constant holding the interval size of contents of current window.
     */
    private static final int CONTENT_MARGIN = 50;

    /**
     * A static constant holding the size ratio of cell size to cell interval size of current window.
     */
    private static final int SIZE_RATIO = 8;

    /**
     * A boolean holding the status that whether the user opened the helper window.
     */
    private boolean isOpenedHelper = false;

    /**
     * A {@code JFrame} holding the instance of current window.
     */
    private JFrame window;

    /**
     * A {@code ArrayList} holding the instances of {@code JTextField} that displays guessed letters typed by the user.
     */
    private ArrayList<JTextField> fields;

    /**
     * A {@code JTextField} holding the instance of {@code JTextField} that displays hint messages.
     */
    private JTextField messageBoard;

    /**
     * A String holding the word in the current line.
     */
    private String currentWord;

    /**
     * An int holding current line number that counts from zero.
     */
    private int currentLine;

    /**
     * An {@code ArrayList} holding score of each confirmed input, where 0 is for grey, 1 is for yellow, 2 is for green.
     */
    private ArrayList<Integer> scoreByOrder;

    /**
     * A {@code JFrame} holding the instance of helper window.
     */
    private JFrame helperWindow;

    /**
     * A {@code JTextField} holding the instance of {@code JTextField} that displays helper output.
     */
    private JTextArea helperOutput;

    /**
     * This method launches the game window with settings given.
     *
     * @param wordSource a String describing the specific source type, included in <var>wordSourceOption</var>.
     * @param initWord   a String holding the word to be guessed.
     * @param hashtag    a String holding the hashtag of this game.
     */
    public void playGame(String wordSource, String initWord, String hashtag) {
        System.out.println("playing Game from word source " + wordSource + " with init word " + initWord + " " +
                hashtag);
        // Initialize related variables.
        int wordLength = initWord.length();
        currentLine = 0;
        currentWord = "";
        fields = new ArrayList<>();
        scoreByOrder = new ArrayList<>();

        // Configure window.
        window = new JFrame("eWordle");
        window.setFocusable(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel windowPanel = new JPanel();
        windowPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        window.add(windowPanel);
        window.pack();
        windowPanel.setFocusable(false);
        windowPanel.setFocusTraversalKeysEnabled(false);
        windowPanel.setBackground(new Color(238, 238, 238));
        windowPanel.setLayout(null);

        //Add hashtag board to the current window panel.
        JTextField hashtagBoard = Settings.textInit("Hashtag: " + hashtag, "Comic Sans MS",
                JTextField.CENTER, Font.BOLD, CONTENT_MARGIN, 0, CONTENT_WIDTH, CONTENT_MARGIN, 15,
                false, false);
        windowPanel.add(hashtagBoard);

        //Add hashtag board to the current window panel.
        JTextField wordSourceBoard = Settings.textInit("Current Word Source: " + wordSource,
                "Comic Sans MS", JTextField.CENTER, Font.BOLD, CONTENT_MARGIN, CONTENT_MARGIN / 2,
                CONTENT_WIDTH, CONTENT_MARGIN, 15, false, false);
        wordSourceBoard.setFocusable(false);
        windowPanel.add(wordSourceBoard);

        // Add message board to the window panel.
        messageBoard = Settings.textInit("", "Comic Sans MS", JTextField.CENTER, Font.BOLD,
                CONTENT_MARGIN, CONTENT_MARGIN, CONTENT_WIDTH, CONTENT_HEIGHT, 20, false,
                false);
        messageBoard.setForeground(Color.RED);
        messageBoard.setFocusable(false);
        windowPanel.add(messageBoard);

        // Add text fields that display letter typed by the user. The number of lines of text fields is wordLength+1
        final double smallMarginSize = 1.0 * (WINDOW_WIDTH - CONTENT_MARGIN * 2) /
                ((SIZE_RATIO + 1) * wordLength - 1);
        final double blockSize = smallMarginSize * SIZE_RATIO;
        for (int row = 0; row <= wordLength; row++)
            for (int column = 0; column < wordLength; column++) {
                int x = (int) (CONTENT_MARGIN + column * smallMarginSize * (SIZE_RATIO + 1));
                int y = (int) (CONTENT_MARGIN * 2 + CONTENT_HEIGHT + row * smallMarginSize * (SIZE_RATIO + 1));
                JTextField field = Settings.textInit("", "", JTextField.CENTER, Font.BOLD, x, y,
                        (int) blockSize, (int) blockSize, 30, true, false);
                field.setBackground(Color.WHITE);
                field.setFocusable(false);
                fields.add(field);
                windowPanel.add(field);
            }

        // Add helper icon.
        JButton helper = Settings.initButton("?", WINDOW_WIDTH - CONTENT_MARGIN,
                WINDOW_HEIGHT - CONTENT_MARGIN, CONTENT_MARGIN, CONTENT_MARGIN, 25,
                event -> createHelperWindow());
        helper.setToolTipText("Launch Helper (a \"*\" mark will be displayed in the result)");
        windowPanel.add(helper);

        window.addKeyListener(newKeyboardListener(initWord, wordSource));
        hashtagBoard.addKeyListener(newKeyboardListener(initWord, wordSource));

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
     * Returns a new instance of current class, where the older copy of this class, if exists, will be eligible for
     * garbage collection.
     *
     * @return a new instance of current class.
     */
    public static Game createInstance() {
        Game.instance = new Game();
        return Game.instance;
    }

    /**
     * This static method modifies foreground and background colors of given {@code JTextField} respectively.
     *
     * @param field      a {@code JTextField} to be modified.
     * @param foreground a {@code Color} holding the color of the preferred foreground.
     * @param background a {@code Color} holding the color of the preferred background.
     */
    public static void setColor(JTextField field, Color foreground, Color background) {
        field.setForeground(foreground);
        field.setBackground(background);
    }

    /**
     * This method returns a new keyboard listener.
     *
     * @param wordSource a String describing the specific source type, included in <var>wordSourceOption</var>.
     * @param initWord   a String holding the word to be guessed.
     * @return a {@code KeyAdapter} processing keyboard inputs.
     */
    private KeyAdapter newKeyboardListener(String initWord, String wordSource) {
        int wordLength = initWord.length();
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                messageBoard.setText("");
                // All possible letters will be converted to uppercase.
                char c = Character.toUpperCase(e.getKeyChar());
                // Typed enter.
                if (c == '\n') {
                    if (currentWord.length() == wordLength) {
                        // Word guessed correct.
                        if (currentWord.equals(initWord)) {
                            for (int i = 0; i < initWord.length(); i++)
                                scoreByOrder.add(2);
                            closeHelperWindow();
                            Results.getInstance().showResults(initWord, currentLine + 1, true,
                                    scoreByOrder, isOpenedHelper);
                            instance = null;
                            window.dispose();
                        }
                        // Word guessed exists in word source of current difficulty level but incorrect.
                        else if (Service.getInstance().checkExistence(currentWord, wordSource).length() == 0) {
                            HashSet<Character> charRemainIncorrect = new HashSet<>();
                            for (int i = 0; i < wordLength; i++)
                                if (currentWord.charAt(i) == initWord.charAt(i))
                                    setColor(fields.get(currentLine * wordLength + i), Color.white,
                                            new Color(121, 167, 107));
                                else
                                    charRemainIncorrect.add(initWord.charAt(i));
                            for (int i = 0; i < wordLength; i++)
                                if (currentWord.charAt(i) != initWord.charAt(i)) {
                                    if (charRemainIncorrect.contains(currentWord.charAt(i))) {
                                        scoreByOrder.add(1);
                                        setColor(fields.get(currentLine * wordLength + i), Color.white,
                                                new Color(198, 180, 102));
                                    } else {
                                        scoreByOrder.add(0);
                                        setColor(fields.get(currentLine * wordLength + i), Color.white,
                                                new Color(121, 124, 126));
                                    }
                                } else
                                    scoreByOrder.add(2);
                            currentWord = "";
                            // Maximum guess tries reached.
                            if (++currentLine > wordLength) {
                                closeHelperWindow();
                                Results.getInstance().showResults(initWord, currentLine, false, scoreByOrder,
                                        isOpenedHelper);
                                window.dispose();
                            }
                        } else
                            messageBoard.setText("Not in word list");
                    } else
                        messageBoard.setText("Not enough length");
                }
                // Typed letters.
                else if ('A' <= c && c <= 'Z') {
                    if (currentWord.length() < wordLength) {
                        JTextField field = fields.get(currentLine * wordLength + currentWord.length());
                        setColor(field, Color.black, Color.white);
                        field.setText("" + c);
                        currentWord += c;
                    } else
                        messageBoard.setText("Time to click enter to confirm");
                }
                // Typed backspace.
                else if (c == '\b') {
                    if (currentWord.length() > 0) {
                        JTextField field = fields.get(currentLine * wordLength + currentWord.length() - 1);
                        field.setText("");
                        setColor(field, Color.black, Color.white);
                        currentWord = currentWord.substring(0, currentWord.length() - 1);
                    } else
                        messageBoard.setText("No more letters to delete");
                }
                // Illegal input.
                else
                    messageBoard.setText("Only alphabetic letters will be accepted");
            }
        };
    }

    /**
     * This method closes the helper window if exists.
     */
    private void closeHelperWindow() {
        if (helperWindow != null)
            helperWindow.dispose();
    }

    /**
     * This method creates a new helper window.
     */
    private void createHelperWindow() {
        // Return if already opened one.
        if (helperWindow != null)
            return;
        isOpenedHelper = true;
        // Configure current helper window.
        final int helperWindowWidth = 600;
        final int helperWindowHeight = 800;
        helperWindow = new JFrame("Helper");
        JPanel helperWindowPanel = new JPanel();
        helperWindowPanel.setPreferredSize(new Dimension(helperWindowWidth, helperWindowHeight));
        helperWindow.setFocusable(true);
        helperWindowPanel.setFocusable(false);
        helperWindowPanel.setFocusTraversalKeysEnabled(false);
        helperWindowPanel.setBackground(new Color(238, 238, 238));
        helperWindowPanel.setLayout(null);
        helperWindow.setResizable(false);
        helperWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                helperWindow = null;
            }
        });
        helperWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helperOutput = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(helperOutput);

        // Add word source board to the helper window.
        int currentHelperHeight = 0;
        JTextField wordSourceBoard = Settings.textInit("Searching Word Source: " + Settings.getWordSource() +
                        ", Word Length: " + Settings.getInitWord().length(), "Comic Sans MS",
                JTextField.CENTER, Font.PLAIN, CONTENT_MARGIN, currentHelperHeight, CONTENT_WIDTH, CONTENT_MARGIN,
                15, false, false);
        wordSourceBoard.setFocusable(false);
        helperWindowPanel.add(wordSourceBoard);

        // Add input board to the helper window.
        currentHelperHeight += CONTENT_MARGIN;
        JTextField inputBoard = Settings.textInit("", "", JTextField.LEFT, Font.PLAIN, CONTENT_MARGIN,
                currentHelperHeight, CONTENT_WIDTH, CONTENT_MARGIN, 20, true, true);
        helperWindowPanel.add(inputBoard);

        // Add search button.
        currentHelperHeight += CONTENT_MARGIN + CONTENT_MARGIN;
        JButton helperButton = Settings.initButton("Search", CONTENT_MARGIN,
                currentHelperHeight, CONTENT_WIDTH, CONTENT_MARGIN, 20,
                event -> {
                    // Handle search.
                    String[] response = Service.getInstance().validateHelperInput(inputBoard.getText()).
                            split("\\$");
                    if (response[0].length() == 0)
                        helperOutput.setText(response[1]);
                    else
                        helperOutput.setText(response[0]);
                    helperOutput.setCaretPosition(0);
                });
        helperButton.setToolTipText(
                "Search candidates in current word source. GUESS Sample: *****(ESS*), G*E**(SU), *****(ESS*)[AB]");
        helperWindowPanel.add(helperButton);

        // Add helper output text field.
        currentHelperHeight += CONTENT_MARGIN * 2;
        final int helperOutputHeight =
                helperWindowHeight - CONTENT_MARGIN - currentHelperHeight;
        helperOutput.setEditable(false);
        helperOutput.setOpaque(true);
        scrollPane.setBorder(null);
        scrollPane.setBounds(CONTENT_MARGIN, currentHelperHeight, CONTENT_WIDTH, helperOutputHeight);
        helperWindowPanel.add(scrollPane);

        helperWindow.add(helperWindowPanel);
        helperWindow.pack();
        helperWindow.setVisible(true);
    }
}
