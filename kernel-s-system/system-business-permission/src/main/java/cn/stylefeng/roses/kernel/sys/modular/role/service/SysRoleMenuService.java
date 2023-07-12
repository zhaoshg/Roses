package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleMenu;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleMenuRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色菜单关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 新增
     *
     * @param sysRoleMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void add(SysRoleMenuRequest sysRoleMenuRequest);

    /**
     * 删除
     *
     * @param sysRoleMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void del(SysRoleMenuRequest sysRoleMenuRequest);

    /**
     * 编辑
     *
     * @param sysRoleMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void edit(SysRoleMenuRequest sysRoleMenuRequest);

    /**
     * 查询详情
     *
     * @param sysRoleMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    SysRoleMenu detail(SysRoleMenuRequest sysRoleMenuRequest);

    /**
     * 获取列表
     *
     * @param sysRoleMenuRequest 请求参数
     * @return List<SysRoleMenu>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    List<SysRoleMenu> findList(SysRoleMenuRequest sysRoleMenuRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysRoleMenuRequest 请求参数
     * @return PageResult<SysRoleMenu>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    PageResult<SysRoleMenu> findPage(SysRoleMenuRequest sysRoleMenuRequest);

    /**
     * 给角色绑定某些菜单
     *
     * @author fengshuonan
     * @since 2023/6/18 20:46
     */
    void bindRoleMenus(Long roleId, List<SysMenu> menuList);

    /**
     * 获取角色绑定的菜单id集合，返回菜单id的集合
     *
     * @author fengshuonan
     * @since 2023/6/19 12:45
     */
    List<Long> getRoleBindMenuIdList(List<Long> roleIdList);

    /**
     * 判断指定角色集合，是否有对应应用的权限
     *
     * @param roleIdList 角色id集合，一般指的是用户拥有的角色id集合
     * @param appId      应用id
     * @return true-角色id集合中包含该应用的权限，false-角色id集合中不包含权限
     * @author fengshuonan
     * @since 2023/6/21 16:27
     */
    boolean validateRoleHaveAppIdPermission(List<Long> roleIdList, Long appId);

}