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

/**
 * The {@code eWordle} class stores the default settings and launch the game.
 *
 * <p>
 * The classes {@code Settings}, {@code Results} and {@code Service} should be access only through static
 * {@code Class.getInstance()} method to guarantee only one instance will be generated all the time so that the
 * latest preference will be reused and memory usage will be minimized.
 *
 * <p>
 * Similarly, the {@code Game} class should be access only through static {@link Game#createInstance()} every time
 * the caller start a new game. After {@link Game#createInstance()}, the older, if any, instance will be eligible
 * for garbage collection.
 *
 * @author Mingchun Zhuang
 * @version 1.0
 */
public class eWordle {
    /**
     * A constant ordered String array that lists word length options.
     */
    private static final String[] wordLengths = new String[]{"5", "6", "7", "8"};
    /**
     * A constant String array that lists word source options ordered by difficulty.
     */
    private static final String[] wordSources = new String[]{"CET-4", "CET-6", "TOEFL", "GRE", "Oxford Dictionary",
            "All"};

    /**
     * This method launches the <var>setting</var> window with default setting and initialize <var>service</var>.
     *
     * @param args a default String array which is not used by this program.
     */
    public static void main(String[] args) {
        Settings.getInstance().configSettings(5, "All", wordLengths, wordSources);
        String initResult = Service.getInstance().initService(wordSources, wordLengths);
        if (initResult.length() > 0) {
            System.out.println("Error while initialization:" + initResult);
            return;
        }
        Settings.getInstance().setVisibleStatus(true);
    }
}
