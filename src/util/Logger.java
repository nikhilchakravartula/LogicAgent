package util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * A simple Logger that logs events to a file logs.txt.
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public class Logger {
  private static Logger logger;
  private BufferedWriter writer;

  Logger() {
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./logs.txt")));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void write(String s) {
    try {
      writer.write(s);
      writer.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  static Logger getInstance() {
    if (logger == null) {
      logger = new Logger();
    }
    return logger;
  }
}
