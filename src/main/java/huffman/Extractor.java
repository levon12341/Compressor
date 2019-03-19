package huffman;

import io.FileInputHelper;
import io.FileOutputHelper;
import tree.BinaryTree;
import tree.Node;

import java.io.*;

class Extractor {
    private HuffmanTreeBuilder treeBuilder;
    private final int ENCODING_TABLE_SIZE = Character.MAX_VALUE;
    private File inputFile;
    private File outputFile;
    private File tableFile;


    public Extractor(HuffmanTreeBuilder treeBuilder,
                     File inputFile,
                     File outputFile,
                     File tableFile) {
        this.treeBuilder = treeBuilder;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.tableFile = tableFile;
    }

    public void extractAndWrite() throws IOException {
        FileInputHelper fi = new FileInputHelper(inputFile);
        FileOutputHelper fo = new FileOutputHelper(outputFile);

        buildHuffmanTree(tableFile);

        BinaryTree huffmanTree = treeBuilder.getTree();
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
                    for (i = 0; !currentNode.isLeaf(); i++) {
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
            s += String.format("%8s", Integer.toBinaryString((byte) cur & 0xff)).replace(' ', '0');
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



    private void buildHuffmanTree(File tableFile) {
        Elem[] frequenceTable = new Elem[ENCODING_TABLE_SIZE];

        try (BufferedReader bf = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(tableFile)))) {
            //bf.readLine();//skip the empty line
            while (true) {
                String currentString = bf.readLine();
                if (currentString == null)
                    break;

                String[] s = currentString.split(" ");

                String letter = s[0];
                int freq = Integer.parseInt(s[1]);

                if (letter.equals("lf")) {  //linefeed
                    frequenceTable['\n'] = new Elem('\n');
                    frequenceTable['\n'].setFrequence(freq);
                } else if (letter.equals("sp")) {//spacebar
                    frequenceTable[' '] = new Elem(' ');
                    frequenceTable[' '].setFrequence(freq);
                } else if (letter.equals("tb")) {
                    frequenceTable['\t'] = new Elem('\t');
                    frequenceTable['\t'].setFrequence(freq);
                } else {
                    frequenceTable[letter.charAt(0)] = new Elem(letter.charAt(0));
                    frequenceTable[letter.charAt(0)].setFrequence(freq);
                }
            }
            treeBuilder = new HuffmanTreeBuilder(frequenceTable);
            treeBuilder.build();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
