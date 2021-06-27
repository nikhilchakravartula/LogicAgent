package inference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import knowledgebase.Atom;
import knowledgebase.KB;
import knowledgebase.Predicate;
import knowledgebase.Sentence;
import knowledgebase.Term;

/**
 * This class performs backward-inference of query, given a kb.
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public class BackwardInference implements Inference {


  int counter = 0;
  // In some cases, the compute and memory isn't sufficient to infer the query from kb. MAXRESOLVES
  // is the max number of resolution steps that the inference algorithm does after which it
  // terminates and concludes that the inference isn't possible and returns false--> Query cannot be
  // inferred.
  private int MAXRESOLVES = 1000000;


  /**
   * @param a1
   * @param a2
   * @return true if a1 and a2 can be resolved. Ex: a1: happy(x) a2: ~happy(y) such that a1 and a2
   *         belong to different sentences.
   */
  boolean canResolve(Atom a1, Atom a2) {
    if (a1.getPredicate().equals(a2.getPredicate()) && (a1.negated() ^ a2.negated()) == true) {
      return canUnify(a1.getPredicate(), a2.getPredicate());
    }
    return false;
  }


  /**
   * @param a1
   * @param a2
   * @return substitution map (term_variable, value) that should be used during unification.
   */
  HashMap<String, Term> getSubstituionMap(Atom a1, Atom a2) {

    HashMap<String, Term> substitutionMap = new HashMap<String, Term>();
    ArrayList<Term> a1Terms = a1.getPredicate().getTerms();
    ArrayList<Term> a2Terms = a2.getPredicate().getTerms();
    for (int i = 0; i < a1Terms.size(); i++) {
      if (a1Terms.get(i).isVariable()) {

        substitutionMap.put(a1Terms.get(i).getId(), a2Terms.get(i));
      } else if (a2Terms.get(i).isVariable()) {

        substitutionMap.put(a2Terms.get(i).getId(), a1Terms.get(i));
      }
    }
    return substitutionMap;


  }


  /**
   * 
   * @param p1
   * @param p2
   * @return true if two predicates can be unified
   */
  boolean canUnify(Predicate p1, Predicate p2) {
    ArrayList<Term> a1Terms = p1.getTerms();
    ArrayList<Term> a2Terms = p2.getTerms();
    for (int i = 0; i < a1Terms.size(); i++) {
      // If p1 and p2 contain different constants at the same index i, cannot unify.
      if (a1Terms.get(i).isConstant() && a2Terms.get(i).isConstant()) {
        if (!a1Terms.get(i).equals(a2Terms.get(i))) {
          return false;
        }
      }
    }
    return true;
  }


  /**
   * @param s
   * @param substitutionMap
   * @return substitute terms in s from substitutionMap
   */
  Sentence substitute(Sentence s, HashMap<String, Term> substitutionMap) {
    Sentence newS1 = s.clone();
    ArrayList<Atom> aux = new ArrayList<Atom>();
    aux.addAll(newS1.getAtoms());

    // substitute
    for (Atom atom : aux) {
      ArrayList<Term> terms = atom.getPredicate().getTerms();
      for (int i = 0; i < terms.size(); i++) {
        String termId = terms.get(i).getId();
        if (substitutionMap.containsKey(termId)) {
          Term subsTerm = substitutionMap.get(termId);
          terms.get(i).setId(subsTerm.getId());
          terms.get(i).setName(subsTerm.getName());
          terms.get(i).setType(subsTerm.getType());

        }
      }
      atom.updateRepr();
    }
    newS1.getAtoms().clear();
    // Changing atoms destroys hash set. Hence, remove atoms and then add atoms.
    newS1.getAtoms().addAll(aux);
    newS1.updateRepr();
    return newS1;
  }

  /**
   * @param sentence
   * @return simplified version of sentence. Ex: happy(x) V happy(y) V ~happy(x) = happy(y)
   */
  Sentence simplify(Sentence sentence) {

    HashSet<Atom> toRemove = new HashSet<Atom>();
    for (Atom atom : sentence.getAtoms()) {
      Atom negatedAtom = atom.negate();
      if (sentence.getAtoms().contains(negatedAtom)) {
        toRemove.add(atom);
        toRemove.add(negatedAtom);
      }
    }
    sentence.getAtoms().removeAll(toRemove);
    sentence.updateRepr();
    return new Sentence(sentence);

  }

  /**
   * @param s1
   * @param s2
   * @param substitutionMap
   * @return unify s1 with s2 with substitutionMap,then merge s1 and s2, and then simplify.
   */
  Sentence unifyAndMerge(Sentence s1, Sentence s2, HashMap<String, Term> substitutionMap) {
    Sentence newS1 = substitute(s1, substitutionMap);
    Sentence newS2 = substitute(s2, substitutionMap);
    newS1 = newS1.merge(newS2);
    newS1 = simplify(newS1);
    Sentence mergedSWithDiffIds = new Sentence(newS1);
    // simplify(mergedSWithDiffIds);
    return mergedSWithDiffIds;
  }

  /**
   * @param sentence
   * @param kb
   * @return all possible resolvents for the sentence in the kb.
   */
  HashSet<Sentence> getResolvents(Sentence sentence, KB kb) {
    HashSet<Sentence> resolvents = new HashSet<Sentence>();
    HashSet<Sentence> candidates;
    boolean noResolution = true;
    for (Atom atom : sentence.getAtoms()) {
      noResolution = true;
      if (!kb.containsPredicate(atom.getPredicate())) {
        // If kb doesn't contain a predicate in sentence, we cannot resolve this sentence.
        resolvents.clear();
        return resolvents;
      }
      // Get negated sentences for a predicate. If predicate is happy(x), get all sentences in kb
      // such that the sentences contains atleast one ~happy(x)
      candidates = kb.getNegatedSentences(atom);
      // For all sentences that unify, resolve and get the resolvents.
      for (Sentence candidate : candidates) {
        for (Atom candidateAtom : candidate.getAtoms()) {
          if (canResolve(atom, candidateAtom)) {
            noResolution = false;
            HashMap<String, Term> sMap = getSubstituionMap(atom, candidateAtom);
            Sentence newSentence = unifyAndMerge(sentence, candidate, sMap);
            resolvents.add(newSentence);

          }

        }
      }
      // If an atom is constant and there is no way to resolve, then this atom will remain dangling
      // in the entire inference procedure. Hence, do not entertain any resolution for the sentence
      // such that inference stops sooner.
      if (!atom.containsVariable() && noResolution) {
        resolvents.clear();
        return resolvents;
      }
      // break; //remove this if wanna try substitution for all atoms at each level
    }
    return resolvents;
  }

  /**
   * @param sentence
   * @param visited
   * @param workingMem
   * @param kb
   * @return recursive helper for infer method.
   */
  boolean inferHelper(Sentence sentence, HashSet<Sentence> visited, HashSet<Sentence> workingMem,
      KB kb) {

    // base case. We reached a contradiction
    if (sentence.size() == 0) {
      return true;
    }

    counter++;


    if (counter > MAXRESOLVES) {
      return false;
    }

    visited.add(sentence);
    // Get all possible resolvents for this sentence.
    HashSet<Sentence> resolvents = getResolvents(sentence, kb);

    ArrayList<Sentence> resolventsAux = new ArrayList<Sentence>();
    resolventsAux.addAll(resolvents);

    // Sort resolvents by size. This is a heuristic to evaluate smallest size resolvent first in the
    // hope of faster convergence.
    Collections.sort(resolventsAux, new Comparator<Sentence>() {
      public int compare(Sentence s1, Sentence s2) {
        return s1.size() - s2.size();
      }
    });

    for (Sentence resolvent : resolventsAux) {
      if (!visited.contains(resolvent) && inferHelper(resolvent, visited, workingMem, kb)) {
        return true;
      }
    }
    // Backtrack
    visited.remove(sentence);
    return false;
  }


  public boolean infer(Sentence sentence, KB kb) {

    // Add negated sentence to kb
    sentence = sentence.negate();
    kb.add(sentence);



    HashSet<Sentence> visited = new HashSet<Sentence>();
    HashSet<Sentence> workingMem = new HashSet<Sentence>();
    // Reset counter
    counter = 0;
    // Perform inference
    boolean ans = inferHelper(sentence, visited, workingMem, kb);
    // Remove negated sentence from kb
    kb.remove(sentence);

    return ans;


  }

}


