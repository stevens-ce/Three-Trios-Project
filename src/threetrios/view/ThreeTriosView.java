package threetrios.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import threetrios.controller.Feature;
import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.Color;

import javax.swing.JOptionPane;


/**
 * This class represents the GUI for the ThreeTrios game. It implements the View interface and
 * extends the JFrame class. It sets up and displays the components/panels for the GUI.
 */
public class ThreeTriosView extends JFrame implements View {
  private final ReadOnlyThreeTriosModel model;
  private JHandPanel redHandPanel; //JPanel or HandPanel
  private JHandPanel blueHandPanel;
  private JPanel gridPanel;
  private Feature feature;

  /**
   * Constructor for the ThreeTriosView class. Initializes the read-only model to be displayed.
   *
   * @param model the read-only model to be displayed.
   */
  public ThreeTriosView(ReadOnlyThreeTriosModel model) {
    this.model = model;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // centering frame on to the screen
    setLocationRelativeTo(null);
  }

  /**
   * Sets up and arranges the components/panels for the game, such as the hand and the grid panel.
   * The left panel is for the red player's hand, the right panel is the blue player's hand, and
   * the center is the grid/board panel.
   */
  private void setupPanels() {
    setTitle(feature.getAssociatedColor().toString() + " Player");
    // Left panel plater red
    redHandPanel = new HandPanel(Color.RED, model, this);
    add(redHandPanel, BorderLayout.WEST);

    // Right panel for player blue
    blueHandPanel = new HandPanel(Color.BLUE, model, this);
    add(blueHandPanel, BorderLayout.EAST);

    // Center panel for the grid
    gridPanel = new GridPanel(model, this);
    add(gridPanel, BorderLayout.CENTER);
  }

  @Override
  public void refresh() {
    // Check if the panels exist if not, create and set em up
    if (redHandPanel == null || blueHandPanel == null || gridPanel == null) {
      setVisible(true);
      return;
    }
    // Refresh Red Hand Panel
    remove(redHandPanel); // Remove the old panel
    // creating a new Panel i think
    redHandPanel = new HandPanel(Color.RED, model, this);
    //readding to layout
    add(redHandPanel, BorderLayout.WEST);

    // Refresh Blue Hand Panel
    remove(blueHandPanel); // Remove the old panel
    blueHandPanel = new HandPanel(Color.BLUE, model, this);
    add(blueHandPanel, BorderLayout.EAST);

    // Refresh Grid Panel
    gridPanel.repaint();

    //recalculate the layout
    revalidate();
    repaint();
  }


  @Override
  public void addFeature(Feature feature) {
    this.feature = feature;
  }

  @Override
  public void displayMessage(String s) {
    JOptionPane.showMessageDialog(this, s);
  }

  @Override
  public void setVisible(boolean b) {
    setupPanels();
    pack();
    super.setVisible(b);
  }

  @Override
  public void highlightCard(String player, int idx) {
    switch (Color.valueOf(player.toUpperCase())) {
      case RED:
        redHandPanel.highlightSelected(idx);
        break;
      case BLUE:
        blueHandPanel.highlightSelected(idx);
        break;
      default:
        throw new IllegalArgumentException("not a player in this game.");
    }
  }

  //
  @Override
  public void clickToFeature(String command) {
    String[] parts = command.split(" ");
    if (parts.length < 2) {
      throw new IllegalArgumentException("Invalid command format: " + command);
    }

    String action = parts[0]; // Either "Grid" or "Hand"
    int param1;
    try {
      param1 = Integer.parseInt(parts[1]); // Row or card index
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid command given: " + command);
    }

    switch (action) {
      case "Grid":
        if (parts.length != 3) {
          throw new IllegalArgumentException("Grid command missing arguments.");
        }
        int param2;
        try {
          param2 = Integer.parseInt(parts[2]); // Column
        } catch (NumberFormatException ex) {
          throw new IllegalArgumentException("Grid command missing col.");
        }
        feature.cellSelected(param1, param2);
        break;

      case "Hand":
        if (parts.length != 3) {
          throw new IllegalArgumentException("Hand command missing arguments.");
        }
        Color color = Color.valueOf(parts[2].toUpperCase()); // Player color
        feature.cardSelected(param1, color);
        break;
      default:
        throw new IllegalArgumentException("Unknown command action: " + action);
    }
  }

}