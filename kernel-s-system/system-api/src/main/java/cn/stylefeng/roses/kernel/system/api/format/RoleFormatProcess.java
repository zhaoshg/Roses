package cn.stylefeng.roses.kernel.system.api.format;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.system.api.RoleServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.role.dto.SysRoleDTO;

import java.util.List;

/**
 * JSON响应对角色id的转化
 *
 * @author fengshuonan
 * @since 2023/5/4 21:20
 */
public class RoleFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        Long roleId = Convert.toLong(businessId);
        RoleServiceApi bean = SpringUtil.getBean(RoleServiceApi.class);
        List<SysRoleDTO> roleInfos = bean.getRolesByIds(ListUtil.list(false, roleId));
        if (ObjectUtil.isEmpty(roleInfos)) {
            return null;
        }
        return roleInfos.get(0).getRoleName();
    }

}
