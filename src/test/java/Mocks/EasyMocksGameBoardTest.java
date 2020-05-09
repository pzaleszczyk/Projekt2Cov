package Mocks;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

import java.rmi.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.easymock.EasyMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import App.Color;
import App.Database;
import App.FileOperations;
import App.GameBoard;
import App.GameBoardDb;
import App.IDatabase;
import App.Mockobase;
import App.Moves;

public class EasyMocksGameBoardTest {

	IDatabase database;
	GameBoardDb board;

	@AfterEach
	public void teardown() {
		board = null;
	}

	// 1
	@Test
	@DisplayName("Should be true if rows value from saved file is 6")
	public void testSaveGame() {
		database = EasyMock.createNiceMock(IDatabase.class);
		board = new GameBoardDb(database);
		//Prepare
		Color[][] new_Board = {{null,null,Color.RED},{null,null,Color.GREEN},{null,null,null}};
		board.Board = new_Board;
		List<Moves> input = new ArrayList<Moves>();
		input.add(new Moves(0,2,Color.RED));
		input.add(new Moves(1,2,Color.GREEN));
		//Expect	
		expect(database.getListOfMoves()).andReturn(input);
		replay(database);
		//Act
		database.clear();
		board.saveGame();
		//Assert
		List<String> actual = FileOperations.loadFile("saveconfigfile.txt");
		assertEquals("6", actual.get(0));
	}
	// 1 Verify
	@Test
	@DisplayName("Should be true if all expected methods were run")
	public void verifySaveGame() {		
		database = EasyMock.createNiceMock(IDatabase.class);
		board = new GameBoardDb(database);
		//Prepare
		Color[][] new_Board = {{null,null,Color.RED},{null,null,Color.GREEN},{null,null,null}};
		board.Board = new_Board;
		List<Moves> input = new ArrayList<Moves>();
		input.add(new Moves(0,2,Color.RED));
		input.add(new Moves(1,2,Color.GREEN));
		//Expect	
		expect(database.getListOfMoves()).andReturn(input);
		replay(database);
		//Act
		database.clear();
		board.saveGame();
		//Verify
		EasyMock.verify(database);
	}
	// 2
	@Test
	public void testCheckConnectionTestException()
	{
		database = EasyMock.createNiceMock(IDatabase.class);
		board = new GameBoardDb(database);

		expect(database.noConnection()).andReturn(true);
		replay(database);

		assertThrows(IllegalArgumentException.class, () -> {
			new GameBoardDb(database, 2, 2);
		});
	}

