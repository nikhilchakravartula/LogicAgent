package knowledgebase;

import java.util.ArrayList;
import java.util.StringTokenizer;
import constants.SyntaxChars;
import constants.TermType;



/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *         A Predicate is anything that evaluates to true or false, and does not contain a
 *         negation(~). happy(John), loves(John,x) are predicates but ~happy(John) is an Atom and
 *         not a predicate.
 */
public class Predicate {
  // A predicate contains a name, and a list of associated terms.
  private String name;
  private ArrayList<Term> terms;


  public ArrayList<Term> getTerms() {
    return terms;
  }

  public String getName() {
    return name;
  }

  /**
   * @return true if any of the terms in this predicate is a variable. Ex: a,b,c,x,y,z, etc.,
   */
  public boolean containsVariable() {
    for (Term term : terms) {
      if (term.isVariable()) {
        return true;
      }
    }
    return false;
  }



  /**
   * @return true if one of the terms is a constant. Socrates, Adam, Lilly etc.
   */
  public boolean containsConstant() {
    for (Term term : terms) {
      if (term.isConstant()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof Predicate)) {
      return false;
    }
    Predicate otherObj = (Predicate) obj;
    return otherObj.getName().equals(name);
  }

  private Predicate() {
    terms = new ArrayList<Term>();
  }

  /**
   * Given a string that represents a predicate, encode this as a Predicate object
   * 
   * @param predicate <br/>
   *
   */
  public Predicate(String predicate) {
    this();
    // Predicate is of the form predicate_name(term1,term2,term3...termn).
    name = new StringTokenizer(predicate, SyntaxChars.OPENPAREN).nextToken().trim();
    // Extract the predicate name
    predicate = predicate.trim();
    // Now extract all the terms
    StringTokenizer str = new StringTokenizer(
        predicate.substring(name.length() + 1, predicate.length() - 1), SyntaxChars.COMMA);

    String termName;
    Term term;

    // Extract terms one by one and create corresponding Term objects
    while (str.hasMoreTokens()) {
      termName = str.nextToken().trim();
      if (termName.toLowerCase().equals(termName)) {
        term = new Term(termName, termName, TermType.VARIABLE);
      } else {
        term = new Term(termName, termName, TermType.CONSTANT);
      }

      // Add terms to the predicate
      terms.add(term);
    }
  }


  public Predicate clone() {
    // It is important to clone each and every object inside the Predicate, including the term.
    Predicate clonedPredicate = new Predicate();
    clonedPredicate.name = new String(this.name);
    for (Term term : this.getTerms()) {
      clonedPredicate.getTerms().add(term.clone());
    }
    return clonedPredicate;
  }

  public String toString() {
    StringBuilder out = new StringBuilder();
    // predicate_name
    out.append(name);
    // predicate_name(
    out.append(SyntaxChars.OPENPAREN);

    // predicate_name(term1,term2...,termn,
    for (Term term : terms) {
      out.append(term.toString() + ",");
    }
    // predicate_name(term1,term2...,termn
    out.deleteCharAt(out.length() - 1);

    // predicate_name(term1,term2...,termn)
    out.append(SyntaxChars.CLOSEDPAREN);
    return out.toString();
  }

}
