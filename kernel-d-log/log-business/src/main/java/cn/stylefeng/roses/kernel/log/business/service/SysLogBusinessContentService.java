package cn.stylefeng.roses.kernel.log.business.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.business.entity.SysLogBusinessContent;
import cn.stylefeng.roses.kernel.log.business.pojo.request.SysLogBusinessContentRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 业务日志记录 服务类
 *
 * @author fengshuonan
 * @date 2023/07/21 19:02
 */
public interface SysLogBusinessContentService extends IService<SysLogBusinessContent> {

    /**
     * 新增
     *
     * @param sysLogBusinessContentRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    void add(SysLogBusinessContentRequest sysLogBusinessContentRequest);

    /**
     * 删除
     *
     * @param sysLogBusinessContentRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    void del(SysLogBusinessContentRequest sysLogBusinessContentRequest);

    /**
     * 编辑
     *
     * @param sysLogBusinessContentRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    void edit(SysLogBusinessContentRequest sysLogBusinessContentRequest);

    /**
     * 查询详情
     *
     * @param sysLogBusinessContentRequest 请求参数
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    SysLogBusinessContent detail(SysLogBusinessContentRequest sysLogBusinessContentRequest);

    /**
     * 获取列表
     *
     * @param sysLogBusinessContentRequest 请求参数
     * @return List<SysLogBusinessContent>   返回结果
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    List<SysLogBusinessContent> findList(SysLogBusinessContentRequest sysLogBusinessContentRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysLogBusinessContentRequest 请求参数
     * @return PageResult<SysLogBusinessContent>   返回结果
     * @author fengshuonan
     * @date 2023/07/21 19:02
     */
    PageResult<SysLogBusinessContent> findPage(SysLogBusinessContentRequest sysLogBusinessContentRequest);

    /**
     * 批量保存业务日志详情
     *
     * @author fengshuonan
     * @since 2023/7/21 19:07
     */
    void batchSaveContent(List<SysLogBusinessContent> sysLogBusinessContentList);

}