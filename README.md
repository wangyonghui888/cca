# panda-merchant

## 项目介绍

- merchant-api 对外的API

> /api/fund/transfer 上下分接口  
> /api/user/test K8S心跳接口
> /api/bet/queryPreBetOrderList 预约投注订单拉取

- merchant-api-new merchant_api拆分出来的 ，保留如下接口

> /api/user/login 登录  
> /api/user/create 注册  
> /api/user/test K8S心跳接口    
> /api/bet/queryBetList 拉单接口  
> /api/bet/getBetDetail 获取注单记录  
> /api/fund/checkBalance 查询玩家余额

- merchant-admin 商户后台-对外
- merchant-oubaogame 模拟商户的登录
- merchant-manage 商户后台--自己
- merchant-order 商户后台--自己
- panda-activity-center 活动中心
- multiterminal-interactive-center 三端服务

## 其他备注

- logstash 集群消费数据kafka,实现高可用, 配置groupId 一样,并且保持partition和logstash 总的threads一致

## 不要只知道写代码,实现功能, 有点架构思想.

- 1 什么样的业务功能要做轻量级,尽量不要引入 很重的中间件.
- 2 非核心业务,跟核心业务要在 代码, DB,服务,甚至 中间件 层面 尽量解耦.

## 引入了报表项目(数据仓库),引入了feign RPC

## 所有清理客户端会话, 清理redis缓存的地方一律在客户端服务处理实现细节. 比如merchant-api,

merchant-api-new,panda_bss-usercenter,panda-bss-order-record,panda-bss-order.
> 禁止在后台服务,比如merchant-manage或者merchant-admin,panda-bss-backend-order.直接操作redis.
> 即使功能上可以实现,也不允许这样做.(限流除外)

- 1 这样做就把后台服务和客户端服务的功能强耦合在一起,微服务的设计理念优势又在哪里呢!!!
- 2 操作redis 的入口太多, 其他人接手不明白情况,一堆坑 ,非常不利于维护

