package knowledgebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.UUID;
import constants.Connectives;
import constants.SyntaxChars;

/**
 * A Sentence is a sequence of Atoms.
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public class Sentence {
  private HashSet<Atom> atoms;
  String repr;

  public HashSet<Atom> getAtoms() {
    return atoms;
  }

  public int size() {
    return atoms.size();
  }

  /**
   * Mainly used in calculating the hash value for the Sentence.
   * 
   * @return a string construction of the Sentence.
   */
  public String hashString() {
    ArrayList<String> aux = new ArrayList<String>();
    StringBuilder sb;

    // Ex: ~predicate_name(Adam,y)
    for (Atom atom : atoms) {
      sb = new StringBuilder();

      if (atom.negated()) {
        // ~
        sb.append(Connectives.NOT);
      }
      // ~predicate_name
      sb.append(atom.getPredicate().getName());

      // ~predicate_name(
      sb.append(SyntaxChars.OPENPAREN);

      // ~predicate_name(Adam,1,
      for (Term term : atom.getPredicate().getTerms()) {
        if (term.isConstant()) {
          sb.append(term.getId());
        } else {
          sb.append(1);

        }
        sb.append(SyntaxChars.COMMA);
      }
      // ~predicate_name(Adam,1
      sb.deleteCharAt(sb.length() - 1);
      // ~predicate_name(Adam,1)
      sb.append(SyntaxChars.CLOSEDPAREN);
      aux.add(sb.toString());
    }

    // Sorting is essential to return the same hash string for equivalent sentences.
    // Ex likes(x,y) V happy(Adam) is equivalent to happy(Adam) V likes(x,y)
    Collections.sort(aux);
    sb = new StringBuilder();
    for (String s : aux) {
      sb.append(s);
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return hashString().hashCode();

  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof Sentence)) {
      return false;
    }
    Sentence otherObj = (Sentence) obj;

    return hashString().equals(otherObj.hashString());
  }


  /**
   * @return a new Sentence that is the negation of the current Sentence.
   */
  public Sentence negate() {
    Sentence negSentence = new Sentence();
    for (Atom atom : atoms) {
      negSentence.atoms.add(atom.negate());
    }
    // Update string representation of the newly constructed sentence.
    negSentence.updateRepr();
    return negSentence;
  }

  public void updateRepr() {
    repr = hashString();
  }


  private Sentence() {
    atoms = new HashSet<Atom>();
  }

  /**
   * Ex: s: happy(Adam) V love(x,y) <br/>
   * s1: happy(Adam) <br/>
   * Merged: love(x,y)
   * 
   * @param s1
   * @retur a new merged Sentence.
   */
  public Sentence merge(Sentence s1) {
    Sentence mergedSentence = clone();
    for (Atom atom : s1.atoms) {
      mergedSentence.atoms.add(atom.clone());
    }
    // Update hash representation of the merged sentence.
    mergedSentence.updateRepr();
    return mergedSentence;
  }

  /**
   * Construct a Sentence object from a raw String rawSentence. The rawSentence consists of & and
   * =>. The method converts them into CNF.
   * 
   * @param rawSentence
   */
  public Sentence(String rawSentence) {
    this();

    ArrayList<Atom> auxAtoms = new ArrayList<Atom>();
    // Ex: a & b => c
    StringTokenizer str = new StringTokenizer(rawSentence, Connectives.IMPLICATION);
    String antecedent = null;
    String consequent = null;
    // antecedent: a & b
    // consequent c
    if (str.countTokens() == 2) {
      antecedent = str.nextToken().trim();
      consequent = str.nextToken().trim();
    } else if (str.countTokens() == 1) {
      consequent = str.nextToken().trim();
    }
    if (antecedent != null)
      buildAntecedentAtoms(antecedent, auxAtoms);
    if (consequent != null)
      buildConsequentAtoms(consequent, auxAtoms);
    // Assign unique id to each variable. This is helpful while unification.
    standardizeVariables(auxAtoms);
    atoms.addAll(auxAtoms);
    // Update representation of the Sentence.
    repr = hashString();
  }


  /**
   * Assign unique_id to each of the variables in the Sentence.
   * 
   * @param auxAtoms
   * 
   */
  void standardizeVariables(ArrayList<Atom> auxAtoms) {

    HashMap<String, String> idMap = new HashMap<String, String>();
    String termName;

    for (Atom atom : auxAtoms) {
      ArrayList<Term> terms = atom.getPredicate().getTerms();
      for (Term term : terms) {

        termName = term.getName();
        if (term.isVariable()) {
          if (!idMap.containsKey(termName)) {
            idMap.put(termName, UUID.randomUUID().toString());
          }
          term.setId(idMap.get(termName));
        }
      }
    }
  }

  /**
   * Copy constructor. Everything is cloned.
   * 
   * @param sentence
   */
  public Sentence(Sentence sentence) {
    this();
    ArrayList<Atom> auxAtoms = new ArrayList<Atom>();
    for (Atom atom : sentence.getAtoms()) {
      auxAtoms.add(atom.clone());
    }
    standardizeVariables(auxAtoms);
    atoms.addAll(auxAtoms);
    repr = hashString();
  }


  public Sentence clone() {
    Sentence clonedSentence = new Sentence();
    for (Atom atom : this.getAtoms()) {
      clonedSentence.atoms.add(atom.clone());
    }
    clonedSentence.repr = clonedSentence.hashString();
    return clonedSentence;
  }

  /**
   * @param antecedent a & ~b & ~c & d...
   * @param auxAtoms out variable that contains the contructed atoms.
   */
  void buildAntecedentAtoms(String antecedent, ArrayList<Atom> auxAtoms) {
    StringTokenizer str = new StringTokenizer(antecedent, Connectives.AND);
    Atom atom;
    Predicate predicate;
    while (str.hasMoreTokens()) {
      String nextToken = str.nextToken().trim();
      if (nextToken.startsWith(Connectives.NOT)) {
        // Convert to CNF. Hence, negated predicate in antecedent becomes non-negated
        predicate = new Predicate(nextToken.substring(1));
        atom = new Atom(predicate, false);
      } else {
        // Convert to CNF. Hence, non-negated predicate in antecedent becomes negated
        predicate = new Predicate(nextToken);
        atom = new Atom(predicate, true);
      }
      auxAtoms.add(atom);
    }
  }


  /**
   * consequent, in this context, always consits of one atom.
   * 
   * @param consequent
   * @param auxAtoms
   */
  void buildConsequentAtoms(String consequent, ArrayList<Atom> auxAtoms) {
    Predicate predicate;
    Atom atom;
    if (consequent.startsWith(Connectives.NOT)) {
      predicate = new Predicate(consequent.substring(1));
      atom = new Atom(predicate, true);
    } else {
      predicate = new Predicate(consequent);
      atom = new Atom(predicate, false);
    }
    auxAtoms.add(atom);

  }



  /**
   * @return String format of Sentence in CNF
   */
  public String toString() {
    if (atoms.size() == 0) {
      return "[null]";
    }
    StringBuilder out = new StringBuilder();
    for (Atom atom : atoms) {
      out.append(atom.toString() + " V ");
    }
    out.deleteCharAt(out.length() - 1);
    out.deleteCharAt(out.length() - 1);
    return out.toString();
  }
}
