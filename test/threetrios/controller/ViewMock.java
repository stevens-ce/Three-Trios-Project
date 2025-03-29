package threetrios.controller;

import threetrios.view.View;

import java.io.IOException;

/**
 * View mock used to test controller.
 */
public class ViewMock implements View {
  private final Appendable transcript;

  /**
   * constructor.
   * @param out appendable
   */
  public ViewMock(Appendable out) {
    if (out == null) {
      throw new IllegalArgumentException();
    }
    transcript = out;
  }

  @Override
  public void refresh() {
    addToTranscript("refresh");
  }

  @Override
  public void addFeature(Feature feature) {
    addToTranscript("view: feature added");
  }

  @Override
  public void displayMessage(String s) {
    addToTranscript("display: " + s);
  }

  @Override
  public void clickToFeature(String command) {
    // not relevant for this test
  }

  @Override
  public void highlightCard(String player, int idx) {
    // not relevant
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