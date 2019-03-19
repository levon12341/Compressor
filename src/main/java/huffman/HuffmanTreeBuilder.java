package huffman;

import tree.*;
import java.math.BigDecimal;

public class HuffmanTreeBuilder {
		private final int ENCODING_TABLE_SIZE = Character.MAX_VALUE;
	    private BinaryTree huffmanTree;
	    private Elem[] chars;
	    private BigDecimal bytedMsgLength;
	
	    //----------------constructor----------------------
	    public HuffmanTreeBuilder() {
	        chars = new Elem[ENCODING_TABLE_SIZE];
	        bytedMsgLength = new BigDecimal(0);
	        for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
	    		chars[i] = new Elem((char)i);
			}
		}
		
		public HuffmanTreeBuilder(Elem[] frequenceTable) {
			chars = frequenceTable;
			for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
				if (chars[i] == null)
					chars[i] = new Elem((char)i);
			}

			bytedMsgLength = new BigDecimal(0);
		}
	    
	    public void build() {
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

	    public void displayFrequenceArray() {
	    	System.out.println("========================Frequence array====================");

	    	for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
	    		if (chars[i].getFrequence() != 0)
	    			System.out.println((char)i + ' ' + chars[i].getFrequence());
			}

			System.out.println("=============================================================");
		}
	    //-----------------------------------------------------
	    public BigDecimal getBytedMsgLength() {
	    	return bytedMsgLength;
	    }
	}

