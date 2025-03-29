package threetrios.view;

import javax.swing.JPanel;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.BasicStroke;
import java.awt.event.MouseListener;

import threetrios.model.CardinalCard;
import threetrios.model.ReadOnlyThreeTriosModel;
import threetrios.model.GridCell;

/**
 * This represents the grid of the ThreeTrios game. It extends the JPanel and it renders the grid
 * cells by differentiating between playable and non-playable cells. It also handles the cell
 * selection.
 */
public class GridPanel extends JPanel implements MouseListener {
  private static final int CELL_OUTLINE = 2;
  private static final int GRID_BORDER_STROKE = 4;
  private final ReadOnlyThreeTriosModel model;
  private final int gridRows;
  private final int gridCols;
  private boolean[][] playableCells;
  private final ThreeTriosView parentView;

  /**
   * This is a constructor for the grid panel. It initializes the model and sets its size,
   * background color, and has a mouser listener for cell selection.
   *
   * @param model the read-only model of the game.
   */
  public GridPanel(ReadOnlyThreeTriosModel model, ThreeTriosView parentView) {
    this.parentView = parentView;
    GridCell[][] grid = model.getGrid();
    this.model = model;
    this.gridCols = grid.length;
    this.gridRows = grid[0].length;
    setPreferredSize(new Dimension(800, 800));
    setBackground(Color.GRAY);

    initializePlayableCells();
    addMouseListener(this);
  }

  /**
   * This initializes an array of playable and unplayable cells based on the model's grid.
   */
  private void initializePlayableCells() {
    // Getting the grid from the model
    GridCell[][] grid = model.getGrid();
    // init the playableCells array based on model grid
    playableCells = new boolean[gridRows][gridCols];
    for (int row = 0; row < gridRows; row++) {
      for (int col = 0; col < gridCols; col++) {
        playableCells[row][col] = grid[row][col].isPlaceable();
      }
    }
  }

  /**
   * This paints the panel, draws each grid cell based on whether or not it's playable, and a border
   * around each cell. It also checks if a cell has a card and renders it. Finally it draws a border
   * around the grid for aesthetics.
   *
   * @param g the <code>Graphics</code> object to protect the rendering of the model.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int cellWidth = getWidth() / gridCols;
    int cellHeight = getHeight() / gridRows;
    GridCell[][] grid = model.getGrid();

    // Draw grid cells based on playable cells
    for (int row = 0; row < gridRows; row++) {
      for (int col = 0; col < gridCols; col++) {
        int x = col * cellWidth;
        int y = row * cellHeight;

        // Set color based on whether the cell is playable
        if (playableCells[row][col]) {
          g2d.setColor(Color.gray); // playable cells
        } else {
          g2d.setColor(Color.darkGray); // non-playable cells
        }

        g2d.fillRect(x + 1, y + 1, cellWidth - 1, cellHeight - 1); // paints base tile

        // If the cell has a card, create a CardPanel instance and paint it here
        if (grid[row][col].hasCard()) {
          CardinalCard card = (CardinalCard) grid[row][col].getCard();
          threetrios.model.Color cardOwnerColor = grid[row][col].getColor();

          // Create a temporary CardPanel to render the card
          CardPanel cardPanel = new CardPanel(card, cardOwnerColor);
          cardPanel.setSize(cellWidth, cellHeight); // Set size to fit cell

          // Use a temporary graphics context to draw the CardPanel
          Graphics2D cardGraphics = (Graphics2D) g.create(x, y, cellWidth, cellHeight);
          cardPanel.paintComponent(cardGraphics);
          cardGraphics.dispose(); // Dispose of the temporary graphics context
        }

        // paints outline
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(CELL_OUTLINE));
        g2d.drawRect(x, y, cellWidth, cellHeight);
      }
    }
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(GRID_BORDER_STROKE)); // Set the thickness of the outline
    g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
  }


  /**
   * Handles the mouse click event.
   *
   * @param e the MouseEvent triggered on click.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    int cellWidth = getWidth() / gridCols;
    int cellHeight = getHeight() / gridRows;
    int row = e.getY() / cellHeight;
    int col = e.getX() / cellWidth;
    // Call clickToFeature here
    parentView.clickToFeature("Grid " + row + " " + col);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    return;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    return;
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    return;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    return;
  }
}
