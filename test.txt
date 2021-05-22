package base.core.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class TextFile extends ArrayList<String> {

    public TextFile(String fileName,String splitter){
        super(Arrays.asList(read(fileName).split(splitter)));
        if(get(0).equals("")) remove(0);
    }

    public TextFile(String fileName){
        this(fileName,"\n");
    }

    public static String read(String fileName){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            try {
                String s;
                while((s = in.readLine()) != null){
                    sb.append(s).append("\n");
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void write(String fileName,String text){
        try {
            PrintWriter out = new PrintWriter(new File(fileName).getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(String fileName){
        try {
            PrintWriter out = new PrintWriter(new File(fileName).getAbsoluteFile());
            try {
                for (String s : this) {
                    out.print(s);
                }
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = "D:\\IdeaProjects\\testProject\\src\\base\\core\\io\\TextFile.java";
        String file = read(path);
        write("test.txt",file);
        TreeSet<String> words = new TreeSet<>(new TextFile(path, "\\W+"));
        System.out.println(words.headSet("a"));
    }
}
