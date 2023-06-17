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
package cn.stylefeng.roses.kernel.system.modular.position.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseExpandFieldEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.system.api.UserOrgServiceApi;
import cn.stylefeng.roses.kernel.system.api.exception.SystemModularException;
import cn.stylefeng.roses.kernel.system.api.exception.enums.organization.PositionExceptionEnum;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrPositionDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrPositionRequest;
import cn.stylefeng.roses.kernel.system.modular.organization.service.HrOrganizationService;
import cn.stylefeng.roses.kernel.system.modular.position.entity.HrPosition;
import cn.stylefeng.roses.kernel.system.modular.position.mapper.HrPositionMapper;
import cn.stylefeng.roses.kernel.system.modular.position.pojo.DutyItem;
import cn.stylefeng.roses.kernel.system.modular.position.pojo.ExpandDutyInfo;
import cn.stylefeng.roses.kernel.system.modular.position.service.HrPositionService;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统职位表 服务实现类
 *
 * @author fengshuonan
 * @since 2020/11/04 11:07
 */
@Service
public class HrPositionServiceImpl extends ServiceImpl<HrPositionMapper, HrPosition> implements HrPositionService {

    @Resource
    private UserOrgServiceApi userOrgServiceApi;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private HrOrganizationService hrOrganizationService;

    @Override
    public void add(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = new HrPosition();
        BeanUtil.copyProperties(hrPositionRequest, sysPosition);

        // 设置状态为启用
        sysPosition.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(sysPosition);
    }

    @Override
    public void del(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = this.querySysPositionById(hrPositionRequest);

        // 该职位下是否有员工，如果有将不能删除
        Boolean userOrgFlag = userOrgServiceApi.getUserOrgFlag(null, sysPosition.getPositionId());
        if (userOrgFlag) {
            throw new SystemModularException(PositionExceptionEnum.CANT_DELETE_POSITION);
        }

        // 逻辑删除
        sysPosition.setDelFlag(YesOrNotEnum.Y.getCode());
        this.updateById(sysPosition);
    }

    @Override
    public void edit(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = this.querySysPositionById(hrPositionRequest);
        BeanUtil.copyProperties(hrPositionRequest, sysPosition);
        this.updateById(sysPosition);
    }

    @Override
    public void changeStatus(HrPositionRequest hrPositionRequest) {
        HrPosition sysPosition = this.querySysPositionById(hrPositionRequest);
        sysPosition.setStatusFlag(hrPositionRequest.getStatusFlag());
        this.updateById(sysPosition);
    }

    @Override
    public HrPosition detail(HrPositionRequest hrPositionRequest) {
        return this.querySysPositionById(hrPositionRequest);
    }

    @Override
    public List<HrPosition> findList(HrPositionRequest hrPositionRequest) {
        return this.list(this.createWrapper(hrPositionRequest));
    }

