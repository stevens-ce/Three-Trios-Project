package threetrios.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * tests CardinalCard methods.
 */
public class CardinalCardTest {
  private CardinalValues createMockCardinalValues() {
    return new CardinalValues(5, 3, 1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_NullValues() {
    new CardinalCard(null, "Card1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor_NullIdentifier() {
    CardinalValues values = createMockCardinalValues();
    new CardinalCard(values, null);
  }

  @Test
  public void testConstructor_ValidParameters() {
    CardinalValues values = createMockCardinalValues();
    CardinalCard card = new CardinalCard(values, "Card1");
    assertNotNull(card);
    assertEquals("Card1", card.getIdentifier());
  }

  @Test
  public void testEquals_SameIdentifier() {
    CardinalValues values = createMockCardinalValues();
    CardinalValues diffValues = new CardinalValues(7, 4, 9, 1);
    CardinalCard card1 = new CardinalCard(values, "Card1");
    CardinalCard card2 = new CardinalCard(diffValues, "Card1");
    assertEquals(card1, card2);
  }

  @Test
  public void testEquals_DifferentIdentifier() {
    CardinalValues values = createMockCardinalValues();
    CardinalCard card1 = new CardinalCard(values, "Card1");
    CardinalCard card2 = new CardinalCard(values, "Card2");
    assertNotEquals(card1, card2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardinalValue_InvalidDirection() {
    CardinalValues values = createMockCardinalValues();
    CardinalCard card = new CardinalCard(values, "Card1");
    card.getCardinalValue(null);
  }

  @Test
  public void testGetCardinalValue() {
    CardinalValues values = createMockCardinalValues();
    CardinalCard card = new CardinalCard(values, "Card1");

    assertEquals(new CardinalAttackValue(Direction.NORTH, AttackValue.FIVE),
            card.getCardinalValue(Direction.NORTH));

    assertEquals(new CardinalAttackValue(Direction.SOUTH, AttackValue.THREE),
            card.getCardinalValue(Direction.SOUTH));

    assertEquals(new CardinalAttackValue(Direction.EAST, AttackValue.ONE),
            card.getCardinalValue(Direction.EAST));

    assertEquals(new CardinalAttackValue(Direction.WEST, AttackValue.TWO),
            card.getCardinalValue(Direction.WEST));
  }
}