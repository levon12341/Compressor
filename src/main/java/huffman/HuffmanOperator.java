package huffman;

import tree.*;
import io.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class HuffmanOperator {
	private final byte ENCODING_TABLE_SIZE = 127;
    private HuffmanTree mainHuffmanTree;
    //private String myString;
    private double ratio;
    private Elem[] chars;
    
    public HuffmanOperator(HuffmanTree MainHuffmanTree) {//for compress
        this.mainHuffmanTree = MainHuffmanTree;
        chars = mainHuffmanTree.getChars();
    }

    public HuffmanOperator() {}//for extract

    
    public void compressAndWrite(File inputFile, File outputFile) throws IOException {
    	//
    	FileInputHelper fi = new FileInputHelper(inputFile);
    	FileOutputHelper fo = new FileOutputHelper(outputFile);
    	setCompressionRatio();
        fo.writeByte((8 - (byte)mainHuffmanTree.getBytedMsgLength()
        		.remainder(new BigDecimal(8)).intValue()) % 8);//indicate number of added zeroes
    	String ost = "";
        
        while(true) {
            String s = ost;
            while (true) {
                char c = (char)fi.readInt();
                if (c == 65535) {
                    if (s.length() == 0) {
                    	fi.close();
                    	fo.close();
                        return;
                    }
                    boolean flag = false;
                    if (s.length() > 8) {
                    	flag = true;
                    	ost = s.substring(8, s.length());
                		s = s.substring(0, 8);
                    }
                    while (s.length() != 8) 
                        s += "0";
                    if (flag)
                    	fo.writeByte(Integer.parseInt(ost,2));
                    fo.writeByte(Integer.parseInt(s, 2));
                    fi.close();
                    fo.close();
                    return;
                } else {
                	s += chars[c].getCode();
                	if (s.length() == 8) {
                		ost = "";
                		break;
                	} else if (s.length() > 8) {
                		ost = s.substring(8, s.length());
                		s = s.substring(0, 8);
                		break;
                	}
                }
            }
            fo.writeByte(Integer.parseInt(s, 2));
        }
    }
    
    private void setCompressionRatio() {
        double sumA = 0, sumB = 0;//A-the original sum
        for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
            if (chars[i].getFrequence() != 0) {
                sumA += 8 * chars[i].getFrequence();
                sumB += chars[i].getCode().length() * chars[i].getFrequence();
            }
        }
        ratio = sumA / sumB;
    }    
    public double getCompressionRatio() {
        return ratio;
    }

    //---------------------------------------end of compression----------------------------------------------------------------
    //------------------------------------------------------------extract-----------------------------------------------------
    public void extractAndWrite(File compressedFile, File treeFile, File extractedFile) throws IOException, ClassNotFoundException {
        FileInputHelper fi = new FileInputHelper(compressedFile),
        		treeReader = new FileInputHelper(treeFile);
        FileOutputHelper fo = new FileOutputHelper(extractedFile);
        BinaryTree huffmanTree = (BinaryTree)treeReader.readObject();
        treeReader.close();
        byte delta = fi.readByte();//number of missing zeroes
        Node currentNode = huffmanTree.getRoot();
        String s = "";
        int next = fi.readInt();
       	while (true) {
       		int cur = next;
       		next = fi.readInt();
       		if (cur == -1) { 
       			int i;
       			while (s.length() != 0) {
       				for (i = 0 ; !currentNode.isLeaf(); i++) {
       					if (i >= s.length())
       						break;
       					if (s.charAt(i) == '1')
       						currentNode = currentNode.getRightChild();
       					else
       						currentNode = currentNode.getLeftChild();
       				}
       				fo.writeChar(currentNode.getLetter());
       				s = s.substring(i, s.length());
       				currentNode = huffmanTree.getRoot();
       			}
       			break;
       		}
       		s += String.format("%8s", Integer.toBinaryString((byte)cur & 0xff)).replace(' ', '0');
       		if (next == -1) 
       			s = s.substring(0, s.length() - delta);
       		int i;
       		for (i = 0; !currentNode.isLeaf(); i++) {
       			if (i < s.length())
       				if (s.charAt(i) == '1')
       					currentNode = currentNode.getRightChild();
       				else
       					currentNode = currentNode.getLeftChild();
       			else
       				break;
       		}
       		if (i < s.length()) {
       			fo.writeChar(currentNode.getLetter());
       			s = s.substring(i, s.length());
       			currentNode = huffmanTree.getRoot();
       		} else {
       			currentNode = huffmanTree.getRoot();
       		}
       	}
        fo.close();
        fi.close();
    }

    public String getEncodingTable() {
        String enc = "";
    	for (int i = 0; i < chars.length; i++) {
        	if (chars[i].getFrequence() != 0) 
        		enc += (char)i + chars[i].getCode() + '\n';
        }
    	return enc;
    }


    public void displayEncodingArray() {
        System.out.println("======================Encoding table====================");
        for (int i = 0; i < ENCODING_TABLE_SIZE; i++) {
            if (chars[i].getFrequence() != 0) {
                System.out.print((char)i + " ");
                System.out.println(chars[i].getCode());
            }
        }
        System.out.println("========================================================");
    }
    
    public HuffmanTree getTree() {
    	return mainHuffmanTree;
    }	
}