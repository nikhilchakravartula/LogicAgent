package knowledgebase;

import constants.TermType;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *         This class implements Term. Terms are the tokens that appear inside the predicate. For
 *         example, if loves(x,Socrates) is a predicate, x and Socrates are the terms inside the
 *         predicate.
 */
public class Term {

  // The name of the term: x, y, z, Socrates, Adam, etc.,
  private String name;
  // The type of the term. Constant or a Variable
  private int type;
  // A unique id for the term. This will be very important when we unify two sentences.
  private String id;

  @Override
  public int hashCode() {
    if (isVariable()) {
      return id.hashCode();
    } else
      return name.hashCode();

  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof Term))
      return false;
    Term other = (Term) obj;
    if (other.isVariable() || isVariable()) {
      return false;
    }
    return other.name.equals(name);

  }

  public String getId() {
    return id;
  }

  public void setType(int type) {
    this.type = type;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public int getType() {
    return type;
  }

  Term(String termName, String id, int type) {
    this.name = termName;
    this.type = type;
    this.id = id;
  }

  private Term() {}

  /**
   * Clone a term
   */
  public Term clone() {
    Term clonedTerm = new Term();
    clonedTerm.type = this.type;
    clonedTerm.id = this.id;
    clonedTerm.name = this.name;
    return clonedTerm;
  }

  public boolean isVariable() {
    return type == TermType.VARIABLE;
  }

  public boolean isConstant() {
    return type == TermType.CONSTANT;
  }

  public String toString() {
    return name;
  }

}
