package klondike;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.view.KlondikeTextualView;
import klondike.view.TextualView;
import org.junit.Test;


/**
 * Testing the view models in Klondike.
 */
public class KlondikeViewTests {

  @Test(expected = IllegalArgumentException.class)
  public void ctorNullModelThrows() {
    new KlondikeTextualView(null, new StringBuilder());
  }

  @Test
  public void toStringHasHeadersAndWidths() {
    KlondikeModel<Card> m = new BasicKlondike();
    m.startGame(m.createNewDeck(), false, 7, 3);

    TextualView view = new KlondikeTextualView(m, new StringBuilder());
    String out = view.toString();

    String[] lines = out.split("\n", -1);
    assertTrue("at least 3 lines expected", lines.length >= 3);
    assertTrue(lines[0].startsWith("Draw:"));
    assertTrue(lines[1].startsWith("Foundation:"));

    if (m.getCardAt(0) == null) {
      assertTrue(lines[1].contains("<none>"));
    }

    int piles = m.getNumPiles();
    int width = 3 * piles;
    for (int i = 2; i < lines.length; i++) {
      assertEquals(width, lines[i].length());
    }

    assertFalse(out.endsWith("\n"));
  }
}
