package cn.stylefeng.roses.kernel.migration.api.pojo;

import lombok.Data;

/**
 * 迁移数据信息
 *
 * @author majianguo
 * @since 2021/7/6 16:30
 */
@Data
public class MigrationInfo {

    /**
     * 版本
     */
    private String version;

    /**
     * 数据
     */
    private Object data;

}
