package cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo;

import lombok.Data;

/**
 * 表格宽度信息的配置
 *
 * @author fengshuonan
 * @since 2023/2/23 22:34
 */
@Data
public class TableWidthItem {

    /**
     * 字段编码，例如：projectName
     */
    private String fieldCode;

    /**
     * 字段编码，例如：项目名称
     */
    private String fieldName;

    /**
     * 是否显示字段，true显示，false-不显示
     */
    private Boolean showFlag;

    /**
     * 字段展示宽度
     */
    private Integer width;

    /**
     * 该字段是否锁定列
     */
    private Boolean lockFlag;

}
