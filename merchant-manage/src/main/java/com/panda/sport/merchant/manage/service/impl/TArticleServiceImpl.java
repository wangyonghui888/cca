package com.panda.sport.merchant.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.match.mapper.TArticleMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.ArticleDelayTypeEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.SArticle;
import com.panda.sport.merchant.common.po.bss.TArticle;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.manage.schedule.DelayTask;
import com.panda.sport.merchant.manage.service.IDelayQueueService;
import com.panda.sport.merchant.manage.service.ISArticleService;
import com.panda.sport.merchant.manage.service.ITArticleService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 赛事文章表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2022-05-29
 */
@Service
public class TArticleServiceImpl extends ServiceImpl<TArticleMapper, TArticle> implements ITArticleService {
    @Resource(name = "articleDelayQueueService")
    private IDelayQueueService<DelayTask> articleDelayQueueService;

    @Autowired
    private ISArticleService isArticleService;

    @Autowired
    private MerchantLogService merchantLogService;


    @Transactional
    @Override
    public void insert(TArticle article) {
        boolean result = save(article);
        if(result) {
            afterArticleSave(article);
        }
    }

    private void afterArticleUpdate(TArticle article, TArticle oldArticle) {
        //清上线文章缓存
        if(null != oldArticle.getMatchId()) {
//            String redisKey = RedisConstants.ARTICLE_ARTICLE_ONLINE+oldArticle.getMatchId();
//            if(RedisTemp.hasKey(redisKey) && RedisTemp.get(redisKey).toString().equals(oldArticle.getId().toString())) {
//                RedisTemp.delete(redisKey);
//            }
            //清除关联赛事
            if(null == article.getMatchId()) {
                baseMapper.clearMatchInfo(article.getId());
            }
        }
        if(article.getIsShow() == 1 && oldArticle.getIsShow() == 0) {
            ArticleDelayQueueServiceImpl articleDelayQueueServiceImpl = (ArticleDelayQueueServiceImpl) articleDelayQueueService;
            articleDelayQueueServiceImpl.doShowAfter(article,true,System.currentTimeMillis());
        }
        //延迟队列消息
        if(null != article.getOnlineTime() && !article.getOnlineTime().equals(oldArticle.getOnlineTime())) {
            DelayTask task = new DelayTask(article.getId(),article.getOnlineTime(), ArticleDelayTypeEnum.AUTO_SHOW.getType());
            articleDelayQueueService.add(task);
            //尝试取消
            if(null != oldArticle.getOnlineTime()) {
                DelayTask cancelTask = new DelayTask(article.getId(),article.getOnlineTime(), ArticleDelayTypeEnum.AUTO_SHOW.getType());
                articleDelayQueueService.tryCancel(cancelTask);
            }
        }
        if(null != article.getTopStartTime() && !article.getTopStartTime().equals(oldArticle.getTopStartTime())) {
            DelayTask task = new DelayTask(article.getId(),article.getTopStartTime(), ArticleDelayTypeEnum.AUTO_TOP.getType());
            articleDelayQueueService.add(task);
            if(null != oldArticle.getTopStartTime()) {
                DelayTask cancelTask = new DelayTask(article.getId(),oldArticle.getTopStartTime(),ArticleDelayTypeEnum.AUTO_TOP.getType());
                articleDelayQueueService.tryCancel(cancelTask);
            }
        }
        if(null != article.getTopEndTime() && !article.getTopEndTime().equals(oldArticle.getTopEndTime())) {
            DelayTask task = new DelayTask(article.getId(),article.getTopEndTime(), ArticleDelayTypeEnum.AUTO_OFFLINE.getType());
            articleDelayQueueService.add(task);
            if(null != oldArticle.getTopEndTime()) {
                DelayTask cancelTask = new DelayTask(article.getId(),oldArticle.getTopEndTime(),ArticleDelayTypeEnum.AUTO_OFFLINE.getType());
                articleDelayQueueService.tryCancel(cancelTask);
            }
        }
    }

    @Override
    public List<TArticle> selectDelay() {
        return baseMapper.selectDelay();
    }

