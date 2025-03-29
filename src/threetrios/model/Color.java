package threetrios.model;

/**
 * representation of the two colors needed for ThreeTriosBasicModel,
 * used to represent the player outside of model.
 */
public enum Color {
  RED("Red"), BLUE("Blue");

  private final String name;

  Color(String name) {
    this.name = name;
  }

  /**
   * returns a string representation of the enum.
   *
   * @return String rep of enum
   */
  public String toString() {
    return this.name;
  }
}