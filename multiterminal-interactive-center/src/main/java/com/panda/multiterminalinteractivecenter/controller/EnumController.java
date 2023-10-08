package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.enums.ExclusiveEnum;
import com.panda.multiterminalinteractivecenter.enums.GroupTypeEnum;
import com.panda.multiterminalinteractivecenter.enums.ThirdGroupTypeEnum;
import com.panda.multiterminalinteractivecenter.enums.UserValueEnum;
import com.panda.multiterminalinteractivecenter.vo.EnumVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询各枚举信息
 */
@RestController
@RequestMapping("/enum")
public class EnumController {

    /**
     * 查询专属类型枚举信息
     *
     * @return
     */
    @GetMapping("/exclusiveType")
    public APIResponse getExclusiveTypeList() {
        List<EnumVO> exclusiveTypeList = Arrays.stream(ExclusiveEnum.values()).map(exclusive -> {
            EnumVO enumVO = new EnumVO();
            enumVO.setCode(exclusive.getExclusiveType());
            enumVO.setName(exclusive.getExclusiveName());
            return enumVO;
        }).collect(Collectors.toList());
        return APIResponse.returnSuccess(exclusiveTypeList);
    }


    /**
     * 查询分组类型枚举信息
     *
     * @return
     */
    @GetMapping("/groupType")
    public APIResponse getGroupTypeList(@RequestParam(value = "tab", required = false) String tab) {
        tab = StringUtils.isBlank(tab)?"ty":tab;
        if(tab.equalsIgnoreCase("ty")){
            return APIResponse.returnSuccess(getTYGroupType());
        }
        return APIResponse.returnSuccess(getDJCPGroupType());
    }

    private Object getDJCPGroupType() {
        return Arrays.stream(ThirdGroupTypeEnum.values()).map(groupType -> {
            EnumVO enumVO = new EnumVO();
            enumVO.setCode(groupType.getGroupType());
            enumVO.setName(groupType.getGroupName());
            return enumVO;
        }).collect(Collectors.toList());
    }

    private List<EnumVO> getTYGroupType() {
        return Arrays.stream(GroupTypeEnum.values()).map(groupType -> {
            EnumVO enumVO = new EnumVO();
            enumVO.setCode(groupType.getGroupType());
            enumVO.setName(groupType.getGroupName());
            return enumVO;
        }).collect(Collectors.toList());
    }

    /**
     * 查询分组类型枚举信息
     *
     * @return
     */
    @GetMapping("/userValue")
    public APIResponse getUserValueList() {
        List<EnumVO> userValueList = Arrays.stream(UserValueEnum.values()).map(userValue -> {
            EnumVO enumVO = new EnumVO();
            enumVO.setCode(userValue.getCode());
            enumVO.setName(userValue.getName());
            return enumVO;
        }).collect(Collectors.toList());
        return APIResponse.returnSuccess(userValueList);
    }
}
