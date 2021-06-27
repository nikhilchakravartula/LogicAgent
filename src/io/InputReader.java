package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * IO class to read input from input.txt
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public class InputReader {
  int noQueries;
  int noSentences;
  String[] rawSentences;
  String[] queries;
  String inputFile;

  public InputReader(String inputFile) {
    this.inputFile = inputFile;
    readInput();
  }

  public int getNQueries() {
    return noQueries;
  }

  public int getNoSentences() {
    return noSentences;
  }

  public String[] getRawSentences() {
    return rawSentences;
  }

  public String[] getQueries() {
    return queries;
  }


  /**
   * Format for input.txt: <br/>
   * ===============================<br/>
   * <N = NUMBER OF QUERIES><br/>
   * <QUERY 1> … <QUERY N><br/>
   * <K = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE> <br/>
   * <SENTENCE 1><br/>
   * … <br/>
   * <SENTENCE K><br/>
   * 
   */
  public void readInput() {
    try (BufferedReader br =
        new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))) {
      noQueries = Integer.parseInt(br.readLine().trim());
      queries = new String[noQueries];

      for (int i = 0; i < noQueries; i++) {
        queries[i] = br.readLine().trim();
      }

      noSentences = Integer.parseInt(br.readLine().trim());
      rawSentences = new String[noSentences];
      for (int i = 0; i < noSentences; i++) {
        rawSentences[i] = br.readLine().trim();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
