package cn.stylefeng.roses.kernel.event.sdk.pojo;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * 业务事件监听的详情封装
 *
 * @author fengshuonan
 * @since 2023/7/14 15:18
 */
@Data
public class BusinessListenerDetail {

    /**
     * Spring的Bean名称
     */
    private String beanName;

    /**
     * 业务监听器对应注解的类方法
     */
    private Method listenerMethod;

}
