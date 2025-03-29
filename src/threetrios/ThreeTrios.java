package threetrios;

import java.util.Random;

import threetrios.controller.ConfigurationFileReader;
import threetrios.controller.ThreeTriosPlayerController;
import threetrios.model.Color;
import threetrios.model.ThreeTriosBasicModel;
import threetrios.player.ActionPlayer;
import threetrios.view.ThreeTriosView;
import threetrios.controller.ThreeTriosController;

/**
 * This is the main class for the Three trios game.
 */
public class ThreeTrios {
  /**
   * This sets up the model and view and runs the game.
   *
   * @param args array of strings. Basic main param.
   */
  public static void main(String[] args) {
    if (args.length == 2) {
      Random rand = new Random(3);
      ThreeTriosBasicModel model = new ThreeTriosBasicModel(rand);

      ThreeTriosView redView = new ThreeTriosView(model);
      ThreeTriosView blueView = new ThreeTriosView(model);

      ConstructPlayer factory = new ConstructPlayer();

      ActionPlayer redPlayer = factory.createPlayer(args[0], model, redView, Color.RED);
      ActionPlayer bluePlayer = factory.createPlayer(args[1], model, blueView, Color.BLUE);

      ThreeTriosController blueControl
              = new ThreeTriosPlayerController(bluePlayer, blueView, model, Color.BLUE);
      ThreeTriosController redControl
              = new ThreeTriosPlayerController(redPlayer, redView, model, Color.RED);

      ConfigurationFileReader reader = new ConfigurationFileReader();
      model.setup(reader.readFile("docs/board_standard_n.txt"),
              reader.readFile("docs/full_card_deck.txt"));
    } else {
      throw new IllegalStateException("game cannot be setup.");
    }
  }
}
