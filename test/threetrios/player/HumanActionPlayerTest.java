package threetrios.player;

import org.junit.Before;
import org.junit.Test;

import threetrios.controller.ViewMock;

import static org.junit.Assert.assertTrue;

/**
 * Tests for HumanActionPlayer class.
 */
public class HumanActionPlayerTest {

  private Appendable transcript;
  private HumanActionPlayer player;


  @Before
  public void setup() {
    transcript = new StringBuilder();
    ViewMock viewMock = new ViewMock(transcript);
    player = new HumanActionPlayer(viewMock);
  }


  @Test
  public void testPlayerMessenger_displayMessage() {
    player.playerMessenger("Game Over!");
    assertTrue(transcript.toString().contains("display: Game Over!"));
  }

  @Test
  public void testPlayerMessenger_highlightCard() {
    player.playerMessenger("Red highlight 3");
    assertTrue(transcript.toString().contains("highlight: Red 3"));
  }

  @Test
  public void testPlayerMessenger_highlightCard_invalid() {
    player.playerMessenger("Red highlight 3a");
    assertTrue(transcript.toString().isEmpty());
  }

  @Test
  public void testPlayerMessenger_emptyMessage() {
    player.playerMessenger("");
    assertTrue(transcript.toString().contains("display: "));
  }

  @Test
  public void testPlayerMessenger_nonHighlightMessage() {
    player.playerMessenger("Player 1 wins!");
    assertTrue(transcript.toString().contains("display: Player 1 wins!"));
  }

  @Test
  public void testPlayerMessenger_multipleHighlightCalls() {
    player.playerMessenger("Red highlight 2");
    player.playerMessenger("Blue highlight 5");
    assertTrue(transcript.toString().contains("highlight: Red 2"));
    assertTrue(transcript.toString().contains("highlight: Blue 5"));
  }

  @Test
  public void testPlayerMessenger_highlightWithoutIndex() {
    player.playerMessenger("Red highlight");
    assertTrue(transcript.toString().isEmpty());
  }

  @Test(expected = NullPointerException.class)
  public void testPlayerMessenger_nullMessage() {
    player.playerMessenger(null);
  }


}