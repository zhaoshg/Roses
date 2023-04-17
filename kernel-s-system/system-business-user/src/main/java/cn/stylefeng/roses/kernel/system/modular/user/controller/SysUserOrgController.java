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
package cn.stylefeng.roses.kernel.system.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserOrgService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组织机构信息
 *
 * @author fengshuonan
 * @since 2023/4/17 17:07
 */
@RestController
@ApiResource(name = "用户组织机构信息", resBizType = ResBizTypeEnum.SYSTEM)
public class SysUserOrgController {

    @Resource
    private SysUserOrgService sysUserOrgService;

    /**
     * 获取当前登录用户所在组织机构列表
     *
     * @author fengshuonan
     * @since 2023/4/17 17:08
     */
    @GetResource(name = "获取用户所在组织机构列表", path = "/sysUserOrg/getUserOrgList", requiredPermission = false)
    public ResponseData<List<HrOrganizationDTO>> getUserOrgList() {
        List<HrOrganizationDTO> userCompanyList = sysUserOrgService.getUserCompanyList();
        return new SuccessResponseData<>(userCompanyList);
    }

}
