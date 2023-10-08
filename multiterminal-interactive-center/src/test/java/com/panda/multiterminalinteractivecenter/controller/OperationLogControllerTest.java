package com.panda.multiterminalinteractivecenter.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.panda.multiterminalinteractivecenter.MultiterminalInteractiveCenterApplication;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.dto.MerchantLogDTO;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.impl.MerchantLogServiceImpl;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogQueryVO;
import com.panda.sport.merchant.common.vo.MerchantPagesTree;
import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultiterminalInteractiveCenterApplication.class)
@Slf4j
public class OperationLogControllerTest {

    @Autowired
    private MerchantLogService merchantLogService;


    @Test
    public void queryLog() {
        MerchantLogQueryVO queryVO = new MerchantLogQueryVO();
        queryVO.setStartTime(1694793600000L);
        queryVO.setEndTime(1695052799999L);
        queryVO.setPageNum(1);
        queryVO.setPageSize(20);
        //查询父级菜单
        queryVO.setPageCode(MerchantLogPageEnum.MAINTENANCE_SET.getCode());
        Response response = merchantLogService.queryLog(queryVO);
        PageVO<MerchantLogDTO> result = (PageVO<MerchantLogDTO>) response.getData();
        System.out.println(result);
    }

    @Test
    public void getChildPageCodeTest() {
        //获取子级菜单
        MerchantLogPageEnum merchantLogPageEnum = MerchantLogPageEnum.DOMAIN_SET_NEW;

        ArrayList<String> pageCodeList = new ArrayList<>();
            List<MerchantPagesTree> uotList = new ArrayList<>();
            for (MerchantLogPageEnum pageEnum : MerchantLogPageEnum.values()) {
                if (MerchantLogConstants.THREE_TERMINAL.equals(pageEnum.getTag())) {
                    uotList.add(buildPagesTree(pageEnum));
                }
            }
        MerchantPagesTree pageTree = uotList.get(0).getNodes().stream().filter(mpt -> merchantLogPageEnum.getCode().equals(mpt.getCode())).findFirst().orElse(new MerchantPagesTree());
        List<MerchantPagesTree> nodes = pageTree.getNodes();
        nodes.forEach(item -> {
            if(CollectionUtil.isNotEmpty(item.getNodes())) {
                pageCodeList.addAll(item.getNodes().stream().map(MerchantPagesTree::getCode).collect(Collectors.toList()));
            }
            pageCodeList.add(item.getCode());
        });
        log.info(merchantLogPageEnum.getCode() + "的子级菜单code :" + pageCodeList);
    }

    private  MerchantPagesTree buildPagesTree(MerchantLogPageEnum merchantLogPageEnum) {
        MerchantPagesTree tree = new MerchantPagesTree();
        tree.setId(merchantLogPageEnum.getId());
        tree.setPid(merchantLogPageEnum.getPrentId());
        tree.setCode(merchantLogPageEnum.getCode());
        String[] pageNames = merchantLogPageEnum.getRemark().split("-");
        tree.setName(pageNames[pageNames.length - 1]);
        tree.setEn(merchantLogPageEnum.getEn());
        return tree;
    }

}