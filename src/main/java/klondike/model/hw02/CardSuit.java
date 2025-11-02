package klondike.model.hw02;

/**
 * enum that assigns every type of card suit its symbol and whether
 * it's black or not.
 */
public enum CardSuit {
  CLUBS('♣', true),
  SPADES('♠', true),
  DIAMONDS('♢', false),
  HEARTS('♡', false);

  private final boolean black;
  private final char symbol;

  /**
   * creates a card with a symbol and color.
   *
   * @param symbol the cards symbol.
   * @param black  whether the card is black.
   */
  CardSuit(char symbol, boolean black) {
    this.symbol = symbol;
    this.black = black;
  }

  /**
   * checks to see if card is black (club, spade).
   *
   * @return true if black, false if not.
   */
  public boolean isBlack() {
    return black;
  }


  /**
   * checks the symbol of card.
   *
   * @return the card's symbol.
   */
  public char symbol() {
    return symbol;
  }
}
