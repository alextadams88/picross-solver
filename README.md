step 1: take input
	input form is of a .txt file
	first line contains number of rows (nr) and number of columns (nc)
	next nr lines contain the clues for that row 
		format is space deliniated set of integers
	next nc lines contain the clues for that column
		same format

data structure:
	Board:
		List<Row>
		List<Column>
	Row:
		List<Cell>
		List<Int> (clues)
	Column:
		List<Cell>
		List<Int> (clues)
	Cell:
		Status
	Status (Enum):
		Unknown
		Filled
		Empty

step 2: use input to build board

step 3: output board
step 4: iterate through all rows and columns once
	looking at the clues and the status to see if any can be determined
	IMPORTANT: all updates in this step shall be 'transitive'
	meaning that, for example, if i discover something on row 5, then when we check column 5, we cannot use
	information from the previous discovery during this step
	
	after the step is complete, then all updates are 'saved'
	and made permanent
	
	the reason for this is so that the user can iterate through the board and watch it be filled step-by-step

repeat steps 3 and 4 until solved
	

updates clues algorithm:
1. do a deep copy of the existing vector to a new vector
2. attempt every possible iteration of applying the clues to the existing vector
	2a. try applying all clues in a naive way - i.e from left to right, while not using any cells marked as EMPTY
	2b. if the result satisfies the conditions of the clues, it is considered a Possibility - save this possibility
	2b1. do another deep copy of the existing vector
	2c. try a different iteration of applying the clues, ex. moving the leftmost clue one unit to the right
	2d. continue this until all possibilities are exhausted
	2e. you now have a list of possibilities
	2f. for each unknown cell, iterate through each possibility - if that cell is either filled or empty in EVERY possibility, then mark it as such in the original vector


	
if clues == 0, fill all ? with x
if clues > 0, loop: apply leftmost clue in leftmost pos, then recursive call with clues - 1 and deep copy of vector
					then apply leftmost clue in leftmost-1 pos, then recursive call
					then apply leftmost clue in leftmost-2 pos, then recursive call
					continue until remaining spaces = sum(remaing clues) + count(remaining clues) - 1;