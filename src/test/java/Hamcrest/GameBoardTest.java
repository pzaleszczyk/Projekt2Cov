package Hamcrest;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import App.*;


public class GameBoardTest {

	GameBoard board;

	@BeforeEach
	void setUp() throws Exception{
		board = new GameBoard();
	}

	@AfterEach
	void tearDown() throws Exception {
		board = null;
	}



	@Test 
	@DisplayName("Should return true if rows = 6")
	void testDefaultAmountOfRows() {
		assertThat(board.getRows(),equalTo(6));
	}
	@Test 
	@DisplayName("Should return true if columns = 7")
	void testDefaultAmountOfColumns() {
		assertThat(board.getColumns(),equalTo(7));
	}

//	@Test 
//	@DisplayName("Should return true if columns = 9")
//	void testNondefaultAmountOfColumns() {
//		GameBoard nboard = new GameBoard(10,9);
//		assertThat(nboard.columns,equalTo(9));
//	}
//
//	@Test 
//	@DisplayName("Should return true if rows = 10")
//	void testNondefaultAmountOfRows() {
//		GameBoard nboard = new GameBoard(10,9);
//		assertThat(nboard.rows,equalTo(10));
//	}
	
	@ParameterizedTest (name = "{0} and {1}")
	@CsvFileSource(resources = "../testdata.csv", numLinesToSkip = 1, delimiter = '\t')
	@DisplayName("Should return true if rows are correct")
	void testNondefaultAmountOfRows(String inr, String inc) {
		int row = Integer.parseInt(inr);
		int column = Integer.parseInt(inc);
		GameBoard nboard = new GameBoard(row, column);
		assertThat(nboard.getRows(),equalTo(row));
	}
	
	@ParameterizedTest (name = "{0} and {1}")
	@CsvFileSource(resources = "../testdata.csv", numLinesToSkip = 1, delimiter = '\t')
	@DisplayName("Should return true if columns are correct")
	void testNondefaultAmountOfColumns(String inr, String inc) {
		int row = Integer.parseInt(inr);
		int column = Integer.parseInt(inc);
		GameBoard nboard = new GameBoard(row, column);
		assertThat(nboard.getColumns(),equalTo(column));
	}

	@Test
	@DisplayName("Should return true if color is RED")
	void testAddingRedDiscToBoard() {
		board.addDisc(5);
		assertThat(board.Board[0][5],equalTo(Color.RED));
	}

	@Test
	@DisplayName("Should return true if color is GREEN")
	void testAddingGreenDiscToBoard() {
		board.addDisc(5);
		board.addDisc(5);
		assertThat( board.Board[1][5],equalTo(Color.GREEN));
	}

	@Test
	@DisplayName("Should return true if list color has correct value")
	void testNextMoveColor() {
		board.nextMove(1, 2);
		assertThat(board.listOfmoves.get(0).color,equalTo(Color.RED));
	}

	@Test
	@DisplayName("Should return true if list column has correct value")
	void testNextMoveColumn() {
		board.nextMove(1, 2);
		assertThat(board.listOfmoves.get(0).column,equalTo(2));
	}

	@Test
	@DisplayName("Should return true if list row has correct value")
	void testNextMoveRow() {
		board.nextMove(1, 2);
		assertThat(board.listOfmoves.get(0).row,equalTo(1));
	}

	@Test
	@DisplayName("Should return true if listOfMoves removed move")
	void testUndoMoveList() {
		board.addDisc(5);
		board.undoMove();

		ArrayList<Moves> expected2 =
				new ArrayList<Moves>();
		assertThat( board.listOfmoves,equalTo(expected2));

	}

	@Test
	@DisplayName("Should return true if Board removed move")
	void testUndoMoveBoard() {
		board.addDisc(5);
		board.undoMove();

		Color[][] expected1 = new Color[6][7];        
		assertThat(board.Board,equalTo(expected1));

	}

	@Test
	@DisplayName("Should return true if listOfMoves is empty")
	void testUndoMoveEmpty() {
		board.undoMove();
		assertThat(board.listOfmoves,equalTo(new ArrayList<Moves>()));
	}


	@Test
	@DisplayName("Should return true if Board has no null")
	void testCheckIfArrayFull() {
		//Prepare
		Color[][] actual =  {
				{Color.RED,Color.GREEN,Color.GREEN},
				{Color.GREEN,Color.RED,Color.GREEN},
				{Color.GREEN,Color.GREEN,Color.RED},
				{Color.GREEN,Color.GREEN,Color.GREEN}
		};
		board.Board = actual;
		board.setColumns(4);
		board.setRows(4);
		assertThat(board.checkIfArrayFull(),is(true));
	}

	@Test
	@DisplayName("Should return true if has 4 or more vertically")
	void testCheckIfWinConditionVertical() {
		Color[][] actual =  {
				{Color.RED,Color.RED,Color.RED,Color.RED},
				{null,null,null,null},
				{null,null,null,null},
				{null,null,null,null}
		};
		board.Board = actual;
		board.setColumns(4);
		board.setRows(4);
		//Checking
		assertThat(board.checkWinCondition(),is(true));
	}


