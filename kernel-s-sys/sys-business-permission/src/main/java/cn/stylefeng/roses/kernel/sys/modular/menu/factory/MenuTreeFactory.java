package cn.stylefeng.roses.kernel.sys.modular.menu.factory;

import cn.stylefeng.roses.kernel.sys.modular.menu.entity.SysMenu;

import java.math.BigDecimal;
import java.util.List;

/**
 * 菜单树的相关信息创建工厂
 * <p>
 * 用在更新菜单树的结构接口上
 *
 * @author fengshuonan
 * @since 2023/6/15 14:30
 */
public class MenuTreeFactory {

    /**
     * 更新菜单的排序
     *
     * @param tree  被更新的菜单树
     * @param level 当前被更新的菜单的层级（level从1开始）
     * @author fengshuonan
     * @since 2023/6/15 14:30
     */
    public static void updateSort(List<SysMenu> tree, Integer level) {

        // 初始的排序值
        int i = 1;

        // 倍数，第1层级从100开始排列
        // 第2层是1000开始
        int beishu = 10;

        for (int integer = 0; integer < level; integer++) {
            beishu = beishu * 10;
        }

        // 第1层级是110，120，130
        // 第2层级是1010，1020，1030
        // 第3层级是10010，10020，10030
        for (SysMenu sysMenu : tree) {
            BigDecimal bigDecimal = new BigDecimal(1);
            bigDecimal = bigDecimal.multiply(new BigDecimal(beishu));
            bigDecimal = bigDecimal.add(new BigDecimal(i * 10));
            sysMenu.setMenuSort(bigDecimal);
            i++;

            // 递归修改子树
            List<SysMenu> children = sysMenu.getChildren();
            if (children != null && children.size() > 0) {
                updateSort(children, level + 1);
            }
        }
    }

}