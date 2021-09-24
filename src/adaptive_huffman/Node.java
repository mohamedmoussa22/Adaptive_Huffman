/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adaptive_huffman;

/**
 *
 * @author Mohamed Ashraf badawy Moussa
 */
public class Node
{
    public char nodeSymbol;
    public String nodeCode;
    public int nodeNumber;
    public int symbolCounter;
    public Node left = null;
    public Node right = null;
    public Node parent = null;

    public Node(char nodeSymbol, String nodeCode, int nodeNumber, int symbolCounter) 
    {
        this.nodeSymbol = nodeSymbol;
        this.nodeCode = nodeCode;
        this.nodeNumber = nodeNumber;
        this.symbolCounter = symbolCounter;
    }
    
    public Node()
    {
        nodeCode = "";
        nodeNumber = 0;
        nodeSymbol = ' ';
        symbolCounter = 0;
        left = null;
        right = null;
        parent = null;
    }
}
