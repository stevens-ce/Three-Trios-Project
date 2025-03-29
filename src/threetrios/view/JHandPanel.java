package threetrios.view;

import javax.swing.JPanel;

/**
 * abstract class that adds the ability to highlight select "cards" in the panel onto the
 * functions of JPanel.
 */
abstract class JHandPanel extends JPanel {
  /**
   * toggles highlight on selected card w/in the panel.
   *
   * @param idx of card in hand to be highlighted.
   */
  abstract void highlightSelected(int idx);
}
