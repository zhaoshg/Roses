package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.SysRoleServiceApi;
import cn.stylefeng.roses.kernel.sys.api.enums.permission.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统角色 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleService extends IService<SysRole>, SysRoleServiceApi {

    /**
     * 新增
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void add(SysRoleRequest sysRoleRequest);

    /**
     * 删除
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void del(SysRoleRequest sysRoleRequest);

    /**
     * 批量删除角色
     *
     * @author fengshuonan
     * @since 2023/6/12 21:11
     */
    void batchDelete(SysRoleRequest sysRoleRequest);

    /**
     * 编辑
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void edit(SysRoleRequest sysRoleRequest);

    /**
     * 查询详情
     *
     * @param sysRoleRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    SysRole detail(SysRoleRequest sysRoleRequest);

    /**
     * 获取列表
     *
     * @param sysRoleRequest 请求参数
     * @return List<SysRole>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    List<SysRole> findList(SysRoleRequest sysRoleRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysRoleRequest 请求参数
     * @return PageResult<SysRole>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest);

    /**
     * 获取角色的数据范围类型
     *
     * @param roleId 角色id
     * @return 数据范围类型
     * @author fengshuonan
     * @since 2023/7/16 23:28
     */
    Integer getRoleDataScopeType(Long roleId);

    /**
     * 更新角色的数据范围类型
     *
     * @param roleId        角色id
     * @param dataScopeType 数据范围类型
     * @author fengshuonan
     * @since 2023/7/16 23:28
     */
    void updateRoleDataScopeType(Long roleId, Integer dataScopeType);

    /**
     * 获取角色对应的数据范围，如果是多个角色，则取最大类型的数据范围
     *
     * @author fengshuonan
     * @since 2023/7/18 23:19
     */
    DataScopeTypeEnum getRoleDataScope(List<Long> roleIds);

}