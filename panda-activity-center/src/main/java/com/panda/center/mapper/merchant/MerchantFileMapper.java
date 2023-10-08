package com.panda.center.mapper.merchant;

import com.panda.center.entity.MerchantFile;
import com.panda.center.vo.MerchantFileVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.mapper
 * @Description :  商户日志orm接口
 * @Date: 2020-09-01 15:30
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Repository
public interface MerchantFileMapper {


    /**
     * [新增]
     *
     * @author duwan
     * @date 2020/12/08
     **/
    int insert(MerchantFile merchantFile);

    /**
     * [刪除]
     *
     * @author duwan
     * @date 2020/12/08
     **/
    int delete(Long id);

    /**
     * [更新]
     *
     * @author duwan
     * @date 2020/12/08
     **/
    int update(MerchantFile merchantFile);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author duwan
     * @date 2020/12/08
     **/
    MerchantFile load(Long id);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author duwan
     * @date 2020/12/08
     **/
    MerchantFile loadExecuteById();

    /**
     * [查询] 根据主键 id 查询
     *
     * @author duwan
     * @date 2020/12/08
     **/
    MerchantFile loadInitMaxTaskById();

    /**
     * [查询] 根据主键 id 查询
     *
     * @author duwan
     * @date 2020/12/08
     **/
    List<MerchantFile> loadInitTaskById();

    /**
     * 查询正在执行的文件
     *
     * @return
     */
    List<MerchantFile> queryExecuteFile(MerchantFileVo requestPageVO);

    /**
     * 根据文件名查询文件
     *
     * @return
     */
    List<MerchantFile> queryFileByName(@Param("fileName") String fileName);

    /**
     * 查询正在执行的文件
     *
     * @return
     */
    List<MerchantFile> queryFileRate(MerchantFileVo requestPageVO);
    /**
     * 获取当前最大序号
     *
     * @return
     */
    Long getLastNum();

    /**
     * 查询文件列表
     *
     * @param requestPageVO
     * @return
     */
    List<MerchantFile> queryList(MerchantFileVo requestPageVO);

    List<MerchantFile> queryListAll(MerchantFileVo requestPageVO);

    List<MerchantFile> queryListExceptionAll(MerchantFileVo requestPageVO);

    /**
     * 查询总数
     *
     * @return
     */
    Integer queryListInitCount(String username);

    /**
     * 查询总数
     *
     * @param requestPageVO
     * @return
     */
    Integer queryListCount(MerchantFileVo requestPageVO);

    /**
     * [查询] 分页查询
     *
     * @author duwan
     * @date 2020/12/08
     **/
    List<MerchantFile> pageList(int offset, int pagesize);

    /**
     * 重置状态
     */
    void resetFileStatus();

    List<MerchantFile> queryExistTask(@Param("merchantCode") String merchantCode);

    List<MerchantFile> queryInnerExistTask();
}
