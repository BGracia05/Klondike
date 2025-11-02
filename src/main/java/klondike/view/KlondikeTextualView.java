package klondike.view;

import java.io.IOException;
import java.util.List;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;

/**
 * Textual view for a Klondike model.
 */
public class KlondikeTextualView implements TextualView {
  private final Appendable out;
  private final KlondikeModel<?> model;

  /**
   * Constructs a textual view with the given model, writing to an internal buffer.
   *
   * @param model the model to visualize
   * @throws IllegalArgumentException if model is null
   */
  public KlondikeTextualView(KlondikeModel<?> model) {
    this(model, new StringBuilder()); // delegates to the two-argument constructor
  }
  /**
   * Constructs a new textual view for the klondike model.
   *
   * @param model         the klondike model.
   * @param out where output will be appended.
   * @throws IllegalArgumentException if model or out is null.
   */

  public KlondikeTextualView(KlondikeModel<?> model, Appendable out) {
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    if (out == null) {
      throw new IllegalArgumentException("output cannot be null");
    }
    this.model = model;
    this.out = out;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    @SuppressWarnings("unchecked")
    List<Card> draw = (List<Card>) model.getDrawCards();
    sb.append("Draw: ");
    for (int i = 0; i < draw.size(); i++) {
      sb.append(draw.get(i).toString());
      if (i < draw.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append('\n');

    sb.append("Foundation: ");
    int f = model.getNumFoundations();
    for (int i = 0; i < f; i++) {
      Card top = (Card) model.getCardAt(i);
      sb.append(top == null ? "<none>" : top.toString());
      if (i < f - 1) {
        sb.append(", ");
      }
    }
    sb.append('\n');

    int piles = model.getNumPiles();
    int rows = model.getNumRows();
    for (int r = 0; r < rows; r++) {
      for (int p = 0; p < piles; p++) {
        int h = model.getPileHeight(p);

        String cell;
        if (h == 0) {
          cell = (r == 0) ? " X " : "   ";
        } else if (r >= h) {
          cell = "   ";
        } else if (!model.isCardVisible(p, r)) {
          cell = " ? ";
        } else {
          Card c = (Card) model.getCardAt(p, r);
          String s = c.toString();
          cell = padRightAligned3(s);
        }

        sb.append(cell);
      }
      if (r < rows - 1) {
        sb.append('\n');
      }
    }

    return sb.toString();
  }

  private static String padRightAligned3(String s) {
    int len = s.length();
    if (len >= 3) {
      return s;
    }
    if (len == 2) {
      return " " + s;
    }
    if (len == 1) {
      return "  " + s;
    }
    return "   ";
  }

  @Override
  public void render() throws IOException {
    out.append(model.toString()).append('\n');
  }
}
