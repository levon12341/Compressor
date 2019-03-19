package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class FileInputHelper implements Closeable {
	private FileInputStream fileInputStream;
	private BufferedReader fileBufferedReader;
	
	public FileInputHelper(File file) throws NoSuchFileException, FileNotFoundException {
		fileInputStream = new FileInputStream(file);
		fileBufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_16));
	}
	
	
    public byte readByte() throws IOException {
    	int cur = fileInputStream.read();
    	if (cur == -1)
    		throw new EOFException();
    	return (byte)cur;
	}
    
    public int readInt() throws IOException {
    	return fileInputStream.read();
    }
    
    public String readLine() throws IOException {
    	return fileBufferedReader.readLine();
    }
    
    @Override
    public void close() throws IOException{
		fileInputStream.close();
		fileBufferedReader.close();
    }
}
