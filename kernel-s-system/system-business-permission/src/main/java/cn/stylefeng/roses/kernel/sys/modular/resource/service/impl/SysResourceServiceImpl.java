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
package cn.stylefeng.roses.kernel.sys.modular.resource.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.context.DbOperatorContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ResourceDefinition;
import cn.stylefeng.roses.kernel.scanner.api.pojo.resource.ResourceUrlParam;
import cn.stylefeng.roses.kernel.sys.modular.resource.entity.SysResource;
import cn.stylefeng.roses.kernel.sys.modular.resource.mapper.SysResourceMapper;
import cn.stylefeng.roses.kernel.sys.modular.resource.pojo.ResourceRequest;
import cn.stylefeng.roses.kernel.sys.modular.resource.service.SysResourceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资源表 服务实现类
 *
 * @author fengshuonan
 * @since 2020/11/23 22:45
 */
@Service
@Slf4j
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

    @Resource(name = "resourceCache")
    private CacheOperatorApi<ResourceDefinition> resourceCache;

    @Override
    public PageResult<SysResource> findPage(ResourceRequest resourceRequest) {
        LambdaQueryWrapper<SysResource> wrapper = createWrapper(resourceRequest);

        // 只查询有用字段
        wrapper.select(SysResource::getResourceName, SysResource::getUrl, SysResource::getClassName, SysResource::getHttpMethod,
                SysResource::getRequiredLoginFlag, SysResource::getRequiredPermissionFlag, SysResource::getIpAddress,
                BaseEntity::getCreateTime);

        Page<SysResource> page = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(page);
    }

    @Override
    public void deleteResourceByProjectCode(String projectCode) {
        LambdaQueryWrapper<SysResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysResource::getProjectCode, projectCode);
        this.remove(wrapper);
    }

    @Override
    public void batchSaveResourceList(List<SysResource> sysResourceList) {
        DbTypeEnum currentDbType = DbOperatorContext.me().getCurrentDbType();
        if (DbTypeEnum.MYSQL.equals(currentDbType)) {
            // 分批插入记录
            List<List<SysResource>> split = ListUtil.split(sysResourceList, RuleConstants.DEFAULT_BATCH_INSERT_SIZE);
            for (List<SysResource> sysResources : split) {
                this.getBaseMapper().insertBatchSomeColumn(sysResources);
            }
        } else {
            this.saveBatch(sysResourceList, sysResourceList.size());
        }
    }

    @Override
    public ResourceDefinition getResourceByUrl(ResourceUrlParam resourceUrlReq) {

        if (ObjectUtil.isEmpty(resourceUrlReq.getUrl()) || ObjectUtil.isEmpty(resourceUrlReq.getUrl())) {
            return null;
        }

        // 先从缓存中查询
        ResourceDefinition tempCachedResourceDefinition = resourceCache.get(resourceUrlReq.getUrl());
        if (tempCachedResourceDefinition != null) {
            return tempCachedResourceDefinition;
        }

        // 缓存中没有去数据库查询
        LambdaQueryWrapper<SysResource> sysResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysResourceLambdaQueryWrapper.eq(SysResource::getUrl, resourceUrlReq.getUrl());
        List<SysResource> resources = this.list(sysResourceLambdaQueryWrapper);
        if (resources == null || resources.isEmpty()) {
            return null;
        }

        // 获取接口资源信息
        SysResource resource = resources.get(0);
        ResourceDefinition resourceDefinition = new ResourceDefinition();
        BeanUtils.copyProperties(resource, resourceDefinition);

        // 获取是否需要登录的标记, 判断是否需要登录，如果是则设置为true,否则为false
        String requiredLoginFlag = resource.getRequiredLoginFlag();
        resourceDefinition.setRequiredLoginFlag(YesOrNotEnum.Y.name().equals(requiredLoginFlag));

        // 获取请求权限的标记，判断是否有权限，如果有则设置为true,否则为false
        String requiredPermissionFlag = resource.getRequiredPermissionFlag();
        resourceDefinition.setRequiredPermissionFlag(YesOrNotEnum.Y.name().equals(requiredPermissionFlag));

        // 查询结果添加到缓存
        resourceCache.put(resourceDefinition.getUrl(), resourceDefinition);

        return resourceDefinition;
    }

    /**
     * 创建wrapper
     *
     * @author fengshuonan
     * @since 2020/11/6 10:16
     */
    private LambdaQueryWrapper<SysResource> createWrapper(ResourceRequest resourceRequest) {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();

        if (ObjectUtil.isEmpty(resourceRequest)) {
            return queryWrapper;
        }

        // 根据资源名称
        String searchText = resourceRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysResource::getResourceName, searchText);
            queryWrapper.or().like(SysResource::getResourceCode, searchText);
            queryWrapper.or().like(SysResource::getUrl, searchText);
            queryWrapper.or().like(SysResource::getClassName, searchText);
        }

        return queryWrapper;
    }
}
