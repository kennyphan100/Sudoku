public class Puzzle {

	// Attributes
	protected int[][] solvedBoard; // board that is being solved
	protected String [][] board; // board used to hold the digits
	protected boolean [][] mutable; // board used to determine if a slot is mutable
	private final int ROWS;
	private final int COLUMNS;
	private final int BOXWIDTH;
	private final int BOXHEIGHT;
	private final String [] VALIDVALUES;
	public static final int EMPTY = 0;
	
	// Parameterized constructor
	public Puzzle(int rows, int columns, int boxWidth, int boxHeight, String [] validValues) {
		this.ROWS = rows;
		this.COLUMNS = columns;
		this.BOXWIDTH = boxWidth;
		this.BOXHEIGHT = boxHeight;
		this.VALIDVALUES = validValues;
		this.board = new String[ROWS][COLUMNS];
		this.solvedBoard = new int[ROWS][COLUMNS];
		this.mutable = new boolean[ROWS][COLUMNS];
		initializeBoard();
		initializeMutableSlots();
	}
	
	 // Copy constructor
	public Puzzle(Puzzle puzzle) {
		this.ROWS = puzzle.ROWS;
		this.COLUMNS = puzzle.COLUMNS;
		this.BOXWIDTH = puzzle.BOXWIDTH;
		this.BOXHEIGHT = puzzle.BOXHEIGHT;
		this.VALIDVALUES = puzzle.VALIDVALUES;
		this.board = new String[ROWS][COLUMNS];
		for(int r = 0; r < ROWS; r++) {
			for(int c = 0; c < COLUMNS; c++) {
				board[r][c] = puzzle.board[r][c];
			}
		}
		this.mutable = new boolean[ROWS][COLUMNS];
		for(int r = 0; r < ROWS;r++) {
			for(int c = 0;c < COLUMNS;c++) {
				this.mutable[r][c] = puzzle.mutable[r][c];
			}
		}
		this.solvedBoard = new int[ROWS][COLUMNS];
		for(int r = 0; r < ROWS; r++) {
			for(int c = 0; c < COLUMNS; c++) {
				solvedBoard[r][c] = puzzle.solvedBoard[r][c];
			}
		}
	}
	
	// Getters & Setters
	public int getNumRows() {
		return this.ROWS;
	}
	
	public int getNumColumns() {
		return this.COLUMNS;
	}
	
	public int getBoxWidth() {
		return this.BOXWIDTH;
	}
	
	public int getBoxHeight() {
		return this.BOXHEIGHT;
	}
	
	public String [] getValidValues() {
		return this.VALIDVALUES;
	}
	
	// Place a digit on the board
	public void makeMove(int row, int col, String value, boolean isMutable) {
		if(this.isValidValue(value) && this.isValidMove(row, col, value) && this.isSlotMutable(row, col)) {
			this.board[row][col] = value;
			this.mutable[row][col] = isMutable;
			this.solvedBoard[row][col] = Integer.parseInt(value);
		}
	}
	
	// Checks if a value is valid
	private boolean isValidValue(String value) {
		for(String str : this.VALIDVALUES) {
			if(str.equals(value)) return true;
		}
		return false;
	}
	
	// Checks if a value could be placed at that slot
	public boolean isValidMove(int row, int col, String value) {
		if(this.inRange(row, col)) {
			if(!this.numInCol(col, value) && !this.numInRow(row, value) && !this.numInBox(row, col, value)) {
				return true;
			}
		}
		return false;
	}
	
	// Checks if a value is in-between 1-9
	public boolean inRange(int row, int col) {
		return row <= this.ROWS && col <= this.COLUMNS && row >= 0 && col >= 0;
	}
	
	// Checks if a value is already in the same column
	public boolean numInCol(int col, String value) {
		if(col <= this.COLUMNS) {
			for(int row=0;row < this.ROWS;row++) {
				if(this.board[row][col].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	// Checks if a value is already in the same row
	public boolean numInRow(int row, String value) {
		if(row <= this.ROWS) {
			for(int col=0;col < this.COLUMNS;col++) {
				if(this.board[row][col].equals(value)) {
					return true;
				}
			}
		}
		return false;
	}
	
	// Checks if a value is already in the same box
	public boolean numInBox(int row, int col, String value) {
		if(this.inRange(row, col)) {
			int boxRow = row / this.BOXHEIGHT;
			int boxCol = col / this.BOXWIDTH;
			
			int startingRow = (boxRow*this.BOXHEIGHT);
			int startingCol = (boxCol*this.BOXWIDTH);
			
			for(int r = startingRow;r <= (startingRow+this.BOXHEIGHT)-1;r++) {
				for(int c = startingCol;c <= (startingCol+this.BOXWIDTH)-1;c++) {
					if(this.board[r][c].equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// Checks if the slot is available to be used
	public boolean isSlotAvailable(int row, int col) {
		 return (this.inRange(row,col) && this.board[row][col].equals("") && this.isSlotMutable(row, col));
	}
	
	// Checks if a slot is mutable
	public boolean isSlotMutable(int row,int col) {
		return this.mutable[row][col];
	}
	
	public String getValue(int row,int col) {
		if(this.inRange(row,col)) {
			return this.board[row][col];
		}
		return "";
	}
	
	public String getSolvedValue(int row, int col) {
		if (this.inRange(row, col)) {
			return String.valueOf(this.solvedBoard[row][col]);
		}
		return "";
	}
	
	public String [][] getBoard() {
		return this.board;
	}
	
	// Checks if the board is full
	public boolean boardFull() {
		for(int r = 0; r < this.ROWS; r++) {
			for(int c = 0; c < this.COLUMNS; c++) {
				if(this.board[r][c].equals("")) return false;
			}
		}
		return true;
	}
	
	// Clean a slot
	public void makeSlotEmpty(int row,int col) {
		this.board[row][col] = "";
	}
	
	@Override
	public String toString() {
		String str = "Game Board:\n";
		for(int row=0;row < this.ROWS;row++) {
			for(int col=0;col < this.COLUMNS;col++) {
				str += this.board[row][col] + " ";
			}
			str += "\n";
		}
		return str + "\n";
	}
	
	//Initializes the Sudoku board with empty strings
	private void initializeBoard() {
		for(int row = 0; row < this.ROWS; row++) {
			for(int col = 0; col < this.COLUMNS; col++) {
				this.board[row][col] = "";
				this.solvedBoard[row][col] = 0;
			}
		}
	}
	
	 //Initializes the Sudoku board with empty strings
	private void initializeMutableSlots() {
		for(int row = 0; row < this.ROWS; row++) {
			for(int col = 0; col < this.COLUMNS; col++) {
				this.mutable[row][col] = true;
			}
		}
	}
}