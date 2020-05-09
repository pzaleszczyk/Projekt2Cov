package Mocks;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import App.Color;
import App.GameBoard;
import App.GameBoardDb;
import App.IDatabase;
import App.Mockobase;
import App.Moves;

//Equals, NotEquals, All, ArrayEquals, True
public class ManualGameBoardTest {

	IDatabase database;
	GameBoardDb board;


	@BeforeEach
	public void setup() throws UnknownHostException {
		//Create mock
		database = new Mockobase();
		board = new GameBoardDb(database);
		database.clear();
	}

	@AfterEach
	public void teardown() {
		board = null;
	}

	//1
	@Test
	@DisplayName("Should return true if list object has correct values")
	void testNextMove() {
		board.nextMove(1, 2);
		assertAll(
				() -> assertEquals(1, database.getCount()),
				() -> assertEquals(1, database.getRow(0)),
				() -> assertEquals(2, database.getColumn(0)),
				() -> assertEquals(Color.RED, database.getColor(0))
				);
	}

	//2
	@Test
	@DisplayName("Should return true if both listOfMoves and Board removed move")
	void testUndoMove() {
		board.addDisc(5);
		board.undoMove();

		Color[][] expected1 = new Color[6][7];        
		ArrayList<Moves> expected2 =
				new ArrayList<Moves>();

		assertAll(
				() -> assertArrayEquals(expected1,board.Board),
				() -> assertEquals(expected2, database.getListOfMoves())
				);
	}

	//3
	@Test
	@DisplayName("Should return true if listOfMoves is empty")
	void testUndoMoveEmpty() {
		board.undoMove();
		assertEquals(new ArrayList<Moves>(), database.getListOfMoves());
	}

	//4
	@Test
	@DisplayName("Should return true if movelist has not changed")
	void testAddDiscFull() {
		GameBoardDb test = new GameBoardDb(database, 2,2);
		test.addDisc(0);
		test.addDisc(0);
		List<Moves> expected = database.getListOfMoves();
		test.addDisc(0);
		assertEquals(expected, database.getListOfMoves());
	}
	
	//5
	@Test
	@DisplayName("Should return true if movelist was cleared")
	void testCreateGameboard() {
		//Mock test for database.clear() and getCount()
		board.addDisc(0);
		long one = database.getCount();
		board = new GameBoardDb(database);
		long zero = database.getCount();
		
		assertAll(
				() -> assertNotEquals(one, zero),
				() -> assertEquals(0 ,zero)
				);
	}
	
	//6
	@Test
	public void testListofmovesOnDraw()
	{
		board = new GameBoardDb(database, 1, 1);
		board.addDisc(0);
		
		assertAll(
				() -> assertEquals(GameBoardDb.Result.DRAW,board.getResult()),
				() -> assertTrue(database.getListOfMoves().size() == 1)
				);
	}
	
	//7
	@Test
	public void testSaveNullException(){
		assertThrows(
				IllegalArgumentException.class,
				() -> database.save(null)
				);
	}
	
	
	//8
	@Test
	public void testCheckConnectionTestNoException()
	{
		Assertions.assertThatCode(() -> new GameBoardDb(database, 2, 2))
	    .doesNotThrowAnyException();
	}
	
}
