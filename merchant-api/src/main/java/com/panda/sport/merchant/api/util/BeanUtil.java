package com.panda.sport.merchant.api.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class BeanUtil {
    public static String[] getNullPropertyNames(Object source){
          final BeanWrapper src = new BeanWrapperImpl(source);
          java.beans.PropertyDescriptor [] pds = src.getPropertyDescriptors();
          Set<String> emptyNames = new HashSet<>();
          for (PropertyDescriptor pd : pds){
               Object srcValue = src.getPropertyValue(pd.getName());
               if(null==srcValue) emptyNames.add(pd.getName());
          }
          String [] result = new String[emptyNames.size()];
          return emptyNames.toArray(result);
    }
}
