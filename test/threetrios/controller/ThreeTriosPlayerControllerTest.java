package threetrios.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import threetrios.model.ThreeTriosModelMessenger;
import threetrios.model.Color;

import threetrios.view.View;

import threetrios.controller.ThreeTriosPlayerController;

import threetrios.player.ActionPlayer;

/**
 * tests controller methods via mocks.
 */
public class ThreeTriosPlayerControllerTest {

  private ThreeTriosModelMessenger model;
  private ThreeTriosPlayerController controller;
  private ThreeTriosPlayerController controller2;

  private StringBuilder transcript;
  private StringBuilder transcript2;

  private View view2;
  private ActionPlayer player2;

  private final String SETUP_TRANSCRIPT = "set receiver\n" + "view: feature added\n"
          + "player: feature added\n";


  @Before
  public void setUp() {
    transcript = new StringBuilder();
    View view = new ViewMock(transcript);
    ActionPlayer player = new ActionPlayerMock(transcript);
    model = new MockThreeTriosModelMessenger(transcript);
    controller = new ThreeTriosPlayerController(player, view, model, Color.RED);

    transcript2 = new StringBuilder();
    view2 = new ViewMock(transcript2);
    player2 = new ActionPlayerMock(transcript2);
  }


  @Test
  public void messageSentMakeMoveRedTurn() {
    controller.messageSent("Red: it's your turn.");
    controller.messageSent("Make next move.");
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "player messenger: It's your turn.\n"
            + "make move\n";
    assertEquals(expected, lines);
  }

  @Test
  public void messageSentMakeMoveBlueTurn() {
    controller.messageSent("Blue: it's your turn.");
    controller.messageSent("Make next move.");
    String lines = transcript.toString();
    assertEquals(SETUP_TRANSCRIPT, lines);
  }

  @Test
  public void messageSentUpdateGrid() {
    controller.messageSent("Update grid state.");
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT + "refresh\n";
    assertEquals(expected, lines);
  }

  @Test
  public void controllersReceiveSendMessageAll() {
    controller2 = new ThreeTriosPlayerController(player2, view2, model, Color.BLUE);
    model.sendMessageAll("Update grid state.");
    String lines = transcript.toString();
    String lines2 = transcript2.toString();
    String expected = SETUP_TRANSCRIPT + "set receiver\n"
            + "sending message: Update grid state.\n" + "refresh\n";
    String expected2 = "view: feature added\n" + "player: feature added\n" + "refresh\n";

    assertEquals("controller 1", expected, lines);
    assertEquals("controller 2", expected2, lines2);
  }

  @Test
  public void turnControllerMakesMove() {
    controller2 = new ThreeTriosPlayerController(player2, view2, model, Color.BLUE);
    model.sendMessageAll("Red: it's your turn.");
    model.sendMessageAll("Make next move.");
    String lines = transcript.toString();
    String lines2 = transcript2.toString();
    String expected = SETUP_TRANSCRIPT + "set receiver\n"
            + "sending message: Red: it's your turn.\n"
            + "sending message: Make next move.\n"
            + "player messenger: It's your turn.\n"
            + "make move\n";
    String expected2 = "view: feature added\n"
            + "player: feature added\n";

    assertEquals("controller 1", expected, lines);
    assertEquals("controller 2", expected2, lines2);
  }

  @Test
  public void gameOverMessageBothControllers() {
    controller2 = new ThreeTriosPlayerController(player2, view2, model, Color.BLUE);
    model.sendMessageAll("Game is Over! Red wins!");
    String lines = transcript.toString();
    String lines2 = transcript2.toString();
    String expected = SETUP_TRANSCRIPT + "set receiver\n"
            + "sending message: Game is Over! Red wins!\n"
            + "display: Game is Over! Red wins!\n";
    String expected2 = "view: feature added\n"
            + "player: feature added\n"
            + "display: Game is Over! Red wins!\n";

    assertEquals("controller 1", expected, lines);
    assertEquals("controller 2", expected2, lines2);
  }

  @Test
  public void recognizedMessageWithGibberish() {
    controller.messageSent("Game is Over! The reckoning of god is upon us");
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "display: Game is Over! The reckoning of god is upon us\n";

    assertEquals("game over acts as expected", expected, lines);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notRecognizedCommandSent() {
    controller.messageSent("you fool. you absolute madwoman.");
  }

  @Test
  public void validCardClickWrongHand() {
    boolean invalid = controller.validCardClick(0, Color.BLUE);
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "player messenger: You can't select other player's card.\n";

    assertFalse(invalid);
    assertEquals(expected, lines);
  }

  @Test
  public void validCardClickNotTurn() {
    boolean invalid = controller.validCardClick(0, Color.RED);
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "player messenger: You can't select a card right now.\n";

    assertFalse(invalid);
    assertEquals(expected, lines);
  }

  @Test
  public void validCardClickInvalidIndexNeg() {
    controller.messageSent("Red: it's your turn.");
    boolean valid = controller.validCardClick(-1, Color.RED);
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "player messenger: I didn't think this was possible? NO.\n";

    assertFalse(valid);
    assertEquals(expected, lines);
  }

  @Test
  public void validCardClickInvalidIndex() {
    controller.messageSent("Red: it's your turn.");
    boolean valid = controller.validCardClick(4, Color.RED);
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "player messenger: I didn't think this was possible? NO.\n";

    assertFalse(valid);
    assertEquals(expected, lines);
  }

  @Test
  public void validCardClickIsValid() {
    controller.messageSent("Red: it's your turn.");
    boolean valid = controller.validCardClick(0, Color.RED);
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "player messenger: Red highlight 0\n";

    assertTrue(valid);
    assertEquals(expected, lines);
  }

  @Test
  public void cardPlayedSuccessful() {
    controller.cardPlayed(0, 1, 1);
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "getCardFromHand called\n"
            + "play card token card 1 1\n";
    assertEquals(expected, lines);
  }

  @Test
  public void cardPlayedFailure() {
    controller.cardPlayed(0, 5, 5);
    String lines = transcript.toString();
    String expected = SETUP_TRANSCRIPT
            + "getCardFromHand called\n"
            + "play card token card 5 5\n"
            + "player messenger: Can't play there.\n"
            + "player messenger: Red highlight 0\n"
            + "make move\n";
    assertEquals(expected, lines);
  }

}