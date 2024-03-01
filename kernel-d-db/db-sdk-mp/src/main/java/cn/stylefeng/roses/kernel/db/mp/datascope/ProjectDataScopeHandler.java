package cn.stylefeng.roses.kernel.db.mp.datascope;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.mp.datascope.config.DataScopeConfig;
import cn.stylefeng.roses.kernel.db.mp.datascope.holder.DataScopeHolder;
import cn.stylefeng.roses.kernel.rule.enums.permission.DataScopeTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.stream.Collectors;

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
                return getEqualsTo(dataScopeConfig.getOrgIdFieldName(), dataScopeConfig.getUserDeptId());

            // 如果是本部门及以下部门
            case DEPT_WITH_CHILD:
                return deptWithChildScope(dataScopeConfig, dataScopeConfig.getUserDeptId());

            // 本公司及以下数据
            case COMPANY_WITH_CHILD:
                return deptWithChildScope(dataScopeConfig, dataScopeConfig.getUserCompanyId());

            // 指定部门数据
            case DEFINE:
                return getInExpression(dataScopeConfig);

            // 仅本人数据
            case SELF:
                return getEqualsTo(dataScopeConfig.getUserIdFieldName(), dataScopeConfig.getUserId());

            // 其他情况
            default:
                return null;
        }
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

    /**
     * 获取equals语法的表达式
     *
     * @author fengshuonan
     * @since 2024-02-29 20:36
     */
    private static EqualsTo getEqualsTo(String fieldName, Long value) {
        Column orgIdColumn = new Column(fieldName);
        LongValue longValue = new LongValue(value);

        // 创建代表等式的 EqualsTo 对象
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(orgIdColumn);
        equalsTo.setRightExpression(longValue);
        return equalsTo;
    }

    /**
     * 获取in的表达式配置
     *
     * @author fengshuonan
     * @since 2024-02-29 20:35
     */
    private static InExpression getInExpression(DataScopeConfig dataScopeConfig) {
        // 创建 org_id 列
        Column orgIdColumn = new Column(dataScopeConfig.getOrgIdFieldName());

        // 创建 IN 表达式的值列表
        ExpressionList expressionList = new ExpressionList();
        expressionList.setExpressions(dataScopeConfig.getSpecificOrgIds().stream().map(LongValue::new).collect(Collectors.toList()));

        // 创建 IN 表达式
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(orgIdColumn);
        inExpression.setRightItemsList(expressionList);
        return inExpression;
    }

    /**
     * 获取本部门及以下
     * <p>
     * 拼接条件最终效果如下：org_id in (select org_id from sys_hr_organization where org_pids like "%[id]%" or org_id = id)
     *
     * @author fengshuonan
     * @since 2024-02-29 20:14
     */
    private static Expression deptWithChildScope(DataScopeConfig dataScopeConfig, Long deptOrCompanyId) {

        // 创建 org_id 列
        Column orgIdColumn = new Column(dataScopeConfig.getOrgIdFieldName());

        // 创建子查询 select 部分
        SubSelect subSelect = new SubSelect();
        PlainSelect selectBody = new PlainSelect();
        selectBody.setSelectItems(ListUtil.of(new SelectExpressionItem(orgIdColumn)));
        selectBody.setFromItem(new Table("sys_hr_organization"));

        // 创建 LIKE 表达式
        LikeExpression likeExpression = new LikeExpression();
        likeExpression.setLeftExpression(new Column("org_pids"));
        likeExpression.setRightExpression(new StringValue("%[" + deptOrCompanyId + "]%"));

        // 设置等于表达式
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(orgIdColumn);
        equalsTo.setRightExpression(new LongValue(deptOrCompanyId));

        // 创建 OR 表达式
        OrExpression orExpression = new OrExpression(likeExpression, equalsTo);

        // 设置子查询的 WHERE 条件
        selectBody.setWhere(orExpression);
        subSelect.setSelectBody(selectBody);

        // 创建 IN 表达式
        return new InExpression(orgIdColumn, subSelect);
    }
}
