# Problem Set 8: Huffman decompression

## Due Tuesday, November 20 @ 11:59pm

---

## Problem Set Overview

This is **not** a pair problem set! You will work on this on your own, but you are strongly encouraged to use the code you created with your partner for Problem Set 7.

In this problem set, you will write Puff.java, the program that decompresses the compressed files your Huff.java file created in Problem Set 7. When your program is complete, you will be able to execute this series of commands:

```
> cp f.txt f.ref.txt         # makes a copy of f.txt and call it f.ref.txt
> java Huff f.txt            # creates compressed file f.zip
> java Puff f.zip            # creates uncompressed file f.txt
> diff f.txt original.txt    # compares two files to see if they are different.
>                            # no output means there are no differences in file -- success!
```

You can and should use the classes you created for PS7, as well as most of the code you wrote for Step 2 of PS7, assuming you managed to get it working. If you did not get it working, I will share some code with you that you can use.

The "exciting" (?) part of this problem set will be using the BinaryIn class, which allows you to read in file bit by bit. See the [https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/BinaryIn.html](Javadoc for BinaryIn) to learn about the different ways you can read in shorts, integers, integers of specific bit lengths, and booleans. I've also given a few examples in the Puff.java file.

## Puff: Decoding to decompress a file

1. Start by using the openBinaryInputFile method in FileIOC to create a BinaryIn object that will allow you to read a .zip file you created with your Huff program. This code is provided for you in Puff.java.

2. Check to make sure that the first two bits are 0x0bc0, which is our signal that we are opening a file compressed with our particular Huffman compression algorithm.


