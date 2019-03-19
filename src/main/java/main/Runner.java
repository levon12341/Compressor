package main;

import exceptions.CompressException;
import huffman.HuffmanOperator;
import huffman.HuffmanTreeBuilder;
import huffman.Elem;
import io.FileInputHelper;
import io.FileOutputHelper;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

public class Runner {
	public static String[] startProc(String proc, String filePath) 
			throws IllegalArgumentException, CompressException, IOException {
		long startTime, finishTime; 
		startTime = System.currentTimeMillis();
		String[] logs;
		if (proc.equals("compress"))
			logs = compress(filePath);
		else if (proc.equals("extract"))
			logs = extract(filePath);
		else
			throw new IllegalArgumentException("Unmeaning command in startProc method: " + proc);
		
		finishTime = System.currentTimeMillis();
		
		logs[0] += "\nЗатрачено времени: " 
				+ ((double)(finishTime - startTime) / (double)1000) + "s";
		return logs;
		
	}
	
	private static String[] compress(String filePath)
			throws IOException, CompressException {
		ArrayList<String> logs = new ArrayList<>();
		String[] toReturn;
		
        File inputFile = new File(filePath),
				compressedFile, tableFile;
        
        HuffmanOperator operator = new HuffmanOperator(new HuffmanTreeBuilder());
        
        try (FileInputHelper fi = new FileInputHelper(inputFile)){
        	HuffmanTreeBuilder treeBuilder = operator.getTreeBuilder();
        	for (; true; )
        		treeBuilder.addChar((char)fi.readByte());
        } catch (MalformedInputException malformedInputException) {
        	logs.add("Текущая кодировка файла не поддерживается");
        } catch (EOFException ignore) {}
        
        operator.getTreeBuilder().build();

        compressedFile = new File(inputFile.getAbsolutePath() + ".cpr");
        compressedFile.createNewFile();
		
		//write encoding table to file
        tableFile = new File(filePath + ".cpr.tbl");
        tableFile.createNewFile();
        try (FileOutputHelper fo = new FileOutputHelper(tableFile)){
			for (Elem item : operator.getEncodingTable()) {
				if (item.getFrequence() != 0) {
					if (item.getLetter() == '\n')
						fo.writeLine("lf" + " " + item.getFrequence() + "\n");
					else if (item.getLetter() == ' ')
						fo.writeLine("sp" + " " + item.getFrequence() + "\n");
					else if (item.getLetter() == '\t')
						fo.writeLine("tb" + " " + item.getFrequence() + '\n');
					else
						fo.writeLine(item.getLetter() + " " + 
							item.getFrequence() + "\n");
				}
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
        operator.compress(inputFile, compressedFile);
        
        logs.add("Путь к сжатому файлу: " + compressedFile.getAbsolutePath());
        logs.add("Без таблицы файл будет невозможно извлечь!");
        
        double idealRatio = operator.getCompressionRatio();
        double realRatio = (double) inputFile.length() 
        		/ ((double) compressedFile.length() + tableFile.length());
        
        logs.add("Идеализированный коэффициент сжатия: " + idealRatio);
        logs.add("Коэффициент сжатия с учетом кодировочной таблицы: " + realRatio);

        toReturn = new String[logs.size()];
        toReturn = logs.toArray(toReturn);
        return toReturn;
    }

    private static String[] extract(String filePath) throws CompressException {
        File compressedFile, extractedFile, tableFile;
        compressedFile = new File(filePath);
    	extractedFile = new File(filePath.replace(".cpr", ".xtr"));
    	tableFile = new File(filePath + ".tbl");
    	ArrayList<String> logs = new ArrayList<>();
    	HuffmanOperator operator = new HuffmanOperator();
    	//extract:
		operator.extract(compressedFile, extractedFile, tableFile);
    	logs.add("Путь к распакованному файлу " + extractedFile.getAbsolutePath());
		String[] toReturn = new String[logs.size()];
		toReturn = logs.toArray(toReturn);
		return toReturn;
    }
}