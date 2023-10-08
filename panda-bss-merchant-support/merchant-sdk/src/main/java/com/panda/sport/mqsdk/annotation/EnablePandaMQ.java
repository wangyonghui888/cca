package com.panda.sport.mqsdk.annotation;

/**
 * @author : Jeffrey
 * @Date: 2020-01-06 10:43
 * @Description :
 */

import com.panda.sport.mqsdk.annotation.config.PandaMQSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(PandaMQSelector.class)
public @interface EnablePandaMQ {

}
