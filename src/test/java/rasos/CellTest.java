package rasos;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static rasos.ColorUtils.*;
import static rasos.Config.ID_A;
import static rasos.Config.ID_B;

public class CellTest {

    @Rule
    public final ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void neutralCell() {
        assertTrue(Cell.neutral().isNeutral());
    }

    @Test
    public void nonNeutralCell() {
        assertFalse(new Cell(2, 50).isNeutral());
    }

    @Test
    public void validatesConstructorParamsOnCellCreation() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        new Cell(9, -9);
    }

    @Test
    public void createsCellWithCorrectValuesOnConstruction() {
        Cell cell = new Cell(5, 5);
        TestUtils.assertCellContents(cell, 5, 5);
    }

    @Test
    public void impossibleToCreateNeutralCellWithSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        new Cell(0, 12);
    }

    @Test
    public void impossibleToCreateControlledCellWithNoSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.CONTROLLED_CELL_WITH_ZERO_SOLDIERS_ERROR);
        new Cell(5, 0);
    }

    @Test
    public void controllingPlayerMustBeAPositiveNumber() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        new Cell(-5, 12);
    }

    @Test
    public void cellCannotContainNegativeNumberOfSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        new Cell(2, -70);
    }

    @Test
    public void setValuesThrowsOnNeutralCellContainingSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(0, 12);
    }

    @Test
    public void setValuesThrowsOnControlledCellWithZeroSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.CONTROLLED_CELL_WITH_ZERO_SOLDIERS_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(5, 0);
    }

    @Test
    public void setValuesThrowsOnNegativeControllingPlayerId() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_CONTROLLING_PLAYER_ID_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(-5, 12);

    }

    @Test
    public void setValuesThrowsOnNegativeAmountOfSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        Cell cell = new Cell(0, 0);
        cell.setValues(2, -70);
    }

    @Test
    public void getNumSoldiers() {
        assertEquals(32, new Cell(3, 32).getNumSoldiers());
    }

    @Test
    public void getControllingPlayerId() {
        assertEquals(3, new Cell(3, 32).getControllingPlayerId());
    }

    @Test
    public void isControlledBy() {
        assertTrue(new Cell(12, 456).isControlledBy(12));
    }

    @Test
    public void updateNumSoldiers() {
        Cell cell = new Cell(3, 32);
        cell.updateNumSoldiers(13);
        assertEquals(13, cell.getNumSoldiers());
    }

    @Test
    public void cannotSetNegativeNumSoldiers() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        Cell cell = new Cell(3, 32);
        cell.updateNumSoldiers(-43);
    }

    @Test
    public void cannotUpdateNumSoldiersToNeutralCell() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEUTRAL_CELL_CONTAINING_SOLDIERS_ERROR);
        Cell cell = new Cell(0, 0);
        cell.updateNumSoldiers(5);
    }

    @Test
    public void whenNumSoldiersSetToZeroCellBecomesNeutral() {
        Cell cell = new Cell(5, 12);
        cell.updateNumSoldiers(0);
        assertTrue(cell.isNeutral());
    }

    @Test
    public void setValues() {
        Cell cell = new Cell(3, 5);
        cell.setValues(2, 4);
        TestUtils.assertCellContents(cell, 2, 4);
    }

    @Test
    public void makeNeutral() {
        Cell cell = new Cell(3, 5);
        cell.makeNeutral();
        assertTrue("Cell should be neutral.", cell.isNeutral());
    }

    @Test
    public void cantSetValuesWithInvalidParams() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage(Cell.NEGATIVE_AMOUNT_OF_SOLDIERS_ERROR);
        Cell cell = new Cell(6, 7);
        cell.setValues(5, -1);
    }

    @Test
    public void humanReadableToString() {
        Cell player1Cell = new Cell(ID_A, 10);
        Cell player2Cell = new Cell(ID_B, 7);
        Cell neutralCell = Cell.neutral();

        assertThat(player1Cell.toString(), is(String.format("[%s]", colorString("10", ANSI_YELLOW))));
        assertThat(player2Cell.toString(), is(String.format("[%s]", colorString(" 7", ANSI_BLUE))));
        assertThat(neutralCell.toString(), is("[  ]"));
    }
}