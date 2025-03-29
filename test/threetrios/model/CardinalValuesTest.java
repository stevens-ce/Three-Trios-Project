package threetrios.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests CardinalValues methods, that it stores values correctly.
 */
public class CardinalValuesTest {
  @Test
  public void testConstructor_WithAttackValues() {
    CardinalValues values = new CardinalValues(AttackValue.ONE, AttackValue.TWO,
            AttackValue.THREE, AttackValue.FOUR);
    assertEquals(AttackValue.ONE, values.getNorth().attackValue);
    assertEquals(AttackValue.TWO, values.getSouth().attackValue);
    assertEquals(AttackValue.THREE, values.getEast().attackValue);
    assertEquals(AttackValue.FOUR, values.getWest().attackValue);
  }

  @Test
  public void testConstructor_WithIntegers() {
    CardinalValues values = new CardinalValues(1, 2, 3, 4);
    assertEquals(AttackValue.ONE, values.getNorth().attackValue);
    assertEquals(AttackValue.TWO, values.getSouth().attackValue);
    assertEquals(AttackValue.THREE, values.getEast().attackValue);
    assertEquals(AttackValue.FOUR, values.getWest().attackValue);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_InvalidAttackValue_Negative() {
    new CardinalValues(-1, 2, 3, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_InvalidAttackValue_Zero() {
    new CardinalValues(0, 2, 3, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_InvalidAttackValue_ExceedsMax() {
    new CardinalValues(11, 2, 3, 4);
  }
}