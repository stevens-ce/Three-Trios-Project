package threetrios.controller;

import threetrios.player.ActionPlayer;

import java.io.IOException;

/**
 * ActionPlayer mock used to test controller.
 */
public class ActionPlayerMock implements ActionPlayer {
  private final Appendable transcript;

  /**
   * constructor.
   * @param out appendable
   */
  public ActionPlayerMock(Appendable out) {
    if (out == null) {
      throw new IllegalArgumentException();
    }
    transcript = out;
  }

  @Override
  public void addFeature(Feature feature) {
    addToTranscript("player: feature added");
  }

  @Override
  public void makeMove() {
    addToTranscript("make move");
  }

  @Override
  public void playerMessenger(String message) {
    addToTranscript("player messenger: " + message);
  }

  private void addToTranscript(String record) {
    try {
      transcript.append(record);
      transcript.append("\n");
    } catch (IOException ex) {
      // who cares
    }
  }
}
