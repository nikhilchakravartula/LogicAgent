package io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


/**
 * Utility class to write inference output to file.
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public class OutputWriter {
  String fileName;
  StringBuilder builder;
  public static final String TRUE = "TRUE";
  public static final String FALSE = "FALSE";

  public OutputWriter(String fileName) {
    this.fileName = fileName;
    this.builder = new StringBuilder();
  }

  public void add(boolean result) {
    builder.append(result == false ? FALSE : TRUE);
    builder.append("\n");
  }

  public void write() {
    if (builder.length() == 0)
      return;
    builder.deleteCharAt(builder.length() - 1);
    try (BufferedWriter bw =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)))) {
      bw.write(builder.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
