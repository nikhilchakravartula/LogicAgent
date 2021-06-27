package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * To read input.txt, read output.txt, perform inference and compare program output to output.txt
 * 
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com) <br/>
 *
 */
public class Test {
  String progFile;
  String trueFile;

  Test(String progFile, String trueFile) {
    this.progFile = progFile;
    this.trueFile = trueFile;


  }

  /**
   * @return true if progFile and trueFile are identical
   * @throws Exception
   */
  public boolean test() throws Exception {

    BufferedReader o1Reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(progFile)));

    BufferedReader o2Reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(trueFile)));
    StringBuilder progString = new StringBuilder();
    StringBuilder trueString = new StringBuilder();
    String line = "";
    while ((line = o1Reader.readLine()) != null) {
      progString.append(line);
    }

    while ((line = o2Reader.readLine()) != null) {
      trueString.append(line);
    }
    System.out.println(trueString);
    System.out.println(progString);

    Logger.getInstance().write(trueString.toString() + "\n");

    Logger.getInstance().write(progString.toString() + "\n");
    return (trueString.toString().equals(progString.toString()));
  }


}

