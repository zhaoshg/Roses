package cn.stylefeng.roses.kernel.event.sdk.publish;

import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.event.sdk.container.EventContainer;
import cn.stylefeng.roses.kernel.event.sdk.pojo.BusinessListenerDetail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BusinessEventPublisher {

    /**
     * 发布一个业务事件
     *
     * @author fengshuonan
     * @since 2023/7/14 15:37
     */
    public static <T> void publishEvent(String businessCode, T businessObject) {

        List<BusinessListenerDetail> listener = EventContainer.getListener(businessCode);

        for (BusinessListenerDetail businessListenerDetail : listener) {

            String beanName = businessListenerDetail.getBeanName();

            Method listenerMethod = businessListenerDetail.getListenerMethod();

            Object bean = SpringUtil.getBean(beanName);

            try {
                listenerMethod.invoke(bean, businessObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }


    }

}
