import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import edu.princeton.cs.algs4.BinaryIn;
import java.util.*;


public class Puff {

  static boolean DEBUG=true;

  // MAIN METHOD
  // It has to throw IOException since you are using classes that throw IOExceptions.
  public static void main (String[] args) throws IOException {

    // Here is some code that might help you use BinaryIn to
    // read in a file bit by bit.

    FileIOC fioc = new FileIOC();
    BinaryIn bi = fioc.openBinaryInputFile(args[0]);

    // This code shows you how to check to see if the first two bytes
    // are the signature 0x0bc0
    short s = bi.readShort();
    if (s != 0x0bc0) {
      throw new IOException("Invalid file type");
    }

    // This is how you would read in a regular 32-bit integer.
    int regularInteger = bi.readInt();

    // Here's how you'd read in an 8-bit integer.
    int eightBitInteger = bi.readInt(8);

    // Here's how you would test if your input stream was empty
    while (!bi.isEmpty()) {

        // And here's how you read in a boolean!
        boolean b = bi.readBoolean();
    }
  }

}
