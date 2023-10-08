package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.AccountPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface TAccountMapper extends BaseMapper<AccountPO> {


    int batchUpdateAccount(List<AccountPO> accounts);


    int insertAccount(AccountPO accounts);

    @Update("update t_account set amount =amount+#{balance},modify_time = unix_timestamp(now()) where uid=#{userId}")
    int addBalance(@Param("userId") Long userId, @Param("balance") Double balance);

    @Update("update t_account set amount =amount-#{balance},modify_time = unix_timestamp(now()) where uid=#{userId} and amount>0 and (amount-${balance}>=0)")
    int subBalance(@Param("userId") Long userId, @Param("balance") Double balance);


    @Select("select (amount/100) from t_account where uid=#{userId}")
    BigDecimal getUserBalance(@Param("userId") Long userId);

    List<AccountPO> selectTAccountByUids(@Param("uidList") List<Long> uidList);
}