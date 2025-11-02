package klondike.model.hw02;

/**
 * created this interface that extends Card to create getters
 * of different values that are going to be called constantly.
 */
public interface BetterCard extends Card {
  /**
   * gets the cards suit.
   *
   * @return the card's suit.
   */
  CardSuit suit();

  /**
   * gets the cards rank.
   *
   * @return the card's rank.
   */
  CardRank rank();

  /**
   * getter for the numeric value of a card.
   *
   * @return the value of a card [1-13].
   */
  default int rankValue() {
    return rank().value();
  }

  /**
   * returns whether the card is black or not.
   *
   * @return true if black, false if red.
   */
  default boolean isBlack() {
    return suit().isBlack();
  }

  /**
   * a getter for the symbol of a card.
   *
   * @return the symbol of a card.
   */
  default char suitSymbol() {
    return suit().symbol();
  }
}
