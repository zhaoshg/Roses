/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.sys.starter.init;

import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.expander.TenantConfigExpander;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenuOptions;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuOptionsService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleMenuService;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 初始化admin管理员的服务
 *
 * @author fengshuonan
 * @since 2020/12/17 21:56
 */
@Service
public class InitAdminService {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuOptionsService sysMenuOptionsService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysRoleMenuOptionsService sysRoleMenuOptionsService;

    /**
     * 初始化后台管理员，后台管理员拥有最高权限
     *
     * @author fengshuonan
     * @since 2020/12/17 21:57
     */
    @Transactional(rollbackFor = Exception.class)
    public void initSuperAdmin() {

        // 找到默认系统租户下，后台管理员角色id
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleCode, SysConstants.BACKEND_ADMIN_ROLE_CODE);
        // 默认根租户
        queryWrapper.eq(SysRole::getTenantId, TenantConfigExpander.getDefaultRootTenantId());
        queryWrapper.select(SysRole::getRoleId);
        SysRole superAdminRole = sysRoleService.getOne(queryWrapper);

        // 获取所有的菜单和功能
        List<SysMenu> totalMenuList = sysMenuService.getTotalMenuList();
        List<SysMenuOptions> totalMenuOptionsList = sysMenuOptionsService.getTotalMenuOptionsList();

        // 后台管理员绑定所有的菜单和菜单功能
        sysRoleMenuService.bindRoleMenus(superAdminRole.getRoleId(), totalMenuList);
        sysRoleMenuOptionsService.bindRoleMenuOptions(superAdminRole.getRoleId(), totalMenuOptionsList);
    }

}
