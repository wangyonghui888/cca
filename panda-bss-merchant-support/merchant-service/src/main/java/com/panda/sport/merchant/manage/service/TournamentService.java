package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.bss.PullDownResultPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.TournamentVo;
import com.panda.sport.merchant.common.vo.merchant.SportVO;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service
 * @Description :  联赛下拉接口
 * @Date: 2020-11-06 13:10:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface TournamentService {

    /**
     * 查询联赛集合根据球类和等级排序
     *
     * @param args
     * @param language
     * @return
     */
    List<TournamentVo> findTournamentListBySportsLevel(String args, String language,Integer level,Long sportId);

    List<TournamentVo> findFilterTournamentListBySportsLevel(String args, String language, Integer level, Long sportId, String dataSourceCode, String merchantCode);

    /**
     * 下拉联赛服务数据
     *
     * @param vo       只需要startTime(必选)  endTime(必选)  sportId(可选)  三个参数
     * @param language
     * @return 对象list
     */
    List<PullDownResultPO> pullDownTournament(SportVO vo, String language);

    Response getSportList(String language);

    Response getSportListByFilter(String language);

    Response getLocalCacheInfo();
}
