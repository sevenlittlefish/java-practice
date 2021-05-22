package base.core.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BinaryFile {

    public static byte[] read(String file) throws IOException {
        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(new File(file).getAbsoluteFile()));
        byte[] data = new byte[bf.available()];
        bf.read(data);
        return data;
    }

    public static void main(String[] args) throws IOException {
        byte[] data = read("E:\\IdeaProjects\\base-project\\src\\base\\core\\io\\BinaryFile.java");
        for (byte b : data) {
            System.out.print((char)b);
        }
    }
}
