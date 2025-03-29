package threetrios.controller;

import threetrios.model.Color;

/**
 * Feature implementation.
 */

public class ViewPlayerFeature implements Feature {
  private int cardIdx;
  private final ThreeTriosController controller;
  private final Color color;

  /**
   * Constructs a new ViewPlayerFeature.
   *
   * @param controller the controller.
   * @param color      the color.
   */
  public ViewPlayerFeature(ThreeTriosController controller, Color color) {
    cardIdx = -1;
    this.controller = controller;
    this.color = color;
  }

  @Override
  public void cardSelected(int cardIdx, Color hand) {
    if (controller.validCardClick(cardIdx, hand)) {
      if (this.cardIdx == cardIdx) {
        this.cardIdx = -1;
      } else {
        this.cardIdx = cardIdx;
      }
    }
  }

  @Override
  public void cellSelected(int row, int col) {
    if (this.cardIdx >= 0) {
      int playIdx = this.cardIdx;
      this.cardIdx = -1;
      controller.cardPlayed(playIdx, row, col);
    }
  }

  @Override
  public Color getAssociatedColor() {
    return color;
  }
}