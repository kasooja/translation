/**
 *
 */
package edu.stanford.nlp.mt.decoder.feat;

import java.util.*;

import edu.stanford.nlp.mt.base.ConcreteTranslationOption;
import edu.stanford.nlp.mt.base.FeatureValue;
import edu.stanford.nlp.mt.base.Featurizable;
import edu.stanford.nlp.mt.base.Sequence;

/**
 * @author danielcer
 *
 * @param <T>
 */
public class PhraseTableScoresFeaturizer<T> implements
        IncrementalFeaturizer<T, String>, IsolatedPhraseFeaturizer<T, String> {

    public final static String PREFIX = "TM";
    public static final String DEBUG_PROPERTY = "ptScoresFeaturizerDEBUG";
    final HashMap<String, String[]> featureNamesHash;
    final boolean tagByTable;
    final boolean phraseReweighting;
    public static final FeatureValue<String> emptyFV = new FeatureValue<String>(
            null, 0.0);
    public static final boolean DEBUG = Boolean.parseBoolean(System.getProperty(
            DEBUG_PROPERTY, "false"));

    private String[] getFeatureNames(String[] phraseScoreNames,
            String phraseTableName) {
        String[] featureNames = new String[phraseScoreNames.length];
        if (DEBUG) {
            System.err.printf(
                    "PhraseTableScoresFeaturizer: generating feature names for %s\n",
                    phraseTableName);
        }
        for (int i = 0; i < phraseScoreNames.length; i++) {
            if (phraseScoreNames[i] != null) {
                if (!tagByTable) {
                    if (phraseScoreNames[i].startsWith(PREFIX + ":")) {
                        featureNames[i] = phraseScoreNames[i];
                    } else {
                        featureNames[i] = String.format("%s:%s", PREFIX, phraseScoreNames[i]);
                    }
                } else {
                    featureNames[i] = String.format("%s:%s:%s", PREFIX, phraseTableName,
                            phraseScoreNames[i]);
                }
            }
            // System.err.printf("\t%d:%s\n", i, featureNames[i]);
        }
        return featureNames;
    }

    /**
     *
     */
    public PhraseTableScoresFeaturizer() {
        tagByTable = false; // the 'weightedbaseline' featurizer generated by
        // FeaturizerFactory requires this to be set to false
        // so that translation model feature names are predictable
        // without having to know which phrase table they were
        // extracted from
        featureNamesHash = new HashMap<String, String[]>();
        phraseReweighting = false;
    }

    /**
     *
     */
    public PhraseTableScoresFeaturizer(boolean tagByTable) {
        this.tagByTable = tagByTable;
        featureNamesHash = new HashMap<String, String[]>();
        phraseReweighting = false;
    }

    public PhraseTableScoresFeaturizer(boolean tagByTable,
            boolean phraseReweighting) {
        this.tagByTable = tagByTable;
        featureNamesHash = new HashMap<String, String[]>();
        this.phraseReweighting = phraseReweighting;
    }

    /**
     * @see
     * edu.stanford.nlp.mt.decoder.feat.IncrementalFeaturizer#featurize(Featurizable)
     */
    @Override
    public FeatureValue<String> featurize(Featurizable<T, String> featurizable) {
        return null;
    }

    /**
     * @see
     * edu.stanford.nlp.mt.decoder.feat.IncrementalFeaturizer#listFeaturize(Featurizable)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<FeatureValue<String>> listFeaturize(
            Featurizable<T, String> featurizable) {
        FeatureValue<String>[] featureValues;
        if (featurizable.phraseTableName == UnknownWordFeaturizer.UNKNOWN_PHRASE_TABLE_NAME) {
            return null; // TODO implement some other type of flagging, so this isn't
            // necessary (/can be done more cleanly)
        }
        if (phraseReweighting) {
            featureValues = new FeatureValue[1];
            String featureName = featurizable.foreignPhrase.toString("DTM:", "_",
                    featurizable.translatedPhrase.toString("=>", "_"));
            featureValues[0] = new FeatureValue<String>(featureName, 1.0);
        } else {
            // lookup/construct the list of feature names
            String phraseTableName = featurizable.phraseTableName;
            String[] featureNames = featureNamesHash.get(phraseTableName);
            if (featureNames == null) {
                featureNames = getFeatureNames(featurizable.phraseScoreNames,
                        phraseTableName);
                featureNamesHash.put(phraseTableName, featureNames);
            }

            // construct array of FeatureValue objects
            featureValues = new FeatureValue[featureNames.length];
            for (int i = 0; i < featureValues.length; i++) {
                featureValues[i] = (i < featurizable.translationScores.length) ? new FeatureValue<String>(
                        featureNames[i], featurizable.translationScores[i]) : emptyFV;
            }

            if (DEBUG) {
                System.err.printf("Translation Phrase Pair: %s/%s\n",
                        featurizable.translatedPhrase, featurizable.foreignPhrase);
                System.err.printf("Feature Values: %s\n",
                        Arrays.toString(featureValues));
            }
        }

        // return the results as a list
        return Arrays.asList(featureValues);
    }

    @Override
    public FeatureValue<String> phraseFeaturize(Featurizable<T, String> f) {
        return featurize(f);
    }

    @Override
    public List<FeatureValue<String>> phraseListFeaturize(
            Featurizable<T, String> f) {
        return listFeaturize(f);
    }

    @Override
    public void initialize(List<ConcreteTranslationOption<T>> options,
            Sequence<T> foreign) {
    }

    public void reset() {
    }
}
