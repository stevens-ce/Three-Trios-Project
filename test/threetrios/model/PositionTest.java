package threetrios.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests that a Position object correctly stores values.
 */
public class PositionTest {
  private Position position;

  @Before
  public void setUp() {
    position = new Position(2, 3); // Example position
  }

  @Test
  public void testGetRow() {
    {
      assertEquals(2, position.getRow());
    }
  }

  @Test
  public void testGetCol() {
    assertEquals(3, position.getCol());
  }

  @Test
  public void testConstructor() {
    Position pos = new Position(0, 0);
    assertEquals(0, pos.getRow());
    assertEquals(0, pos.getCol());

    pos = new Position(5, 10);
    assertEquals(5, pos.getRow());
    assertEquals(10, pos.getCol());
  }

}