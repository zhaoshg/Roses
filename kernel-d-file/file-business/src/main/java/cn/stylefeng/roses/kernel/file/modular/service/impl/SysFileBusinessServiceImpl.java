package cn.stylefeng.roses.kernel.file.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.file.modular.entity.SysFileBusiness;
import cn.stylefeng.roses.kernel.file.modular.enums.SysFileBusinessExceptionEnum;
import cn.stylefeng.roses.kernel.file.modular.mapper.SysFileBusinessMapper;
import cn.stylefeng.roses.kernel.file.modular.pojo.request.SysFileBusinessRequest;
import cn.stylefeng.roses.kernel.file.modular.service.SysFileBusinessService;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务关联的文件业务实现层
 *
 * @author fengshuonan
 * @date 2023/03/31 13:30
 */
@Service
public class SysFileBusinessServiceImpl extends ServiceImpl<SysFileBusinessMapper, SysFileBusiness> implements SysFileBusinessService {

    @Override
    public void add(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = new SysFileBusiness();
        BeanUtil.copyProperties(sysFileBusinessRequest, sysFileBusiness);
        this.save(sysFileBusiness);
    }

    @Override
    public void del(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = this.querySysFileBusiness(sysFileBusinessRequest);
        this.removeById(sysFileBusiness.getFileBusinessId());
    }

    @Override
    public void edit(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = this.querySysFileBusiness(sysFileBusinessRequest);
        BeanUtil.copyProperties(sysFileBusinessRequest, sysFileBusiness);
        this.updateById(sysFileBusiness);
    }

    @Override
    public SysFileBusiness detail(SysFileBusinessRequest sysFileBusinessRequest) {
        return this.querySysFileBusiness(sysFileBusinessRequest);
    }

    @Override
    public PageResult<SysFileBusiness> findPage(SysFileBusinessRequest sysFileBusinessRequest) {
        LambdaQueryWrapper<SysFileBusiness> wrapper = createWrapper(sysFileBusinessRequest);
        Page<SysFileBusiness> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysFileBusiness> findList(SysFileBusinessRequest sysFileBusinessRequest) {
        LambdaQueryWrapper<SysFileBusiness> wrapper = this.createWrapper(sysFileBusinessRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/03/31 13:30
     */
    private SysFileBusiness querySysFileBusiness(SysFileBusinessRequest sysFileBusinessRequest) {
        SysFileBusiness sysFileBusiness = this.getById(sysFileBusinessRequest.getFileBusinessId());
        if (ObjectUtil.isEmpty(sysFileBusiness)) {
            throw new ServiceException(SysFileBusinessExceptionEnum.SYS_FILE_BUSINESS_NOT_EXISTED);
        }
        return sysFileBusiness;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/03/31 13:30
     */
    private LambdaQueryWrapper<SysFileBusiness> createWrapper(SysFileBusinessRequest sysFileBusinessRequest) {
        LambdaQueryWrapper<SysFileBusiness> queryWrapper = new LambdaQueryWrapper<>();

        Long fileBusinessId = sysFileBusinessRequest.getFileBusinessId();
        Long businessId = sysFileBusinessRequest.getBusinessId();
        Long fileId = sysFileBusinessRequest.getFileId();
        Integer downloadCount = sysFileBusinessRequest.getDownloadCount();
        Long tenantId = sysFileBusinessRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(fileBusinessId), SysFileBusiness::getFileBusinessId, fileBusinessId);
        queryWrapper.eq(ObjectUtil.isNotNull(businessId), SysFileBusiness::getBusinessId, businessId);
        queryWrapper.eq(ObjectUtil.isNotNull(fileId), SysFileBusiness::getFileId, fileId);
        queryWrapper.eq(ObjectUtil.isNotNull(downloadCount), SysFileBusiness::getDownloadCount, downloadCount);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysFileBusiness::getTenantId, tenantId);

        return queryWrapper;
    }

}