package Mocks;

import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import App.Color;
import App.GameBoardDb;
import App.IDatabase;
import App.Mockobase;
import App.Moves;

public class MockitoGameBoardTest {

	IDatabase database;
	GameBoardDb board;


	@AfterEach
	public void teardown() {
		board = null;
	}

	// 1 - verify
	@Test
	@DisplayName("Return true if setList and getList is called")
	public void verifyLoadGame() {
		//Mock
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 3, 3);
		//Prepare
		List<Moves> input = new ArrayList<Moves>();
		input.add(new Moves(1,2,Color.GREEN));
		input.add(new Moves(0,2,Color.RED));
		//Expects
		Mockito.when(database.getListOfMoves()).thenReturn(input);
		//Act
		board.saveGame();
		board.Board = null;
		board.loadGame();
		//Assert
		Mockito.verify(database, times(2)).getListOfMoves();
		Mockito.verify(database).setListOfMoves(Mockito.any());
		//assertNotNull(board.Board);
	} 
	// 1
	@Test
	@DisplayName("Return true if setList and getList is called")
	public void testLoadGame() {
		//Mock
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 3, 3);
		//Prepare
		List<Moves> input = new ArrayList<Moves>();
		input.add(new Moves(1,2,Color.GREEN));
		input.add(new Moves(0,2,Color.RED));
		//Expects
		Mockito.when(database.getListOfMoves()).thenReturn(input);
		//Act
		board.saveGame();
		board.Board = null;
		board.loadGame();
		//Assert
		Color[][] expected = new Color[3][3];
		Assertions.assertAll(
				() -> Assertions.assertNotEquals(expected, board.Board),
				() -> Assertions.assertNotNull(board.Board)
				);
	} 


	// 2 - verify
	@Test
	@DisplayName("Should return true if save is called twice")
	void verifyNextMoveTwice() {
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 3, 3);
		List<Moves> list = new ArrayList<Moves>();

		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));

		board.nextMove(1, 2);
		board.nextMove(1, 2);

		Mockito.verify(database, Mockito.times(2)).save(Mockito.any(Moves.class));
		//Assertions.assertNotEquals(new ArrayList<Moves>(), list);
	}
	//2
	@Test
	@DisplayName("Should return true if list has 2 values")
	void testNextMoveTwice() {
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 3, 3);
		List<Moves> list = new ArrayList<Moves>();

		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));

		board.nextMove(1, 2);
		board.nextMove(1, 2);

		Assertions.assertEquals(2, list.size());
	}

	// 3 
	@Test
	@DisplayName("Should return true if Mockobase movelist is not equal mocked list")
	void testUndoMoveEmptyThenAdd() {
		// Spy test using Mockobase
		database = Mockito.spy(Mockobase.class);
		board = new GameBoardDb(database, 3, 3);
		List<Moves> list = new ArrayList<Moves>();

		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));

		board.undoMove();
		board.addDisc(0);

		List<Moves> spied = database.getListOfMoves();
		Assertions.assertNotEquals(list, spied);
	}

	// 3 - Verify
	@Test
	@DisplayName("Should return true if verify pass")
	void verifyUndoMoveEmptyThenAdd() {
		// Spy test using Mockobase
		database = Mockito.spy(Mockobase.class);
		board = new GameBoardDb(database, 3, 3);
		List<Moves> list = new ArrayList<Moves>();

		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));

		board.undoMove();
		board.addDisc(0);

		List<Moves> spied = database.getListOfMoves();
		Mockito.verify(database).save(Mockito.any(Moves.class));
		Mockito.verify(database, times(1)).getListOfMoves();
		Mockito.verify(database, times(1)).getCount();
	}


	//4
	@Test
	@DisplayName("Should return true if list size is 1")
	void testAddDiscFullThenUndo() {
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 1, 2);
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));

		Mockito.doAnswer((i) -> {
			list.remove((int) i.getArgument(0));
			return null;
		}).when(database).remove(Mockito.anyInt());

		Mockito.when(database.getListOfMoves()).thenReturn(list);
		Mockito.when(database.getCount()).thenReturn(1l);
		//Act
		board.addDisc(0);
		board.addDisc(0); // Column is full
		board.undoMove();
		//Assert
		assertEquals(0, list.size());
	}

	//4 - verify
	@Test
	@DisplayName("Should return true if verify pass")
	void verifyAddDiscFullThenUndo() {
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 1, 2);
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));

		Mockito.doAnswer((i) -> {
			list.remove((int) i.getArgument(0));
			return null;
		}).when(database).remove(Mockito.anyInt());

		Mockito.when(database.getListOfMoves()).thenReturn(list);
		Mockito.when(database.getCount()).thenReturn(1l);
		//Act
		board.addDisc(0);
		board.addDisc(0); // Column is full
		board.undoMove();
		//Assert
		Mockito.verify(database).remove(0);
		Mockito.verify(database).save(Mockito.any(Moves.class));
		Mockito.verify(database).getCount();
	}
	// 5 - verify
	@Test
	public void verifyMultipleUndos(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 5, 5);
		//Expect
		Mockito.when(database.getCount()).thenReturn(0l);
		//Act
		board.undoMove();
		board.undoMove();
		board.undoMove();
		board.undoMove();
		board.undoMove();
		//Verify
		Mockito.verify(database, times(5)).getCount();
	}
	// 5
	@Test
	public void testMultipleUndos(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 5, 5);
		//Expect
		Mockito.when(database.getCount()).thenReturn(0l);
		//Act
		board.undoMove();
		board.undoMove();
		board.undoMove();
		board.undoMove();
		board.undoMove();
		//Assert
		Assertions.assertEquals(0, board.getMove_number());
	}

	// 6 - verify
	@Test
	public void verifyVerticalWinRed(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 4, 4);
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));
		//Act
		board.setWinner_name("Andrew");
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(0);

		Mockito.verify(database, times(4)).save(Mockito.any(Moves.class));
	}
	// 6 
	@Test
	public void testVerticalWinRed(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 4, 4);
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));
		//Act
		board.setWinner_name("Andrew");
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(0);
		Assertions.assertTrue(list.size() == 4);
	}

	// 7 - verify
	@Test
	public void verifyHorizontalWinGreen(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 4, 4);
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));
		//Act
		board.setWinner_name("Andrew");
		board.rotateColor();
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(1);
		board.rotateColor();
		board.addDisc(2);
		board.rotateColor();
		board.addDisc(3);

		Mockito.verify(database, times(4)).save(Mockito.any(Moves.class));
	}
	// 7 
	@Test
	public void testHorizontalWinGreen(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 4, 4);
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));
		//Act
		board.setWinner_name("Andrew");
		board.rotateColor();
		board.addDisc(0);
		board.rotateColor();
		board.addDisc(1);
		board.rotateColor();
		board.addDisc(2);
		board.rotateColor();
		board.addDisc(3);
		Assertions.assertTrue(list.size() == 4);
	}
	// 8 - verify
	@Test
	public void verifyDiagonalWinGreen(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 4, 4);
		Color[][] test = {
				{null,null,null,null},
				{null,Color.GREEN,null,null},
				{null,null,Color.GREEN,null},
				{null,null,null,Color.GREEN}};
		board.Board = test;
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));
		//Act
		board.setWinner_name("John");
		board.rotateColor();
		board.addDisc(0);
		

		Mockito.verify(database, times(1)).save(Mockito.any(Moves.class));
	}
	// 8 
	@Test
	public void testDiagonalWinGreen(){
		database = Mockito.mock(IDatabase.class);
		board = new GameBoardDb(database, 4, 4);
		Color[][] test = {
				{null,null,null,null},
				{null,Color.GREEN,null,null},
				{null,null,Color.GREEN,null},
				{null,null,null,Color.GREEN}};
		board.Board = test;
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		Mockito.doAnswer((i) -> {
			list.add(i.getArgument(0));
			return null;
		}).when(database).save(Mockito.any(Moves.class));
		//Act
		board.setWinner_name("John");
		board.rotateColor();
		board.addDisc(0);
		
		Assertions.assertTrue(list.size() == 1);
	}


}
