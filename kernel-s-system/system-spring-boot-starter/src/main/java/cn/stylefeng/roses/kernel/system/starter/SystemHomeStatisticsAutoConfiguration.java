package cn.stylefeng.roses.kernel.system.starter;

import cn.stylefeng.roses.kernel.system.modular.home.aop.InterfaceStatisticsAop;
import cn.stylefeng.roses.kernel.system.modular.home.timer.InterfaceStatisticsTimer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 首页统计数据的装载
 *
 * @author xixiaowei
 * @since 2022/2/9 17:57
 */
@Configuration
public class SystemHomeStatisticsAutoConfiguration {

    /**
     * 接口统计的AOP
     *
     * @author xixiaowei
     * @since 2022/2/9 14:00
     */
    @Bean
    public InterfaceStatisticsAop interfaceStatisticsAspect() {
        return new InterfaceStatisticsAop();
    }

    /**
     * 定时将统计数据存入数据库
     *
     * @author xixiaowei
     * @since 2022/2/9 17:58
     */
    @Bean
    public InterfaceStatisticsTimer interfaceStatisticsHolder() {
        return new InterfaceStatisticsTimer();
    }

}
