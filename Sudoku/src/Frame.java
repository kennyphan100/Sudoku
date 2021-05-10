import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JPanel buttonSelectionPanel;
	private JPanel windowPanel;
	private JButton solveButton;
	private Panel panel;
	
	public Frame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Sudoku");
		this.setMinimumSize(new Dimension(900, 800));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("Game");
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new NewGameListener(PuzzleType.NINEBYNINE, 26));

		file.add(newGame);

		file.add(newGame);
		menuBar.add(file);
		this.setJMenuBar(menuBar);
		
		windowPanel = new JPanel();
		windowPanel.setLayout(new FlowLayout());
		windowPanel.setPreferredSize(new Dimension(800, 600));
		
		buttonSelectionPanel = new JPanel();
		buttonSelectionPanel.setPreferredSize(new Dimension(100, 500));

		panel = new Panel();
		
		solveButton = new JButton("Solve Sudoku");
		solveButton.setFocusable(false);
		solveButton.setFont(new Font("Comic Sans", Font.BOLD, 25));
		solveButton.setBorder(null);
		solveButton.setForeground(Color.white);
		solveButton.setBackground(Color.black);
		solveButton.addActionListener(panel.new SolveListener());

		windowPanel.add(solveButton);
		windowPanel.add(panel);
		windowPanel.add(buttonSelectionPanel);
		this.add(windowPanel);
		
		rebuildInterface(PuzzleType.NINEBYNINE, 26);
	}
	
	public void rebuildInterface(PuzzleType puzzleType, int fontSize) {
		// generate a sudoku board
		Puzzle generatedPuzzle = new BoardGenerator().generateRandomSudoku(puzzleType);
		panel.Puzzle(generatedPuzzle);
		panel.setFontSize(fontSize);
		buttonSelectionPanel.removeAll();
		
		// add the digits 1-9
		for(String value : generatedPuzzle.getValidValues()) {
			JButton b = new JButton(value);
			b.setPreferredSize(new Dimension(50, 50));
			b.addActionListener(panel.new NumActionListener());
			buttonSelectionPanel.add(b);
		}
		panel.repaint();
		buttonSelectionPanel.revalidate();
		buttonSelectionPanel.repaint();
	}
	
	private class NewGameListener implements ActionListener {
		private PuzzleType puzzleType;
		private int fontSize;

		public NewGameListener(PuzzleType puzzleType, int fontSize) {
			this.puzzleType = puzzleType;
			this.fontSize = fontSize;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			rebuildInterface(puzzleType, fontSize);
		}
	}
	
	public static void main(String[] args) {
		new Frame();
	}
}