package cn.stylefeng.roses.kernel.sys.modular.org.factory;

import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 组织机构创建工厂
 *
 * @author fengshuonan
 * @since 2023/6/11 11:22
 */
public class OrganizationFactory {

    /**
     * 通过组织机构列表，获取这个组织机构列表的所有父级id集合
     *
     * @author fengshuonan
     * @since 2023/6/11 11:22
     */
    public static Set<Long> getOrgParentIdList(List<HrOrganization> organizationList) {

        Set<Long> orgIdList = new HashSet<>();

        for (HrOrganization hrOrganization : organizationList) {
            String orgParentIdListStr = hrOrganization.getOrgPids();

            // 去掉中括号符号
            orgParentIdListStr = orgParentIdListStr.replaceAll("\\[", "");
            orgParentIdListStr = orgParentIdListStr.replaceAll("]", "");

            // 获取所有上级id列表
            String[] orgParentIdList = orgParentIdListStr.split(",");

            for (String orgIdStr : orgParentIdList) {
                Long orgId = Convert.toLong(orgIdStr);
                orgIdList.add(orgId);
            }
        }

        return orgIdList;
    }

}
