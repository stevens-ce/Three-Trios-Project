package threetrios.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * class with one method that accepts a file and reads it as an array of strings.
 */
public class ConfigurationFileReader {

  /**
   * reads given filePath and returns the file as a String[], where every line is one element.
   *
   * @param filePath this is the path to the file.
   * @return String[] consisting of every line in the file
   */
  public String[] readFile(String filePath) {
    List<String> fileLines = new ArrayList<String>();
    try {
      Scanner sc = new Scanner(new File(filePath));
      while (sc.hasNextLine()) {
        fileLines.add(sc.nextLine());
      }
      return fileLines.toArray(new String[fileLines.size()]);
    } catch (FileNotFoundException ex) {
      throw new IllegalArgumentException("file not found.");
    }
  }
}
