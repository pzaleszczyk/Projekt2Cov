package App;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class GameBoard {
	
	public Color Board[][];
	public List<Moves> listOfmoves;
	public List<Score> leaderboards = new ArrayList<Score>(); 
	private int rows;
	private int columns;
	private double score;
	private String winner_name;
	private Color current_color;
	private Result result;


	public enum Result {
		NONE, DRAW, VICTORY;
	}

	public GameBoard() {
		
		this.setRows(6);
		this.setColumns(7);
		createGameboard();
	}

	private void createGameboard() {
		setResult(Result.NONE);
		List<String> list = FileOperations.loadFile("leaderboards.txt");
		leaderboards = Score.toList(list);
		listOfmoves = new ArrayList<Moves>();
		setCurrent_color(Color.RED);
		Board = new Color[getRows()][getColumns()];
	}

	public GameBoard(int rows, int columns) {
		if(rows < 1 || columns < 1)
			throw new IllegalArgumentException();
		this.setRows(rows);
		this.setColumns(columns);
		createGameboard();
	}

	public void addDisc(int column){
		if(column < 0) {
			throw new IllegalArgumentException();
		}
		for(int i = 0 ; i < getRows(); i ++) {
			if(Board[i][column] == null) {
				Board[i][column] = getCurrent_color();
				nextMove(i,column);
				return;
			}
		}
		System.out.println("Column is full");
	}

	public void rotateColor() {
		if(getCurrent_color() == Color.RED)
			setCurrent_color(Color.GREEN);
		else
			setCurrent_color(Color.RED);
	}

	public void nextMove(int row, int column) {
		if(column < 0 || row < 0) {
			throw new IllegalArgumentException();
		}
		saveMove(row, column);
		if(checkWinCondition()) {
			setResult(Result.VICTORY);
			showBoard();
			System.out.println(getCurrent_color().toString()+" won!");
			calculateScore();
			addToLeaderboards(getWinner_name());
			FileOperations.saveFile("leaderboards.txt", leaderboards);
		}
		if(checkIfArrayFull()){
			setResult(Result.DRAW);
			System.out.println("Draw!");
		}
		showBoard();
		rotateColor();
	}

	private void saveMove(int row, int column) {
		Moves move = new Moves(row,column,getCurrent_color());
		listOfmoves.add(move);
	}

	
	private void removeMove() {
		listOfmoves.remove(0);
	}
	

	public void undoMove() {
		if(listOfmoves.isEmpty()) {
			System.out.println("List of moves is empty");
			return;
		}		
		int row = listOfmoves.get(0).row;
		int column = listOfmoves.get(0).column;
		removeMove();
		Board[row][column] = null;
		rotateColor();
	}
	
	

	private double calculateScore() {
		int score = 0;
		for(Color[] a : Board)
			for(Color b : a)
			{
				if(b==getCurrent_color())
					score++;
			}
		return score/(getRows()*getColumns());
	}

	private boolean checkWinDiag() {
		int count = 0;
		for(int col = 0; col < this.getColumns() ; col++)
			for(int row = 0; row < this.getRows(); row++) {

				count = 0;
				for(int i = 0; i < 5; i ++)
					//Bounds
					if(row+i >= 0 && row+i < getRows() && col+i >= 0 && col+i < getColumns()) {
						if(Board[row+i][col+i] == getCurrent_color()) {
							count++;
							if(count >= 4)
								return true;
						}
						else
							count = 0;
					}
			}
		return false;
	}

	private boolean checkWinVert() {
		int count = 0;
		for(int col = 0; col < this.getColumns() ; col++) {
			count = 0;
			for(int row = 0; row < this.getRows(); row++) {
				if(Board[row][col] == getCurrent_color()) {
					count++;
					if(count >= 4)
						return true;
				}
				else {
					count = 0;
				}
			}
		}
		return false;
	}

	private boolean checkWinHoriz() {
		int count = 0;
		for(int row = 0; row < this.getRows(); row++) {
			count = 0;
			for(int col = 0; col < this.getColumns() ; col++)
				if(Board[row][col] == getCurrent_color()) {
					count++;
					if(count >= 4)
						return true;
				}
				else {		
					count=0;
				}
		}
		return false;
	}

	public boolean checkWinCondition() {
		if(checkWinHoriz())
			return true;
		if(checkWinVert())
			return true;
		if(checkWinDiag())
			return true;
		return false;
	}

	public boolean checkIfArrayFull() {
		for(Color[] a : Arrays.asList(Board)) {
			for(Color b : a) {
				if(b==null)
					return false;
			}
		}
		return true;
	}

	public void addToLeaderboards(String name) {
		if(name == null || name == "")
			throw new IllegalArgumentException();
		Score record = new Score(getScore(),name);
		leaderboards.add(record);
		leaderboards.sort(Comparator.comparing(Score::getScore).reversed());

	}

	public void saveGame() {
		FileOperations.saveFile("savefile", listOfmoves);
		List<Integer> config = new ArrayList<Integer>();
		config.add(getRows());
		config.add(getColumns());
		FileOperations.saveFile("saveconfigfile", config);
	}

	public void loadGame() {
		List<String> config = FileOperations.loadFile("saveconfigfile");
		List<String> list = FileOperations.loadFile("savefile");
		listOfmoves = Moves.toList(list);

		int rows = Integer.parseInt(config.get(0));
		int columns = Integer.parseInt(config.get(1));

		Color new_Board[][] = new Color[rows][columns];

		for(Moves move : listOfmoves) {
			new_Board[move.row][move.column] = move.color; 
		}
		Board = new_Board;
		this.setRows(rows);
		this.setColumns(columns);
	}


	public void showLeaderboards() {
		for(Score a : leaderboards) {
			System.out.println(a.name + " : " + a.score);
		}
	}

	public void showBoard() {
		System.out.println("BOARD: ");
		for(Color[] a : Board) {
			for(Color b : a) {
				if(b == null)
					System.out.print(" []");
				else
					System.out.print(" "+b);
			}
			System.out.println();
		}
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getWinner_name() {
		return winner_name;
	}

	public void setWinner_name(String winner_name) {
		this.winner_name = winner_name;
	}

	public Color getCurrent_color() {
		return current_color;
	}

	public void setCurrent_color(Color current_color) {
		this.current_color = current_color;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	//	public static void main(String args[]) {
	//		GameBoard start = new GameBoard(2,2);
	//
	//		start.addDisc(0);
	//		start.addDisc(0);
	//		start.addDisc(1);
	//		start.addDisc(1);
	//
	//		start.addDisc(0);
	//		
	//		start.saveGame();
	//
	//	}

}
