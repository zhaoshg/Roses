package cn.stylefeng.roses.kernel.sys.modular.message.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.request.SysMessageRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统消息服务类
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
public interface SysMessageService extends IService<SysMessage> {

    /**
     * 新增系统消息
     *
     * @param sysMessageRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    void add(SysMessageRequest sysMessageRequest);

    /**
     * 删除系统消息
     *
     * @param sysMessageRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    void del(SysMessageRequest sysMessageRequest);

    /**
     * 批量删除系统消息
     *
     * @param sysMessageRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    void batchDelete(SysMessageRequest sysMessageRequest);

    /**
     * 编辑系统消息
     *
     * @param sysMessageRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    void edit(SysMessageRequest sysMessageRequest);

    /**
     * 查询详情系统消息
     *
     * @param sysMessageRequest 请求参数
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    SysMessage detail(SysMessageRequest sysMessageRequest);

    /**
     * 获取系统消息列表
     *
     * @param sysMessageRequest         请求参数
     * @return List<SysMessage>  返回结果
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    List<SysMessage> findList(SysMessageRequest sysMessageRequest);

    /**
     * 获取系统消息分页列表
     *
     * @param sysMessageRequest                请求参数
     * @return PageResult<SysMessage>   返回结果
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    PageResult<SysMessage> findPage(SysMessageRequest sysMessageRequest);

}
