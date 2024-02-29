package cn.stylefeng.roses.kernel.db.mp.datascope;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.db.mp.datascope.holder.DataScopeHolder;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.Arrays;

/**
 * 项目数据权限的处理器
 *
 * @author fengshuonan
 * @since 2024/2/28 21:16
 */
public class ProjectDataScopeHandler implements MultiDataPermissionHandler {

    /**
     * 不存在的业务id值
     */
    public static final Long NONE_ID_VALUE = -1L;

    /**
     * 用在数据范围筛选的用户id字段的默认字段名称
     */
    public static final String DEFAULT_USER_ID_FIELD_NAME = "user_id";

    /**
     * 用在数据范围筛选的组织id字段的默认字段名称
     */
    public static final String DEFAULT_ORG_ID_FIELD_NAME = "org_id";

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {

        // 获取数据范围上下文的配置，如果没有则直接略过
        DataScopeConfig dataScopeConfig = DataScopeHolder.get();
        if (ObjectUtil.isEmpty(dataScopeConfig)) {
            return null;
        }

        // 数据校验处理
        dataScopeConfig = this.validateDataScopeConfig(dataScopeConfig);
        if (dataScopeConfig == null) {
            return null;
        }

        // 获取数据范围的类型
        DataScopeTypeEnum dataScopeTypeEnum = dataScopeConfig.getDataScopeType();
        switch (dataScopeTypeEnum) {

            // 如果是全部数据，返回空，不对sql进行处理
            case ALL:
                return null;

            // 如果是本部门数据，则限制查询只能查询本部门数据
            case DEPT:
                // todo
                break;

            case DEPT_WITH_CHILD:
                // 本部门及以下数据
                break;
            case COMPANY_WITH_CHILD:
                // 本公司及以下数据
                break;
            case DEFINE:
                // 指定部门数据
                break;

            case SELF:
                // 仅本人数据
        }


        // 创建 org_id 列
        Column column = new Column("org_id");

        // 创建 IN 表达式的值列表
        ExpressionList expressionList = new ExpressionList();
        expressionList.setExpressions(Arrays.asList(new LongValue(1), new LongValue(2), new LongValue(3)));

        // 创建 IN 表达式
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(column);
        inExpression.setRightItemsList(expressionList);

        return inExpression;
    }

    /**
     * 校验数据范围配置是否正确
     *
     * @author fengshuonan
     * @since 2024-02-29 11:00
     */
    private DataScopeConfig validateDataScopeConfig(DataScopeConfig dataScopeConfig) {
        if (dataScopeConfig == null) {
            return null;
        }

        DataScopeTypeEnum dataScopeType = dataScopeConfig.getDataScopeType();
        if (dataScopeType == null) {
            return null;
        }

        // 如果数据范围为全部，直接返回空，也就是不进行数据范围sql拦截器
        if (DataScopeTypeEnum.ALL.equals(dataScopeType)) {
            return null;
        }

        // 如果数据范围是本人，则查询本人id是否传递
        else if (DataScopeTypeEnum.SELF.equals(dataScopeType)) {
            if (ObjectUtil.isEmpty(dataScopeConfig.getUserId())) {
                dataScopeConfig.setUserId(NONE_ID_VALUE);
            }
            if (ObjectUtil.isEmpty(dataScopeConfig.getUserIdFieldName())) {
                dataScopeConfig.setUserIdFieldName(DEFAULT_USER_ID_FIELD_NAME);
            }
        }

        // 如果是本公司及以下数据，则查询公司id是否传递
        else if (DataScopeTypeEnum.COMPANY_WITH_CHILD.equals(dataScopeType)) {
            if (ObjectUtil.isEmpty(dataScopeConfig.getUserCompanyId())) {
                dataScopeConfig.setUserCompanyId(NONE_ID_VALUE);
            }
            if (ObjectUtil.isEmpty(dataScopeConfig.getOrgIdFieldName())) {
                dataScopeConfig.setOrgIdFieldName(DEFAULT_ORG_ID_FIELD_NAME);
            }
        }

        // 如果是本部门及以下数据，则查询部门id是否传递
        else if (DataScopeTypeEnum.DEPT_WITH_CHILD.equals(dataScopeType)) {
            if (ObjectUtil.isEmpty(dataScopeConfig.getUserDeptId())) {
                dataScopeConfig.setUserDeptId(NONE_ID_VALUE);
            }
            if (ObjectUtil.isEmpty(dataScopeConfig.getOrgIdFieldName())) {
                dataScopeConfig.setOrgIdFieldName(DEFAULT_ORG_ID_FIELD_NAME);
            }
        }

        // 如果是本部门数据，则查询部门id是否传递
        else if (DataScopeTypeEnum.DEPT.equals(dataScopeType)) {
            if (ObjectUtil.isEmpty(dataScopeConfig.getUserDeptId())) {
                dataScopeConfig.setUserDeptId(NONE_ID_VALUE);
            }
            if (ObjectUtil.isEmpty(dataScopeConfig.getOrgIdFieldName())) {
                dataScopeConfig.setOrgIdFieldName(DEFAULT_ORG_ID_FIELD_NAME);
            }
        }

        // 如果是指定部门数据，则查询部门id是否传递
        else if (DataScopeTypeEnum.DEFINE.equals(dataScopeType)) {
            if (ObjectUtil.isEmpty(dataScopeConfig.getSpecificOrgIds())) {
                dataScopeConfig.setSpecificOrgIds(ListUtil.list(true, NONE_ID_VALUE));
            }
            if (ObjectUtil.isEmpty(dataScopeConfig.getOrgIdFieldName())) {
                dataScopeConfig.setOrgIdFieldName(DEFAULT_ORG_ID_FIELD_NAME);
            }
        }

        return dataScopeConfig;
    }

}
