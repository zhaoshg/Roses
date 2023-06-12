package cn.stylefeng.roses.kernel.sys.modular.menu.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 系统菜单 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:28
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 新增
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void add(SysMenuRequest sysMenuRequest);

    /**
     * 删除
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void del(SysMenuRequest sysMenuRequest);

    /**
     * 编辑
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    void edit(SysMenuRequest sysMenuRequest);

    /**
     * 查询详情
     *
     * @param sysMenuRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    SysMenu detail(SysMenuRequest sysMenuRequest);

    /**
     * 获取列表
     *
     * @param sysMenuRequest 请求参数
     * @return List<SysMenu>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    List<SysMenu> findList(SysMenuRequest sysMenuRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysMenuRequest 请求参数
     * @return PageResult<SysMenu>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    PageResult<SysMenu> findPage(SysMenuRequest sysMenuRequest);

    /**
     * 校验菜单是否绑定到某个app下
     *
     * @return true-该app下有菜单，false-该app下没菜单
     * @author fengshuonan
     * @since 2023/6/12 19:23
     */
    boolean validateMenuBindApp(Set<Long> appIdList);


}