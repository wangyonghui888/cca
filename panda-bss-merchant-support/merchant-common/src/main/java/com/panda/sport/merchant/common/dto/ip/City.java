package com.panda.sport.merchant.common.dto.ip;


import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class City implements Serializable {
    private static final long serialVersionUID = 2819257632275334581L;

    public final static String CACHE_KEY = "ip_cache";
    public final static String CACHE_KEY_TEMP = "ip_cache_temp";
    /**
     * @var Reader
     */
    private Reader reader;

    public City(String name) throws Exception {
        this.reader = new Reader(name);
    }

    public City(InputStream in) throws Exception {
        this.reader = new Reader(in);
    }

    public boolean reload(String name) {
        try {
            Reader r = new Reader(name);
            this.reader = r;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String[] find(String addr, String language) throws Exception {
        return this.reader.find(addr, language);
    }

    public Map<String, String> findMap(String addr, String language) throws Exception {
        String[] data = this.reader.find(addr, language);
        if (data == null) {
            return null;
        }
        String[] fields = this.reader.getSupportFields();
        Map<String, String> m = new HashMap<String, String>();
        for (int i = 0, l = data.length; i < l; i++) {
            m.put(fields[i], data[i]);
        }

        return m;
    }

    public CityInfo findInfo(String addr, String language) throws Exception {

        String[] data = this.reader.find(addr, language);
        if (data == null) {
            return null;
        }

        return new CityInfo(data);
    }

    public int buildTime() {
        return this.reader.getBuildUTCTime();
    }

    public boolean isIPv4() {
        return this.reader.isIPv4();
    }

    public boolean isIPv6() {
        return this.reader.isIPv6();
    }

    public String fields() {
        return Arrays.toString(this.reader.getSupportFields());
    }

    public String languages() {
        return this.reader.getSupportLanguages();
    }

}