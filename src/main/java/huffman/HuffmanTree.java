package huffman;

import tree.*;
import java.math.BigDecimal;

public class HuffmanTree {
		private final byte ENCODING_TABLE_SIZE = 127;
	    //private String myString;
	    private BinaryTree huffmanTree;
	    private Elem[] chars;
	    private BigDecimal bytedMsgLength;
	
	    //----------------constructor----------------------
	    public HuffmanTree() {//a0String newString) {
	        //myString = newString;
	        chars = new Elem[ENCODING_TABLE_SIZE];
	        bytedMsgLength = new BigDecimal(0);
	        for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
	    		chars[i] = new Elem();
	    	}
	    }
	    
	    public void init() {
	    	huffmanTree = getHuffmanTree();
	    	fillEncodingArray(huffmanTree.getRoot(), "", "");
	    }
	
	    //--------------------frequence array------------------------
	    public void addChar(char c) {
	    	chars[c].incrementFrequence();
	    }
	
	    public Elem[] getFrequenceArray() {
	        return chars;
	    }
	
	    //------------------------huffman tree creation------------------
	    private BinaryTree getHuffmanTree() {
	        PriorityQueue pq = new PriorityQueue();
	        
	        for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
	            if (chars[i].getFrequence() != 0) {
	                Node newNode = new Node((char) i, chars[i].getFrequence());
	                BinaryTree newTree = new BinaryTree(newNode);
	                pq.insert(newTree);
	            }
	        }
	
	        int startSize = pq.getSize();
	        while (true) {
	            BinaryTree tree1 = pq.remove();
	        	try {
	                BinaryTree tree2 = pq.remove();
	
	                Node newNode = new Node();
	                newNode.addChild(tree1.getRoot());
	                newNode.addChild(tree2.getRoot());
	
	                BinaryTree newTree = new BinaryTree(newNode);
	                pq.insert(newTree);
	            } catch (IndexOutOfBoundsException e) {
	                return tree1;
	            }
	        }
	        
	    }
	
	    public BinaryTree getTree() {
	        return huffmanTree;
	    }
	
	    //-------------------encoding array------------------
	    public void fillEncodingArray(Node node, String codeBefore, String direction) {
	        if (node.isLeaf()) {
	            chars[(int)node.getLetter()].setCode(codeBefore + direction);
	            bytedMsgLength = bytedMsgLength.add(new BigDecimal(chars[(int)node.getLetter()].getCode().length() 
	            		* chars[(int)node.getLetter()].getFrequence()));
	        } else {
	            fillEncodingArray(node.getLeftChild(), codeBefore + direction, "0");
	            fillEncodingArray(node.getRightChild(), codeBefore + direction, "1");
	        }
	    }
	
	    public Elem[] getChars() {
	        return chars;
	    }
	
	    public void displayEncodingArray() {
	        fillEncodingArray(huffmanTree.getRoot(), "", "");
	
	        System.out.println("======================Encoding table====================");
	        for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
	            if (chars[i].getFrequence() != 0) {
	                System.out.print((char)i + " ");
	                System.out.println(chars[i].getCode());
	            }
	        }
	        System.out.println("========================================================");
	    }
	    //-----------------------------------------------------
	    public BigDecimal getBytedMsgLength() {
	    	return bytedMsgLength;
	    }
	    /*String getOriginalString() {
	        return myString;
	    }*/
	}

