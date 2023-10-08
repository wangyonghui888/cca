package com.panda.sport.merchant.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.panda.sport.merchant.common.enums.ArticleDelayTypeEnum;
import com.panda.sport.merchant.common.po.bss.TArticle;
import com.panda.sport.merchant.common.po.bss.TArticleStatistics;
import com.panda.sport.merchant.manage.schedule.DelayTask;
import com.panda.sport.merchant.manage.service.IDelayQueueService;
import com.panda.sport.merchant.manage.service.ITArticleService;
import com.panda.sport.merchant.manage.service.ITArticleStatisticsService;
import com.panda.sport.merchant.manage.util.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

@Service("articleDelayQueueService")
@Slf4j
public class ArticleDelayQueueServiceImpl implements IDelayQueueService<DelayTask>, InitializingBean, DisposableBean {
    private DelayQueue<DelayTask> queue = new DelayQueue<>();
    private static final String TAB = "延迟队列->";

    @Autowired
    private ITArticleService articleService;
    @Autowired
    private ITArticleStatisticsService articleStatisticsService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;



    @Override
    public boolean add(DelayTask item) {
        return queue.add(item);
    }

    @Override
    public boolean tryCancel(DelayTask item) {
        return queue.remove(item);
    }

    public void consumer() throws InterruptedException {
        DelayTask task = queue.take();
        log.info("{}消费{}",TAB, JSON.toJSONString(task));
        int type = task.getType();
        Long id = task.getId();
        QueryWrapper<TArticle> queryWrapper = new QueryWrapper<>();
        String key = buildKey(task);
        if(!redisTemplate.opsForValue().setIfAbsent(key,task.getId()+"",10,TimeUnit.SECONDS)) {
            return;
        }
        if(type == ArticleDelayTypeEnum.AUTO_SHOW.getType()) {
            //文章自动上线
            queryWrapper.eq("id",id);
            TArticle article = articleService.getOne(queryWrapper);
            if(null == article || null == article.getOnlineTime()) return;
            if(article.getOnlineTime().longValue() != task.getTime()) {
                log.info("{}上线时间已经变更 | task={}",TAB,JSON.toJSONString(task));
                return;
            }
            if(article.getIsShow() == 1) {
                log.info("{}文章已经上线 | task={}",TAB,JSON.toJSONString(task));
                return;
            }
            doShow(article);
        }else if(type == ArticleDelayTypeEnum.AUTO_TOP.getType()) {
            //文章自动置顶
            queryWrapper.eq("id",id).select("id","top_start_time","is_top","permanent_top","top_end_time");
            TArticle article = articleService.getOne(queryWrapper);
            if(null == article || null == article.getTopStartTime()) return;
            if(article.getTopStartTime().longValue() != task.getTime()) {
                log.info("{}置顶时间已经变更 | task={}",TAB,JSON.toJSONString(task));
                return;
            }
            if(article.getIsTop() == 1) {
                log.info("{}文章已经置顶 | task={}",TAB,JSON.toJSONString(task));
                return;
            }
            doTopStart(article);
        }else if(type == ArticleDelayTypeEnum.AUTO_OFFLINE.getType()) {
            //置顶文章自动下线
            queryWrapper.eq("id",id).select("id","top_end_time","is_top","permanent_top");
            TArticle article = articleService.getOne(queryWrapper);
            if(null == article || null == article.getTopEndTime()) return;
            if(article.getTopEndTime().longValue() != task.getTime()) {
                log.info("{}置顶过期时间已经变更 | task={}",TAB,JSON.toJSONString(task));
                return;
            }
            if(article.getIsTop() == 0) {
                log.info("{}文章已经置顶过期 | task={}",TAB,JSON.toJSONString(task));
                article.setUpdateTime(System.currentTimeMillis());
                article.setTopEndTime(null);
                articleService.updateById(article);
                return;
            }
            doTopEnd(article);
        }
        redisTemplate.delete(key);
    }

    private String buildKey(DelayTask task) {
        StringBuilder sb = new StringBuilder(RedisConstants.ARTICLE_DELAY_QUEUE_CONSUMER).append(task.getId())
                .append(":").append(task.getType()).append(":").append(task.getTime());
        return sb.toString();
    }

    private void doTopEnd(TArticle article) {
        article.setIsTop(0);
        article.setUpdateTime(System.currentTimeMillis());
        article.setTopEndTime(null);
        if(article.getPermanentTop() == 0) {
            article.setSortPriority(0);
        }
        articleService.updateById(article);
    }

    private void doTopStart(TArticle article) {
        article.setIsTop(1);
        article.setUpdateTime(System.currentTimeMillis());
        article.setTopStartTime(null);
        //永久置顶会在上线时已经设置了排序优先级
        if(article.getPermanentTop() == 0) {
            article.setSortPriority(1);
        }
        articleService.updateById(article);
    }

    @Transactional
    public void doShow(TArticle article) {
        article.setIsShow(1);
        //如果是普通置顶
        if(article.getIsTop() == 1) {
            article.setSortPriority(1);
        }
        //如果是永久置顶
        if(article.getPermanentTop() == 1) {
            article.setSortPriority(2);
        }
        article.setOnlineTime(null);
        long curTime = System.currentTimeMillis();
        article.setShowTime(curTime);
        article.setUpdateTime(curTime);
        boolean flag = articleService.updateById(article);
        doShowAfter(article,flag,curTime);
    }

