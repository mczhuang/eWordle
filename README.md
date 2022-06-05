## eWordle<a href="https://github.com/mczhuang/eWordle"> <img src="https://github.githubassets.com/images/modules/site/icons/footer/github-mark.svg" width="3%"></a>

An extended version of [Wordle](http://nytimes.com/games/wordle) (Mini Project of EBU4201) with full-featured helper.

### How to Play

+ Guess the Wordle word in tries just one more than the word length you selected.
+ Each guess must be a valid word with the length you selected.
+ For each letter you confirm to enter, it will show:
    + Green if it is in the word and in the correct spot.
    + Yellow if it is in the word but in the wrong spot.
    + Grey if it is not in the word in any spot.

### Complie & Run


```shell
cd ./src
javac eWordle.java && java eWordle
```
### Preferences

+ #### Word Source or Difficulty

    > There are 6 word sources offered: CET-4, CET-6, TOEFL, GRE, Oxford Dictionary, and All.

+ #### Word Length

    > There are 4 options about the word length you later guess available: 5, 6, 7, and 8.

+ #### Wordle Word or Hashtag

    > Enter the word to be guessed or leave empty to guess a random word.
    >
    > To compete with others in a cloned environment, share your hashtag shown after game starts.
  

### Helper Guide (Launch via square "?" icon in game) 

Replace *s with known letters. Optionally add **all** filling letters inside "()" and omitted letters inside "[]".

```
G*ES*           --> Places marked * are unknown.
*****(ESS*)[AB] --> Places marked * are unknown but contain at least 1E2S and no A or B.
G*E**(SU)       --> Places marked * ONLY contain 1S1U (None Matched, Occurrence matter).
```


 
