package cn.stylefeng.roses.kernel.sys.modular.role.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRoleLimit;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleLimitRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色权限限制 服务类
 *
 * @author fengshuonan
 * @date 2023/09/08 12:55
 */
public interface SysRoleLimitService extends IService<SysRoleLimit> {

	/**
     * 新增
     *
     * @param sysRoleLimitRequest 请求参数
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    void add(SysRoleLimitRequest sysRoleLimitRequest);

	/**
     * 删除
     *
     * @param sysRoleLimitRequest 请求参数
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    void del(SysRoleLimitRequest sysRoleLimitRequest);

	/**
     * 编辑
     *
     * @param sysRoleLimitRequest 请求参数
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    void edit(SysRoleLimitRequest sysRoleLimitRequest);

	/**
     * 查询详情
     *
     * @param sysRoleLimitRequest 请求参数
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    SysRoleLimit detail(SysRoleLimitRequest sysRoleLimitRequest);

	/**
     * 获取列表
     *
     * @param sysRoleLimitRequest        请求参数
     * @return List<SysRoleLimit>   返回结果
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    List<SysRoleLimit> findList(SysRoleLimitRequest sysRoleLimitRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysRoleLimitRequest              请求参数
     * @return PageResult<SysRoleLimit>   返回结果
     * @author fengshuonan
     * @date 2023/09/08 12:55
     */
    PageResult<SysRoleLimit> findPage(SysRoleLimitRequest sysRoleLimitRequest);

}