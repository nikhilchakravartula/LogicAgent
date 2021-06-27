package inference;

import knowledgebase.KB;
import knowledgebase.Sentence;

/**
 * Contract for writing an inference mechanism
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public interface Inference {

  /**
   * @param sentence
   * @param kb
   * @return true if sentence can be inferred from kb.
   */
  public boolean infer(Sentence sentence, KB kb);
}
