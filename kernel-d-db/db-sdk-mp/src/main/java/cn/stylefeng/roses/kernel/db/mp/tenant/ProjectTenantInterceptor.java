package cn.stylefeng.roses.kernel.db.mp.tenant;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.db.api.pojo.tenant.TenantTableProperties;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;

import java.util.List;

/**
 * 租户隔离插件
 *
 * @author fengshuonan
 * @since 2023/8/30 10:14
 */
public class ProjectTenantInterceptor implements TenantLineHandler {

    /**
     * 租户相关的表的配置
     */
    private TenantTableProperties tenantTableProperties;

    public ProjectTenantInterceptor(TenantTableProperties tenantTableProperties) {
        this.tenantTableProperties = tenantTableProperties;
    }

    @Override
    public Expression getTenantId() {
        LoginUser loginUserNullable = LoginContext.me().getLoginUserNullable();
        if (loginUserNullable == null) {
            return null;
        }
        if (ObjectUtil.isEmpty(loginUserNullable.getTenantId())) {
            return null;
        }
        return new LongValue(loginUserNullable.getTenantId());
    }

    @Override
    public boolean ignoreTable(String tableName) {

        if (tenantTableProperties == null) {
            return true;
        }

        List<String> businessTableList = tenantTableProperties.getBusinessTableList();
        if (ObjectUtil.isEmpty(businessTableList)) {
            return true;
        }

        for (String tenantTable : businessTableList) {
            if (tenantTable.equalsIgnoreCase(tableName)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }

}
