package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
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
     * @param sysRoleMenuRequest        请求参数
     * @return List<SysRoleMenu>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    List<SysRoleMenu> findList(SysRoleMenuRequest sysRoleMenuRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysRoleMenuRequest              请求参数
     * @return PageResult<SysRoleMenu>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    PageResult<SysRoleMenu> findPage(SysRoleMenuRequest sysRoleMenuRequest);

}