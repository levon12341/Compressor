package main;

import huffman.HuffmanOperator;
import huffman.HuffmanTree;
import io.FileInputHelper;
import io.FileOutputHelper;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;

public class Runner {
	public static String[] startProc(String proc, String filePath) 
			throws IllegalArgumentException, FileNotFoundException, IOException {
		long startTime, finishTime; 
		startTime = System.currentTimeMillis();
		String[] logs;
		if (testExistance(filePath)) {
			if (proc.equals("compress")) 
				logs = compress(filePath);
			else if (proc.equals("extract")) 
				logs = extract(filePath);
			else
				throw new IllegalArgumentException("Unmeaning command in startProc method");
			finishTime = System.currentTimeMillis();
		} else {
			logs = new String[2];
			logs[0] = "Файл не найден";
		}
		
		finishTime = System.currentTimeMillis();
		
		logs[0] += "\nЗатрачено времени: " 
				+ ((double)(finishTime - startTime) / (double)1000) + "s";
		return logs;
		
	}
	
	private static boolean testExistance(String filePath) {
		if (new File(filePath).exists())
			return true;
		return false;
	}
	
	private static String[] compress(String filePath) throws IOException {
		ArrayList<String> logs = new ArrayList<>();
		String[] toReturn;
		
        File inputFile = new File(filePath),
        		compressedFile, treeFile;
        
        HuffmanOperator operator = new HuffmanOperator(new HuffmanTree());
        
        try (FileInputHelper fi = new FileInputHelper(inputFile)){
        	HuffmanTree tree = operator.getTree();
        	for (; true; )
        		tree.addChar((char)fi.readByte());
        } catch (MalformedInputException malformedInputException) {
        	logs.add("Текущая кодировка файла не поддерживается");
        } catch (EOFException ignore) {}
        
        operator.getTree().init();

        compressedFile = new File(inputFile.getAbsolutePath() + ".cpr");
        compressedFile.createNewFile();
        
        treeFile = new File(filePath + ".cpr.tree");
        treeFile.createNewFile();
        try (FileOutputHelper fo = new FileOutputHelper(treeFile)){
        	fo.writeObject(operator.getTree().getTree());
        	fo.closeObjectOutputStream();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        try {
        	operator.compressAndWrite(inputFile, compressedFile);
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        }
        
        logs.add("Путь к сжатому файлу: " + compressedFile.getAbsolutePath());
        logs.add("Без таблицы файл будет невозможно извлечь!");
        
        double idealRatio = operator.getCompressionRatio();
        double realRatio = (double) inputFile.length() 
        		/ ((double) compressedFile.length() + treeFile.length());
        
        logs.add("Идеализированный коэффициент сжатия: " + idealRatio);
        logs.add("Коэффициент сжатия с учетом кодировочной таблицы: " + realRatio);

        toReturn = new String[logs.size()];
        toReturn = logs.toArray(toReturn);
        return toReturn;
    }

    private static String[] extract(String filePath) throws IOException {
        File compressedFile, extractedFile, treeFile;
        compressedFile = new File(filePath);
    	extractedFile = new File(filePath.replace(".cpr", ".xtr"));
    	treeFile = new File(filePath + ".tree");
    	ArrayList<String> logs = new ArrayList<>();
    	HuffmanOperator operator = new HuffmanOperator();
    		//extract:
    	try {
    		operator.extractAndWrite(compressedFile, treeFile, extractedFile);
    		logs.add("Путь к распакованному файлу " + extractedFile.getAbsolutePath());
    	} catch (ClassNotFoundException noClass) {
   			logs.add(noClass.getCause().toString());
   		}
		String[] toReturn = new String[logs.size()];
		toReturn = logs.toArray(toReturn);
		return toReturn;
    }
}
