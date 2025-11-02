package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import org.junit.Test;

/**
 * A class for testing the KlondikeModel. All tests
 * in this class cannot create Card type objects. Instead,
 * the tests use the createNewDeck method to help create
 * example games.
 */
public class KlondikeModelTests {

  private KlondikeModel<Card> testGame() {
    return new BasicKlondike();
  }

  private List<Card> deck(KlondikeModel<Card> m) {
    return new ArrayList<>(m.createNewDeck());
  }

  @Test(expected = IllegalStateException.class)
  public void countBeforeGameThrows() {
    KlondikeModel<Card> m = testGame();
    m.getNumPiles();
  }

  @Test
  public void startGameCheckCounts() {
    KlondikeModel<Card> m = new BasicKlondike();
    List<Card> deck = m.createNewDeck();
    m.startGame(deck, false, 6, 4);
    assertEquals(6, m.getNumPiles());
    assertEquals(4, m.getNumDraw());
    assertEquals(4, m.getNumFoundations());
    assertTrue(m.getNumRows() >= 1);
  }

  @Test(expected = IllegalStateException.class)
  public void callingGetNumPilesBeforeStartThrows() {
    KlondikeModel<Card> m = new BasicKlondike();
    m.getNumPiles();
  }

  @Test
  public void moveAceToEmptyFoundationScoreIncrease() {
    KlondikeModel<Card> m = new BasicKlondike();
    List<Card> deck = m.createNewDeck();
    m.startGame(deck, false, 7, 3);
    int foundationIndex = 0;
    m.moveToFoundation(0, foundationIndex);
    assertEquals(1, m.getScore());
    assertEquals("A♣", m.getCardAt(foundationIndex).toString());
  }

  @Test
  public void getDrawCardsAtMostNumDraw() {
    KlondikeModel<Card> m = new BasicKlondike();
    List<Card> deck = m.createNewDeck();
    m.startGame(deck, false, 7, 3);
    assertTrue(m.getDrawCards().size() <= m.getNumDraw());
  }

  @Test
  public void moveAceToEmptyFoundationIncreasesScoreByOne() {
    KlondikeModel<Card> m = new BasicKlondike();
    List<Card> deck = m.createNewDeck();
    // Unshuffled, pile 0 face-up will be A♣ with our deck ordering.
    m.startGame(deck, false, 7, 3);
    int foundationIndex = 0;
    m.moveToFoundation(0, foundationIndex);
    assertEquals(1, m.getScore());
    assertEquals("A♣", m.getCardAt(foundationIndex).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void discardDrawWhenEmptyThrows() {
    KlondikeModel<Card> m = new BasicKlondike();
    List<Card> full = m.createNewDeck();
    List<Card> tiny = new ArrayList<>();
    for (Card c : full) {
      String s = c.toString();
      if (s.endsWith("♠")) {
        String r = s.substring(0, s.length() - 1);
        if (r.equals("A") || r.equals("2") || r.equals("3")) {
          tiny.add(c);
        }
      }
    }
    assertEquals(3, tiny.size());

    m.startGame(tiny, false, 2, 3);

    m.discardDraw();
  }

  @Test(expected = IllegalStateException.class)
  public void startAgainThrows() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    m.startGame(d, false, 7, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEnoughCardsToDealThrows() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    List<Card> tiny = d.subList(0, 5);
    m.startGame(tiny, false, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void brokenRunDeckThrows() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    int idxAceHearts = -1;
    for (int i = 0; i < d.size(); i++) {
      if (d.get(i).toString().equals("A♡")) {
        idxAceHearts = i;
        break;
      }
    }
    assertTrue(idxAceHearts >= 0);
    d.remove(idxAceHearts);
    m.startGame(d, false, 7, 3);
  }

  @Test
  public void drawCardsAtMostNumDrawAndIsCopy() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    List<Card> visible = m.getDrawCards();
    assertTrue(visible.size() <= m.getNumDraw());
    if (!visible.isEmpty()) {
      Card head = visible.getFirst();
      visible.clear();
      List<Card> visible2 = m.getDrawCards();
      assertFalse(visible2.isEmpty());
      assertEquals(head.toString(), visible2.getFirst().toString());
    }
  }

  @Test
  public void moveAceToEmptyFoundationUpdatesScoreAndTop() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    m.moveToFoundation(0, 0);
    assertEquals(1, m.getScore());
    assertEquals("A♣", m.getCardAt(0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void movingNonAceToEmptyFoundationThrows() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    m.moveToFoundation(1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveToFoundationInvalidIndexThrows() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    m.moveToFoundation(0, 99);
  }

  @Test(expected = IllegalArgumentException.class)
  public void movePilePhysicallyImpossibleThrowsArgument() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    m.movePile(0, 2, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void moveToEmptyRequiresKing() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 2, 3);
    if (m.getPileHeight(1) > 0) {
      m.moveToFoundation(1, 0);
    }
    m.movePile(0, 1, 1);
  }

  @Test
  public void discardRotatesHead() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    List<Card> before = m.getDrawCards();
    if (!before.isEmpty()) {
      String first = before.get(0).toString();
      m.discardDraw();
      List<Card> after = m.getDrawCards();
      if (!after.isEmpty()) {
        assertNotEquals(first, after.get(0).toString());
      }
    }
  }

  @Test(expected = IllegalStateException.class)
  public void moveDrawWhenEmptyThrows() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = m.createNewDeck();
    m.startGame(d, false, 7, 3);
    m.moveDraw(0);
  }


  @Test(expected = IllegalArgumentException.class)
  public void getHiddenCardThrows() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    m.getCardAt(6, 0);
  }

  @Test
  public void isCardVisibleMatchesCardAtEligibility() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    int p = 6;
    int h = m.getPileHeight(p);
    assertTrue(m.isCardVisible(p, h - 1));
    assertFalse(m.isCardVisible(p, 0));
  }

  @Test
  public void gameNotOverWhileDrawAvailable() {
    KlondikeModel<Card> m = testGame();
    List<Card> d = deck(m);
    m.startGame(d, false, 7, 3);
    assertFalse(m.isGameOver());
  }
}

