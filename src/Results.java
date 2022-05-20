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

/**
 * The {@code Results} class manages a result window that enables user to see the result and choose whether to restart
 * or go back to settings.
 *
 * @author Mingchun Zhuang
 * @version 1.0
 */
public class Results {
    /**
     * A static variable storing the only one instance instantiated.
     */
    private static Results instance;

    /**
     * A static constant holding the width of current window.
     */
    private static final int WINDOW_WIDTH = 600;

    /**
     * A static constant holding the height of current window.
     */
    private static final int WINDOW_HEIGHT = 650;

    /**
     * A static constant holding the height of each content box.
     */
    private static final int CONTENT_HEIGHT = 100;

    /**
     * A static constant holding the size of the interval of contents of current window.
     */
    private static final int CONTENT_MARGIN = 50;

    /**
     * A static constant holding the width of each content box.
     */
    private static final int CONTENT_WIDTH = WINDOW_WIDTH - CONTENT_MARGIN * 2;

    /**
     * A static {@code JFrame} holding the instance of current window.
     */
    private final JFrame window;

    /**
     * A static {@code JTextField} holding the instance of text field that displays success or fail status.
     */
    private final JTextField resultBoard;

    /**
     * A static {@code JTextField} holding the instance of text field that displays the word that the user tried to
     * guess.
     */
    private final JTextField wordBoard;

    /**
     * A static {@code JTextField} holding the instance of text field that displays the tries that the user used to
     * guess.
     */
    private final JTextField triesBoard;

    /**
     * The only constructor for class {@code Results}.
     *
     * <p>
     * This constructor will initiate the window and complete the configuration. The window is hidden and waits for
     * calling through {@link Results#showResults(String, int, boolean)}.
     */
    public Results() {
        Results.instance = this;

        // Configure window settings.
        window = new JFrame("Results");
        window.setLocationRelativeTo(null);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setBackground(Color.WHITE);
        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add result board to the window.
        int currentHeight = CONTENT_MARGIN;
        resultBoard = Settings.textInit("", "Comic Sans MS", JTextField.CENTER, Font.BOLD,
                CONTENT_MARGIN, currentHeight, CONTENT_WIDTH, CONTENT_HEIGHT, 80, false,
                false);
        window.add(resultBoard);

        // Add the hint board that displays "Guessing" and word board to the window.
        currentHeight += CONTENT_HEIGHT + CONTENT_MARGIN;
        JTextField hintBoard = Settings.textInit("Guessing", "Comic Sans MS", JTextField.CENTER,
                Font.PLAIN, CONTENT_MARGIN, currentHeight - CONTENT_MARGIN / 2, CONTENT_WIDTH, CONTENT_MARGIN,
                30, false, false);
        window.add(hintBoard);
        wordBoard = Settings.textInit("", "", JTextField.CENTER, Font.PLAIN, CONTENT_MARGIN,
                currentHeight, CONTENT_WIDTH, CONTENT_HEIGHT, 60, false, false);
        window.add(wordBoard);

        // Add the tries board to the window.
        currentHeight += CONTENT_HEIGHT + CONTENT_MARGIN;
        triesBoard = Settings.textInit("", "Comic Sans MS", JTextField.CENTER, Font.PLAIN,
                CONTENT_MARGIN, currentHeight, CONTENT_WIDTH, CONTENT_HEIGHT, 60, false,
                false);
        window.add(triesBoard);
        currentHeight += CONTENT_HEIGHT + CONTENT_MARGIN;

        // Add two buttons to the window with event handlers respectively.
        JButton toSettings = Settings.initButton("Setting", CONTENT_MARGIN, currentHeight,
                (CONTENT_WIDTH - CONTENT_MARGIN) / 2, CONTENT_HEIGHT, 50, event -> {
            Settings.getInstance().setVisibleStatus(true);
            window.setVisible(false);
        });
        toSettings.setToolTipText("Go back to Preferences page");
        window.add(toSettings);
        JButton toRestart = Settings.initButton("Restart",
                CONTENT_MARGIN * 2 + (CONTENT_WIDTH - CONTENT_MARGIN) / 2, currentHeight,
                (CONTENT_WIDTH - CONTENT_MARGIN) / 2, CONTENT_HEIGHT, 50, event -> {
            Game.createInstance().playGame(Settings.getWordSource(), Settings.getInitWord());
            window.setVisible(false);
        });
        toRestart.setToolTipText("Use current preferences with the same word");
        window.add(toRestart);
    }

    /**
     * Returns an instance of current class, where only one copy of instance will exist.
     *
     * <p>
     * If no instance found, a new one will be generated and stored. Otherwise, the stored one will be return.
     *
     * @return an instance of current class.
     */
    public static Results getInstance() {
        if (Results.instance == null)
            Results.instance = new Results();
        return Results.instance;
    }

    /**
     * This static method shows result window with given parameters.
     *
     * @param initWord  a String that the user tried to guess.
     * @param tries     an int describing the number of tries used.
     * @param isSuccess a boolean describing the final status of the game.
     */
    public void showResults(String initWord, int tries, boolean isSuccess) {
        window.setLocationRelativeTo(null);
        resultBoard.setText(isSuccess ? "Success" : "Failed");
        Game.setColor(resultBoard, isSuccess ? new Color(121, 167, 107) : new Color(121, 124, 126),
                Color.white);
        wordBoard.setText(initWord);
        triesBoard.setText("Tries Used:" + tries);
        window.setAlwaysOnTop(true);
        window.setVisible(true);
    }
}
