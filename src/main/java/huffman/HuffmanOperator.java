package huffman;

import exceptions.CompressException;
import java.io.*;

public class HuffmanOperator {
    private final int ENCODING_TABLE_SIZE = Character.MAX_VALUE;
    
    private HuffmanTreeBuilder treeBuilder;
    private double ratio;
    private Elem[] chars;
    
    public HuffmanOperator(HuffmanTreeBuilder treeBuilder) {//for compress
        this.treeBuilder = treeBuilder;
        chars = this.treeBuilder.getChars();
    }

    public HuffmanOperator() {} //for extract

    public void compress(File inputFile, File outputFile) throws CompressException {

        if (!inputFile.exists())
            throw new CompressException("Не найден файл для сжатия");
        else if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Compressor compressor = new Compressor(treeBuilder, inputFile, outputFile);

        try {
            compressor.compressAndWrite();
            ratio = compressor.getCompressionRatio();
        } catch (IOException e) {
            throw new CompressException("Исходный файл поврежден");
        }
    }

    public void extract(File inputFile, File outputFile, File tableFile) throws CompressException {

        if (!inputFile.exists())
            throw new CompressException("Не найден файл для извлечения");
        else if (!tableFile.exists())
            throw new CompressException("Не найден файл с частотной таблицей");
        else if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Extractor extractor = new Extractor(treeBuilder, inputFile, outputFile, tableFile);

        try {
            extractor.extractAndWrite();
        } catch (IOException e) {
            throw new CompressException("Исходный файл поврежден");
        }
    }

    public Elem[] getEncodingTable() {
        return chars;
    }

    public double getCompressionRatio() {
        return ratio;
    }

    /* debug tips
    public void displayFrequenceTable(Elem[] chars) {
        for (int i = 0; i < chars.length; i++) 
            if (chars[i] != null && chars[i].getFrequence() != 0) {
                if (chars[i].getLetter() == '\n')
                    System.out.print("lf");
                else if (chars[i].getLetter() == '\t')
                    System.out.print("tb");
                else if (chars[i].getLetter() == ' ')
                    System.out.print("sp");
                else 
                    System.out.print(chars[i].getLetter());
                System.out.println(" " + chars[i].getFrequence());
            }
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
    }*/
    
    public HuffmanTreeBuilder getTreeBuilder() {
    	return treeBuilder;
    }	
}