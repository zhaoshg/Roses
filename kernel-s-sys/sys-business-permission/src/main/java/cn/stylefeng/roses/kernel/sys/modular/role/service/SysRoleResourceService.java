package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleResource;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleResourceRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色资源关联 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
public interface SysRoleResourceService extends IService<SysRoleResource> {

	/**
     * 新增
     *
     * @param sysRoleResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void add(SysRoleResourceRequest sysRoleResourceRequest);

	/**
     * 删除
     *
     * @param sysRoleResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void del(SysRoleResourceRequest sysRoleResourceRequest);

	/**
     * 编辑
     *
     * @param sysRoleResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    void edit(SysRoleResourceRequest sysRoleResourceRequest);

	/**
     * 查询详情
     *
     * @param sysRoleResourceRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    SysRoleResource detail(SysRoleResourceRequest sysRoleResourceRequest);

	/**
     * 获取列表
     *
     * @param sysRoleResourceRequest        请求参数
     * @return List<SysRoleResource>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    List<SysRoleResource> findList(SysRoleResourceRequest sysRoleResourceRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysRoleResourceRequest              请求参数
     * @return PageResult<SysRoleResource>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    PageResult<SysRoleResource> findPage(SysRoleResourceRequest sysRoleResourceRequest);

}