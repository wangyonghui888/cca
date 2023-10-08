package com.panda.sport.merchant.common.utils;

import java.util.Random;

public class CreateAdminPassword {

    public static char[] special= new char[]{'@','#','$','%','&','*','?'};

    public static String capital = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String lowercase = "abcdefghijklmnopqrstuvwxyz";

    public  static String getPassword(){
        StringBuilder password = new StringBuilder();
        StringBuilder str = new StringBuilder();
        str.append(getCapital()).append(getNum()).append(getLowercase());
        Random random = new Random();
        char[] sChar = str.toString().toCharArray();
        int num = random.nextInt(sChar.length);
        char c1 = sChar[num];
        sChar[num]=getSpecial();
        password.append(c1);
        password.append(getStr(sChar));
        return password.toString();
    }


    public  static String getStr(){
        StringBuilder numStr = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<10;i++){
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }

    private  static  String getStr(char[] sChar){
        Random random = new Random();
        for(int i =0;i<sChar.length;i++){
            int num1 = random.nextInt(sChar.length);
            int num2 = random.nextInt(sChar.length);
            char char1 = sChar[num1];
            char char2 = sChar[num2];
            sChar[num2] = char1;
            sChar[num1] = char2;
        }
        return new String(sChar);
    }

    private static char getSpecial(){
        Random random = new Random();
        return special[random.nextInt(special.length)];
    }

    private static String getCapital(){
        StringBuilder lStr = new StringBuilder();
        char[] cs = capital.toCharArray();
        Random random = new Random();
        lStr.append(cs[random.nextInt(cs.length)]);
        return lStr.toString();
    }

    private static String getNum(){
        StringBuilder numStr = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<4;i++){
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }

    private static String getLowercase(){
        StringBuilder lStr = new StringBuilder();
        char[] cs = lowercase.toCharArray();
        Random random = new Random();
        lStr.append(cs[random.nextInt(cs.length)]);
        lStr.append(cs[random.nextInt(cs.length)]);
        return lStr.toString();
    }
}
