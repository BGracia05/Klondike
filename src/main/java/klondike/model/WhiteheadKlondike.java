package klondike.model.hw04;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import klondike.model.hw02.Card;
import klondike.model.hw02.CardImplementation;
import klondike.model.hw02.CardRank;
import klondike.model.hw02.CardSuit;
import klondike.model.hw02.KlondikeModel;

/**
 * implementation of Klondike Model that manages rules and the state of game.
 * WHITEHEAD EDITION.
 */
public class WhiteheadKlondike implements KlondikeModel<Card> {

  private boolean started;

  interface MovePolicy {
    boolean canPlaceOnCascade(Card movingTop, Card destBottom);

    boolean foundationAccepts(Card topOnFoundationOrNull, CardSuit boundSuitOrNull, Card candidate);
  }

  private final class BasicMovePolicy implements MovePolicy {
    @Override
    public boolean canPlaceOnCascade(Card movingTop, Card destBottom) {
      return isOppositeColor(movingTop, destBottom)
          && valueOf(movingTop) + 1 == valueOf(destBottom);
    }

    @Override
    public boolean foundationAccepts(Card top, CardSuit boundSuit, Card c) {
      if (top == null) {
        return valueOf(c) == 1 && (boundSuit == null || boundSuit == suitOf(c));
      }
      return suitOf(c) == suitOf(top) && valueOf(c) == valueOf(top) + 1;
    }
  }

  private final MovePolicy policy = new BasicMovePolicy();

  private static final class Cascade {
    final Deque<Card> down = new ArrayDeque<>();
    final Deque<Card> up = new ArrayDeque<>();
  }

  private List<Cascade> cascades;

  private static final class Foundation {
    final Deque<Card> stack = new ArrayDeque<>();
    CardSuit boundSuit;
  }

  private List<Foundation> foundations;

  private Deque<Card> draw;
  private int numDraw;

  private int rows;

