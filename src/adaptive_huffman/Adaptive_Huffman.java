/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adaptive_huffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mohamed Ashraf badawy Moussa
 */
public class Adaptive_Huffman {

    public static Node NYT = new Node();
    public static Node oldNYT = new Node();
    public static Node root = new Node();
    public static Scanner input = new Scanner(System.in);
    public static Vector<String> shortCode = new Vector<>();
    public static Vector<Node> validSwapRotate = new Vector<>();
    public static Vector<Node> validSwapNodes = new Vector<>();

    public static void printTree() {
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {

            Node tempNode = queue.poll();
            System.out.println(tempNode.nodeSymbol + " " + tempNode.nodeCode + " " + tempNode.symbolCounter + " " + tempNode.nodeNumber);

            /*Enqueue left child */
            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            /*Enqueue right child */
            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
    }

    public static void updateTree() {
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node tempNode = queue.poll();
            if (tempNode.left != null && tempNode.right != null) {
                tempNode.left.nodeCode = tempNode.nodeCode + "0";
                tempNode.right.nodeCode = tempNode.nodeCode + "1";
                if (tempNode.symbolCounter != (tempNode.left.symbolCounter + tempNode.right.symbolCounter)) {
                    tempNode.symbolCounter = (tempNode.left.symbolCounter + tempNode.right.symbolCounter);
                }
            }
            /*Enqueue left child */
            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            /*Enqueue right child */
            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
    }

    public static void increment(Node node) {
        while (node != null) {
            node.symbolCounter++;
            node = node.parent;
        }
    }

    public static void swapNodes(Node node) {
        validSwapNodes.clear();
        Node curr = node.parent.parent;
        Node prev = node.parent;
        while (curr != null) {
            if (curr.right == prev) {
                validSwapNodes.add(curr.left);
            } else if (curr.left == prev) {
                validSwapNodes.add(curr.right);
            }
            prev = curr;
            curr = curr.parent;
        }
        if (!validSwapNodes.isEmpty()) {
            Node max = validSwapNodes.get(0);
            for (int i = 1; i < validSwapNodes.size(); i++) {
                if (validSwapNodes.get(i).nodeNumber > max.nodeNumber && validSwapNodes.get(i).symbolCounter == max.symbolCounter) {
                    max = validSwapNodes.get(i);
                }
            }

            if (node.nodeNumber <= max.nodeNumber && node.symbolCounter == max.symbolCounter) {
                char c = node.nodeSymbol;
                node.nodeSymbol = max.nodeSymbol;
                max.nodeSymbol = c;

                int num = node.symbolCounter;
                node.symbolCounter = max.symbolCounter;
                max.symbolCounter = num;
            }

        }

    }

    public static void swapRotate() {
        // swap condition 
        validSwapRotate.clear();
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {

            Node tempNode = queue.poll();
            if (tempNode.left != null && tempNode.right != null) {
                if (tempNode.left.symbolCounter > tempNode.right.symbolCounter) {
                    validSwapRotate.add(tempNode);
                }
            }
            /*Enqueue left child */
            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            /*Enqueue right child */
            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
        for (int i = 0; i < validSwapRotate.size(); i++) {
            Node max = validSwapRotate.get(i).right;
            Node temp = validSwapRotate.get(i).left;

            validSwapRotate.get(i).left = validSwapRotate.get(i).right;
            validSwapRotate.get(i).right = temp;

            int x = max.nodeNumber;
            max.nodeNumber = temp.nodeNumber;
            temp.nodeNumber = x;

            String STR = max.nodeCode;
            max.nodeCode = temp.nodeCode;
            temp.nodeCode = STR;

            updateTree();
        }
    }

    public static Node search(String key) {
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node tempNode = queue.poll();
            if (tempNode.nodeCode.equals(key)) {
                return tempNode;
            }
            /*Enqueue left child */
            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            /*Enqueue right child */
            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
        return null;
    }

    public static Node search(char key) {
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node tempNode = queue.poll();
            if (tempNode.nodeSymbol == key) {
                return tempNode;
            }
            /*Enqueue left child */
            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            /*Enqueue right child */
            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
        return null;
    }

    public static String compress(String text) {
        String compressedText = "";
        shortCode.add("00");
        shortCode.add("01");
        shortCode.add("10");

        root.parent = null;
        root.left = new Node();
        root.right = new Node();
        root.nodeCode = "";
        root.nodeNumber = 100;

        root.left.nodeCode = "0";
        root.left.nodeNumber = 98;
        root.left.nodeSymbol = '*';
        root.left.symbolCounter = 0;
        root.left.parent = root;

        root.right.nodeCode = "1";
        root.right.nodeNumber = 99;
        root.right.nodeSymbol = text.charAt(0);
        root.right.symbolCounter = 1;
        root.right.parent = root;

        root.symbolCounter++;
        NYT = root.left;

        if (text.charAt(0) == 'A' || text.charAt(0) == 'a') {
            compressedText += shortCode.get(0);
        } else if (text.charAt(0) == 'B' || text.charAt(0) == 'b') {
            compressedText += shortCode.get(1);
        } else {
            compressedText += shortCode.get(2);
        }

        for (int i = 1; i < text.length(); i++) {
            char symbol = text.charAt(i);
            Node found = search(symbol);
            if (found == null) {
                if (text.charAt(i) == 'A' || text.charAt(i) == 'a') {
                    compressedText += NYT.nodeCode + shortCode.get(0);
                } else if (text.charAt(i) == 'B' || text.charAt(i) == 'b') {
                    compressedText += NYT.nodeCode + shortCode.get(1);
                } else {
                    compressedText += NYT.nodeCode + shortCode.get(2);
                }

                NYT.left = new Node();
                NYT.right = new Node();
                NYT.left.nodeCode = NYT.nodeCode + "0";
                NYT.left.nodeNumber = NYT.nodeNumber - 2;
                NYT.left.nodeSymbol = '*';
                NYT.left.symbolCounter = 0;
                NYT.left.parent = NYT;
                NYT.right.nodeCode = NYT.nodeCode + "1";
                NYT.right.nodeNumber = NYT.nodeNumber - 1;
                NYT.right.nodeSymbol = text.charAt(i);
                NYT.right.symbolCounter = 1;
                NYT.right.parent = NYT;
                increment(NYT);
                NYT.nodeSymbol = '#';

                NYT = NYT.left;
                swapRotate();
            } else {
                found = search(symbol);
                compressedText += found.nodeCode;
                swapNodes(found);
                found = search(symbol);
                increment(found);
                swapRotate();
            }
        }
        return compressedText;
    }

    public static String decompress(String text) {
        String originalText = "";
        String STR = text.charAt(0) + "" + text.charAt(1) + "";

        if (STR.equals("00")) {
            originalText += "A";
        } else if (STR.equals("01")) {
            originalText += "B";
        } else {
            originalText += "C";
        }

        root.parent = null;
        root.left = new Node();
        root.right = new Node();
        root.nodeCode = "";
        root.nodeNumber = 100;

        root.left.nodeCode = "0";
        root.left.nodeNumber = 98;
        root.left.nodeSymbol = '*';
        root.left.symbolCounter = 0;
        root.left.parent = root;

        root.right.nodeCode = "1";
        root.right.nodeNumber = 99;
        root.right.symbolCounter = 1;
        root.right.parent = root;

        if (STR.equals("00")) {
            root.right.nodeSymbol = 'A';
        } else if (STR.equals("01")) {
            root.right.nodeSymbol = 'B';
        } else {
            root.right.nodeSymbol = 'C';
        }

        root.symbolCounter++;
        NYT = root.left;

        for (int i = 2; i < text.length(); i++) {
            STR = "" + text.charAt(i) + "";

            while (true) {
                Node found = search(STR);
                if (found == null) {
                    break;
                }
                if (found.nodeCode.equals(NYT.nodeCode)) {
                    String temp = text.charAt(i + 1) + "" + text.charAt(i + 2) + "";
                    if (temp.equals("00")) {
                        originalText += "A";
                    } else if (temp.equals("01")) {
                        originalText += "B";
                    } else {
                        originalText += "C";
                    }
                    i += 2;

                    NYT.left = new Node();
                    NYT.right = new Node();

                    NYT.left.nodeCode = NYT.nodeCode + "0";
                    NYT.left.nodeNumber = NYT.nodeNumber - 2;
                    NYT.left.nodeSymbol = '*';
                    NYT.left.symbolCounter = 0;
                    NYT.left.parent = NYT;

                    NYT.right.nodeCode = NYT.nodeCode + "1";
                    NYT.right.nodeNumber = NYT.nodeNumber - 1;
                    NYT.right.symbolCounter = 1;
                    NYT.right.parent = NYT;

                    if (temp.equals("00")) {
                        NYT.right.nodeSymbol = 'A';
                    } else if (temp.equals("01")) {
                        NYT.right.nodeSymbol = 'B';
                    } else {
                        NYT.right.nodeSymbol = 'C';
                    }

                    increment(NYT);
                    NYT.nodeSymbol = '#';

                    NYT = NYT.left;
                    swapRotate();

                    break;
                } else if (found.nodeSymbol == 'A' || found.nodeSymbol == 'B' || found.nodeSymbol == 'C') {
                    char c;
                    if (found.nodeSymbol == 'A') {
                        originalText += "A";
                        c = 'A';
                    } else if (found.nodeSymbol == 'B') {
                        originalText += "B";
                        c = 'B';
                    } else {
                        originalText += "C";
                        c = 'C';
                    }
                    swapNodes(found);
                    found = search(c);
                    increment(found);
                    swapRotate();
                    break;
                } else {
                    i++;
                    STR += "" + text.charAt(i);
                }
                
            }
        }
        return originalText;
    }

    public static void main(String[] args) {
        System.out.println("Adaprive Huffman.");
        System.out.println("1- Compress Text.");
        System.out.println("2- Decompress Text.");
        System.out.print("Enter your choice : ");
        int choice = input.nextInt();
        if (choice == 1) {
            File file = new File("Text.txt");
            String text = null;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                text = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Adaptive_Huffman.class.getName()).log(Level.SEVERE, null, ex);
            }
            String compressedText = compress(text);
            System.out.println("output : " + compressedText);
            printTree();
            try (PrintWriter out = new PrintWriter("output.txt")) {
                out.println(compressedText);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Adaptive_Huffman.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (choice == 2) {
            File file = new File("output.txt");
            String text = null;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                text = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Adaptive_Huffman.class.getName()).log(Level.SEVERE, null, ex);
            }
            String compressedText = decompress(text);
            System.out.println("Text : " + compressedText);
            printTree();
            try (PrintWriter out = new PrintWriter("Text2.txt")) {
                out.println(compressedText);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Adaptive_Huffman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