	// 2 - verify
	@Test
	public void verifyCheckConnectionTestException()
	{
		database = EasyMock.createNiceMock(IDatabase.class);
		board = new GameBoardDb(database);

		expect(database.noConnection()).andReturn(true);
		replay(database);

		try {
			new GameBoardDb(database, 2, 2);
		}catch(Exception e) {
			database.clear();
			EasyMock.verify(database);
		}
	}
	//3 - verify
	@Test
	public void verifyAddThenUndoAndGetPreviousMoveRowColumn() {
		//Prep
		database = EasyMock.createStrictMock(IDatabase.class);
		List<Moves> list = new ArrayList<Moves>();
		//Expect
		EasyMock.expect(database.noConnection()).andReturn(false);
		database.clear();
		EasyMock.expectLastCall().andAnswer(() -> null).times(1);
		database.save(EasyMock.anyObject(Moves.class));
		EasyMock.expectLastCall().andAnswer(() -> {
			//Add
			list.add((Moves) EasyMock.getCurrentArguments()[0]);
			return null;
		}).times(1);
		EasyMock.expect(database.getCount()).andReturn(3l);
		EasyMock.expect(database.getRow(EasyMock.anyInt())).andReturn(0);
		EasyMock.expect(database.getColumn(EasyMock.anyInt())).andReturn(1);
		database.remove(0);
		EasyMock.expectLastCall().andAnswer(() -> {
			//Remove
			list.remove(EasyMock.getCurrentArguments()[0]);
			return null;
		}).times(1);
		replay(database);
		//Act
		board = new GameBoardDb(database, 3, 3);
		board.Board = new Color[][] {{null,null,null},{null,Color.RED,null},{null,Color.GREEN,null}};
		board.addDisc(1);
		Color expected = board.Board[0][1]; // RED
		board.undoMove();
		Color actual = board.Board[0][1]; // NULL
		//Verify
		EasyMock.verify(database);
	}
	// 3 
	@Test
	public void testAddThenUndoAndGetPreviousMoveRowColumn() {
		//Prep
		database = EasyMock.createStrictMock(IDatabase.class);
		List<Moves> list = new ArrayList<Moves>();
		//Expects. D:
		EasyMock.expect(database.noConnection()).andReturn(false);
		database.clear();
		EasyMock.expectLastCall().andAnswer(() -> null).times(1);
		database.save(EasyMock.anyObject(Moves.class));
		EasyMock.expectLastCall().andAnswer(() -> {
			//Add
			list.add((Moves) EasyMock.getCurrentArguments()[0]);
			return null;
		}).times(1);
		EasyMock.expect(database.getCount()).andReturn(3l);
		EasyMock.expect(database.getRow(EasyMock.anyInt())).andReturn(0);
		EasyMock.expect(database.getColumn(EasyMock.anyInt())).andReturn(1);
		database.remove(0);
		EasyMock.expectLastCall().andAnswer(() -> {
			//Remove
			list.remove(EasyMock.getCurrentArguments()[0]);
			return null;
		}).times(1);
		replay(database);
		//Act
		board = new GameBoardDb(database, 3, 3);
		board.Board = new Color[][] {{null,null,null},{null,Color.RED,null},{null,Color.GREEN,null}};
		board.addDisc(1);
		Color expected = board.Board[0][1]; // RED
		board.undoMove();
		Color actual = board.Board[0][1]; // NULL
		//Verify
		assertNotEquals(expected, actual);
	}
	//4
	@Test
	public void testListofmovesOnVictory()
	{
		//Prep
		database = EasyMock.createMock(IDatabase.class);
		List<Moves> list = new ArrayList<Moves>();
		Color[][] test = {
				{null,null,null,null},
				{null,Color.GREEN,null,null},
				{null,null,Color.GREEN,null},
				{null,null,null,Color.GREEN}};
		//Expect
		EasyMock.expect(database.noConnection()).andReturn(false);
		database.clear();
		EasyMock.expectLastCall().andAnswer(() -> null).times(1);
		database.save(EasyMock.anyObject(Moves.class));
		EasyMock.expectLastCall().andAnswer(() -> {
			//Add
			list.add((Moves) EasyMock.getCurrentArguments()[0]);
			return null;
		}).times(1);
		EasyMock.expect(database.getListOfMoves()).andReturn(list);
		replay(database);
		//Act
		board = new GameBoardDb(database, 4, 4);
		board.setWinner_name("Andrew");
		board.Board = test;
		board.rotateColor();
		board.addDisc(0);
		assertAll(
				() -> assertEquals(GameBoardDb.Result.VICTORY,board.getResult()),
				() -> assertTrue(database.getListOfMoves().size() == 1)
				);
	}

