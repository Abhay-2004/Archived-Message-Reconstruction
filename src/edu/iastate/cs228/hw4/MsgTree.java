/**
 * Represents a binary tree for encoding and decoding messages. 
 * The tree is constructed iteratively from a given encoding string, 
 * where each leaf node represents a character, and internal nodes are 
 * denoted by the '^' character. This class also provides methods to 
 * decode messages based on the constructed tree and to print character 
 * codes and statistics of the encoded message.
 * 
 * @author Abhay Prasanna Rao
 */
package edu.iastate.cs228.hw4;

import java.util.Stack;


public class MsgTree {
    public char payloadChar;
    public MsgTree leftChild;
    public MsgTree rightChild;

    /**
     * Constructs a binary tree for message encoding and decoding using an iterative approach.
     * The tree is built based on a preorder traversal encoding string.
     *
     * @param encoding The encoding string representing the structure of the binary tree.
     * @throws IllegalArgumentException If the encoding string is null or empty.
     */
    
    public MsgTree(String encoding) {
        if (encoding == null || encoding.isEmpty()) {
            throw new IllegalArgumentException("Invalid encoding string");
        }

        Stack<MsgTree> nodeStack = new Stack<>();
        this.payloadChar = encoding.charAt(0);
        nodeStack.push(this);
        MsgTree currentNode = this;
        boolean isInsertingLeft = true;

        for (int i = 1; i < encoding.length(); i++) {
            char currentChar = encoding.charAt(i);
            MsgTree newNode = new MsgTree(currentChar);

            if (isInsertingLeft) {
                currentNode.leftChild = newNode;
            } else {
                currentNode.rightChild = newNode;
            }

            if (currentChar == '^') {
                currentNode = nodeStack.push(newNode);
                isInsertingLeft = true;
            } else {
                if (!nodeStack.isEmpty()) {
                    currentNode = nodeStack.pop();
                }
                isInsertingLeft = false;
            }
        }
    }

    /**
     * Constructs a single tree node with a specified character.
     * Both left and right children of this node are initialized to null.
     *
     * @param payloadChar The character to be stored in this node.
     */
    public MsgTree(char payloadChar) {
        this.payloadChar = payloadChar;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * Prints the binary codes of characters encoded in the binary tree.
     * This method performs a recursive traversal of the tree and prints 
     * each character with its corresponding binary code path.
     *
     * @param root The root of the binary tree.
     * @param path The binary path (as a string of 0s and 1s) leading to the current node.
     */
    public static void printCodes(MsgTree root, String path) {
        if (root == null) {
            return;
        }

        if (root.payloadChar != '^') {
            System.out.println(root.payloadChar + "\t" + path);
        }

        printCodes(root.leftChild, path + "0");
        printCodes(root.rightChild, path + "1");
    }

    /**
     * Decodes a given message using the binary tree.
     * This method traverses the tree based on each bit (0 or 1) in the message 
     * and constructs the decoded message accordingly.
     *
     * @param root The root of the binary tree used for decoding.
     * @param msg  The encoded message as a string of binary digits.
     * @throws IllegalArgumentException If the root or the message string is null or empty.
     * @throws IllegalStateException    If the message contains an invalid bit sequence.
     */
    public void decode(MsgTree root, String msg) {
        if (root == null || msg == null || msg.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments for decoding");
        }

        System.out.println("\nMESSAGE:");
        StringBuilder decodedMessage = new StringBuilder();
        MsgTree currentNode = root;

        for (char bit : msg.toCharArray()) {
            currentNode = (bit == '0') ? currentNode.leftChild : currentNode.rightChild;
            if (currentNode == null) {
                throw new IllegalStateException("Invalid bit sequence in message.");
            }

            if (currentNode.payloadChar != '^') {
                decodedMessage.append(currentNode.payloadChar);
                currentNode = root;
            }
        }

        System.out.println(decodedMessage.toString());
        printStatistics(msg, decodedMessage.toString());
    }

    /**
     * Prints statistics of the encoded message, including average bits per character, 
     * total number of characters, and the percentage of space saved by the encoding.
     *
     * @param encodedMessage The original encoded message as a string of binary digits.
     * @param decodedMessage The decoded message.
     */
    private void printStatistics(String encodedMessage, String decodedMessage) {
        System.out.println("STATISTICS:\n-----------------------");
        double avgBitsPerChar = (double) encodedMessage.length() / decodedMessage.length();
        System.out.println(String.format("Avg bits/char: %.1f", avgBitsPerChar));
        System.out.println("Total Characters: " + decodedMessage.length());
        double spaceSaving = (1.0 - (double) encodedMessage.length() / (decodedMessage.length() * 16)) * 100;
        System.out.println(String.format("Space Saving: %.1f%%", spaceSaving));
    }
}
