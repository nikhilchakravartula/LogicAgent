import inference.BackwardInference;
import inference.Inference;
import io.InputReader;
import io.OutputWriter;
import knowledgebase.KB;
import knowledgebase.Sentence;


/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *         This is the entry point to the application. The following is the flow of the program:
 *         Read input from input.txt file Initialize the knowledge base and store the input in the
 *         KB Read the queries Perform inference Write the output of the query into output.txt
 */
public class LogicAgent {

  public static void main(String[] args) throws Exception {

    String inputFile = "";
    String progOutputFile = "";

    inputFile = "./input.txt";
    progOutputFile = "./output.txt";

    InputReader inputReader = new InputReader(inputFile);
    OutputWriter outputWriter = new OutputWriter(progOutputFile);
    // Initialize KB
    KB kb = new KB();
    for (String rawSentence : inputReader.getRawSentences()) {
      // Read sentence from input.txt and add to kb
      Sentence s = new Sentence(rawSentence);
      kb.add(s);
    }
    // Initialize inference
    Inference inference = new BackwardInference();


    // Read each query, perform inference and write to output
    for (String query : inputReader.getQueries()) {
      try {
        boolean ans = inference.infer(new Sentence(query), kb);


        outputWriter.add(ans);
      } catch (Exception soe) {
        // In case of any exception, write false into the output.
        outputWriter.add(false);
      } catch (StackOverflowError soe) {
        // In some cases the inference cannot converge and in that case, write false
        outputWriter.add(false);
      }

    }

    outputWriter.write();

  }



}


