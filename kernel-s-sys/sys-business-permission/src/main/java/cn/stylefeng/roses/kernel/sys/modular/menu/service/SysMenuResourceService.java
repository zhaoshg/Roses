package cn.stylefeng.roses.kernel.sys.modular.menu.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuResource;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuResourceRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单资源绑定 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
public interface SysMenuResourceService extends IService<SysMenuResource> {

	/**
     * 新增
     *
     * @param sysMenuResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void add(SysMenuResourceRequest sysMenuResourceRequest);

	/**
     * 删除
     *
     * @param sysMenuResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void del(SysMenuResourceRequest sysMenuResourceRequest);

	/**
     * 编辑
     *
     * @param sysMenuResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void edit(SysMenuResourceRequest sysMenuResourceRequest);

	/**
     * 查询详情
     *
     * @param sysMenuResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    SysMenuResource detail(SysMenuResourceRequest sysMenuResourceRequest);

	/**
     * 获取列表
     *
     * @param sysMenuResourceRequest        请求参数
     * @return List<SysMenuResource>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    List<SysMenuResource> findList(SysMenuResourceRequest sysMenuResourceRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysMenuResourceRequest              请求参数
     * @return PageResult<SysMenuResource>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    PageResult<SysMenuResource> findPage(SysMenuResourceRequest sysMenuResourceRequest);

}