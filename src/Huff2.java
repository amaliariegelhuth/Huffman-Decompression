import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import edu.princeton.cs.algs4.BinaryIn;
import edu.princeton.cs.algs4.BinaryOut;
import java.util.*;


public class Huff2 {

  static boolean DEBUG=true;

  public static void main (String[] args) throws IOException {

    // STEP 1: Count Frequencies
    TreeMap<Integer,Integer> freq = new TreeMap<Integer,Integer>();

    FileIOC fioc = new FileIOC();
    FileReader fr = fioc.openInputFile(args[0]);

    int c;
    while ((c = fr.read()) != -1) {

      Integer sofar = freq.get(c);
      if (sofar != null) {
        sofar = sofar + 1;
      } else {
        sofar = 1;
      }
      freq.put(c,sofar);
    }
    fr.close();

    // STEP 2: Build Huffman Tree

    PriorityQueue<HuffTree> pq = new PriorityQueue<HuffTree>();
    for (int item : freq.keySet()) {
      HuffTree ht = new HuffTree(item, freq.get(item));
      pq.add(ht);
    }

    while (pq.size() > 1) {
      HuffTree ht2 = pq.poll();
      HuffTree ht1 = pq.poll();
      HuffTree newht = new HuffTree();
      newht.mergeTrees(ht2, ht1);
      pq.add(newht);
    }

    HuffTree wholething = pq.poll();

    wholething.printMe();

    // STEP 3: Print out binary file
    BinaryOut bo = fioc.openBinaryOutputFile();

    short s = 0x0bc0;
    bo.write(s);

    int numkeys = freq.keySet().size();
    bo.write(numkeys);

    for (int qq : freq.keySet()) {
      bo.write(qq, 8);
      bo.write(freq.get(qq));
    }

    fioc = new FileIOC();
    fr = fioc.openInputFile(args[0]);

    int cc;
    while ((cc = fr.read()) != -1) {
      String mycode = wholething.huffcodes.get(cc);
      String[] codeparts = mycode.split("");
      int mycodeint = Integer.parseInt(mycode, 2);
      for (int zz = 0; zz < codeparts.length; zz++) {
        if (codeparts[zz].equals("0")) {
          bo.write(false);
        } else {
          bo.write(true);
        }
      }
    }
    fr.close();
    bo.flush();
    bo.close();

  }

  public static class HuffTree implements Comparable<HuffTree> {

  Node top;
  int size;
  HashMap<Integer, String> huffcodes = new HashMap<Integer, String>();

  public class Node {
    Node rchild;
    Node lchild;
    int weight;
    int character;

    public Node (Node lchild, Node rchild, int weight) {
      this.lchild = lchild;
      this.rchild = rchild;
      this.weight = weight;
    }

    public Node (int weight, int character) {
      this.weight = weight;
      this.character = character;
    }

    public Node() {
      rchild = null;
      lchild = null;
      weight = 0;
      character = -1;
    }
  }

  public HuffTree() {
    this.top = new Node();
  }

  public HuffTree(int character, int weight) {
    Node newnode = new Node(weight, character);
    this.top = newnode;
  }

  public int compareTo(HuffTree other) {
    if (this.top.weight > other.top.weight) {
      return 1;
    } else if (this.top.weight == other.top.weight) {
	     return 1;
    } else {
      return -1;
    }
  }

  public void mergeTrees(HuffTree left, HuffTree right) {
    Node newparent = new Node(left.top, right.top, (left.top.weight + right.top.weight));
    int newsize = left.size + right.size;
    this.top = newparent;
    this.size = newsize + 1;

  }

  public String printTree(Node n, String s) {
    if (n == null) {
      return s;
    }
    if (n.rchild == null && n.lchild == null) {
	    System.out.println(s + " " + n.weight + " "  +(char) n.character);
      huffcodes.put(n.character, s);
      return s;
    }
    if (n.lchild != null) {
      String news = s + "0";
      printTree(n.lchild, news);
    }
    if (n.rchild != null) {
      String news = s + "1";
      printTree(n.rchild, news);
    }
    return s;
  }

  public void printMe() {
      System.out.println(printTree(top, ""));
  }

}

}
