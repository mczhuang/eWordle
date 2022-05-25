# Tests for eWordle<a href="https://github.com/mczhuang/eWordle"> <img src="https://github.githubassets.com/images/modules/site/icons/footer/github-mark.svg" width="3%"></a>

As most bugs happen in the boundary cases, most test cases are picked intentionally in the border.

Apart from the boundary cases, the rest test cases include default preferences and random configurations.

## Unit Tests 

All unit tests shown below are screenshotted, unless specified, saved in respective folders.

+ Preferences
  + Word: Shorter Input
  + Word: Longer Input
  + Word: Illegal Input
  + Word: Not Exist
  + Hashtag: Exceeded Hashtag
  + Hashtag: Invalid Hashtag
+ Game (Test Using Default Word Length 5)
  + Delete Letter in Empty Word
  + Append Letter to Full Word
  + Confirm Incomplete Word
  + Illegal Input
  + Shorter Helper Input
  + Longer Helper Input
  + Invalid Characters in Helper Input
  + Nested Round Brackets Helper Input
  + Nested Square Brackets Helper Input
  + Mixed Brackets Helper Input
  + Multiple Common Letters inside Round Brackets Helper Input
  + Null Input inside Round Brackets Helper Input
  + Asterisks inside Square Brackets Helper Input
  + Simple Form Helper Input
  + No Asterisk Correct Helper Input
  + No Asterisk Error Helper Input
  + Correct Filter Helper Input
+ Results
  + Share Success Results with Helper
  + Share Failed Results with Helper
  + Share Success Results without Helper
  + Share Failed Results without Helper
  + Restart with Word Generated Randomly (Passed, No Photo)
  + Restart with Word from Hashtag (Passed, No Photo)
  + Restart with Word from Input (Passed, No Photo)

## Integration Tests

All integration tests shown below are screenshotted, saved in respective folders.

Each test case as shown below contains:

+ Screenshots included

  + Success Result Test: Result in success in the first try. (Photo 1)
  + Failed Result Test: Result in failed. (Photo 2)

+ Checks only

  + Helper Test: Filter out the correct word using helper.
  + Color Test: Incorrect word input that make 3 colors visible.

### Boundary Cases

+ Lower bound case of Word Length: 5
  + Lower bound case of Word Source: CET-4
    + Word: Random
    + Word: ABOUT (lower bound)
    + Word: from hashtag containing ZEBRA (upper bound)
  + Upper bound case of Word Source: All
    + Word: Random
    + Word: ABOUT (lower bound)
    + Word: from hashtag containing ZYMIN (upper bound)
+ Upper bound case of Word Length: 8
  + Lower bound case of Word Source: CET-4
    + Word: Random
    + Word: ABNORMAL (lower bound)
    + Word: from hashtag containing YOUTHFUL (upper bound)
  + Upper bound case of Word Source: All
    + Word: Random
    + Word: ABNORMAL (lower bound)
    + Word: from hashtag containing ZYZZYVAS (upper bound)

### Default Preferences

+ Word Length: 5
  + Word Source: All
    + Word: Random

+ Word Length: 5
  + Word Source: All
    + Word: from hashtag containing GUESS

### Random Preferences

+ Word Length: 7
  + Word Source: Oxford Dictionary
    + Word: ACCOUNT
    + Word: ABANDON
    + Word: ZYMURGY
