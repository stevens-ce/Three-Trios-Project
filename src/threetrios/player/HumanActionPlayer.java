package threetrios.player;

import threetrios.controller.Feature;
import threetrios.view.View;

/**
 * Shell version of ActionPlayer for when player is human, i.e. all actions come through view.
 */
public class HumanActionPlayer implements ActionPlayer {
  private final View view;

  public HumanActionPlayer(View view) {
    this.view = view;
  }

  @Override
  public void addFeature(Feature feature) {
    // does nothing: handled by view
  }

  @Override
  public void makeMove() {
    // does nothing: handled by view
  }

  @Override
  public void playerMessenger(String message) {
    if (message.contains("highlight")) {
      try {
        String[] parts = message.split(" ");
        int idx = Integer.parseInt(parts[parts.length - 1]);
        view.highlightCard(parts[0], idx);
      } catch (NumberFormatException ex) {
        // should be nonexistent case
      }
    } else {
      view.displayMessage(message);
    }
  }
}