  @Override
  public List<Card> createNewDeck() {
    List<Card> deck = new ArrayList<>(52);
    for (int val = 1; val <= 13; val++) {
      for (CardSuit s : new CardSuit[]{
          CardSuit.CLUBS, CardSuit.DIAMONDS, CardSuit.HEARTS, CardSuit.SPADES}) {
        deck.add(new CardImplementation(CardRank.rankVal(val), s));
      }
    }
    return deck;
  }


  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
      throws IllegalArgumentException, IllegalStateException {
    if (started) {
      throw new IllegalStateException("Already started game!");
    }
    if (deck == null || numPiles <= 0 || numDraw <= 0) {
      throw new IllegalArgumentException("null deck or negative numPiles/numDraw");
    }

    int neededCards = numPiles * (numPiles + 1) / 2;
    if (deck.size() < neededCards) {
      throw new IllegalArgumentException("Not enough cards for cascades");
    }

    List<Card> work = new ArrayList<>(deck);
    if (shuffle) {
      Collections.shuffle(work);
    }

    cascades = new ArrayList<>(numPiles);
    for (int i = 0; i < numPiles; i++) {
      cascades.add(new Cascade());
    }

    int index = 0;
    for (int row = 0; row < numPiles; row++) {
      for (int pile = row; pile < numPiles; pile++) {
        cascades.get(pile).up.addLast(work.get(index++));
      }
    }

    int numFoundations = validateDeckCountAces(deck);

    foundations = new ArrayList<>(numFoundations);
    for (int i = 0; i < numFoundations; i++) {
      foundations.add(new Foundation());
    }

    draw = new ArrayDeque<>(work.subList(index, work.size()));
    this.numDraw = numDraw;

    recomputeRows();
    started = true;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
      throws IllegalArgumentException, IllegalStateException {
    ensureStarted();

    if (srcPile < 0 || srcPile == destPile || srcPile >= cascades.size()
        || destPile < 0 || destPile >= cascades.size() || numCards <= 0) {
      throw new IllegalArgumentException("invalid numCards or indices");
    }

    Cascade src = cascades.get(srcPile);
    Cascade dst = cascades.get(destPile);

    if (src.up.size() < numCards) {
      throw new IllegalArgumentException("Not enough cards facing up");
    }
    if (numCards > 1 && !isSingleSuitDescendingRun(src.up, numCards)) {
      throw new IllegalStateException("Whitehead: moved run must be one suit and descending");
    }

    Card movingTop = peekNthFromEnd(src.up, numCards);

    if (!dst.up.isEmpty()) {
      Card destBottom = dst.up.peekLast();
      boolean sameColor = isRed(movingTop) == isRed(destBottom);
      boolean descendingByOne = valueOf(movingTop) + 1 == valueOf(destBottom);
      if (!(sameColor && descendingByOne)) {
        throw new IllegalStateException("Cannot place on destination");
      }
    }

    Deque<Card> buffer = new ArrayDeque<>();
    for (int i = 0; i < numCards; i++) {
      buffer.addFirst(src.up.removeLast());
    }
    for (Card c : buffer) {
      dst.up.addLast(c);
    }
    recomputeRows();
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    ensureStarted();
    if (destPile < 0 || destPile >= cascades.size()) {
      throw new IllegalArgumentException("Invalid destination pile");
    }
    if (draw.isEmpty()) {
      throw new IllegalStateException("No draw cards to move");
    }

    Card card = draw.peekFirst();
    Cascade dst = cascades.get(destPile);

    if (!dst.up.isEmpty()) {
      Card destBottom = dst.up.peekLast();
      boolean sameColor = isRed(card) == isRed(destBottom);
      boolean descendingByOne = valueOf(card) + 1 == valueOf(destBottom);
      if (!(sameColor && descendingByOne)) {
        throw new IllegalStateException("Cannot place draw on destination");
      }
    }

    draw.removeFirst();
    dst.up.addLast(card);
    recomputeRows();
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    ensureStarted();
    if (srcPile < 0 || srcPile >= cascades.size()
        || foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Invalid pile indices");
    }
    Cascade src = cascades.get(srcPile);
    if (src.up.isEmpty()) {
      throw new IllegalStateException("Source pile has no face-up card");
    }

    Card card = src.up.peekLast();
    Foundation f = foundations.get(foundationPile);

    if (!foundationAccepts(f, card)) {
      throw new IllegalStateException("Selected foundation does not accept this card");
    }

    src.up.removeLast();
    pushToFoundation(f, card);
    recomputeRows();
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
      throws IllegalArgumentException, IllegalStateException {
    ensureStarted();
    if (foundationPile < 0 || foundationPile >= foundations.size()) {
      throw new IllegalArgumentException("Invalid foundation index");
    }
    if (draw.isEmpty()) {
      throw new IllegalStateException("No draw cards to move");
    }

    Card card = draw.peekFirst();
    Foundation f = foundations.get(foundationPile);

    if (!foundationAccepts(f, card)) {
      throw new IllegalStateException("Selected foundation does not accept this card");
    }

    draw.removeFirst();
    pushToFoundation(f, card);
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    ensureStarted();
    if (draw.isEmpty()) {
      throw new IllegalStateException("No draw cards to discard");
    }
    draw.addLast(draw.removeFirst());
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    ensureStarted();
    return rows;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    ensureStarted();
    return cascades.size();
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    ensureStarted();
    return numDraw;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    ensureStarted();

    if (!draw.isEmpty()) {
      return false;
    }

    for (Cascade c : cascades) {
      if (!c.up.isEmpty()) {
        Card top = c.up.peekLast();
        if (existsAcceptingFoundation(top)) {
          return false;
        }
      }
    }
    int n = cascades.size();
    for (int s = 0; s < n; s++) {
      Cascade src = cascades.get(s);
      int upSize = src.up.size();
      for (int k = 1; k <= upSize; k++) {
        Card movingTop = peekNthFromEnd(src.up, k);
        for (int d = 0; d < n; d++) {
          if (d != s) {
            Cascade dst = cascades.get(d);
            boolean canMoveRun = (k == 1) || isSingleSuitDescendingRun(src.up, k);
            if (dst.up.isEmpty()) {
              if (canMoveRun) {
                return false;
              }
            } else {
              if (canMoveRun && canPlaceOnCascade(movingTop, dst.up.peekLast())) {
                return false;
              }
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public int getScore() throws IllegalStateException {
    ensureStarted();

    int add = 0;
    for (Foundation f : foundations) {
      Card top = f.stack.peekLast();
      if (top != null) {
        add += valueOf(top);
      }
    }
    return add;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    ensureStarted();
    Cascade c = cascadeAt(pileNum);
    return c.down.size() + c.up.size();
  }

  @Override
  public Card getCardAt(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    ensureStarted();
    Cascade c = cascadeAt(pileNum);
    int height = c.down.size() + c.up.size();
    if (card < 0 || card >= height) {
      throw new IllegalArgumentException("Row out of range");
    }
    if (card < c.down.size()) {
      throw new IllegalArgumentException("Requested card is not visible");
    }
    int offset = card - c.down.size();
    int i = 0;
    for (Card cc : c.up) {
      if (i++ == offset) {
        return cc;
      }
    }
    throw new IllegalStateException("Visible index not found");
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    ensureStarted();
    Foundation f = foundationAt(foundationPile);
    return f.stack.peekLast();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
      throws IllegalArgumentException, IllegalStateException {
    ensureStarted();
    Cascade c = cascadeAt(pileNum);
    int height = c.down.size() + c.up.size();
    if (card < 0 || card >= height) {
      throw new IllegalArgumentException("row is out of bounds");
    }
    return card >= c.down.size();
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    ensureStarted();
    List<Card> res = new ArrayList<>(numDraw);
    int i = 0;
    for (Card c : draw) {
      if (i++ >= numDraw) {
        break;
      }
      res.add(c);
    }
    return res;
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    ensureStarted();
    return foundations.size();
  }

  private void ensureStarted() {
    if (!started) {
      throw new IllegalStateException("Game has not been started");
    }
  }

  private Cascade cascadeAt(int idx) {
    if (idx < 0 || idx >= cascades.size()) {
      throw new IllegalArgumentException("Bad pile index");
    }
    return cascades.get(idx);
  }

  private Foundation foundationAt(int idx) {
    if (idx < 0 || idx >= foundations.size()) {
      throw new IllegalArgumentException("Bad foundation index");
    }
    return foundations.get(idx);
  }

  private void recomputeRows() {
    int max = 0;
    for (Cascade c : cascades) {
      max = Math.max(max, c.down.size() + c.up.size());
    }
    rows = max;
  }

  private Card peekNthFromEnd(Deque<Card> d, int n) {
    if (n <= 0 || n > d.size()) {
      throw new IllegalArgumentException("Invalid n");
    }
    int index = d.size() - n;
    int i = 0;
    for (Card c : d) {
      if (i++ == index) {
        return c;
      }
    }
    throw new IllegalStateException("peekNthFromEnd fell through");
  }

  private boolean existsAcceptingFoundation(Card card) {
    for (Foundation f : foundations) {
      if (foundationAccepts(f, card)) {
        return true;
      }
    }
    return false;
  }

  private boolean foundationAccepts(Foundation f, Card card) {
    Card top = f.stack.peekLast();
    CardSuit bound = f.boundSuit;
    return policy.foundationAccepts(top, bound, card);
  }

  private void pushToFoundation(Foundation f, Card card) {
    if (f.stack.isEmpty()) {
      f.boundSuit = suitOf(card);
    } else if (f.boundSuit == null) {
      f.boundSuit = suitOf(card);
    }
    f.stack.addLast(card);
  }

  private boolean canPlaceOnCascade(Card movingTop, Card destBottom) {
    return (isRed(movingTop) == isRed(destBottom))
        && valueOf(movingTop) + 1 == valueOf(destBottom);
  }

  private boolean isOppositeColor(Card a, Card b) {
    return isRed(a) != isRed(b);
  }

  private boolean isRed(Card c) {
    CardSuit s = suitOf(c);
    return s == CardSuit.HEARTS || s == CardSuit.DIAMONDS;
  }

  private int valueOf(Card c) {
    String s = c.toString();
    if (s == null || s.length() < 2) {
      throw new IllegalArgumentException("Bad card: " + s);
    }
    String r = s.substring(0, s.length() - 1);
    return switch (r) {
      case "A" -> 1;
      case "2" -> 2;
      case "3" -> 3;
      case "4" -> 4;
      case "5" -> 5;
      case "6" -> 6;
      case "7" -> 7;
      case "8" -> 8;
      case "9" -> 9;
      case "10" -> 10;
      case "J" -> 11;
      case "Q" -> 12;
      case "K" -> 13;
      default -> throw new IllegalArgumentException("Bad rank part: " + s);
    };
  }

  private CardSuit suitOf(Card c) {
    String s = c.toString();
    if (s == null || s.length() < 2) {
      throw new IllegalArgumentException("Bad card: " + s);
    }
    char ch = s.charAt(s.length() - 1);
    return switch (ch) {
      case '♣' -> CardSuit.CLUBS;
      case '♠' -> CardSuit.SPADES;
      case '♢' -> CardSuit.DIAMONDS;
      case '♡' -> CardSuit.HEARTS;
      default -> throw new IllegalArgumentException("Bad suit char: " + ch + " in " + s);
    };
  }

  private int validateDeckCountAces(List<Card> deck) {
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("Empty deck");
    }
    Map<CardSuit, Map<Integer, Integer>> counts = new HashMap<>();
    for (Card c : deck) {
      CardSuit s = suitOf(c);
      int v = valueOf(c);
      counts.computeIfAbsent(s, k -> new HashMap<>()).merge(v, 1, Integer::sum);
    }

    Integer runLen = null;
    int aces = 0;

    for (Map.Entry<CardSuit, Map<Integer, Integer>> e : counts.entrySet()) {
      Map<Integer, Integer> m = e.getValue();
      int l = 0;
      for (int v = 1; v <= 13; v++) {
        if (m.containsKey(v)) {
          l = v;
        } else {
          break;
        }
      }
      if (l == 0) {
        throw new IllegalArgumentException("Suit " + e.getKey() + " has no Ace");
      }
      for (int v = 1; v <= l; v++) {
        if (!m.containsKey(v)) {
          throw new IllegalArgumentException("Broken run for " + e.getKey());
        }
      }
      Integer mult = null;
      for (int v = 1; v <= l; v++) {
        int cnt = m.get(v);
        if (mult == null) {
          mult = cnt;
        } else if (mult != cnt) {
          throw new IllegalArgumentException("Unequal multiplicities in " + e.getKey());
        }
      }
      aces += m.get(1);

      if (runLen == null) {
        runLen = l;
      } else if (!runLen.equals(l)) {
        throw new IllegalArgumentException("Unequal run lengths across suits");
      }
    }

    return aces;
  }

  private boolean isSingleSuitDescendingRun(Deque<Card> up, int k) {
    if (k <= 0 || k > up.size()) {
      return false;
    }
    Card[] buf = new Card[k];
    int skip = up.size() - k;
    int idx = 0;
    for (Card c : up) {
      if (skip-- > 0) {
        continue;
      }
      buf[idx++] = c;
    }
    CardSuit suit = suitOf(buf[0]);
    for (int i = 1; i < k; i++) {
      if (suitOf(buf[i]) != suit) {
        return false;
      }
      if (valueOf(buf[i]) != valueOf(buf[i - 1]) - 1) {
        return false;
      }
    }
    return true;
  }

}
