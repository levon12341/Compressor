package io;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.ObjectOutputStream;

public class FileOutputHelper implements Closeable {
    private File outputFile;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream oos;
    private BufferedWriter bw;

    public FileOutputHelper(File file) throws FileNotFoundException {
        outputFile = file;
        fileOutputStream = new FileOutputStream(outputFile);
    }

    public void writeByte(byte msg) throws IOException {
        fileOutputStream.write(msg);
    }
    
    public void writeByte(int msg) throws IOException {
        fileOutputStream.write(msg);
    }

    public void writeChar(char msg) throws IOException {
    	if (bw == null)
    		bw = Files.newBufferedWriter(Paths.get(outputFile.getAbsolutePath()));
    	bw.write(Character.toString(msg));
    }

    public void writeObject(Serializable object) throws IOException {
    	if (oos == null)
    		oos = new ObjectOutputStream(fileOutputStream);
    	oos.writeObject(object);
    }
    
    public boolean closeObjectOutputStream() throws IOException {
    	if (oos == null)
    		return false;
    	oos.close();
    	return true;
    }
    
    @Override
    public void close() throws IOException {
    	if (bw != null)
    		bw.close();
    	closeObjectOutputStream();
    	fileOutputStream.close();
    }

    public void finalize() throws IOException {
        close();
    }
}