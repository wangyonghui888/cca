package com.panda.sport.mqsdk.annotation.config;

import com.panda.sport.mqsdk.mq.config.PandaRocketMQConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class PandaMQSelector implements ImportSelector, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{PandaRocketMQConfig.class.getName()};
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}