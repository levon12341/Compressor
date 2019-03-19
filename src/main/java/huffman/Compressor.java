package huffman;

import io.FileInputHelper;
import io.FileOutputHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.NoSuchFileException;

class Compressor {
    private HuffmanTreeBuilder treeBuilder;
    private final int ENCODING_TABLE_SIZE = Character.MAX_VALUE;
    private Elem[] chars;
    private double ratio;
    private Compressor compressor;
    private File inputFile;
    private File outputFile;

    Compressor(HuffmanTreeBuilder treeBuilder,
               File inputFile,
               File outputFile) {
        this.treeBuilder = treeBuilder;
        this.chars = this.treeBuilder.getChars();
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void compressAndWrite()
            throws IOException {
        //
        FileInputHelper fi = new FileInputHelper(inputFile);
        FileOutputHelper fo = new FileOutputHelper(outputFile);
        setCompressionRatio();
        fo.writeByte((8 - (byte)treeBuilder.getBytedMsgLength()
                .remainder(new BigDecimal(8)).intValue()) % 8);//indicate number of added zeroes
        String ost = "";
        while(true) {
            String s = ost;
            while (true) {
                int c = fi.readInt();
                if (c == -1) {
                    boolean flag = false;
                    if (s.length() > 8) {
                        flag = true;
                        ost = s.substring(8, s.length());
                        s = s.substring(0, 8);
                    }
                    while (s.length() != 8)
                        s += "0";
                    if (flag) {
                        System.out.print(ost);
                        fo.writeByte(Integer.parseInt(ost, 2));
                    }
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
            try {
                fo.writeByte(Integer.parseInt(s, 2));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
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
}
