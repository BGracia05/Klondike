package klondike.model.hw02;

/**
 * assigns a rank to every type of card.
 */
public enum CardRank {
  ACE(1, "A"),
  TWO(2, "2"),
  THREE(3, "3"),
  FOUR(4, "4"),
  FIVE(5, "5"),
  SIX(6, "6"),
  SEVEN(7, "7"),
  EIGHT(8, "8"),
  NINE(9, "9"),
  TEN(10, "10"),
  JACK(11, "J"),
  QUEEN(12, "Q"),
  KING(13, "K");

  private final int value;
  private final String character;

  CardRank(int value, String character) {
    this.value = value;
    this.character = character;
  }

  /**
   * gets value of card.
   *
   * @return value of card.
   */
  public int value() {
    return value;
  }

  /**
   * gets the character of a card.
   *
   * @return the card's character.
   */
  public String character() {
    return character;
  }

  /**
   * converts a int value into it's dedicated rank.
   *
   * @param value the integer that is to be converted to a rank.
   * @return a rank derived from the value.
   * @throws IllegalArgumentException if value of card is less than 1 or greater than 13.
   * @throws AssertionError           if method falls through for whatever reason.
   */
  public static CardRank rankVal(int value) {
    if (value < 1 || value > 13) {
      throw new IllegalArgumentException("Value must be in between 1 and 13");
    }
    for (CardRank r : values()) {
      if (r.value == value) {
        return r;
      }
    }
    throw new AssertionError("Unknown value");
  }
}
