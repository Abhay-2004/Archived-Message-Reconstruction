/**
 * Main class for the Archived Message Reconstruction project.
 * This class handles the user interaction for file input and orchestrates 
 * the process of decoding a message from a file. The file contains an encoding
 * scheme and an encoded message which are used to construct a binary tree for 
 * message decoding.
 * 
 * The class reads the file specified by the user, constructs a binary tree 
 * based on the encoding scheme, and then decodes the message using this tree.
 * It also prints the character codes and the decoded message to the console.
 * 
 * Error handling is included for issues related to file reading and processing.
 * 
 * @author Abhay Prasanna Rao
 */
package edu.iastate.cs228.hw4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    /**
     * The main method to run the Archived Message Reconstruction application.
     * It prompts the user for the name of the file containing the encoded message,
     * reads the file content, constructs a binary tree for the encoding scheme,
     * and decodes the message.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please enter filename to decode:");
            String filename = scanner.nextLine();

            String content = new String(Files.readAllBytes(Paths.get(filename))).trim();
            int pos = content.lastIndexOf('\n');
            String pattern = content.substring(0, pos); // Pattern
            String binCode = content.substring(pos + 1).trim(); // Encoded message

            // Construct the tree and decode the message
            MsgTree root = new MsgTree(pattern);
            System.out.println("\nCharacter Code\n-------------------------");
            MsgTree.printCodes(root, "");
            root.decode(root, binCode);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
