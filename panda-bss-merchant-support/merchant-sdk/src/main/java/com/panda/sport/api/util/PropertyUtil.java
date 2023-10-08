package com.panda.sport.api.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class PropertyUtil{
    public static Properties getConfig(String path){
        Properties props=null;
        InputStream in = null;
        try{
            props = new Properties();
            in = PropertyUtil.class.getResourceAsStream(path);
            props.load(new InputStreamReader(in));

        }catch(Exception ex){
            log.error("load file error path:"+path,ex);
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("close IOException file error path:"+path,e);
                }
            }
        }
        return props;
    }
}