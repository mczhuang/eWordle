# eWordle<a href="https://github.com/mczhuang/eWordle"> <img src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png" width="3%"></a>
An extended version of [Wordle](http://nytimes.com/games/wordle) (Mini Project of EBU4201 - INTRODUCTORY JAVA PROGRAMMING).
## Quick Start

### How to Play

+ Guess the Wordle word in tries just one more than the word length you selected.
+ Each guess must be a valid word with the length you selected.
+ For each letter you confirm to enter, It will show:
    + Green if it is in the word and in the correct spot.
    + Yellow if it is in the word but in the wrong spot.
    + Grey if it is not in the word in any spot.

### Complie & Run

Please make sure you have configured Java environment correctly.

```shell
cd ./src
javac eWordle.java && java eWordle
```
### Preference

+ #### Word Source or Difficulty

    > There are 6 word sources or difficulties options offered: CET-4, CET-6, TOEFL, GRE, Oxford Dictionary, and All.

+ #### Word Length

    > There are 4 options about the word length you later guess available: 5, 6, 7, and 8.

+ #### Wordle Word 

    > If you would like to prepare this game for others, you can configure the word to be guessed later here. Otherwise, just leave it empty to make system to generate a random word for you to guess.
    > 
    > Hint: the word selected or generated will be shown in the command line after the game starts.

### Play

Choose an ideal configuration as shown above and click Start to enjoy eWordle!

## Future Features

A hashtag will be supported in the upcoming future, where you can enjoy competing with others in the same configuration and the same Wordle word.
