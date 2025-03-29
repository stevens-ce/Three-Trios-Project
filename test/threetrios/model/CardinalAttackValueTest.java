package threetrios.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests CardinalAttackValue methods.
 */
public class CardinalAttackValueTest {

  @Test
  public void testConstructorInitialization() {
    Direction direction = Direction.NORTH;
    AttackValue attackValue = AttackValue.A;
    CardinalAttackValue cav = new CardinalAttackValue(direction, attackValue);
    assertEquals("Direction should be initialized correctly",
            direction, cav.direction);
    assertEquals("AttackValue should be initialized correctly",
            attackValue, cav.attackValue);
  }

  @Test
  public void testGetOppositeDirection_North() {
    CardinalAttackValue cav = new CardinalAttackValue(Direction.NORTH, AttackValue.FIVE);
    assertEquals("Opposite of NORTH should be SOUTH", Direction.SOUTH,
            cav.getOppositeDirection());
  }

  @Test
  public void testGetOppositeDirection_South() {
    CardinalAttackValue cav = new CardinalAttackValue(Direction.SOUTH, AttackValue.FIVE);
    assertEquals("Opposite of SOUTH should be NORTH", Direction.NORTH,
            cav.getOppositeDirection());
  }

  @Test
  public void testGetOppositeDirection_East() {
    CardinalAttackValue cav = new CardinalAttackValue(Direction.EAST, AttackValue.FIVE);
    assertEquals("Opposite of EAST should be WEST", Direction.WEST,
            cav.getOppositeDirection());
  }

  @Test
  public void testGetOppositeDirection_West() {
    CardinalAttackValue cav = new CardinalAttackValue(Direction.WEST, AttackValue.FIVE);
    assertEquals("Opposite of WEST should be EAST", Direction.EAST,
            cav.getOppositeDirection());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOppositeDirection_InvalidDirection() {
    Direction invalidDirection = null;
    CardinalAttackValue cav = new CardinalAttackValue(invalidDirection, AttackValue.FIVE);
    cav.getOppositeDirection();
  }

}