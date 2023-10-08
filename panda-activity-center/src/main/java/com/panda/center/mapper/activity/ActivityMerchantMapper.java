package com.panda.center.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.center.entity.ActivityMerchant;

import java.util.Map;


public interface ActivityMerchantMapper extends BaseMapper<ActivityMerchant> {

    Map getSummerTaskActivity();
}
