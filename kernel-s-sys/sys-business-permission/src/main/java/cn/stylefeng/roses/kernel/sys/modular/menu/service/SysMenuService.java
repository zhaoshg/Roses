package cn.stylefeng.roses.kernel.sys.modular.menu.service;

import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.response.AppGroupDetail;
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
     * 获取菜单管理界面的每个应用组下的菜单信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:28
     */
    List<AppGroupDetail> getAppMenuGroupDetail(SysMenuRequest sysMenuRequest);

    /**
     * 校验菜单是否绑定到某个app下
     *
     * @return true-该app下有菜单，false-该app下没菜单
     * @author fengshuonan
     * @since 2023/6/12 19:23
     */
    boolean validateMenuBindApp(Set<Long> appIdList);

    /**
     * 获取菜单的所属appId
     *
     * @author fengshuonan
     * @since 2023/6/13 22:49
     */
    Long getMenuAppId(Long menuId);

    /**
     * 获取所有的菜单信息，用在角色绑定权限界面
     *
     * @author fengshuonan
     * @since 2023/6/15 9:24
     */
    List<SysMenu> getTotalMenus();

    /**
     * 调整菜单上下级结构和菜单的顺序
     *
     * @author fengshuonan
     * @since 2023/6/15 11:28
     */
    void updateMenuTree(SysMenuRequest sysMenuRequest);

}