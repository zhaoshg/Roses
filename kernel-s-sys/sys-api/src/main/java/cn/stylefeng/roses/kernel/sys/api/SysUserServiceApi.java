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
package cn.stylefeng.roses.kernel.sys.api;

import cn.stylefeng.roses.kernel.sys.api.pojo.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.UserOrgDTO;

import java.util.List;

/**
 * 基础核心业务Api
 *
 * @author fengshuonan
 * @date 2023-06-10 20:50:43
 */
public interface SysUserServiceApi {

    /**
     * 获取用户的基本信息
     *
     * @author fengshuonan
     * @since 2023/6/11 21:07
     */
    SimpleUserDTO getUserInfoByUserId(Long userId);

    /**
     * 获取用户的主要任职信息
     * <p>
     * 返回一条结果，只返回主部门的信息
     *
     * @author fengshuonan
     * @since 2023/6/11 21:07
     */
    UserOrgDTO getUserMainOrgInfo(Long userId);

    /**
     * 获取用户绑定的组织机构列表，主要任职部门和次要任职部门都返回
     *
     * @author fengshuonan
     * @since 2023/6/11 21:08
     */
    List<UserOrgDTO> getUserOrgList(Long userId);

    /**
     * 获取某个机构下的所有用户id集合
     *
     * @param orgId             组织机构id
     * @param containSubOrgFlag 是否包含指定机构的子集机构，true-包含子集，false-不包含
     * @author fengshuonan
     * @since 2023/6/11 21:46
     */
    List<Long> getOrgUserIdList(Long orgId, Boolean containSubOrgFlag);

    /**
     * 获取用户的角色id列表
     *
     * @author fengshuonan
     * @since 2023/6/12 11:29
     */
    List<Long> getUserRoleIdList(Long userId);

    /**
     * 获取用户真实姓名
     *
     * @author fengshuonan
     * @since 2023/6/16 22:26
     */
    String getUserRealName(Long userId);

}
