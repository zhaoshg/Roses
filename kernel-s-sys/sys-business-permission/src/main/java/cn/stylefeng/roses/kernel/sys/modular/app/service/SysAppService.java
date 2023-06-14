package cn.stylefeng.roses.kernel.sys.modular.app.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.app.entity.SysApp;
import cn.stylefeng.roses.kernel.sys.modular.app.pojo.request.SysAppRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统应用 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
public interface SysAppService extends IService<SysApp> {

    /**
     * 新增
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void add(SysAppRequest sysAppRequest);

    /**
     * 删除
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void del(SysAppRequest sysAppRequest);

    /**
     * 批量删除应用
     *
     * @author fengshuonan
     * @since 2023/6/12 19:30
     */
    void batchDelete(SysAppRequest sysAppRequest);

    /**
     * 编辑
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void edit(SysAppRequest sysAppRequest);

    /**
     * 查询详情
     *
     * @param sysAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    SysApp detail(SysAppRequest sysAppRequest);

    /**
     * 获取列表
     *
     * @param sysAppRequest 请求参数
     * @return List<SysApp>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    List<SysApp> findList(SysAppRequest sysAppRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysAppRequest 请求参数
     * @return PageResult<SysApp>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    PageResult<SysApp> findPage(SysAppRequest sysAppRequest);

    /**
     * 获取启用的应用列表（用在给菜单界面用）
     *
     * @author fengshuonan
     * @since 2023/6/14 21:48
     */
    List<AppGroupDetail> getAppList();

}