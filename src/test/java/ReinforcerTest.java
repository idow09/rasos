import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;

public class ReinforcerTest {

    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();
    private Reinforcer reinforcer;
    private Board board;

    @Before
    public void setUp() throws Exception {
        reinforcer = new Reinforcer();
        board = new Board(18);
    }

    @Test
    @Ignore
    public void ignoresNullReinforcementMoves() {
        reinforcer.apply(7, 52, null, new Board(16));
    }

    @Test
    public void appliesReinforcementSimplestCase_playerA() {
        testAppliesReinforcementSimplestCase(1);
    }

    @Test
    public void appliesReinforcementSimplestCase_playerB() {
        testAppliesReinforcementSimplestCase(2);
    }

    @Test
    public void throwsOnTryToReinforceACellThatYouDoNotControl() throws Exception {
        board.setCell(1, 1, new Cell(1, 4));

        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("You cannot reinforce a cell that you don't control");

        reinforcer.apply(1, 1, Collections.singleton(new ReinforcementMove(3, 1, 1)), board);
    }

    @Test(expected = RuntimeException.class)
    public void reinforceOnlyTheAmountOfSoldiersYouAreAllowed() throws Exception {
        board.setCell(3, 1, new Cell(1, 4));
        reinforcer.apply(1, 1, Collections.singleton(new ReinforcementMove(3, 1, 5)), board);
    }

    @Test(expected = RuntimeException.class)
    public void sumOfReinforcementsPerTurnIsCoherentWithPredefinedAmount() throws Exception {
        board.setCell(3, 2, new Cell(2, 7));
        board.setCell(3, 1, new Cell(2, 3));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 2));

        reinforcer.apply(2, 2, moves, board);
    }

    @Test
    public void allowsMultipleReinforcementMoves() throws Exception {
        board.setCell(3, 2, new Cell(2, 7));
        board.setCell(3, 1, new Cell(2, 3));
        ArrayList<ReinforcementMove> moves = new ArrayList<>();
        moves.add(new ReinforcementMove(3, 2, 1));
        moves.add(new ReinforcementMove(3, 1, 1));

        reinforcer.apply(2, 2, moves, board);

        TestUtils.assertCellContents(board.cellAt(3, 2), 2, 8);
        TestUtils.assertCellContents(board.cellAt(3, 1), 2, 4);
    }

    private void testAppliesReinforcementSimplestCase(int playerId) {
        int soldiers = 13;
        board.setCell(2, 3, new Cell(playerId, soldiers));

        reinforcer.apply(playerId, 1, Collections.singleton(new ReinforcementMove(2, 3, 1)), board);

        TestUtils.assertCellContents(board.cellAt(2, 3), playerId, soldiers + 1);
    }


}