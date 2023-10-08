package com.panda.multiterminalinteractivecenter.constant;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DJDomain {

    public static Map<String,List<String>> bwDomains = new HashMap<>();

    public static Map<String,List<String>> topcpDomains = new HashMap<>();

    public static Map<String,List<String>> bwUseDomains = new HashMap<>();

    public static Map<String,List<String>> topcpUseDomains = new HashMap<>();

    private static List<String> bwVip1 = Lists.newArrayList();
    private static List<String> bwVip2 = Lists.newArrayList();
    private static List<String> bwVip3 = Lists.newArrayList();
    private static List<String> bwVip4 = Lists.newArrayList();

    private static List<String> bwBj1 = Lists.newArrayList();
    private static List<String> bwBj2 = Lists.newArrayList();
    private static List<String> bwBj3 = Lists.newArrayList();
    private static List<String> bwBj4 = Lists.newArrayList();

    private static List<String> bwDef1 = Lists.newArrayList();
    private static List<String> bwDef2 = Lists.newArrayList();
    private static List<String> bwDef3 = Lists.newArrayList();
    private static List<String> bwDef4 = Lists.newArrayList();

    private static List<String> topcpVip1 = Lists.newArrayList();
    private static List<String> topcpVip2 = Lists.newArrayList();
    private static List<String> topcpVip3 = Lists.newArrayList();
    private static List<String> topcpVip4 = Lists.newArrayList();

    private static List<String> topcpBj1 = Lists.newArrayList();
    private static List<String> topcpBj2 = Lists.newArrayList();
    private static List<String> topcpBj3 = Lists.newArrayList();
    private static List<String> topcpBj4 = Lists.newArrayList();

    private static List<String> topcpDef1 = Lists.newArrayList();
    private static List<String> topcpDef2 = Lists.newArrayList();
    private static List<String> topcpDef3 = Lists.newArrayList();
    private static List<String> topcpDef4 = Lists.newArrayList();


    public static void init(){
        // ===================================bw===================================
        //vip
        bwVip1.add("https://djuatpcs3.o0081.com");
        bwVip1.add("https://djuat-bwtxpc.kkgnru.com");
        bwVip1.add("https://vip1-djuatpcs3.o0081.com");
        bwVip1.add("https://vip2-djuat-bwtxpc.kkgnru.com");

        bwVip2.add("https://djuath5s3.o0081.com");
        bwVip2.add("https://djuat-bwtxh5.kkgnru.com");
        bwVip2.add("https://vip1-djuath5s3.o0081.com");
        bwVip2.add("https://vip2-djuat-bwtxh5.kkgnru.com");

        bwVip3.add("https://duatbw-api.kkgnru.com");
        bwVip3.add("https://duatbw-api2.i9cge.com");
        bwVip3.add("https://vip1-duatbw-api.kkgnru.com");
        bwVip3.add("https://vip2-duatbw-api2.i9cge.com");

        bwVip4.add("https://djuathw-stat.shangdao158.com");
        bwVip4.add("https://vip1-djuathw-stat.shangdao158.com");
        bwVip4.add("https://vip2-djuathw-stat.shangdao158.com");
        bwVip4.add("https://vip3-djuathw-stat.shangdao158.com");

        // 北京
        bwBj1.add("https://djuat-bwhwpc.o0081.com");
        bwBj1.add("https://bj1-djuat-bwhwpc.o0081.com");
        bwBj1.add("https://bj2-djuat-bwhwpc.o0081.com");
        bwBj1.add("https://bj3-djuat-bwhwpc.o0081.com");

        bwBj2.add("https://djuat-bwhwh5.o0081.com");
        bwBj2.add("https://bj1-djuat-bwhwh5.o0081.com");
        bwBj2.add("https://bj2-djuat-bwhwh5.o0081.com");
        bwBj2.add("https://bj3-djuat-bwhwh5.o0081.com");

        bwBj3.add("https://duatbw-api3.o0081.com");
        bwBj3.add("https://duatbw-txapi.kkgnru.com");
        bwBj3.add("https://bj1-duatbw-api3.o0081.com");
        bwBj3.add("https://bj2-duatbw-api3.o0081.com");

        bwBj4.add("https://djuatali-stat.sjgrf523.com");
        bwBj4.add("https://bj1-djuatali-stat.sjgrf523.com");
        bwBj4.add("https://bj2-djuatali-stat.sjgrf523.com");
        bwBj4.add("https://bj3-djuatali-stat.sjgrf523.com");

        // 默认
        bwDef1.add("https://djuat-bwalipc.i9cge.com");
        bwDef1.add("https://ty1-djuat-bwalipc.i9cge.com");
        bwDef1.add("https://ty2-djuat-bwalipc.i9cge.com");
        bwDef1.add("https://ty3-djuat-bwalipc.i9cge.com");

        bwDef2.add("https://djuat-bwalih5.i9cge.com");
        bwDef2.add("https://ty1-djuat-bwalih5.i9cge.com");
        bwDef2.add("https://ty2-djuat-bwalih5.i9cge.com");
        bwDef2.add("https://ty3-djuat-bwalih5.i9cge.com");

        bwDef3.add("https://duatbw-hwapi.i9cge.com");
        bwDef3.add("https://duatbw-aliapi.o0081.com");
        bwDef3.add("https://ty1-duatbw-hwapi.i9cge.com");
        bwDef3.add("https://ty2-duatbw-aliapi.o0081.com");

        bwDef4.add("http://dj-uat.klsdfjoiuwe23.com");
        bwDef4.add("http://ty1-dj-uat.klsdfjoiuwe23.com");
        bwDef4.add("http://ty2-dj-uat.klsdfjoiuwe23.com");
        bwDef4.add("http://ty3-dj-uat.klsdfjoiuwe23.com");

        // ===================================topcp===================================

        topcpVip1.add("https://djuat-bobtxpc.kkgnru.com");
        topcpVip1.add("https://vip1-djuat-bobtxpc.kkgnru.com");
        topcpVip1.add("https://vip2-djuat-bobtxpc.kkgnru.com");
        topcpVip1.add("https://vip3-djuat-bobtxpc.kkgnru.com");


        topcpVip2.add("https://djuat-bobtxh5.kkgnru.com");
        topcpVip2.add("https://vip1-djuat-bobtxh5.kkgnru.com");
        topcpVip2.add("https://vip2-djuat-bobtxh5.kkgnru.com");
        topcpVip2.add("https://vip3-djuat-bobtxh5.kkgnru.com");

        topcpVip3.add("https://duatbob-api.kkgnru.com");
        topcpVip3.add("https://duatbob-api2.i9cge.com");
        topcpVip3.add("https://vip1-duatbob-api.kkgnru.com");
        topcpVip3.add("https://vip2-duatbob-api2.i9cge.com");

        topcpVip4.add("http://dj-uat2.klsdfjoiuwe23.com");
        topcpVip4.add("http://vip1-dj-uat2.klsdfjoiuwe23.com");
        topcpVip4.add("http://vip2-dj-uat2.klsdfjoiuwe23.com");
        topcpVip4.add("http://vip3-dj-uat2.klsdfjoiuwe23.com");

        // 北京
        topcpBj1.add("https://djuat-bobalipc.i9cge.com");
        topcpBj1.add("https://bj1-djuat-bobalipc.i9cge.com");
        topcpBj1.add("https://bj2-djuat-bobalipc.i9cge.com");
        topcpBj1.add("https://bj3-djuat-bobalipc.i9cge.com");

        topcpBj2.add("https://djuat-bobhwh5.o0081.com");
        topcpBj2.add("https://bj1-djuat-bobhwh5.o0081.com");
        topcpBj2.add("https://bj2-djuat-bobhwh5.o0081.com");
        topcpBj2.add("https://bj3-djuat-bobhwh5.o0081.com");

        topcpBj3.add("https://duatbob-api3.o0081.com");
        topcpBj3.add("https://duatbob-txapi.kkgnru.com");
        topcpBj3.add("https://bj1-duatbob-api3.o0081.com");
        topcpBj3.add("https://bj2-duatbob-txapi.kkgnru.com");

        topcpBj4.add("http://dj-uat3.klsdfjoiuwe23.com");
        topcpBj4.add("http://bj1-dj-uat3.klsdfjoiuwe23.com");
        topcpBj4.add("http://bj2-dj-uat3.klsdfjoiuwe23.com");
        topcpBj4.add("http://bj3-dj-uat3.klsdfjoiuwe23.com");

        // 默认
        topcpDef1.add("https://djuat-bobhwpc.o0081.com");
        topcpDef1.add("https://ty1-djuat-bobhwpc.o0081.com");
        topcpDef1.add("https://ty2-djuat-bobhwpc.o0081.com");
        topcpDef1.add("https://ty3-djuat-bobhwpc.o0081.com");

        topcpDef2.add("https://djuat-bobalih5.i9cge.com");
        topcpDef2.add("https://ty1-djuat-bobalih5.i9cge.com");
        topcpDef2.add("https://ty2-djuat-bobalih5.i9cge.com");
        topcpDef2.add("https://ty3-djuat-bobalih5.i9cge.com");

        topcpDef3.add("https://duatbob-hwapi.i9cge.com");
        topcpDef3.add("https://duatbob-aliapi.o0081.com");
        topcpDef3.add("https://ty1-duatbob-hwapi.i9cge.com");
        topcpDef3.add("https://ty2-duatbob-aliapi.o0081.com");

        topcpDef4.add("http://dj-uat.klsdfjoiuwe23.com");
        topcpDef4.add("http://ty1-dj-uat.klsdfjoiuwe23.com");
        topcpDef4.add("http://ty2-dj-uat.klsdfjoiuwe23.com");
        topcpDef4.add("http://ty3-dj-uat.klsdfjoiuwe23.com");

        bwDomains.put("vip1",bwVip1);
        bwDomains.put("vip2",bwVip2);
        bwDomains.put("vip3",bwVip3);
        bwDomains.put("vip4",bwVip4);

        bwDomains.put("bj1",bwBj1);
        bwDomains.put("bj2",bwBj2);
        bwDomains.put("bj3",bwBj3);
        bwDomains.put("bj4",bwBj4);

        bwDomains.put("def1",bwDef1);
        bwDomains.put("def2",bwDef2);
        bwDomains.put("def3",bwDef3);
        bwDomains.put("def4",bwDef4);

        topcpDomains.put("vip1",topcpVip1);
        topcpDomains.put("vip2",topcpVip2);
        topcpDomains.put("vip3",topcpVip3);
        topcpDomains.put("vip4",topcpVip4);

        topcpDomains.put("bj1",topcpBj1);
        topcpDomains.put("bj2",topcpBj2);
        topcpDomains.put("bj3",topcpBj3);
        topcpDomains.put("bj4",topcpBj4);

        topcpDomains.put("def1",topcpDef1);
        topcpDomains.put("def2",topcpDef2);
        topcpDomains.put("def3",topcpDef3);
        topcpDomains.put("def4",topcpDef4);
    }

    public static List<String> getOtherDomain(int i) {
        return null;
    }
}
