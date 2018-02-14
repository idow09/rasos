package rasos;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class GameEndCheckerTest {
    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void emptyBoardIsEndingTheGame() {
        assertTrue("Game should end when the board is empty.", new GameEndChecker().isEndOfGame(new Board(5)));
    }

    @Test
    public void gameIsContinuedWhenBoardIsNotEmpty() {
        Board board = new Board(5);
        board.populateHomeBases(11);
        assertFalse("Game should continue when the board is not empty.", new GameEndChecker().isEndOfGame(board));
    }

    @Test
    public void gameIsEndedWhenOnePlayerDoesNotHaveSoldiersLeft() {
        Board board = new Board(5);
        board.cellAt(2, 4).setValues(1, 9);
        assertTrue("Game should end when a player does not have soldiers.", new GameEndChecker().isEndOfGame(board));
    }

    @Test
    public void emptyBoardHasNoWinner() {
        assertEquals("WinnerId should be zero for an empty board.", 0, new GameEndChecker().getWinnerId(new Board(5)));
    }

    @Test
    public void whenAPlayerDoesNotHaveSoldiersLeftHeLoses() {
        int id = 2;
        Board board = new Board(5);
        board.cellAt(3, 1).setValues(id, 12);
        String message = String.format("WinnerId should be %d.", id);
        assertEquals(message, id, new GameEndChecker().getWinnerId(board));
    }

    @Test
    public void getWinnerIdThrowsWhenItIsNotEndOfGame() {
        String message = "Should throw an exception when trying to get winnerId for an ongoing game.";
        expectedEx.expect(RuntimeException.class);
        expectedEx.reportMissingExceptionWithMessage(message);
        Board board = new Board(5);
        board.populateHomeBases(6);
        new GameEndChecker().getWinnerId(board);
    }
}