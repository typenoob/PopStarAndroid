package com.example.popstar.io;

import android.content.Context;
import android.util.Log;

import com.example.popstar.Stars;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileIO {
    public static Object readObject(Context context,String name){
        try {
            FileInputStream fis = context.openFileInput(name);
            ObjectInputStream ois=new ObjectInputStream(fis);
            return ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void writeObject(Context context,String name,Object object){
        try {
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
