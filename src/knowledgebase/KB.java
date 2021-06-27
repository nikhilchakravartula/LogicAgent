package knowledgebase;

import java.util.HashMap;
import java.util.HashSet;

public class KB {

  HashSet<Sentence> sentences;
  HashMap<String, Integer> rawAtoms;

  class KBVal {

    private HashSet<Sentence> positiveSentences;
    private HashSet<Sentence> negativeSentences;

    HashSet<Sentence> getPositiveSentences() {
      return positiveSentences;
    }

    HashSet<Sentence> getNegativeSentences() {
      return negativeSentences;
    }

    public KBVal() {
      // TODO Auto-generated constructor stub
      positiveSentences = new HashSet<Sentence>();
      negativeSentences = new HashSet<Sentence>();
    }

    public String toString() {
      StringBuilder out = new StringBuilder();
      out.append("\n====Positive====\n");
      for (Sentence sentence : positiveSentences) {
        out.append(sentence.toString() + "\n");
      }

      out.append("\n====Negative====\n");
      for (Sentence sentence : negativeSentences) {
        out.append(sentence.toString() + "\n");
      }

      out.append("\n====END====\n");
      return out.toString();
    }

  }

  HashMap<Predicate, KBVal> kb;

  public KB() {
    kb = new HashMap<Predicate, KBVal>();
    sentences = new HashSet<Sentence>();
    rawAtoms = new HashMap<String, Integer>();
  }



  /**
   * @param sentence
   * @return true if the KB contains sentence.
   */
  boolean contains(Sentence sentence) {

    return sentences.contains(sentence);
  }

  /**
   * @param predicate
   * @return true if there is any sentence s in the KB that contains the sub-sentence sentence.
   */

  boolean subset(Sentence sentence) {

    HashSet<Integer> auxSentence = new HashSet<Integer>();
    for (Atom atom : sentence.getAtoms()) {
      auxSentence.add(rawAtoms.get(atom.getSplitRepr()));
    }

    for (Sentence s : sentences) {
      if (s.size() > sentence.size())
        continue;

      HashSet<Integer> auxS = new HashSet<Integer>();
      for (Atom atom : s.getAtoms()) {
        auxS.add(rawAtoms.get(atom.getSplitRepr()));
      }

      for (Integer str : auxS) {
        if (!auxSentence.contains(str))
          return false;
      }
    }
    return true;
  }


  /**
   * @param predicate
   * @return true if KB contains a sentence with predicate.
   */
  public boolean containsPredicate(Predicate predicate) {
    return kb.containsKey(predicate);
  }


  /**
   * Remove sentence from KB
   * 
   * @param sentence
   */

  public void remove(Sentence sentence) {
    sentences.remove(sentence);
    for (Atom atom : sentence.getAtoms()) {
      kb.get(atom.getPredicate()).positiveSentences.remove(sentence);
      kb.get(atom.getPredicate()).negativeSentences.remove(sentence);
    }
  }


  /**
   * @return number of sentences in the KB
   */

  int getNoSentences() {
    return sentences.size();
  }

  public HashSet<Sentence> getSentences() {
    return sentences;
  }

  /**
   * Add sentence to KB
   * 
   * @param sentence
   */
  public void add(Sentence sentence) {
    if (sentences.contains(sentence))
      return;


    sentences.add(sentence);
    for (Atom atom : sentence.getAtoms()) {

      if (!rawAtoms.containsKey(atom.getSplitRepr())) {
        rawAtoms.put(atom.getSplitRepr(), rawAtoms.size() + 1);
      }


      if (!kb.containsKey(atom.getPredicate())) {
        kb.put(atom.getPredicate(), new KBVal());
      }
      if (atom.negated()) {
        kb.get(atom.getPredicate()).negativeSentences.add(sentence);
      } else
        kb.get(atom.getPredicate()).positiveSentences.add(sentence);
    }
  }

  /**
   * @param atom
   * @return sentences that contain ~atom.
   */
  public HashSet<Sentence> getNegatedSentences(Atom atom) {
    if (atom.negated()) {
      return kb.get(atom.getPredicate()).positiveSentences;
    } else {
      return kb.get(atom.getPredicate()).negativeSentences;
    }

  }

  public String toString() {
    StringBuilder out = new StringBuilder();
    for (Sentence sentence : sentences) {
      out.append(sentence.toString());
      out.append("\n");
    }
    return out.toString();
  }

}
