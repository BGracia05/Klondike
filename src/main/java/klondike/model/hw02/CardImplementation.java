package klondike.model.hw02;

import java.util.Objects;

/**
 * Implementation of a standard playing card.
 */
public final class CardImplementation implements BetterCard {
  private final CardRank rank;
  private final CardSuit suit;

  /**
   * creates a new card with its given rank and suit.
   *
   * @param rank the rank of the card.
   * @param suit the suit of the card.
   */
  public CardImplementation(CardRank rank, CardSuit suit) {
    this.rank = Objects.requireNonNull(rank);
    this.suit = Objects.requireNonNull(suit);
  }

  @Override
  public CardSuit suit() {
    return suit;
  }

  @Override
  public CardRank rank() {
    return rank;
  }

  @Override
  public String toString() {
    return rank.character() + suit().symbol();
  }

  /**
   * A rewritten equals method that checks if 2 objects are the same suit and rank.
   *
   * @param o the other object we are checking to see if it equals to main object.
   * @return true if objects are equal to each-other, else false.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Card)) {
      return false;
    }
    Card other = (Card) o;
    return this.toString().equals(other.toString());
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(rank, suit);
  }

  /**
   * Helper Methods.
   */
  private static CardSuit parseSuit(char c) {
    return switch (c) {
      case '♣' -> CardSuit.CLUBS;
      case '♠' -> CardSuit.SPADES;
      case '♢' -> CardSuit.DIAMONDS;
      case '♡' -> CardSuit.HEARTS;
      default -> null;
    };
  }

  private static CardRank parseRank(String s) {
    return switch (s) {
      case "A" -> CardRank.ACE;
      case "2" -> CardRank.TWO;
      case "3" -> CardRank.THREE;
      case "4" -> CardRank.FOUR;
      case "5" -> CardRank.FIVE;
      case "6" -> CardRank.SIX;
      case "7" -> CardRank.SEVEN;
      case "8" -> CardRank.EIGHT;
      case "9" -> CardRank.NINE;
      case "10" -> CardRank.TEN;
      case "J" -> CardRank.JACK;
      case "Q" -> CardRank.QUEEN;
      case "K" -> CardRank.KING;
      default -> null;
    };
  }
}