	//4 - verify
	@Test
	public void verifyListofmovesOnVictory()
	{
		//Prep
		database = EasyMock.createMock(IDatabase.class);
		List<Moves> list = new ArrayList<Moves>();
		Color[][] test = {
				{null,null,null,null},
				{null,Color.GREEN,null,null},
				{null,null,Color.GREEN,null},
				{null,null,null,Color.GREEN}};
		//Expect
		EasyMock.expect(database.noConnection()).andReturn(false);
		database.clear();
		EasyMock.expectLastCall().andAnswer(() -> null).times(1);
		database.save(EasyMock.anyObject(Moves.class));
		EasyMock.expectLastCall().andAnswer(() -> {
			//Add
			list.add((Moves) EasyMock.getCurrentArguments()[0]);
			return null;
		}).times(1);
		replay(database);
		//Act
		board = new GameBoardDb(database, 4, 4);
		board.setWinner_name("Andrew");
		board.Board = test;
		board.rotateColor();
		board.addDisc(0);
		EasyMock.verify(database);
	}
	//5
	@Test
	public void testcreateGameBoardCustom() {
		database = EasyMock.createMock(IDatabase.class);
		database.clear();
		EasyMock.expectLastCall().andAnswer(() -> null).times(1);
		EasyMock.expect(database.noConnection()).andReturn(false);
		replay(database);

		board = new GameBoardDb(database, 5 , 6);
		assertEquals(5, board.getRows());
	}
	// 5 - verify
	@Test
	public void verifycreateGameBoardCustom() {
		database = EasyMock.createMock(IDatabase.class);;
		database.clear();
		EasyMock.expectLastCall().andAnswer(() -> null).times(1);
		EasyMock.expect(database.noConnection()).andReturn(false);
		replay(database);

		board = new GameBoardDb(database, 5 , 6);
		EasyMock.verify(database);
	}
	//6 - verify
	@Test
	public void verifyVictoryIntoDraw() {
		database = EasyMock.createNiceMock(IDatabase.class);;
		EasyMock.expect(database.noConnection()).andReturn(false);
		EasyMock.expect(database.getCount()).andReturn(4l);
		replay(database);

		board = new GameBoardDb(database, 4 , 1);
		board.Board = new Color[][] {{null}, {Color.GREEN}, {Color.GREEN}, {Color.GREEN}};
		board.rotateColor();
		board.setWinner_name("Winner");
		board.addDisc(0);
		GameBoardDb.Result a = board.getResult();
		board.undoMove();
		board.rotateColor();
		board.addDisc(0);
		GameBoardDb.Result b = board.getResult();

		EasyMock.verify(database);
	}
	//6
	@Test
	public void testVictoryIntoDraw() {
		database = EasyMock.createNiceMock(IDatabase.class);;
		EasyMock.expect(database.noConnection()).andReturn(false);
		EasyMock.expect(database.getCount()).andReturn(4l);
		replay(database);

		board = new GameBoardDb(database, 4 , 1);
		board.Board = new Color[][] {{null}, {Color.GREEN}, {Color.GREEN}, {Color.GREEN}};
		board.rotateColor();
		board.setWinner_name("Winner");
		board.addDisc(0);
		GameBoardDb.Result a = board.getResult();
		board.undoMove();
		board.rotateColor();
		board.addDisc(0);
		GameBoardDb.Result b = board.getResult();

		assertNotEquals(a,b);
	}
	
	//7
	@Test
	public void verifyDrawIntoVictory() {
		database = EasyMock.createNiceMock(IDatabase.class);;
		EasyMock.expect(database.noConnection()).andReturn(false);
		EasyMock.expect(database.getCount()).andReturn(4l);
		replay(database);

		board = new GameBoardDb(database, 4 , 1);
		board.Board = new Color[][] {{null}, {Color.GREEN}, {Color.GREEN}, {Color.GREEN}};
		board.addDisc(0);
		GameBoardDb.Result a = board.getResult();
		board.undoMove();
		board.rotateColor();
		board.setWinner_name("Winner");
		board.addDisc(0);
		GameBoardDb.Result b = board.getResult();

		EasyMock.verify(database);
	}

	//7
	@Test
	public void testDrawIntoVictory() {
		database = EasyMock.createNiceMock(IDatabase.class);;
		EasyMock.expect(database.noConnection()).andReturn(false);
		EasyMock.expect(database.getCount()).andReturn(4l);
		replay(database);

		board = new GameBoardDb(database, 4 , 1);
		board.Board = new Color[][] {{null}, {Color.GREEN}, {Color.GREEN}, {Color.GREEN}};
		board.addDisc(0);
		GameBoardDb.Result a = board.getResult();
		board.undoMove();
		board.rotateColor();
		board.setWinner_name("Winner");
		board.addDisc(0);
		GameBoardDb.Result b = board.getResult();

		assertNotEquals(a,b);
	}
	
	//8
	@Test
	public void testLoadGameEmpty() {
		//Mock
		database = EasyMock.createNiceMock(IDatabase.class);
		//Expects
		EasyMock.expect(database.noConnection()).andReturn(false);
		List<Moves> moves = new ArrayList<Moves>();
		EasyMock.expect(database.getListOfMoves()).andReturn(moves);
		replay(database);
		//Act
		board = new GameBoardDb(database, 3, 3);
		board.saveGame();
		board.Board = null;
		board.loadGame();
		//Verify
		assertNotNull(board.Board);
	} 
	//8 - verify
	@Test
	public void verifyLoadGameEmpty() {
		//Mock
		database = EasyMock.createNiceMock(IDatabase.class);
		//Expects
		EasyMock.expect(database.noConnection()).andReturn(false);
		List<Moves> moves = new ArrayList<Moves>();
		EasyMock.expect(database.getListOfMoves()).andReturn(moves);
		replay(database);
		//Act
		board = new GameBoardDb(database, 3, 3);
		board.saveGame();
		board.Board = null;
		board.loadGame();
		//Verify
		EasyMock.verify(database);
	} 
}
