package threetrios.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/**
 * tests GridCell methods.
 */
public class GridCellTest {

  private GridCell placeableCell;
  private GridCell nonPlaceableCell;
  private Card testCard;


  @Before
  public void setUp() {
    placeableCell = new GridCell(true);
    nonPlaceableCell = new GridCell(false);
    testCard = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "TestCard");
  }

  @Test
  public void testIsPlaceable() {
    assertTrue("Placeable cell should return true", placeableCell.isPlaceable());
    assertFalse("Non-placeable cell should return false", nonPlaceableCell.isPlaceable());
  }

  @Test
  public void testPlaceCard() {
    assertNull("Placeable cell should be empty initially", placeableCell.getCard());
    placeableCell.placeCard(testCard, Color.RED);
    assertEquals("Card should be placed in the cell", testCard, placeableCell.getCard());
    assertEquals("Card placement dyes cell", Color.RED, placeableCell.getColor());
  }

  @Test
  public void testPlaceCardInNonPlaceableCell() {
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      nonPlaceableCell.placeCard(testCard, Color.BLUE);
    });
    assertEquals("This cell cannot contain a card", exception.getMessage());
  }

  @Test
  public void testPlaceCardInFullCell() {
    placeableCell.placeCard(testCard, Color.BLUE);

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      placeableCell.placeCard(testCard, Color.BLUE);
    });
    assertEquals("This cell is full", exception.getMessage());
  }

  @Test
  public void testPlaceNullCard() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      placeableCell.placeCard(null, Color.RED);
    });
    assertEquals("placeCard param null.", exception.getMessage());
  }

  @Test
  public void testPlaceNullColor() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      placeableCell.placeCard(testCard, null);
    });
    assertEquals("placeCard param null.", exception.getMessage());
  }

  @Test
  public void testFlip_ValidColor() {
    placeableCell.placeCard(testCard, Color.BLUE);
    assertEquals(Color.BLUE, placeableCell.getColor());
    placeableCell.flip();
    assertEquals(Color.RED, placeableCell.getColor());
  }

  @Test(expected = IllegalStateException.class)
  public void testFlip_NoCard() {
    placeableCell.flip();
  }

  @Test(expected = IllegalStateException.class)
  public void testFlip_NotPlaceable() {
    nonPlaceableCell.flip();
  }

  @Test
  public void testIsValidHole() {
    assertFalse(nonPlaceableCell.isValid());
  }

  @Test
  public void testIsValidEmptyThenFull() {
    assertTrue(placeableCell.isValid());
    placeableCell.placeCard(testCard, Color.BLUE);
    assertFalse(placeableCell.isValid());
  }

}