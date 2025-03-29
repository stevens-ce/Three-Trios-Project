package threetrios.model;

import threetrios.controller.ConfigurationFileReader;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * tests grid methods.
 */
public class GridTest {

  private Grid grid;
  private Grid solidGrid;

  @Before
  public void setUp() throws IOException {
    ConfigurationFileReader reader = new ConfigurationFileReader();
    File gridFile = File.createTempFile("testGrid", ".txt");
    FileWriter writer = new FileWriter(gridFile);
    writer.write("3 3\n");
    writer.write("CXX\n");
    writer.write("CXC\n");
    writer.write("XXX\n");
    writer.close();

    grid = new Grid(reader.readFile(gridFile.getAbsolutePath()));
    gridFile.deleteOnExit();

    String[] solidConfig = reader.readFile("hw5" + File.separator + "docs" + File.separator
            + "board_reachable.txt");
    solidGrid = new Grid(solidConfig);
  }

  @Test
  public void testGridInitialization() {
    assertNotNull(grid);
    assertEquals(3, grid.getCardCellCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCellAtNegativeRow() {
    grid.cellAt(new Position(-5, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCellAtNegativeCol() {
    grid.cellAt(new Position(0, -5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCellAtExcessiveRow() {
    grid.cellAt(new Position(5, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCellAtExcessiveCol() {
    grid.cellAt(new Position(0, 5));
  }

  @Test
  public void testCountCellsOfColor() {
    Card card1 = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    Card card2 = new CardinalCard(new CardinalValues(2, 3, 4, 5), "Card2");
    grid.place(card1, new Position(0, 0), Color.RED);
    grid.place(card2, new Position(1, 0), Color.BLUE);
    assertEquals(1, grid.countCellsOfColor(Color.RED));
  }

  @Test
  public void testPlaceCard() {
    Card card = new CardinalCard(new CardinalValues(1, 2, 3, 4), "Card1");
    Position position = new Position(0, 0);
    grid.place(card, position, Color.BLUE);
    assertEquals(card, grid.cellAt(new Position(0, 0)).getCard());
  }

  @Test
  public void testPlaceCardInInvalidPosition() {
    Card card = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    Position invalidPosition = new Position(-1, -1);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      grid.place(card, invalidPosition, Color.RED);
    });
    assertEquals("Position is out of grid bounds.", exception.getMessage());
  }

  @Test
  public void testAreAllCellsOccupied() {
    assertFalse(grid.areAllCellsOccupied());

    Card card1 = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    Card card2 = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card2");

    grid.place(card1, new Position(0, 0), Color.RED);
    grid.place(card2, new Position(1, 0), Color.BLUE);
    assertFalse(grid.areAllCellsOccupied());
    Card card3 = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card3");
    grid.place(card3, new Position(1, 2), Color.RED);
    assertTrue(grid.areAllCellsOccupied());
  }

  @Test
  public void testEngageAdjacent() {
    Card attacker = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    Card defender = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card2");

    grid.place(attacker, new Position(0, 0), Color.BLUE);
    grid.place(defender, new Position(1, 0), Color.RED);
    grid.engageAdj(attacker);
    // attacker retains color
    assertEquals(Color.BLUE, grid.cellAt(new Position(0, 0)).getColor());
    //oppnent's color flips
    assertEquals(Color.BLUE, grid.cellAt(new Position(1, 0)).getColor());
  }

  @Test
  public void testInvalidConfigNotEnoughRows() {
    String[] badArray = {"9 2", "XCC", "CCC"};
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Grid(badArray);
    });
    assertEquals("not enough rows to construct grid.", exception.getMessage());
  }

  @Test
  public void testInvalidConfigNotEnoughCols() {
    String[] badArray = {"2 4", "XCC", "CCC"};
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Grid(badArray);
    });
    assertEquals("not enough cols to construct grid.", exception.getMessage());
  }

  @Test
  public void testConfigInvalidSymbols() {
    String[] badArray = {"2 2", "CX", "CO"};
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Grid(badArray);
    });
    assertEquals("unrecognized symbol on grid.", exception.getMessage());
  }

  @Test
  public void testAllCardsOnGridEmpty() {
    Map<Position, GridCell> emptyMap = grid.allCardsOnGrid();
    assertTrue(emptyMap.isEmpty());
  }

  @Test
  public void testAllCardsOnGridFull() {
    Card card1 = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    Card card2 = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card2");
    Card card3 = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card3");

    grid.cellAt(new Position(0, 0)).placeCard(card1, Color.RED);
    grid.cellAt(new Position(1, 0)).placeCard(card2, Color.RED);
    grid.cellAt(new Position(1, 2)).placeCard(card3, Color.RED);

    Map<Position, GridCell> fullMap = grid.allCardsOnGrid();

    for (Position pos : fullMap.keySet()) {
      assertEquals(grid.cellAt(pos), fullMap.get(pos));
    }

    assertEquals(grid.getCardCellCount(), fullMap.size());
  }

  // test that card can flip >1 card -> chain reaction
  @Test
  public void testPlayCardChainFlip() {
    Card red1 = new CardinalCard(new CardinalValues(4, 4, 4, 4),
            "RED1");
    Card red2 = new CardinalCard(new CardinalValues(1, 1, 1, 1),
            "RED2");
    Card blue1 = new CardinalCard(new CardinalValues(10, 10, 10, 10),
            "BLUE1");

    solidGrid.place(red1, new Position(1, 1), Color.RED);
    solidGrid.place(red2, new Position(2, 1), Color.RED);

    assertEquals(2, solidGrid.countCellsOfColor(Color.RED));
    assertEquals(0, solidGrid.countCellsOfColor(Color.BLUE));

    solidGrid.place(blue1, new Position(0, 1), Color.BLUE);
    solidGrid.engageAdj(blue1);

    assertEquals(0, solidGrid.countCellsOfColor(Color.RED));
    assertEquals(3, solidGrid.countCellsOfColor(Color.BLUE));
  }
}
