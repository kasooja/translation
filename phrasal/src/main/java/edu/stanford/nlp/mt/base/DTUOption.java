package edu.stanford.nlp.mt.base;

import java.util.*;

/**
 * 
 * @author Michel Galley
 * 
 * @param <T>
 */
public class DTUOption<T> extends TranslationOption<T> {

  public final RawSequence<T>[] dtus;

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private final static RawSequence emptySeq = new RawSequence(new Object[0]);

  @SuppressWarnings("unchecked")
  public DTUOption(int id, float[] scores, String[] phraseScoreNames,
      RawSequence<T>[] dtus, RawSequence<T> foreign, PhraseAlignment alignment) {
    super(id, scores, phraseScoreNames, emptySeq, foreign, alignment);
    this.dtus = dtus;
  }

  public DTUOption(int id, float[] scores, String[] phraseScoreNames,
      RawSequence<T>[] dtus, RawSequence<T> foreign, PhraseAlignment alignment,
      boolean forceAdd) {
    super(id, scores, phraseScoreNames, null, foreign, alignment, forceAdd);
    this.dtus = dtus;
  }

  @Override
  public String toString() {
    StringBuilder sbuf = new StringBuilder("TranslationOption: \"");
    for (int i = 0; i < dtus.length; ++i) {
      if (i > 0)
        sbuf.append(" ").append(DTUTable.GAP_STR.word()).append(" ");
      sbuf.append(dtus[i].toString());
    }
    sbuf.append(String.format("\" scores: %s\n", Arrays.toString(scores)));
    return sbuf.toString();
  }

  @Override
  public boolean hasTargetGap() {
    return true;
  }

}
