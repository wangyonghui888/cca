package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.dto.DomainGroupThresholdDTO;
import com.panda.multiterminalinteractivecenter.dto.LineCarrierDTO;
import com.panda.multiterminalinteractivecenter.entity.Domain2DomainGroup;
import com.panda.multiterminalinteractivecenter.entity.LineCarrier;
import com.panda.multiterminalinteractivecenter.entity.TyDomain;
import com.panda.multiterminalinteractivecenter.enums.DomainEnableEnum;
import com.panda.multiterminalinteractivecenter.enums.DomainTypeEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.exception.BusinessException;
import com.panda.multiterminalinteractivecenter.mapper.Domain2DomainGroupMapper;
import com.panda.multiterminalinteractivecenter.mapper.LineCarrierMapper;
import com.panda.multiterminalinteractivecenter.mapper.TyDomainMapper;
import com.panda.multiterminalinteractivecenter.service.DomainService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.LineCarrierVo;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class LineCarrierServiceImpl extends ServiceImpl<LineCarrierMapper, LineCarrier> implements IService<LineCarrier> {

    private final LineCarrierMapper lineCarrierMapper;

    private final TyDomainMapper domainMapper;
    private final AbstractMerchantDomainService abstractMerchantDomainService;
    private final Domain2DomainGroupMapper domain2DomainGroupMapper;
    private final DomainService domainService;
    private final MerchantLogService merchantLogService;

    /**
     * 新增线路商
     * @param lineCarrier lineCarrier
     * @return num
     */
    public int saveLineCarrier(LineCarrier lineCarrier, HttpServletRequest request){
        int num = lineCarrierMapper.selectByName(lineCarrier);
        if(num > 0){
            throw new BusinessException("线路商名称：" + lineCarrier.getLineCarrierName().trim() + "重复!");
        }
        Long currentTime = System.currentTimeMillis();
        lineCarrier.setCreateTime(currentTime);
        lineCarrier.setUpdateTime(currentTime);
        int result = baseMapper.insert(lineCarrier);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("lineCarrierName"), null, lineCarrier.getLineCarrierName());
        merchantLogService.saveLog(MerchantLogPageEnum.LINE_CARRIER, MerchantLogTypeEnum.SAVE, filedVO,  null, lineCarrier.getLineCarrierName(), request);
        return result;
    }

    /**
     * 分页查询
     * @param lineCarrier lineCarrier
     * @return num
     */
    public APIResponse<Object> getLineCarrierList(LineCarrier lineCarrier){
        Integer pageIndex = lineCarrier.getPageIndex();
        Integer pageSize = lineCarrier.getPageSize();
        pageSize = (pageSize == null || pageSize == 0) ? 100 : pageSize;
        pageIndex = pageIndex == null ? 1 : pageIndex;
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<LineCarrier> pageInfo = new PageInfo<>(lineCarrierMapper.getLineCarrier(lineCarrier));
        return APIResponse.returnSuccess(pageInfo);
    }

    /**
     * 编辑线路商
     * @param lineCarrier lineCarrier
     * @return num
     */
    public int updateLineCarrier(LineCarrier lineCarrier, HttpServletRequest request){
        LineCarrier lineCarrierVo = lineCarrierMapper.editCheckName(lineCarrier.getLineCarrierName(),lineCarrier.getTab());
        if(lineCarrierVo != null){
            if(lineCarrier.getLineCarrierName().trim().equals(lineCarrierVo.getLineCarrierName().trim())
                    && !lineCarrier.getId().equals(lineCarrierVo.getId())){
                throw new BusinessException("线路商名称:【"+lineCarrierVo.getLineCarrierName().trim()+"】已存在!");
            }
        }
        lineCarrier.setUpdateTime(System.currentTimeMillis());
        LineCarrier old = lineCarrierMapper.getLineCarrierById(lineCarrier.getId());

        int result = lineCarrierMapper.updateLineCarrier(lineCarrier);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("lineCarrierName"), old.getLineCarrierName(), lineCarrier.getLineCarrierName());
        merchantLogService.saveLog(MerchantLogPageEnum.LINE_CARRIER, MerchantLogTypeEnum.EDIT, filedVO,  null,
               lineCarrier.getLineCarrierName() + StringPool.AMPERSAND + lineCarrier.getId(), request);
        return result;
    }

    /**
     * 编辑线路商状态
     * @param lineCarrier lineCarrier
     * @return num
     */
    public int updateLineCarrierStatus(LineCarrier lineCarrier, HttpServletRequest request){
        LineCarrier vo = lineCarrierMapper.getLineCarrierById(lineCarrier.getId());

        lineCarrier.setUpdateTime(System.currentTimeMillis());
        int num = lineCarrierMapper.updateLineCarrierStatus(lineCarrier);
        //操作日志
        String beforeValue = lineCarrier.getLineCarrierStatus() == 1 ? "禁用" : "启用";
        String afterValue = lineCarrier.getLineCarrierStatus() == 1 ? "启用" : "禁用";
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("status"), beforeValue, afterValue);
        merchantLogService.saveLog(MerchantLogPageEnum.LINE_CARRIER, MerchantLogTypeEnum.EDIT_INFO_STATUS, filedVO,  null,
                lineCarrier.getLineCarrierName() + StringPool.AMPERSAND + lineCarrier.getId(), request);

        if(vo.getLineCarrierStatus() == 1 && lineCarrier.getLineCarrierStatus() == 0){
            // 这里操作了关闭线路商开关

            // 这里需要进行线路商一键切换
            LineCarrierDTO lineCarrierDTO = LineCarrierDTO
                    .builder()
                    .id(vo.getId())
                    .lineCarrierName(vo.getLineCarrierName())
                    .lineCarrierStatus(vo.getLineCarrierStatus())
                    .operationType(2)
                    .step(3)
                    .tab(vo.getTab())
                    .build();
            this.ASwitch(lineCarrierDTO, null);
        }
        return num;
    }

    /**
     * 单条查询
     * @param lineCarrier lineCarrier
     * @return vo
     */
    public APIResponse<Object> queryLineCarrier(LineCarrier lineCarrier){
        LineCarrier vo = lineCarrierMapper.getLineCarrierById(lineCarrier.getId());
        return APIResponse.returnSuccess(vo);
    }

    /**
     * 删除
     * @param id id
     */
    public String delLineCarrierById(Long id, HttpServletRequest request){

        LineCarrier vo = lineCarrierMapper.getLineCarrierById(id);

        if(vo == null){
            log.info("线路商id：{}，已删除，无需再次操作",id);
            return "此线路商已删除，无需再次操作！";
        }

        List<TyDomain> tyDomains = abstractMerchantDomainService.getDomainServiceBean(vo.getTab()).getDomainListByLineId(id,vo.getTab());

        if(CollectionUtils.isNotEmpty(tyDomains)){
            return "此线路商包含域名数据，请删除域名数据后再次操作！";
        }

        lineCarrierMapper.delLineCarrierById(id);

        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("lineCarrierName"), vo.getLineCarrierName(), "-");
        merchantLogService.saveLog(MerchantLogPageEnum.LINE_CARRIER, MerchantLogTypeEnum.DEL, filedVO,  null,
                vo.getLineCarrierName() + StringPool.AMPERSAND + vo.getId(), request);
