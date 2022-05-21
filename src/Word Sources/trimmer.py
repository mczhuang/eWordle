CONST_MINIMUM_LENGTH = 5
CONST_MAXIMUM_LENGTH = 8
output_location = './Trimmed.csv'
locations = ['./CET-4.csv', './CET-6.csv', './TOEFL.csv', './GRE.csv', './Oxford Dictionary.csv', './All.csv']
difficulty = dict()
for index, loc in enumerate(locations):
    for line in open(loc):
        word = line.replace('\n', '').replace(' ','').lower()
        if word not in difficulty and word.isalpha() and CONST_MINIMUM_LENGTH <= len(word) <= CONST_MAXIMUM_LENGTH:
            difficulty[word] = index + 1
with open(output_location, "w") as f:
    for it in sorted(difficulty.items()):
        f.write(str(it[0]) + ',' + str(it[1]) + '\n')

