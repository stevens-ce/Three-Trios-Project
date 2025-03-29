package threetrios.player;

import org.junit.Before;
import org.junit.Test;

import threetrios.model.Color;

import threetrios.strategy.ModelMockMaxFlip;
import threetrios.strategy.TTStrategy;
import threetrios.strategy.MaxFlipStrategy;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/**
 * tests for MachineActionPlayer impl of ActionPlayer.
 */
public class MachineActionPlayerTest {

  private FeatureMock featureMock;
  private MachineActionPlayer player;
  private ModelMockMaxFlip mockModel;
  private StringBuilder modelTranscript;

  @Before
  public void setup() {
    modelTranscript = new StringBuilder();
    mockModel = new ModelMockMaxFlip(modelTranscript);
    String[] mockGrid = {"3 3", "CCC", "CCC", "CCC"};
    mockModel.setup(mockGrid, null);
    featureMock = new FeatureMock();
    TTStrategy strategy = new MaxFlipStrategy();
    Color color = Color.RED;
    player = new MachineActionPlayer(mockModel, strategy, color);
    player.addFeature(featureMock);
  }


  @Test
  public void testMakeMove() {
    player.makeMove();

    String featureTranscript = featureMock.getTranscript();
    assertTrue(featureTranscript.contains("Card clicked at index"));
    assertTrue(featureTranscript.contains("Grid cell clicked"));

    String modelTranscriptString = modelTranscript.toString();
    assertTrue(modelTranscriptString.contains("numCardsFlipped"));
  }

  @Test
  public void testMakeMoveWithoutFeature() {
    // Creating a player without adding a feature
    TTStrategy strategy = new MaxFlipStrategy();
    MachineActionPlayer playerWithoutFeature = new MachineActionPlayer(mockModel, strategy,
            Color.RED);

    assertThrows(IllegalStateException.class, playerWithoutFeature::makeMove);
  }

  @Test
  public void testMakeMoveWithMaxFlipStrategy() {
    player.makeMove();
    String modelTranscriptString = modelTranscript.toString();
    assertTrue(modelTranscriptString.contains("numCardsFlipped"));
    String featureTranscript = featureMock.getTranscript();
    assertTrue(featureTranscript.contains("Card clicked at index"));
    assertTrue(featureTranscript.contains("Grid cell clicked"));
  }

  @Test
  public void testMultipleMoves() {
    for (int i = 0; i < 3; i++) {
      player.makeMove();
    }
    String featureTranscript = featureMock.getTranscript();
    // 3 calls each to cardSelected and cellSelected
    assertTrue(featureTranscript.split("\n").length >= 6);

    String modelTranscriptString = modelTranscript.toString();
    assertTrue(modelTranscriptString.contains("numCardsFlipped"));
  }


}