package io;

import java.io.*;

public class VFS {
    private static VFS vfs;

    private VFS() {

    }

    public static VFS instance() {
        if (vfs == null)
            vfs = new VFS();
        return vfs;
    }

    public BufferedWriter getWriter(File file) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
    }

    public BufferedReader getReader(File file) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }
}
