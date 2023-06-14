package cn.stylefeng.roses.kernel.sys.modular.menu.factory;

import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.sys.modular.menu.enums.SysMenuExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.menu.pojo.request.SysMenuRequest;
import cn.stylefeng.roses.kernel.sys.modular.menu.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 菜单参数校验
 *
 * @author fengshuonan
 * @since 2023/6/14 23:09
 */
public class MenuValidateFactory {

    /**
     * 校验新增菜单时候的参数合法性
     *
     * @author fengshuonan
     * @since 2023/6/14 23:10
     */
    public static void validateAddMenuParam(SysMenuRequest sysMenuRequest) {

        SysMenuService sysMenuService = SpringUtil.getBean(SysMenuService.class);

        // 同应用下菜单编码不能重复
        Long menuId = sysMenuRequest.getMenuId();

        // 如果是新增菜单
        if (menuId == null) {
            String menuCode = sysMenuRequest.getMenuCode();
            LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysMenuLambdaQueryWrapper.eq(SysMenu::getMenuCode, menuCode);
            long alreadyCount = sysMenuService.count(sysMenuLambdaQueryWrapper);
            if (alreadyCount > 0) {
                throw new ServiceException(SysMenuExceptionEnum.MENU_CODE_REPEAT);
            }
        }


    }

}
