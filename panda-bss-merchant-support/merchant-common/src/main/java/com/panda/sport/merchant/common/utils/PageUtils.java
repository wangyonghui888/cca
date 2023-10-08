package com.panda.sport.merchant.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;

public class PageUtils {
    private static final Logger log = LoggerFactory.getLogger(PageUtils.class);

    public PageUtils() {
    }


    public static <PO, PAGE extends IPage<PO>, VO> Page<VO> page(PAGE page, Class<VO> voType) {
        Page<VO> voPage  = new Page();
        BeanUtils.copyProperties(page, voPage);
        ArrayList<VO> voArrayList = new ArrayList();
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach((po) -> {
                try {
                    VO vo = voType.newInstance();
                    BeanUtils.copyProperties(po, vo);
                    voArrayList.add(vo);
                } catch (IllegalAccessException | InstantiationException var4) {
                    log.error("集合浅拷贝出错 -> {}", var4.getMessage(), var4);
                    throw new RuntimeException("集合浅拷贝出错");
                }
            });
        }

        voPage.setRecords(voArrayList);
        return voPage;
    }

}
