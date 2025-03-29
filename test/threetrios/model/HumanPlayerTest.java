package threetrios.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * tests the HumanPlayer implementation of Player.
 */
public class HumanPlayerTest {
  private HumanPlayer player;

  @Before
  public void setUp() {
    player = new HumanPlayer(Color.BLUE);
  }

  @Test
  public void testInitialization() {
    assertEquals(Color.BLUE, player.getColor());
    assertTrue(player.getHand().isEmpty());
    assertFalse(player.isTurn());
  }

  @Test
  public void testAddToHand() {
    Card card = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    player.addToHand(card);
    assertFalse(player.getHand().isEmpty());
    assertEquals(1, player.getHand().size());
    assertEquals(card, player.getHand().get(0));
  }

  @Test
  public void testRemoveFromHand() {
    Card card = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    player.addToHand(card);
    assertEquals(1, player.getHand().size());
    player.removeFromHand(card);
    assertTrue(player.getHand().isEmpty());
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      player.removeFromHand(card);
    });
    assertEquals("This card is not in hand.", exception.getMessage());
  }

  @Test
  public void testNextTurn() {
    assertFalse(player.isTurn());
    player.nextTurn();
    assertTrue(player.isTurn());
    player.nextTurn();
    assertFalse(player.isTurn());
  }


  @Test
  public void testGetHandReturnsCopy() {
    Card card = new CardinalCard(new CardinalValues(1, 2, 3, 4),
            "Card1");
    player.addToHand(card);
    assertEquals(1, player.getHand().size());
    List<Card> handCopy = player.getHand();
    handCopy.remove(0);
    assertEquals(1, player.getHand().size());
  }

  @Test
  public void testToString() {
    assertEquals("Blue Player", player.toString());
  }
}