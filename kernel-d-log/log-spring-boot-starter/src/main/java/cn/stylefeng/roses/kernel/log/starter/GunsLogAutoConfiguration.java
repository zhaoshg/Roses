/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.log.starter;

import cn.stylefeng.roses.kernel.log.api.LogManagerApi;
import cn.stylefeng.roses.kernel.log.api.LogRecordApi;
import cn.stylefeng.roses.kernel.log.api.threadpool.LogManagerThreadPool;
import cn.stylefeng.roses.kernel.log.db.DbLogManagerServiceImpl;
import cn.stylefeng.roses.kernel.log.db.DbLogRecordServiceImpl;
import cn.stylefeng.roses.kernel.log.db.service.SysLogService;
import cn.stylefeng.roses.kernel.log.db.service.impl.SysLogServiceImpl;
import cn.stylefeng.roses.kernel.log.requestapi.RequestApiLogRecordAop;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 系统日志的自动配置
 *
 * @author fengshuonan
 * @since 2020/12/1 17:12
 */
@Configuration
public class GunsLogAutoConfiguration {

    /**
     * 系统日志service
     *
     * @author liuhanqing
     * @since 2020/12/28 22:09
     */
    @Bean
    @ConditionalOnMissingBean(SysLogService.class)
    public SysLogService sysLogService() {
        return new SysLogServiceImpl();
    }

    /**
     * 每个请求接口记录日志的AOP
     * <p>
     * 根据配置文件初始化日志记录器，采用数据库存储
     *
     * @param sysLogService 系统日志service
     * @author liuhanqing
     * @since 2020/12/20 13:02
     */
    @Bean
    public RequestApiLogRecordAop requestApiLogRecordAop(SysLogService sysLogService) {
        return new RequestApiLogRecordAop(new DbLogRecordServiceImpl(new LogManagerThreadPool(), sysLogService));
    }

    /**
     * 日志管理器
     *
     * @author liuhanqing
     * @since 2020/12/20 18:53
     */
    @Bean
    public LogManagerApi logManagerApi() {
        return new DbLogManagerServiceImpl();
    }

    /**
     * 日志记录的api
     *
     * @author fengshuonan
     * @since 2021/3/4 22:16
     */
    @Bean
    public LogRecordApi logRecordApi(SysLogService sysLogService) {
        return new DbLogRecordServiceImpl(new LogManagerThreadPool(), sysLogService);
    }

}