    @Override
    public PageResult<HrPosition> findPage(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> wrapper = this.createWrapper(hrPositionRequest);

        Page<HrPosition> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public void batchDel(HrPositionRequest hrPositionRequest) {
        List<Long> positionIds = hrPositionRequest.getPositionIds();
        for (Long userId : positionIds) {
            HrPositionRequest tempRequest = new HrPositionRequest();
            tempRequest.setPositionId(userId);
            this.del(tempRequest);
        }
    }

    @Override
    public void fillDutyInfo(Long userId, List<HrOrganizationDTO> results) {

        // 获取用户拓展字段中的职务信息
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(BaseExpandFieldEntity::getExpandField);
        SysUser currentUser = this.sysUserService.getOne(sysUserLambdaQueryWrapper, false);

        if (currentUser == null) {
            return;
        }

        Map<String, Object> expandField = currentUser.getExpandField();

        if (ObjectUtil.isEmpty(expandField)) {
            return;
        }

        // 获取职务的信息包装
        ExpandDutyInfo expandDutyInfo = BeanUtil.mapToBean(expandField, ExpandDutyInfo.class, false, CopyOptions.create().ignoreError());

        // 多组织的职务列表
        List<DutyItem> ptDuty = expandDutyInfo.getPtDuty();

        // 如果多组织列表为空，则直接返回默认的职务名称
        if (ObjectUtil.isEmpty(ptDuty)) {
            for (HrOrganizationDTO result : results) {
                if (ObjectUtil.isEmpty(result.getPositionName())) {
                    result.setPositionName(expandDutyInfo.getDuty());
                }
            }
        }

        // 多组织的职务列表不为空，则获取职务的masterOrgId对应的公司id
        for (DutyItem dutyItem : ptDuty) {

            String masterOrgId = dutyItem.getOrgId();
            Long masterOrgIdCompanyId = this.hrOrganizationService.getMasterOrgIdCompanyId(masterOrgId);
            if (masterOrgIdCompanyId == null) {
                continue;
            }

            // 遍历参数的组织机构列表，填充职务名称
            for (HrOrganizationDTO result : results) {
                if (result.getOrgId().equals(masterOrgIdCompanyId)) {
                    if (ObjectUtil.isEmpty(result.getPositionName())) {
                        result.setPositionName(dutyItem.getDutyName());
                    }
                }
            }
        }
    }

    @Override
    public String getPositionName(Long positionId) {
        if (positionId == null) {
            return null;
        }
        LambdaQueryWrapper<HrPosition> hrPositionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrPositionLambdaQueryWrapper.eq(HrPosition::getPositionId, positionId);
        hrPositionLambdaQueryWrapper.select(HrPosition::getPositionName);
        HrPosition one = this.getOne(hrPositionLambdaQueryWrapper, false);
        if (one == null) {
            return null;
        } else {
            return one.getPositionName();
        }
    }

    @Override
    public Integer positionNum() {
        return Convert.toInt(this.count());
    }

    @Override
    public HrPositionDTO getPositionDetail(Long positionId) {
        HrPositionDTO hrPositionDTO = new HrPositionDTO();
        HrPositionRequest request = new HrPositionRequest();
        request.setPositionId(positionId);
        HrPosition detail = this.detail(request);
        if (ObjectUtil.isNotNull(detail)) {
            BeanUtil.copyProperties(detail, hrPositionDTO, CopyOptions.create().ignoreError());
        }
        return hrPositionDTO;
    }

    @Override
    public List<HrPositionDTO> getPositionDetailList(List<Long> positionIdList) {
        ArrayList<HrPositionDTO> hrPositionDTOS = new ArrayList<>();
        for (Long positionId : positionIdList) {
            HrPositionDTO positionDetail = this.getPositionDetail(positionId);
            hrPositionDTOS.add(positionDetail);
        }
        return hrPositionDTOS;
    }

    /**
     * 根据主键id获取对象信息
     *
     * @return 实体对象
     * @author chenjinlong
     * @since 2021/2/2 10:16
     */
    private HrPosition querySysPositionById(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = this.getById(hrPositionRequest.getPositionId());
        if (ObjectUtil.isEmpty(hrPosition) || YesOrNotEnum.Y.getCode().equals(hrPosition.getDelFlag())) {
            throw new SystemModularException(PositionExceptionEnum.CANT_FIND_POSITION, hrPositionRequest.getPositionId());
        }
        return hrPosition;
    }


    /**
     * 实体构建 QueryWrapper
     *
     * @author chenjinlong
     * @since 2021/2/2 10:17
     */
    private LambdaQueryWrapper<HrPosition> createWrapper(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> queryWrapper = new LambdaQueryWrapper<>();

        Long positionId = hrPositionRequest.getPositionId();
        String positionName = hrPositionRequest.getPositionName();
        String positionCode = hrPositionRequest.getPositionCode();
        Integer statusFlag = hrPositionRequest.getStatusFlag();

        // SQL条件拼接
        queryWrapper.eq(ObjectUtil.isNotNull(positionId), HrPosition::getPositionId, positionId);
        queryWrapper.like(StrUtil.isNotEmpty(positionName), HrPosition::getPositionName, positionName);
        queryWrapper.eq(StrUtil.isNotEmpty(positionCode), HrPosition::getPositionCode, positionCode);
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), HrPosition::getStatusFlag, statusFlag);

        // 查询未删除状态的
        queryWrapper.eq(HrPosition::getDelFlag, YesOrNotEnum.N.getCode());

        // 根据排序升序排列
        queryWrapper.orderByAsc(HrPosition::getPositionSort);

        return queryWrapper;
    }

}
