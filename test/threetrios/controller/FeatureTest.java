package threetrios.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import threetrios.model.Color;

/**
 * tests for Feature interface.
 */
public class FeatureTest {
  private StringBuilder transcript;
  private Feature feature;

  @Before
  public void setUp() {
    transcript = new StringBuilder();
    ThreeTriosController controller = new ControllerMock(transcript);
    feature = new ViewPlayerFeature(controller, Color.RED);
  }

  @Test
  public void cardSelectedPlayedInCellSelected() {
    feature.cardSelected(1, Color.RED);
    feature.cellSelected(2, 2);
    String lines = transcript.toString();
    String expected = "valid click 1 Red\n"
            + "card played 1 2 2\n";

    assertEquals(expected, lines);
  }

  @Test
  public void selectCardTwiceCantPlay() {
    feature.cardSelected(1, Color.RED);
    feature.cardSelected(1, Color.RED);
    feature.cellSelected(1, 1);
    String lines = transcript.toString();
    String expected = "valid click 1 Red\n"
            + "valid click 1 Red\n";

    assertEquals(expected, lines);
  }

  @Test
  public void selectNewCard() {
    feature.cardSelected(1, Color.RED);
    feature.cardSelected(2, Color.RED);
    feature.cellSelected(1, 1);
    String lines = transcript.toString();
    String expected = "valid click 1 Red\n"
            + "valid click 2 Red\n"
            + "card played 2 1 1\n";

    assertEquals(expected, lines);
  }

  @Test
  public void testAssociatedColor() {
    assertEquals(Color.RED, feature.getAssociatedColor());
  }
}