package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.NoSuchFileException;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FileInputHelper implements Closeable {
	private FileInputStream fileInputStream;
	private BufferedReader fileBufferedReader;
	private ObjectInputStream ois;
	
	public FileInputHelper(File file) throws NoSuchFileException, FileNotFoundException {
		fileInputStream = new FileInputStream(file);
		fileBufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
	}
	
	
    public byte readByte() throws IOException {
    	int cur = fileInputStream.read();
    	if (cur == -1)
    		throw new EOFException();
    	return (byte)cur;
    }
    
    public Object readObject() throws IOException, ClassNotFoundException {
   		ois = new ObjectInputStream(fileInputStream);
   		return ois.readObject();
    }
    
    public short readInt() throws IOException {
    	return (short)fileInputStream.read();
    }
    
    public String readLine() throws IOException {
    	return fileBufferedReader.readLine();
    }
    
    @Override
    public void close() throws IOException{
    	fileInputStream.close();
    }
}
