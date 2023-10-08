package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.bss.mapper.MerchantRateMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.MerchantCodeConfig;
import com.panda.sport.merchant.common.po.merchant.MerchantLevelPO;
import com.panda.sport.merchant.common.po.merchant.MerchantRatePO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.utils.MerchantUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantRateVO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.MerchantRateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Jeffrey
 * @Date: 2020-01-24 12:30
 * @Description :
 */
@Service
public class MerchantRateServiceImpl implements MerchantRateService {

    @Autowired
    private MerchantRateMapper rateMapper;

    private List<MerchantRateVO> convertPObyList(List<MerchantRatePO> list) {
        List<MerchantRateVO> returnList = new ArrayList<>();
        list.forEach(val -> {
            MerchantRateVO tempVo = new MerchantRateVO();
            BeanUtils.copyProperties(val, tempVo);
            returnList.add(tempVo);
        });
        return returnList;
    }


    /**
     * 查询费率信息列表
     *
     * @param merchantRateVO
     * @return
     */
    @Override
    public List<MerchantRateVO> queryList(MerchantRateVO merchantRateVO) {
        List<MerchantRatePO> data = rateMapper.selectByRateVO(merchantRateVO);
        return convertPObyList(data);
    }

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private LoginUserService loginUserService;

    /**
     * 增加费率信息
     *
     * @param merchantRateVO
     * @param request
     * @return
     */
    @Override
    public boolean add(MerchantRateVO merchantRateVO, HttpServletRequest request) {
        MerchantRatePO tempPo = new MerchantRatePO();
        convertVOByPO(merchantRateVO, tempPo);
        int row = rateMapper.insert(tempPo);
        if (row > 0) {
            //记录日志
            MerchantUtil filedUtil = new MerchantUtil<MerchantCodeConfig>();
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            MerchantLogFiledVO filedVO = filedUtil.compareObject(null, merchantRateVO,MerchantUtil.filterTrateField,MerchantUtil.FIELD_MERCHNTRATE_MAPPING);

            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_TRATE_SET, MerchantLogTypeEnum.NEW_MERCHANT_RATE, filedVO,
                    MerchantLogConstants.MERCHANT_IN, request.getHeader("user-id"), request.getHeader("merchantName"), request.getHeader("user-id"), "", merchantRateVO.getModifyUser(),request.getHeader("language") , IPUtils.getIpAddr(request));

            return true;
        }
        return false;
    }

    /**
     * 修改费率信息
     *
     * @param merchantRateVO
     * @param language
     * @return
     */
    @Override
    public boolean update(MerchantRateVO merchantRateVO, Integer userId, String language, String ip) {
        MerchantRatePO tempPo = new MerchantRatePO();
        convertVOByPO(merchantRateVO, tempPo);
        MerchantRatePO merchantRatePO = rateMapper.selectById(merchantRateVO.getId());
        int row = rateMapper.updateByPrimaryKeySelective(tempPo);
        if (row > 0) {
            //记录日志
            String username = loginUserService.getLoginUser(userId);
            MerchantFieldUtil fieldUtil = new MerchantFieldUtil<MerchantLevelPO>();
            MerchantLogFiledVO filedVO = fieldUtil.compareObject(merchantRatePO, tempPo);
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_TRATE_SET, MerchantLogTypeEnum.EDIT_MERCHANT_RATE, filedVO, MerchantLogConstants.MERCHANT_IN, userId.toString(), username
                    , null, null, merchantRatePO.getId().toString(), language,ip);
            return true;
        }
        return false;
    }


    /**
     * 查询所有费率列表
     *
     * @return
     */
    @Override
    public List<MerchantRatePO> queryRateList() {

        return rateMapper.queryRateList();
    }
}
