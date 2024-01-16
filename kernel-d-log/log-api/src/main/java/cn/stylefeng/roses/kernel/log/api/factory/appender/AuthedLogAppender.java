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
package cn.stylefeng.roses.kernel.log.api.factory.appender;

import cn.stylefeng.roses.kernel.log.api.pojo.record.LogRecordDTO;

/**
 * 日志信息追加，用来追加用户的登录信息
 *
 * @author fengshuonan
 * @since 2020/10/27 17:45
 */
public class AuthedLogAppender {

    /**
     * 填充token和userId字段
     * <p>
     * 但是此方法会依赖auth-api模块，所以用这个方法得引入auth模块
     *
     * @author fengshuonan
     * @since 2020/10/27 18:22
     */
    public static void appendAuthedHttpLog(LogRecordDTO logRecordDTO, String token, Long userId, Long orgId) {

        // 填充当前登录的用户信息
        try {
            // 填充登录用户的token
            logRecordDTO.setToken(token);

            // 填充登录用户的userId
            logRecordDTO.setUserId(userId);

            // 填充用户登录的机构id
            logRecordDTO.setUserCurrentOrgId(orgId);

        } catch (Exception ignored) {
            // 获取不到用户登录信息，就不填充
        }

    }

}
