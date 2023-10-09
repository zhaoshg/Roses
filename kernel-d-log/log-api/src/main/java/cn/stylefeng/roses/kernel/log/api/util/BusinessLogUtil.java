package cn.stylefeng.roses.kernel.log.api.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.log.api.context.BusinessLogHolder;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;

/**
 * 业务日志
 *
 * @author fengshuonan
 * @since 2023/7/21 18:35
 */
public class BusinessLogUtil {

    /**
     * 设置日志的摘要信息，便于后台搜索
     *
     * @author fengshuonan
     * @since 2023/7/21 17:30
     */
    public static void setLogTitle(String logTitle) {
        if (StrUtil.isEmpty(logTitle)) {
            return;
        }
        BusinessLogHolder.setLogTitle(logTitle);
    }

    /**
     * 添加日志记录
     *
     * @author fengshuonan
     * @since 2023/7/21 16:53
     */
    public static void addContent(Object... contentObject) {
        if (ObjectUtil.isEmpty(contentObject)) {
            return;
        }
        try {
            StringBuilder stringBuffer = new StringBuilder();
            for (Object param : contentObject) {
                if (param instanceof String) {
                    stringBuffer.append(param);
                } else {
                    stringBuffer.append(JSON.toJSONString(param, JSONWriter.Feature.PrettyFormat));
                }
            }
            BusinessLogHolder.addContent(stringBuffer.toString());
        } catch (Exception e) {
            // ignore
        }
    }

}
