package threetrios.model;

/**
 * representation of all possible attack values from 1-10.
 */
public enum AttackValue {
  ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), A(10);

  private final int number;

  AttackValue(int number) {
    this.number = number;
  }

  /**
   * returns number value of attack.
   *
   * @return int value
   */
  public int getValue() {
    return this.number;
  }

  /**
   * returns the number value as a String; NOT as a word.
   *
   * @return String of value
   */
  public String toString() {
    if (this == A) {
      return "A";
    } else {
      return "" + this.number;
    }
  }

}