	@Test
	@DisplayName("Should return true if has 4 or more horizontally")
	void testCheckIfWinConditionHorizontal() {
		Color[][] actual =  {
				{Color.RED,null,null,null},
				{Color.RED,null,null,null},
				{Color.RED,null,null,null},
				{Color.RED,null,null,null}
		};
		board.Board = actual;
		board.setColumns(4);
		board.setRows(4);

		//Checking
		assertThat(board.checkWinCondition(),is(true));
	}

	@Test
	@DisplayName("Should return true if draw after move")
	void testCheckIfDrawWithNextMove() {
		Color[][] actual =  {
				{null,Color.GREEN},
				{Color.RED,Color.GREEN}
		};
		board.Board = actual;
		board.setColumns(2);
		board.setRows(2);
		board.addDisc(0);

		//Checking
		assertThat( board.getResult(),equalTo(GameBoard.Result.DRAW));
	}


	@Test
	@DisplayName("Should return true if victory after move")
	void testCheckIfVictoryWithNextMove() {
		//Backup leaderboards file
		ArrayList<Score> backup = new ArrayList<Score>();
		for(Score b : board.leaderboards) {
			backup.add(b);
		}    	
		//Prepare
		Color[][] actual =  {
				{null,null,null,null},
				{Color.RED,null,null,null},
				{Color.RED,null,null,null},
				{Color.RED,null,null,null}
		};
		board.Board = actual;
		board.setColumns(4);
		board.setRows(4);
		board.setWinner_name("winner");
		//Act
		board.addDisc(0);

		//Load backup
		FileOperations.saveFile("leaderboards.txt",backup);

		//Assert
		assertThat(board.getResult(),equalTo(GameBoard.Result.VICTORY));
	}

	@ParameterizedTest
	@EnumSource(Color.class) 
	@DisplayName("Should return true if has 4 or more diagonally")
	void testCheckIfWinConditionDiagonal(Color input) {
		board.setCurrent_color(input);
		//Prepare
		Color[][] actual =  {
				{input,null,null,null},
				{null,input,null,null},
				{null,null,input,null},
				{null,null,null,input}
		};
		board.Board = actual;
		board.setColumns(4);
		board.setRows(4);
		//Checking
		assertThat(board.checkWinCondition(),is(true));
	}  
	
	@ParameterizedTest
	@DisplayName("Should return true if leaderboards name is correct")
	@ValueSource(strings = {"Daniel", "John"})
	void testaddToLeaderBoardsName(String input) {
		board.setScore(120);
		board.addToLeaderboards(input);
		Score test = board.leaderboards.get(0);
		assertThat(test.name,equalTo(input));	
	}

	@ParameterizedTest
	@DisplayName("Should return true if leaderboards score is correct")
	@ValueSource(doubles = {10.0, 20.0})
	void testaddToLeaderBoardsScore(double input) {
		board.setScore(input);
		board.addToLeaderboards("Winner");
		Score test = board.leaderboards.get(0);
		assertThat(test.score,equalTo(input));
	}

	@DisplayName("Should be true if rotated color is..")
	@ParameterizedTest(name = "{0} loops => {1} ")
	@CsvSource({ "1,GREEN", "2, RED", "3, GREEN" })
	public void testRotateColor(int loops, Color expected) {
		for(int i = 0; i < loops; i++) {
			board.rotateColor();
		}
		assertThat(board.getCurrent_color(),equalTo(expected));
	}



	@Test
	@DisplayName("Should be true if rows value from saved file is 5")
	public void testSaveGame() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.addDisc(0);
		actual_board.addDisc(1);
		actual_board.saveGame();
		List<String> actual = FileOperations.loadFile("saveconfigfile");

		assertThat(actual.get(0),equalTo("5"));
	}

	@Test
	public void testLoadGame() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.addDisc(0);
		actual_board.addDisc(1);
		actual_board.saveGame();
		actual_board = new GameBoard(10,10);

		actual_board.loadGame();
		assertThat(actual_board.getRows(),equalTo(5));
	}  

	@Test
	@DisplayName("Should be true if rows value from saved file is 5")
	public void testSaveGameNoneDiscs() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.saveGame();
		List<String> actual = FileOperations.loadFile("saveconfigfile");

		assertThat(actual.get(0),equalTo("5"));
	}

	@Test
	public void testLoadGameNoneDiscs() {
		GameBoard actual_board = new GameBoard(5,5);
		actual_board.saveGame();
		actual_board = new GameBoard(10,10);

		actual_board.loadGame();
		assertThat(actual_board.getRows(),equalTo(5));
	}
	
	@ParameterizedTest
	@NullSource
	@DisplayName("Should be true if color is null")
	public void testtoColorNull(String input) {
		Color actual = Color.toColor(input);
		assertThat(actual,is(nullValue()));
	}


	@ParameterizedTest
	@EmptySource
	@DisplayName("Should be true if color is null")
	public void testtoColorEmpty(String input) {
		Color actual = Color.toColor(input);
		assertThat(actual,is(nullValue()));
	}
	
	@Test
	@DisplayName("Should return true if movelist has not changed")
	void testAddDiscFull() {
		GameBoard test = new GameBoard(2,2);
		test.addDisc(0);
		test.addDisc(0);
		List<App.Moves> expected = test.listOfmoves;
		test.addDisc(0);
		assertThat(test.listOfmoves,equalTo(expected));
	}
}

