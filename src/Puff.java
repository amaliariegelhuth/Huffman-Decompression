import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import edu.princeton.cs.algs4.BinaryIn;
import java.util.*;
/*
Amalia Riegelhuth
amaliariegelhuth

CSCI 1102 Computer Science 2

*/
public class Puff {

  static boolean DEBUG=true;

  static PriorityQueue <HuffTree> pq = new PriorityQueue <HuffTree>();

  // MAIN METHOD
  // It has to throw IOException since you are using classes that throw IOExceptions.
  static class HuffTree implements Comparable<HuffTree> {
    Node top; //top node
    int size = 1;//huff tree size
    int weight; //weight of whole tree

    public HuffTree() { // constructor that creates empty hufftree to use as pointer
      this.top = new Node();
    }
    public HuffTree(int weight) { //constructor used when making huff trees for all of the characters
      this.weight = weight;
      top = new Node();
    }
    public HuffTree(Node leftChild, Node rightChild, int weight) { //constructor used when combining trees
      top = new Node();
      top.leftChild = leftChild;
      top.rightChild = rightChild;
      this.weight = weight;
    }

    public int getWeight() {
      return weight;
    }

    public int compareTo(HuffTree tree) { //implementing comparable
      if (weight >= tree.weight){
        return 1;
      } else {
        return -1;
      }
    }

    public void traverse (Node n, String s, TreeMap<String, Info> map) {
      if (n == null) {
        return;
      }
      if (n.rightChild == null && n.leftChild == null) { //if it is a leaf, set the character's huffcode info to the string
        // System.out.println(s);
        map.get(n.character).huffCode = s;
      }
      if (n.leftChild != null) { //if there is a left child, keep traversing with the left child and add 0 to string
        traverse(n.leftChild, s + "0", map);
      }
      if (n.rightChild != null) { //if there is a right child, keep traversing with the right child and add 1 to string
        traverse(n.rightChild, s + "1", map);
      }

    }

    class Node { // Node class to be used to store values and to link HuffTree
      Node rightChild;
      Node leftChild;
      Node parent;
      String character;
      int freq;

      public String toString() { // prints out node
        String out = "";
        out = out + ("Right child: " + rightChild + "/n");
        out = out + ("Left child: " + leftChild + "/n");
        out = out + ("Parent: " + parent + "/n");
        out = out + ("Character: " + character + "/n");
        out = out + ("Frequency: " + freq);
        return out;
      }
    }
  }

  public static class Info { // class to hold values for frequency map
    int freq;
    String huffCode;
    int num; // holds number of times that value appears in text to know when to stop counting extra flush zeros

    public Info(int f){ //constructor for when we first add the characters and their info to the map
      freq = f;
    }
    public Info(int f, String h){
      freq = f;
      huffCode = h;
    }
    public Info(String h) {
      huffCode = h;
    }
    public int getFreq() {
      return freq;
    }
    public void setFreq(int n) {
      freq = n;
    }
    public String getHuffCode() {
      return huffCode;
    }
    public void setHuffCode(String s) {
      huffCode = s;
    }
    public int getNum() {
      return num;
    }
    public void setNum(int number) {
      num = number;
    }
  }

  public static void createTree(TreeMap<String, Info> map) { // creates a Huffman Code Tree
    for (Map.Entry<String, Info> entry : map.entrySet()){ //for each entry in the frequency map, make a huff tree with weight as the frequency and set the tree's top node info to the info from the entry
      HuffTree ht = new HuffTree(entry.getValue().getFreq());
      ht.top.character = entry.getKey();
      ht.top.freq = entry.getValue().getFreq();
      pq.add(ht); //then add it to the priority queue
    }
    while (pq.size() > 1){ //for each tree in priority queue
      HuffTree t1 = pq.poll();
      HuffTree t2 = pq.poll();
      HuffTree newHT = new HuffTree(t1.top, t2.top, t1.weight + t2.weight); //make a tree from the two smaller trees and set the weight as the sum of the two small ones
      pq.add(newHT); //add this new tree to the priority queue
    }
  }

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
    // number of keys
    int size = bi.readInt();

    TreeMap<String, Info> frequencyMap = new TreeMap<>(); //map that holds the characters (as strings) and their frequencies

    // Here's how you'd read in an 8-bit integer.
    // int eightBitInteger = bi.readInt(8);
    // add keys and values (of class Info) to the frequency map
    for (int n = 0; n < size; n++) {
      String key = Integer.toString(bi.readInt(8));
      Info i = new Info(bi.readInt());
      frequencyMap.put(key, i);
      // System.out.println(i.getFreq());
    }


    createTree(frequencyMap); // creates huff tree
    HuffTree fullHuffmanTree = pq.poll(); //takes the remaining tree out of the priority queue
    FileWriter fw = fioc.openOutputFile();
    HuffTree traverseHT = new HuffTree();
    traverseHT.top = fullHuffmanTree.top; // sets traverseHT as pointer to fullHuffmanTree
    fullHuffmanTree.traverse(fullHuffmanTree.top, "", frequencyMap);
    // Here's how you would test if your input stream was empty
    // System.out.println(frequencyMap.get("P").getFreq());
    while (!bi.isEmpty()) {
        if (traverseHT.top.leftChild == null && traverseHT.top.rightChild == null) {
          Info i = frequencyMap.get(traverseHT.top.character); // holds current vlaue in frequency map
          if (i.getNum() < i.getFreq()) { // checks to make sure there are no extra characters at the end due to flush
            fw.write(Integer.parseInt(traverseHT.top.character));
            i.setNum(i.getNum() + 1); // adds 1 to num in order to track how many times a character has been used
          }
          traverseHT.top = fullHuffmanTree.top;
        }
        // And here's how you read in a boolean!
        boolean b = bi.readBoolean();
        if (!b) {
          traverseHT.top = traverseHT.top.leftChild;
        } else {
          traverseHT.top = traverseHT.top.rightChild;
        }

    }
    fw.close();
  }

}
