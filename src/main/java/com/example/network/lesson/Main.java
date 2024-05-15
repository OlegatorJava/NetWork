package com.example.network.lesson;

import com.example.network.message.AbstractMessage;
import com.example.network.message.SimpleMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    File file;
    ObjectOutputStream objOut;
    public Main(){
        file = new File("text.txt");
        try {
            file.createNewFile();
            objOut = new ObjectOutputStream(new FileOutputStream(file, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void m(AbstractMessage message) {

       /* File file = new File("mydir","a\\b\\c");
        System.out.println(file.exists());
        System.out.println(file.getName());
        System.out.println(file.getParent() + "  ");// /mydir/nameDir/text.txt
        System.out.println(File.separator);
        System.out.println(file.isFile());
        System.out.println(file.isDirectory());
        System.out.println(file.mkdir());*/
        /*byte[] bytes = {65,66,67,-1,-2,-3};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        System.out.println(Arrays.toString(in.readAllBytes()));
        int x;
        while ((x = in.read()) != -1){
            System.out.println(x + " ");
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bo.write(10);
        bo.write(65);
        bo.write(65);
        bo.write(67);
        byte[] arr = bo.toByteArray();
        System.out.println(Arrays.toString(arr));*/
       /* byte[] bytes = "Hello".getBytes();
        try (FileOutputStream fin = new FileOutputStream("mydir/text.txt", true))
            {
            fin.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
     /*  long start = System.currentTimeMillis();
         byte[] outData = new byte[1024 * 1024];
        for (int i = 0; i < outData.length; i++) {
            outData[i]=65;
        }
        try(FileOutputStream out = new FileOutputStream("mydir/text.txt")) {
                out.write(outData);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(System.currentTimeMillis() - start);

        try(FileInputStream in = new FileInputStream("mydir/text.txt")) {
            int count;
            while ((count = in.read()) > -1){
                System.out.println((char) count);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(System.currentTimeMillis() - start);

*/
        /** BufferedOutputStream **/
        /*try (OutputStream out = new BufferedOutputStream(new
                FileOutputStream("mydir/text.txt"))) {
            for (int i = 0; i < 1000000; i++) {
                out.write(65);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);
        try (InputStream in = new BufferedInputStream(new
                FileInputStream("mydir/text.txt"))) {
            int x;
            while ((x = in.read()) != -1) {
                System.out.print((char)x);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);*/
        /*try (DataOutputStream out = new DataOutputStream(new
                FileOutputStream("mydir/text.txt"))) {
//            out.writeInt(128);
//            out.writeInt(43);
            //out.writeLong(128L);
            out.writeUTF(new Streams().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        try (DataInputStream in = new DataInputStream(new
                FileInputStream("mydir/text.txt"))) {
            //System.out.println(in.readInt());
            //System.out.println(in.readLong());
            System.out.println(in.readUTF());
            System.out.println(in.readUTF());
            System.out.println(in.readUTF());
            System.out.println(in.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        byte[] byteCat = null;

        try {
            //AbstractMessage catOut = SimpleMessage.of("Барсик");
            AbstractMessage catOut2 = SimpleMessage.of("Мурзик");
            objOut.writeObject(message);
            objOut.writeObject(catOut2);
            System.out.println("Кот до сериализации: " + message);
            //System.out.println("Вот так он выглядит в байтовом представлении: " + Arrays.toString(byteCat));
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    public List<AbstractMessage> read(){
        try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file))) {
            LinkedList<AbstractMessage> abstractMessages = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                AbstractMessage catIn = (AbstractMessage) objIn.readObject();
                System.out.println("А вот такого кота мы восстановили из набора байтов: " + catIn);
                abstractMessages.add(catIn);
                System.out.println(abstractMessages);
            }
//            AbstractMessage catIn2 = (AbstractMessage) objIn.readObject();
//            System.out.println("А вот такого кота мы восстановили из набора байтов: " + catIn2);
            return  abstractMessages;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Что то пошло не так");
        return new ArrayList<>();
    }
}
