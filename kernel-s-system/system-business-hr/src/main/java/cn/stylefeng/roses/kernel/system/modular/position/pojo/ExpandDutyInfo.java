package cn.stylefeng.roses.kernel.system.modular.position.pojo;

import lombok.Data;

import java.util.List;

/**
 * 存在拓展字段的职务信息
 *
 * @author fengshuonan
 * @since 2023/6/15 16:56
 */
@Data
public class ExpandDutyInfo {

    /**
     * 主要职务名称
     */
    private String duty;

    /**
     * 对应公司的职务名称
     */
    private List<DutyItem> ptDuty;

}
