package threetrios.controller;

import java.io.IOException;

import threetrios.model.Color;

/**
 * controller mock to test feature.
 */
public class ControllerMock implements ThreeTriosController {
  Appendable transcript;

  public ControllerMock(Appendable out) {
    this.transcript = out;
  }

  @Override
  public boolean validCardClick(int cardIdx, Color hand) {
    addToTranscript("valid click " + cardIdx + " " + hand);
    return true;
  }

  @Override
  public void cardPlayed(int cardIdx, int row, int col) {
    addToTranscript("card played " + cardIdx + " " + row + " " + col);
  }

  @Override
  public void updateView() {
    // not relevant to feature test
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