    public void doShowAfter(TArticle article,boolean flag,long curTime) {
        if (flag) {
            if (article.getPermanentTop() == 1) {
                //只能有一个永久置顶处于上线状态
                QueryWrapper<TArticle> wrapper = new QueryWrapper<>();
                wrapper.select("id", "is_top").eq("permanent_top", 1).eq("is_show", 1);
                List<TArticle> articles = articleService.list(wrapper);
                if (ObjectUtils.isNotEmpty(articles)) {
                    articles.stream().filter(a -> {
                        if (a.getId().longValue() == article.getId().longValue()) return false;
                        return true;
                    }).forEach(a -> {
                        a.setPermanentTop(0);
                        //如果是普通置顶
                        if (a.getIsTop() == 1) {
                            a.setSortPriority(1);
                        } else {
                            a.setSortPriority(0);
                        }
                        a.setUpdateTime(curTime);
                    });
                    articleService.updateBatchById(articles);
                }
            }
            //插入文章数据
            TArticleStatistics articleStatistics = new TArticleStatistics();
            BeanUtils.copyProperties(article, articleStatistics);
            articleStatistics.setArticleId(article.getId());
            articleStatistics.setCreateTime(curTime);
            articleStatistics.setUpdateTime(curTime);
            articleStatisticsService.saveOrUpdate(articleStatistics);

            //上缓存 key值=赛事id,  value值：文章ID
//            if(ObjectUtils.isNotEmpty(article.getMatchId())) {
//                String redisKey = RedisConstants.ARTICLE_ARTICLE_ONLINE+article.getMatchId();
//                redisTemplate.opsForValue().set(redisKey,article.getId(),15,TimeUnit.DAYS);
//            }
        }
    }
    /**
     * 因为队列的数据存在内存中，需要节点重启时加载数据到延迟队列中，鉴于手动上线、延迟队列自动上线会清除上线时间（online_time），手动置顶、
     * 延迟队列自动置顶会清除掉置顶上线时间，延迟队列自动置顶下线会清除置顶过期时间，所有只需要把上线时间、置顶上线时间、置顶过期时间不为null
     * 的文章数据查询出来执行下列操作
     */
    @Override
    public void init() {
        List<TArticle> delays = articleService.selectDelay();
        List<DelayTask> tasks = new ArrayList<>(); //延迟消息
        List<TArticle> dirtys = new ArrayList<>(); //脏数据

        dataPartition(delays,  tasks, dirtys);
        tasks.stream().forEach(delayTask -> add(delayTask));
        articleService.updateBatchById(delays);
    }

    /**
     * 初始化数据划分到相对应的集合
     * @param delays
     * @param tasks
     * @param dirtys
     */
    private void dataPartition(List<TArticle> delays,  List<DelayTask> tasks, List<TArticle> dirtys) {
        long curTime = System.currentTimeMillis();
        delays.stream().forEach(d ->{
            Long onlineTime = d.getOnlineTime();
            if(null != onlineTime) {
                if(onlineTime.longValue() > 0 && d.getIsShow() == 0) {
                    DelayTask task = new DelayTask(d.getId(),onlineTime,ArticleDelayTypeEnum.AUTO_SHOW.getType());
                    tasks.add(task);
                }else {
                    d.setOnlineTime(null);
                    dirtys.add(d);
                }
            }
            Long topStartTime = d.getTopStartTime();
            Long topEndTime = d.getTopEndTime();
            if(null != topStartTime) {
                if(topStartTime.longValue() > 0 && d.getIsTop() == 0) {
                    boolean flag = true;
                    if(null != topEndTime && topEndTime.longValue() >0 && topEndTime.longValue() < curTime) {
                        flag = false;
                        d.setTopEndTime(null);
                        d.setTopStartTime(null);
                        dirtys.add(d);
                    }
                    if(flag) {
                        DelayTask task = new DelayTask(d.getId(),topStartTime,ArticleDelayTypeEnum.AUTO_TOP.getType());
                        tasks.add(task);
                    }
                }else {
                    d.setTopStartTime(null);
                    dirtys.add(d);
                }

            }
            if(null != topEndTime) {
                if(topEndTime.longValue() >0) {
                    DelayTask task = new DelayTask(d.getId(),topEndTime,ArticleDelayTypeEnum.AUTO_OFFLINE.getType());
                    tasks.add(task);
                }
            }

        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //启动线程异步执行初始化、消费动作，为防止多个节点同时加载数据，拿到redis lock的节点才会初始化，前提是节点启动间隔时间不能超过key的有效时间，可以允许多节点重复初始化，但是不允许消息丢失
        new Thread(() ->{
            boolean lock = redisTemplate.opsForValue().setIfAbsent(RedisConstants.ARTICLE_DELAY_QUEUE_INIT, String.valueOf(Thread.currentThread().getId()), 2, TimeUnit.MINUTES);
            if(lock) {
                log.info("{}开始初始化....",TAB);
                try {
                    init();
                }catch (Exception e) {
                    log.error("{}初始化异常 | e={}",TAB,e);
                    redisTemplate.delete(RedisConstants.ARTICLE_DELAY_QUEUE_INIT);
                }
            }
            for(;;) {
                try {
                    consumer();
                }catch (Exception e) {
                    log.error("{}消费消息异常 | e=",TAB,e);
                }
            }
        }).start();
    }

    @Override
    public void destroy() throws Exception {
        if(redisTemplate.hasKey(RedisConstants.ARTICLE_DELAY_QUEUE_INIT)) {
            redisTemplate.delete(RedisConstants.ARTICLE_DELAY_QUEUE_INIT);
        }
    }
}
