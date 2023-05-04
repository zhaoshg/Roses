package cn.stylefeng.roses.kernel.system.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.system.api.PositionServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrPositionDTO;

/**
 * JSON响应对职务id的转化
 *
 * @author fengshuonan
 * @since 2023/5/4 21:20
 */
public class PositionFormatProcess extends BaseSimpleFieldFormatProcess {

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        Long positionId = Convert.toLong(businessId);
        PositionServiceApi bean = SpringUtil.getBean(PositionServiceApi.class);
        HrPositionDTO positionDetail = bean.getPositionDetail(positionId);
        if (ObjectUtil.isEmpty(positionDetail)) {
            return null;
        }
        return positionDetail.getPositionName();
    }

}
