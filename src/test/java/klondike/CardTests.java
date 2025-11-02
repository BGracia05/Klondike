package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import klondike.model.hw02.Card;
import klondike.model.hw02.CardImplementation;
import klondike.model.hw02.CardRank;
import klondike.model.hw02.CardSuit;
import org.junit.Test;

/**
 * tests for Card.
 */
public class CardTests {
  @Test
  public void toStringFormatsCorrectly() {
    Card c1 = new CardImplementation(CardRank.ACE, CardSuit.HEARTS);
    Card c2 = new CardImplementation(CardRank.TEN, CardSuit.SPADES);
    Card c3 = new CardImplementation(CardRank.QUEEN, CardSuit.DIAMONDS);
    assertEquals("A♡", c1.toString());
    assertEquals("10♠", c2.toString());
    assertEquals("Q♢", c3.toString());
  }

  @Test
  public void equalsAcrossImplementations() {
    Card mine = new CardImplementation(CardRank.KING, CardSuit.CLUBS);
    Card foreign = new Card() {
      @Override public String toString() {
        return "K♣";
      }
    };
    assertTrue(mine.equals(foreign));
    assertEquals(mine.hashCode(), new CardImplementation(CardRank.KING, CardSuit.CLUBS).hashCode());
  }

  @Test
  public void notEqualDifferentSuitOrRank() {
    Card a = new CardImplementation(CardRank.ACE, CardSuit.HEARTS);
    Card b = new CardImplementation(CardRank.ACE, CardSuit.SPADES);
    Card c = new CardImplementation(CardRank.TWO, CardSuit.HEARTS);
    assertNotEquals(a, b);
    assertNotEquals(a, c);
    assertNotEquals(b, c);
  }

  @Test
  public void hashCodeConsistentWithEquals() {
    Card c1 = new CardImplementation(CardRank.JACK, CardSuit.DIAMONDS);
    Card c2 = new CardImplementation(CardRank.JACK, CardSuit.DIAMONDS);
    assertEquals(c1, c2);
    assertEquals(c1.hashCode(), c2.hashCode());
  }
}