    @Transactional
    @Override
    public void edit(TArticle article, TArticle oldArticle) {
        boolean flag = updateById(article);
        if(flag) {
            afterArticleUpdate(article,oldArticle);
        }
    }

    @Transactional
    @Override
    public void showBatch(HttpServletRequest request, List<Long> idList) {
        //查询所有id匹配的文章，把上线状态的过滤掉
        List<TArticle> articles = listByIds(idList);
        articles = articles.stream().filter(article -> {
            if (article.getIsShow() == 1) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        idList = articles.stream().map(TArticle::getId).collect(Collectors.toList());
        if(idList.size() == 0) return;
        baseMapper.showBatch(idList);
        ArticleDelayQueueServiceImpl articleDelayQueueServiceImpl = (ArticleDelayQueueServiceImpl) articleDelayQueueService;
        articles.stream().forEach(article -> articleDelayQueueServiceImpl.doShow(article));

        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
        vo.getBeforeValues().add("下架");
        vo.getAfterValues().add("上线");
        String ids = idList.stream().map(String::valueOf).collect(Collectors.joining(","));
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_74, MerchantLogTypeEnum.BATCH_ONLINE, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, null,ids , language , ip);
    }

    @Override
    public void offline(HttpServletRequest request,List<Long> idList) {
        //查询所有id匹配的文章，把下线状态的过滤掉
        List<TArticle> articles = listByIds(idList);
        articles = articles.stream().filter(article -> {
            if (article.getIsShow() == 0) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        idList = articles.stream().map(TArticle::getId).collect(Collectors.toList());
        if(idList.size() == 0) return;
        baseMapper.offlineBatch(idList);
        //清上线文章缓存
//        idList = articles.stream().filter(article -> {
//            if(null != article.getMatchId()) {
//                String key = RedisConstants.ARTICLE_ARTICLE_ONLINE + article.getMatchId();
//                if(RedisTemp.hasKey(key) && RedisTemp.get(key).toString().equals(article.getId().toString())) {
//                    return true;
//                }
//            }
//            return false;
//        }).map(TArticle::getMatchId).collect(Collectors.toList());
//        Set<String> keys = idList.stream().map(id -> RedisConstants.ARTICLE_ARTICLE_ONLINE + id).collect(Collectors.toSet());
//        RedisTemp.delete(keys);

        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
        vo.getBeforeValues().add("上线");
        vo.getAfterValues().add("下架");
        String ids = idList.stream().map(String::valueOf).collect(Collectors.joining(","));
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_74, MerchantLogTypeEnum.BATCH_OFFLINE, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, null,ids , language , ip);
    }



    @Transactional
    @Override
    public void batchExport(List<TArticle> tArticleList, List<SArticle> sArticleList) {
        saveBatch(tArticleList);
        for(SArticle sArticle : sArticleList) {
            sArticle.setArticleStatus(1);
        }
        isArticleService.updateBatchById(sArticleList);
    }

    private void afterArticleSave(TArticle article) {
        if(article.getIsShow() == 1) {
            ArticleDelayQueueServiceImpl articleDelayQueueServiceImpl = (ArticleDelayQueueServiceImpl) articleDelayQueueService;
            articleDelayQueueServiceImpl.doShowAfter(article,true,System.currentTimeMillis());
        }
        //延迟队列消息
        if(null != article.getOnlineTime()) {
            DelayTask task = new DelayTask(article.getId(),article.getOnlineTime(), ArticleDelayTypeEnum.AUTO_SHOW.getType());
            articleDelayQueueService.add(task);
        }
        if(null != article.getTopStartTime()) {
            DelayTask task = new DelayTask(article.getId(),article.getTopStartTime(), ArticleDelayTypeEnum.AUTO_TOP.getType());
            articleDelayQueueService.add(task);
        }
        if(null != article.getTopEndTime()) {
            DelayTask task = new DelayTask(article.getId(),article.getTopEndTime(), ArticleDelayTypeEnum.AUTO_OFFLINE.getType());
            articleDelayQueueService.add(task);
        }
    }
}
