package cn.stylefeng.roses.kernel.db.mp.datascope;

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

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {

        // 创建 org_id 列
        Column column = new Column("org_id");

        // 创建 IN 表达式的值列表
        ExpressionList expressionList = new ExpressionList();
        expressionList.setExpressions(Arrays.asList(
                new LongValue(1),
                new LongValue(2),
                new LongValue(3)
        ));

        // 创建 IN 表达式
        InExpression inExpression = new InExpression();
        inExpression.setLeftExpression(column);
        inExpression.setRightItemsList(expressionList);

        return inExpression;
    }

}
