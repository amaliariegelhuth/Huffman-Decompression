# Problem Set 8: Huffman decompression

## Due Tuesday, November 20 @ 11:59pm

---

## Problem Set Overview

This is **not** a pair problem set! You will work on this on your own, but you are strongly encouraged to use the code you created with your partner for Problem Set 7.

In this problem set, you will write Puff.java, the program that decompresses the compressed files your Huff.java file created in Problem Set 7. When your program is complete, you will be able to execute this series of commands (in Unix or on a Mac and in the unix-like shell in Windows 10):

```
> cp myfile.txt myfile.original.txt         # makes a copy the file you're going to compress
> java Huff myfile.txt                      # creates compressed file myfile.zip
> java Puff myfile.zip                      # creates uncompressed file myfile.txt
> diff myfile.txt myfile.original.txt       # compares two files to see if they are different.
>                                           # no output means = no differences in files -- success!
```

You can and should use the classes you created for PS7, as well as most of the code you wrote for Step 2 of PS7, assuming you managed to get it working. If you did not get it working, email me and I will share some code with you that you can use.

The challenging part of this problem set will be using the algs4 `BinaryIn` class, which allows you to read in a file bit by bit. See the [Javadoc for `BinaryIn`](https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/BinaryIn.html) to learn about the different ways you can read in shorts, regular old 32-bit integers, integers of specific bit lengths, and booleans. I've also given a few examples in the included `Puff.java` file.

## Puff: Decoding to decompress a file

1. Start by using the `openBinaryInputFile` method in `FileIOC` to create a `BinaryIn` object that will allow you to read a `.zip` file you created with your Huff program. This code is provided for you in `Puff.java`.

2. Check to make sure that the first two bits are `0x0bc0`, which is our signal that we are opening a file compressed with our particular Huffman compression algorithm.

3. Next you will read in a regular old 32-bit integer to learn how many keys there are in the frequency table for your compressed file. 

4. Now you are going to start reading in your frequency table. For each key-value pair, you'll first read in an 8-bit integer for the character (the key), and then you'll read in a regular old 32-bit integer for the frequency (the value). You'll store these key-value pairs in a `TreeMap`, just as you did in Problem Set 7. As you read these in, keep count of how many you have read in. Once you have read in the number of keys (found above in step 3), you can create your Huffman tree.

5. Create your Huffman Tree exactly as you did in Problem Set 7, using a `PriorityQueue`, merging the two lowest priority trees, etc. I just copied and pasted by code from Problem Set 7, and it worked great. Hopefully that will work for you.

6. Now you have your Huffman Tree, which we will call `fullHuffmanTree`. It's time to start decoding the Huffman coded part of the file. You'll be using the `readBoolean()` method in BinaryIn to read in the file bit by bit. 

First, open a file to write out to using the `openOutputFile()` method in `FileIOC`, which returns a `FileWriter` object, which has a `write()` method that lets you write out ints as chars to a file.

Next, create a new `HuffTree` object, which we'll call `traverseHT`, whose top pointer points at the top of `fullHuffmanTree`, which you created in step 5. You will be using `traverseHT` to traverse the Huffman tree down from its top node to the leaf nodes in order to decode the compressed text. 

Then, follow this pseudocode, more or less, to decode the remainder of the .zip file

```
While your BinaryIn object is not empty: 
  if traverseHT.top has no left child and no right child, you have reached a leaf node:
    print out the character at that node to the output file
    reset your traverseHT.top pointer to fullHuffmanTree.top
  read in the next bit as a boolean
  if it is false:
    set traverseHT.top to traverseHT.leftchild
  else:
    set traverseHT.top to traverseHT.rightchild
```
    
7. When you reach the end of the file close the `FileWriter` object.


## Testing your code.
The output of your Puff program should be just the text of the file you compressed. For instance, if you run Huff on the `mississippi.txt` file, then use Puff to inflate your mississippi.zip file, the output file of Puff should contain the word `MISSISSIPPI`. The input to Huff should be identical to the output of Puff.

## Submitting your code
As usual, submit your full repo to GitHub.