//        // 批量关闭域名状态
//        domainMapper.closeDomainByLineId(id,vo.getTab(),DomainEnableEnum.WAIT_USE.getCode());
//
//        // 这里需要进行线路商一键切换
//        LineCarrierDTO lineCarrierDTO = LineCarrierDTO
//                .builder()
//                .id(vo.getId())
//                .lineCarrierName(vo.getLineCarrierName())
//                .lineCarrierStatus(vo.getLineCarrierStatus())
//                .operationType(2)
//                .step(2)
//                .tab(vo.getTab())
//                .build();
//        this.ASwitch(lineCarrierDTO);

        return null;
    }

    /**
     * 列表查询
     * @return list
     */
    public APIResponse<Object> queryLineCarrierList(String tab, String name){
        return APIResponse.returnSuccess(lineCarrierMapper.queryLineCarrierList(tab,name));
    }

    public APIResponse<Boolean> validateThreshold(LineCarrierDTO lineCarrierDTO) {

//        if(lineCarrierDTO.getOperationType() == 2){
//            List<Long> domainList = lineCarrierMapper.getDomainListByLineId(lineCarrierDTO.getId(),lineCarrierDTO.getTab());
//            if(CollectionUtils.isNotEmpty(domainList)){
//                APIResponse.returnFail(false,
//                        String.format("线路商%s下还有%d个域名，请将相关域名绑定到其他线路商在做删除！",
//                                lineCarrierDTO.getLineCarrierName(),domainList.size()));
//            }
//        }

        List<DomainGroupThresholdDTO> domainGroupThresholdDTOList = lineCarrierMapper.getDomainGroupByLineId(lineCarrierDTO);

        if(CollectionUtils.isEmpty(domainGroupThresholdDTOList)){
            return APIResponse.returnSuccess(true);
        }
        String operationType = lineCarrierDTO.getOperationType()==1?"禁用":"删除";
        String msgTemp = "%s域名组%s域名数量将低于%d报警阈值\n";
        StringBuilder msgReplace = new StringBuilder();
        for (DomainGroupThresholdDTO domainGroupThresholdDTO : domainGroupThresholdDTOList) {
            List<String> domainGroupNameList = Lists.newArrayList();
            if(domainGroupThresholdDTO.getH5Count() == null || domainGroupThresholdDTO.getH5Count() < domainGroupThresholdDTO.getH5Threshold()){
                domainGroupNameList.add(String.format(msgTemp,domainGroupThresholdDTO.getDomainGroupName(), DomainTypeEnum.H5.getValue(),domainGroupThresholdDTO.getH5Threshold()));
            }
            if(domainGroupThresholdDTO.getPcCount() == null || domainGroupThresholdDTO.getPcCount() < domainGroupThresholdDTO.getPcThreshold()){
                domainGroupNameList.add(String.format(msgTemp,domainGroupThresholdDTO.getDomainGroupName(),DomainTypeEnum.PC.getValue(),domainGroupThresholdDTO.getH5Threshold()));
            }
            if(domainGroupThresholdDTO.getApiCount() == null || domainGroupThresholdDTO.getApiCount() < domainGroupThresholdDTO.getApiThreshold()){
                domainGroupNameList.add(String.format(msgTemp,domainGroupThresholdDTO.getDomainGroupName(),DomainTypeEnum.APP.getValue(),domainGroupThresholdDTO.getH5Threshold()));
            }
            if(domainGroupThresholdDTO.getImgCount() == null || domainGroupThresholdDTO.getImgCount() < domainGroupThresholdDTO.getImgThreshold()){
                domainGroupNameList.add(String.format(msgTemp,domainGroupThresholdDTO.getDomainGroupName(),DomainTypeEnum.IMAGE.getValue(),domainGroupThresholdDTO.getH5Threshold()));
            }
            msgReplace.append(domainGroupNameList);
        }
        if(StringUtils.isNotBlank(msgReplace)){
            String msg = String.format("【%s】%s后，\n%s，且正在使用的域名将会被自动切换，请确认是否%s！",
                    lineCarrierDTO.getLineCarrierName(),operationType,msgReplace,operationType);
            APIResponse.returnFail(false,msg);
        }
        return APIResponse.returnSuccess(true);
    }

    public APIResponse<Boolean> validateSwitch(LineCarrierDTO lineCarrierDTO) {
        List<DomainGroupThresholdDTO> usedDomainGroupThresholdDTOS =  lineCarrierMapper.getUsedDomainByLineId(lineCarrierDTO);
        List<DomainGroupThresholdDTO> waitUsedDomainGroupThresholdDTOS =  lineCarrierMapper.getWaitUsedDomainByLineId(lineCarrierDTO);

        if(CollectionUtils.isEmpty(usedDomainGroupThresholdDTOS)){
            return APIResponse.returnSuccess(true);
        }
        if(CollectionUtils.isEmpty(waitUsedDomainGroupThresholdDTOS)){
            return APIResponse.returnFail(false,"此线路上的所有域名组都没有可替代的域名！");
        }
        StringBuilder msgReplace = new StringBuilder();
        String msgTemp = "【%s】域名组，" + "【%s】类型域名%d个，需要%d个！\n";
        for (DomainGroupThresholdDTO usedDomainGroupThresholdDTO : usedDomainGroupThresholdDTOS) {
            for (DomainGroupThresholdDTO waitUsedDomainGroupThresholdDTO : waitUsedDomainGroupThresholdDTOS) {

                if(Objects.equals(usedDomainGroupThresholdDTO.getId(), waitUsedDomainGroupThresholdDTO.getId())){
                    // 同一个域名组 对比当前链路上使用中的域名和非当前线路商待使用的域名数量
                    if(usedDomainGroupThresholdDTO.getH5Count()!=null && usedDomainGroupThresholdDTO.getH5Count()!=0
                            &&waitUsedDomainGroupThresholdDTO.getH5Count()<usedDomainGroupThresholdDTO.getH5Count()){
                        msgReplace.append(String.format(msgTemp,
                                usedDomainGroupThresholdDTO.getDomainGroupName(),DomainTypeEnum.H5.getValue(),
                                waitUsedDomainGroupThresholdDTO.getH5Count(), usedDomainGroupThresholdDTO.getH5Count()));
                    }
                    if(usedDomainGroupThresholdDTO.getPcCount()!=null && usedDomainGroupThresholdDTO.getPcCount()!=0
                            &&waitUsedDomainGroupThresholdDTO.getH5Count()<usedDomainGroupThresholdDTO.getPcCount()){
                        msgReplace.append(String.format(msgTemp,
                                usedDomainGroupThresholdDTO.getDomainGroupName(),DomainTypeEnum.PC.getValue(),
                                usedDomainGroupThresholdDTO.getPcCount(),usedDomainGroupThresholdDTO.getPcCount()));
                    }
                    if(usedDomainGroupThresholdDTO.getApiCount()!=null && usedDomainGroupThresholdDTO.getApiCount()!=0
                            &&waitUsedDomainGroupThresholdDTO.getApiCount()<usedDomainGroupThresholdDTO.getApiCount()){
                        msgReplace.append(String.format(msgTemp,
                                usedDomainGroupThresholdDTO.getDomainGroupName(),DomainTypeEnum.APP.getValue(),
                                usedDomainGroupThresholdDTO.getApiCount(),usedDomainGroupThresholdDTO.getApiCount()));
                    }
                    if(usedDomainGroupThresholdDTO.getImgCount()!=null && usedDomainGroupThresholdDTO.getImgCount()!=0
                            &&waitUsedDomainGroupThresholdDTO.getH5Count()<usedDomainGroupThresholdDTO.getImgCount()){
                        msgReplace.append(String.format(msgTemp,
                                usedDomainGroupThresholdDTO.getDomainGroupName(),DomainTypeEnum.IMAGE.getValue(),
                                usedDomainGroupThresholdDTO.getImgCount(),usedDomainGroupThresholdDTO.getImgCount()));
                    }
                }
            }
        }

        if(StringUtils.isNotBlank(msgReplace)){
            String msg = "%s等域名不足，请补充域名后再试！";
            return APIResponse.returnFail("0001",String.format(msg,msgReplace),false);
        }

        return APIResponse.returnSuccess(true);
    }

    public APIResponse<Boolean> ASwitch(LineCarrierDTO lineCarrierDTO, HttpServletRequest request) {

        final String tab = lineCarrierDTO.getTab();

        List<TyDomain> domainList = abstractMerchantDomainService
                .getDomainServiceBean(lineCarrierDTO.getTab())
                        .getDomainListByLineId(lineCarrierDTO.getId(),tab);

        for (TyDomain domain : domainList) {
            if(domain.getStatus()==0 || !Objects.equals(domain.getEnable(), DomainEnableEnum.USED.getCode())){
                log.info("域名{}不是在使用中，无需切换",domain.getDomainName());
                continue;
            }

            // 做校验 ， 防止下方代码报错
            List<Domain2DomainGroup> domain2DomainGroups = domain2DomainGroupMapper.selectByDomainId(domain.getId(),tab);
            if (CollectionUtil.isEmpty(domain2DomainGroups)) {
                log.info("tab:【{}】，【{}】该域名没有域名组,无法完成域名切换！",tab, domain.getDomainName());
                continue;
            }

            // 开始在新的域名组里找新的待使用域名进行替换
            final Integer newDomainType = domain.getDomainType();

            // 每个域名组都拉一个新的进来
            for (Domain2DomainGroup domain2DomainGroup : domain2DomainGroups) {

                domainService.switchByParamV2(domain,newDomainType,domain2DomainGroup.getDomainGroupId(),lineCarrierDTO.getTargetId(),2);
            }

        }

        // 这里批量关闭域名
        domainMapper.closeDomainByLineId(lineCarrierDTO.getId(), lineCarrierDTO.getTab(), DomainEnableEnum.WAIT_USE.getCode());

        if(request != null) {
            LineCarrier vo = lineCarrierMapper.getLineCarrierById(lineCarrierDTO.getId());
            LineCarrier targetVo = lineCarrierMapper.getLineCarrierById(lineCarrierDTO.getTargetId());

            MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("lineCarrierName"), vo.getLineCarrierName(), targetVo.getLineCarrierName());
            merchantLogService.saveLog(MerchantLogPageEnum.LINE_CARRIER, MerchantLogTypeEnum.LINE_CARRIER_SELECT, filedVO,  null,
                    vo.getLineCarrierName() + StringPool.AMPERSAND + vo.getId(), request);
        }
        return APIResponse.returnSuccess(true);
    }

    public LineCarrier getByLineCarrierId(Long lineCarrierId) {
        return lineCarrierMapper.getLineCarrierById(lineCarrierId);
    }
}
