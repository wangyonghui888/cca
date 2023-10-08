package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author YK
 * @Description: 我的消息
 * @date 2020/3/12 16:40
 */
@Repository
public interface MerchantNewsMapper extends BaseMapper<MerchantNews> {

    @Select("select * from merchant_news where upload = #{uploadUnique} and type=3")
    MerchantNews selectByUpload(@Param("uploadUnique") String uploadUnique);

   @Update("UPDATE merchant_news set merchant_name =#{merchantName} where merchant_code = #{merchantCode}")
    int updateMerchantName(@Param("merchantCode")String  merchantCode,@Param("merchantName")String  merchantName);
}
