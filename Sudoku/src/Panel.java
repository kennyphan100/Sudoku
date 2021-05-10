import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class Panel extends JPanel {

	// Attributes
	private Puzzle puzzle;
	private int currentlySelectedCol;
	private int currentlySelectedRow;
	private int usedWidth;
	private int usedHeight;
	private int fontSize;
	private boolean solveSudoku = false;

	public static boolean solveSudoku(Puzzle puzzle) {
		for (int i = 0; i < puzzle.solvedBoard.length; i++) {
			for (int j = 0; j < puzzle.board[i].length; j++) {
				
				// Search for an empty cell
				if (puzzle.solvedBoard[i][j] == Puzzle.EMPTY) {
	
					// Try possible numbers
					for(int number = 1; number <= 9; number++) {
						 if (isValid(i, j, number, puzzle)) {
							 puzzle.solvedBoard[i][j] = number;
							 
							 if(solveSudoku(puzzle)) {
								 return true;
							 } else {
								 puzzle.solvedBoard[i][j] = Puzzle.EMPTY;
							 }
						 } 	
					}
					return false;
				}
			}
		}
		return true; // Sudoku solved
	}
	
	public static boolean isInRow(int row, int number, Puzzle puzzle) {
		for(int i = 0; i < puzzle.solvedBoard.length; i++) {
			if (puzzle.solvedBoard[row][i] == number) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isInColumn(int column, int number, Puzzle puzzle) {
		for(int j = 0; j < puzzle.solvedBoard[0].length; j++) {
			if(puzzle.solvedBoard[j][column] == number) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isInBox(int row, int col, int number, Puzzle puzzle) {
		int r = row - row % 3;
		int c = col - col % 3;
		
		for (int i = r; i < r + 3; i++) {
			for(int j = c; j < c + 3; j++) {
				if(puzzle.solvedBoard[i][j] == number) {
					return true;
				}
			}
		}
		return false;
	}
	public static boolean isValid(int row, int col, int number, Puzzle puzzle) {
		if (!(isInRow(row, number, puzzle) || isInColumn(col, number, puzzle) || isInBox(row, col, number, puzzle))) {
			return true;
		}
		return false;	
	}
	public static void displayBoard(Puzzle puzzle) {
		for(int i = 0; i < puzzle.solvedBoard.length; i++) {
			
			if (i % 3 == 0 && i != 0) {
				System.out.println("---------------------");
			}
			
			for(int j = 0; j < puzzle.solvedBoard[i].length; j++) {
				
				if(j == puzzle.solvedBoard[i].length - 1) {
					System.out.println(puzzle.solvedBoard[i][j]);
				} else {
					if (j % 3 == 0 && j != 0 ) {
						System.out.print("| " + puzzle.solvedBoard[i][j] + " ");
					} else {
						System.out.print(puzzle.solvedBoard[i][j] + " ");
					}
				}
				
			}
		}
	}
	

	public Panel() {
		this.setPreferredSize(new Dimension(540, 450));
		this.addMouseListener(new SudokuPanelMouseAdapter());
		this.puzzle = new BoardGenerator().generateRandomSudoku(PuzzleType.NINEBYNINE);
		currentlySelectedCol = -1;
		currentlySelectedRow = -1;
		usedWidth = 0;
		usedHeight = 0;
		fontSize = 26;
	}
	
	public Panel(Puzzle puzzle) {
		this.setPreferredSize(new Dimension(540, 450));
		this.addMouseListener(new SudokuPanelMouseAdapter());
		this.puzzle = puzzle;
		currentlySelectedCol = -1;
		currentlySelectedRow = -1;
		usedWidth = 0;
		usedHeight = 0;
		fontSize = 26;
	}
	
	public void Puzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(1.0f, 1.0f, 1.0f));
		
		int slotWidth = this.getWidth() / puzzle.getNumColumns();
		int slotHeight = this.getHeight() / puzzle.getNumRows();
		
		usedWidth = (this.getWidth() / puzzle.getNumColumns()) * puzzle.getNumColumns();
		usedHeight = (this.getHeight() / puzzle.getNumRows()) * puzzle.getNumRows();
		
		g2d.fillRect(0, 0, usedWidth, usedHeight);
		
		g2d.setColor(new Color(0.0f, 0.0f, 0.0f));
		for(int x = 0; x <= usedWidth; x += slotWidth) {
			if((x/slotWidth) % puzzle.getBoxWidth() == 0) {
				g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(x, 0, x, usedHeight);
			}
			else {
				g2d.setStroke(new BasicStroke(1));
				g2d.drawLine(x, 0, x, usedHeight);
			}
		}

		for(int y = 0; y <= usedHeight; y += slotHeight) {
			if((y/slotHeight) % puzzle.getBoxHeight() == 0) {
				g2d.setStroke(new BasicStroke(2));
				g2d.drawLine(0, y, usedWidth, y);
			}
			else {
				g2d.setStroke(new BasicStroke(1));
				g2d.drawLine(0, y, usedWidth, y);
			}
		}
		
		// Draw the digits onto the board
		Font f = new Font("Times New Roman", Font.PLAIN, fontSize);
		g2d.setFont(f);
		FontRenderContext fContext = g2d.getFontRenderContext();
		
		
		
		if (solveSudoku) {
			for(int row = 0; row < puzzle.getNumRows(); row++) {
				for(int col = 0; col < puzzle.getNumColumns(); col++) {
						int textWidth = (int) f.getStringBounds(puzzle.getSolvedValue(row, col), fContext).getWidth();
						int textHeight = (int) f.getStringBounds(puzzle.getSolvedValue(row, col), fContext).getHeight();
						g2d.drawString(puzzle.getSolvedValue(row, col), (col*slotWidth)+((slotWidth/2)-(textWidth/2)), (row*slotHeight)+((slotHeight/2)+(textHeight/2)));
				}
			}
			solveSudoku = false;
			
		} else {
			for(int row = 0; row < puzzle.getNumRows(); row++) {
				for(int col = 0; col < puzzle.getNumColumns(); col++) {
					if(!puzzle.isSlotAvailable(row, col)) {
						int textWidth = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getWidth();
						int textHeight = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getHeight();
						g2d.drawString(puzzle.getValue(row, col), (col*slotWidth)+((slotWidth/2)-(textWidth/2)), (row*slotHeight)+((slotHeight/2)+(textHeight/2)));
					}
				}
			}
		}

		
		
		// Highlight the slot that the user clicks
		if(currentlySelectedCol != -1 && currentlySelectedRow != -1) {
			g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
			g2d.fillRect(currentlySelectedCol * slotWidth,currentlySelectedRow * slotHeight, slotWidth, slotHeight);
		}
		
		
	}
	
	public void messageFromNumActionListener(String buttonValue) {
		if(currentlySelectedCol != -1 && currentlySelectedRow != -1) {
			puzzle.makeMove(currentlySelectedRow, currentlySelectedCol, buttonValue, true);
			repaint();
		}
	}
	
	public class NumActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			messageFromNumActionListener(((JButton) e.getSource()).getText());	
		}
	}
	
	private class SudokuPanelMouseAdapter extends MouseInputAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				int slotWidth = usedWidth / puzzle.getNumColumns();
				int slotHeight = usedHeight / puzzle.getNumRows();
				currentlySelectedRow = e.getY() / slotHeight;
				currentlySelectedCol = e.getX() / slotWidth;
				e.getComponent().repaint();
			}
		}
	}
	
	public class SolveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			solveSudoku(puzzle);
			
			// print the solved board onto the GUI
			solveSudoku = true;
			repaint();
			
			
		}
		
	}
	
	
	
}