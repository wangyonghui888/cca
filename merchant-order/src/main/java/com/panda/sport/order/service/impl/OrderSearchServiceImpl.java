package com.panda.sport.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.panda.sport.backup.mapper.BackUpSMatchInfoMapper;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.order.feign.BssBackendClient;
import com.panda.sport.order.service.OrderSearchService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@AllArgsConstructor
public class OrderSearchServiceImpl implements OrderSearchService {

    private BackUpSMatchInfoMapper backUpSMatchInfoMapper;

    @Autowired
    private BssBackendClient bssBackendClient;


    @Override
    public Response<?> getSportIdByMatchManageId(String matchId) {
        Long sportId = null;
         if(StringUtils.isNotEmpty(matchId) && matchId.length()>7) {//长赛事id
             Object obj = bssBackendClient.getSportIdByMatchManageId(matchId);
             if (ObjectUtil.isNotEmpty(obj)) {
                 Map<String, Object> map = (Map<String, Object>) obj;
                 if (ObjectUtil.isNotEmpty(map.get("data"))) {
                     sportId = Long.valueOf(map.get("data").toString());
                 }
             }
         }else{//短赛事id
             sportId = backUpSMatchInfoMapper.getSportIdByMatchIdSMatch(matchId, "s_match_info");
             if (sportId == null) {
                 sportId = backUpSMatchInfoMapper.getSportIdByMatchIdSMatch(matchId, "s_virtual_match_info");
             }
             if (sportId == null) {
                 sportId = backUpSMatchInfoMapper.getSportIdByOutrightIdSMatch(matchId);
             }
             if (sportId == null) {
                 sportId = backUpSMatchInfoMapper.getSportIdById(matchId);
             }
         }

        return Response.returnSuccess(sportId);
    }

}