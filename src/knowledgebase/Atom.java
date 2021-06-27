package knowledgebase;

import constants.Connectives;
import constants.SyntaxChars;

/**
 * An Atom consists of a predicate, along with information to check if it is negated or not.
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public class Atom {
  private Predicate predicate;
  private boolean isNegated;
  private String repr;
  private String splitRepr;

  public Atom(Predicate predicate, boolean isNegated) {
    this.predicate = predicate;
    this.isNegated = isNegated;
    updateRepr();
    splitRepr = splitReprString();

  }

  public Atom negate() {
    Atom negatedAtom = clone();
    negatedAtom.isNegated = negatedAtom.isNegated ^ true;
    negatedAtom.updateRepr();
    return negatedAtom;
  }

  public Atom clone() {
    return new Atom(this.predicate.clone(), this.negated());

  }

  public boolean containsVariable() {
    return predicate.containsVariable();
  }

  public boolean containsConstant() {
    return predicate.containsConstant();
  }

  public boolean negated() {
    return isNegated;
  }

  public Predicate getPredicate() {
    return predicate;
  }

  public void updateRepr() {
    repr = hashString();
  }



  public String toString() {
    StringBuilder out = new StringBuilder();
    if (isNegated) {
      out.append(Connectives.NOT);
    }
    out.append(predicate.toString());
    return out.toString();
  }

  public String hashString() {
    StringBuilder sb = new StringBuilder();
    if (isNegated) {
      sb.append(Connectives.NOT);
    }
    sb.append(predicate.getName() + SyntaxChars.OPENPAREN);
    for (Term term : predicate.getTerms()) {
      sb.append(term.getId());
      sb.append(SyntaxChars.COMMA);
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(SyntaxChars.CLOSEDPAREN);
    return sb.toString();

  }

  public String getSplitRepr() {
    return splitRepr;
  }

  public String splitReprString() {
    StringBuilder sb = new StringBuilder();
    if (negated()) {
      sb.append(Connectives.NOT);
    }
    sb.append(getPredicate().getName() + SyntaxChars.OPENPAREN);
    for (Term term : getPredicate().getTerms()) {
      if (term.isConstant()) {
        sb.append(term.getId());
      } else
        sb.append("1");

    }
    sb.append(SyntaxChars.CLOSEDPAREN);
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
    if (!(obj instanceof Atom))
      return false;

    Atom otherObj = (Atom) obj;

    return hashString().equals(otherObj.hashString());

  }
}
