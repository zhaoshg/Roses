package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuOptionsRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色和菜单下的功能关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleMenuOptionsService extends IService<SysRoleMenuOptions> {

    /**
     * 新增
     *
     * @param sysRoleMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void add(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest);

    /**
     * 删除
     *
     * @param sysRoleMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void del(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest);

    /**
     * 编辑
     *
     * @param sysRoleMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void edit(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest);

    /**
     * 查询详情
     *
     * @param sysRoleMenuOptionsRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    SysRoleMenuOptions detail(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest);

    /**
     * 获取列表
     *
     * @param sysRoleMenuOptionsRequest 请求参数
     * @return List<SysRoleMenuOptions>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    List<SysRoleMenuOptions> findList(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysRoleMenuOptionsRequest 请求参数
     * @return PageResult<SysRoleMenuOptions>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    PageResult<SysRoleMenuOptions> findPage(SysRoleMenuOptionsRequest sysRoleMenuOptionsRequest);

    /**
     * 删除角色绑定的菜单功能
     *
     * @author fengshuonan
     * @since 2023/6/15 23:38
     */
    void removeRoleBindOptions(Long optionsId);

    /**
     * 给角色绑定菜单功能
     *
     * @author fengshuonan
     * @since 2023/6/18 20:52
     */
    void bindRoleMenuOptions(Long roleId, List<SysMenuOptions> sysMenuOptionsList);

}