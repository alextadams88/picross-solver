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
	

	