package threetrios.player;

import threetrios.controller.Feature;
import threetrios.model.Color;

/**
 * Mock implementation of the Feature interface for testing.
 */
public class FeatureMock implements Feature {

  private StringBuilder transcript = new StringBuilder();

  @Override
  public void cardSelected(int cardIdx, Color player) {
    addToTranscript("Card clicked at index " + cardIdx + " in " + player + " hand");
  }

  @Override
  public void cellSelected(int row, int col) {
    addToTranscript("Grid cell clicked: (" + row + ", " + col + ")");
  }

  @Override
  public Color getAssociatedColor() {
    return null;
  }

  public String getTranscript() {
    return transcript.toString();
  }

  /**
   * Returns the transcript of all actions taken by the feature.
   *
   * @param record to add to transcript.
   */
  //use this instead of calling append on my own
  private void addToTranscript(String record) {
    try {
      transcript.append(record);
      transcript.append("\n");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

