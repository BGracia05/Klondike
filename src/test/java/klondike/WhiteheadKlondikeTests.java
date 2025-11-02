package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw04.WhiteheadKlondike;
import org.junit.Test;

/**
 * Whitehead Klondike is derived from Basic Klondike, so these tests only see the differences
 * that were made in rules.
 */
public class WhiteheadKlondikeTests {

  @Test
  public void allCascadeCardsAreFaceUpAtStart() {
    KlondikeModel<Card> m = new WhiteheadKlondike();
    List<Card> deck = m.createNewDeck();
    m.startGame(deck, false, 7, 3);

    int rows = m.getNumRows();
    for (int pile = 0; pile < 7; pile++) {
      int height = m.getPileHeight(pile);
      for (int r = 0; r < height; r++) {
        assertTrue("Every dealt card should be visible in Whitehead",
            m.isCardVisible(pile, r));
      }
    }
  }

  @Test
  public void cascadeMoveMustBeSameColorAndDescending() {
    KlondikeModel<Card> m = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>(m.createNewDeck());

    int i1 = lastIndexForPileTop(1);
    int i2 = lastIndexForPileTop(2);
    Card sixClub = find(deck, "6♣");
    Card sevenClub = find(deck, "7♣");
    deck.set(deck.indexOf(sixClub), deck.get(i1));
    deck.set(i1, sixClub);
    deck.set(deck.indexOf(sevenClub), deck.get(i2));
    deck.set(i2, sevenClub);

    m.startGame(deck, false, 7, 3);
    m.movePile(0, 1, 1);
  }

  @Test
  public void cascadeMoveOppositeColorShouldFail() {
    KlondikeModel<Card> m = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>(m.createNewDeck());

    int i1 = lastIndexForPileTop(1);
    int i2 = lastIndexForPileTop(2);
    Card sixClub = find(deck, "6♣");
    Card sevenHeart = find(deck, "7♡");

    deck.set(deck.indexOf(sixClub), deck.get(i1));
    deck.set(i1, sixClub);
    deck.set(deck.indexOf(sevenHeart), deck.get(i2));
    deck.set(i2, sevenHeart);

    m.startGame(deck, false, 7, 3);

    try {
      m.movePile(0, 1, 1);
      fail("Expected IllegalStateException (opposite color not allowed in Whitehead)");
    } catch (IllegalStateException expected) {
      // ok
    }
  }

  @Test
  public void moveMultipleCardsMustBeSingleSuitDescending() {
    KlondikeModel<Card> m = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>(m.createNewDeck());
    int p3Top = lastIndexForPileTop(3);       // index 5
    int p3SecondFromTop = p3Top - 1;          // index 4
    int p4Top = lastIndexForPileTop(4);       // index 9

    Card sevenClub = find(deck, "7♣");
    Card eightClub = find(deck, "8♣");
    Card nineClub = find(deck, "9♣");
    deck.set(deck.indexOf(eightClub), deck.get(p3SecondFromTop));
    deck.set(p3SecondFromTop, eightClub);
    deck.set(deck.indexOf(sevenClub), deck.get(p3Top));
    deck.set(p3Top, sevenClub);
    deck.set(deck.indexOf(nineClub), deck.get(p4Top));
    deck.set(p4Top, nineClub);


    m.startGame(deck, false, 7, 3);
    m.movePile(2, 2, 3);
  }

  @Test
  public void multiCardMoveWithMixedSuitsShouldFail() {
    KlondikeModel<Card> m = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>(m.createNewDeck());

    int p3Top = lastIndexForPileTop(3);
    int p3SecondFromTop = p3Top - 1;
    int p4Top = lastIndexForPileTop(4);

    Card sevenSpade = find(deck, "7♠");
    Card eightClub = find(deck, "8♣");
    Card nineClub = find(deck, "9♣");

    deck.set(deck.indexOf(sevenSpade), deck.get(p3SecondFromTop));
    deck.set(p3SecondFromTop, sevenSpade);
    deck.set(deck.indexOf(eightClub), deck.get(p3Top));
    deck.set(p3Top, eightClub);
    deck.set(deck.indexOf(nineClub), deck.get(p4Top));
    deck.set(p4Top, nineClub);

    m.startGame(deck, false, 7, 3);

    try {
      m.movePile(2, 2, 3);
      fail("Expected IllegalStateException: multi-card move must be single-suit descending");
    } catch (IllegalStateException expected) {
      // ok
    }
  }

  @Test
  public void moveDrawRespectsSameColorDescending() {
    KlondikeModel<Card> m = new WhiteheadKlondike();

    List<Card> deck = new ArrayList<>(m.createNewDeck());

    int p1Top = lastIndexForPileTop(1);
    int firstDraw = 28;

    Card queenClub = find(deck, "Q♣");
    Card jackClub = find(deck, "J♣");

    deck.set(deck.indexOf(queenClub), deck.get(p1Top));
    deck.set(p1Top, queenClub);
    deck.set(deck.indexOf(jackClub), deck.get(firstDraw));
    deck.set(firstDraw, jackClub);

    m.startGame(deck, false, 7, 3);

    m.moveDraw(0);
  }

  @Test
  public void foundationsUnchangedAcesStartSuitAscend() {
    KlondikeModel<Card> m = new WhiteheadKlondike();
    List<Card> deck = new ArrayList<>(m.createNewDeck());
    int p1Top = lastIndexForPileTop(1);
    int firstDraw = 28;

    Card aceClub = find(deck, "A♣");
    Card twoClub = find(deck, "2♣");

    deck.set(deck.indexOf(aceClub), deck.get(p1Top));
    deck.set(p1Top, aceClub);
    deck.set(deck.indexOf(twoClub), deck.get(firstDraw));
    deck.set(firstDraw, twoClub);

    m.startGame(deck, false, 7, 3);

    m.moveToFoundation(0, 0);

    m.moveDrawToFoundation(0);

    assertEquals(4, m.getNumFoundations());
    assertEquals("2♣", m.getCardAt(0).toString());
  }

  private static Card find(List<Card> deck, String face) {
    for (Card c : deck) {
      if (face.equals(c.toString())) {
        return c;
      }
    }
    throw new IllegalArgumentException("Card not found: " + face);
  }


  private static int lastIndexForPileTop(int pileIndex1Based) {
    int count = pileIndex1Based * (pileIndex1Based + 1) / 2;
    return count - 1;
  }

}
