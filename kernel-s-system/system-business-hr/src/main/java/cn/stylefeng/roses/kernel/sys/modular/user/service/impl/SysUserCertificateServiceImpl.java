package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserCertificate;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserCertificateExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserCertificateMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserCertificateRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserCertificateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户证书业务实现层
 *
 * @author fengshuonan
 * @since 2023/11/09 22:44
 */
@Service
public class SysUserCertificateServiceImpl extends ServiceImpl<SysUserCertificateMapper, SysUserCertificate> implements SysUserCertificateService {

	@Override
    public void add(SysUserCertificateRequest sysUserCertificateRequest) {
        SysUserCertificate sysUserCertificate = new SysUserCertificate();
        BeanUtil.copyProperties(sysUserCertificateRequest, sysUserCertificate);
        this.save(sysUserCertificate);
    }

    @Override
    public void del(SysUserCertificateRequest sysUserCertificateRequest) {
        SysUserCertificate sysUserCertificate = this.querySysUserCertificate(sysUserCertificateRequest);
        this.removeById(sysUserCertificate.getUserCertificateId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysUserCertificateRequest sysUserCertificateRequest) {
        this.removeByIds(sysUserCertificateRequest.getBatchDeleteIdList());
    }

    @Override
    public void edit(SysUserCertificateRequest sysUserCertificateRequest) {
        SysUserCertificate sysUserCertificate = this.querySysUserCertificate(sysUserCertificateRequest);
        BeanUtil.copyProperties(sysUserCertificateRequest, sysUserCertificate);
        this.updateById(sysUserCertificate);
    }

    @Override
    public SysUserCertificate detail(SysUserCertificateRequest sysUserCertificateRequest) {
        return this.querySysUserCertificate(sysUserCertificateRequest);
    }

    @Override
    public PageResult<SysUserCertificate> findPage(SysUserCertificateRequest sysUserCertificateRequest) {
        LambdaQueryWrapper<SysUserCertificate> wrapper = createWrapper(sysUserCertificateRequest);
        Page<SysUserCertificate> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    public List<SysUserCertificate> findList(SysUserCertificateRequest sysUserCertificateRequest) {
        LambdaQueryWrapper<SysUserCertificate> wrapper = this.createWrapper(sysUserCertificateRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    private SysUserCertificate querySysUserCertificate(SysUserCertificateRequest sysUserCertificateRequest) {
        SysUserCertificate sysUserCertificate = this.getById(sysUserCertificateRequest.getUserCertificateId());
        if (ObjectUtil.isEmpty(sysUserCertificate)) {
            throw new ServiceException(SysUserCertificateExceptionEnum.SYS_USER_CERTIFICATE_NOT_EXISTED);
        }
        return sysUserCertificate;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    private LambdaQueryWrapper<SysUserCertificate> createWrapper(SysUserCertificateRequest sysUserCertificateRequest) {
        LambdaQueryWrapper<SysUserCertificate> queryWrapper = new LambdaQueryWrapper<>();

        Long userCertificateId = sysUserCertificateRequest.getUserCertificateId();
        Long userId = sysUserCertificateRequest.getUserId();
        Long certificateType = sysUserCertificateRequest.getCertificateType();
        String certificateNo = sysUserCertificateRequest.getCertificateNo();
        String issuingAuthority = sysUserCertificateRequest.getIssuingAuthority();
        String dateIssued = sysUserCertificateRequest.getDateIssued();
        String dateExpires = sysUserCertificateRequest.getDateExpires();
        String delFlag = sysUserCertificateRequest.getDelFlag();

        queryWrapper.eq(ObjectUtil.isNotNull(userCertificateId), SysUserCertificate::getUserCertificateId, userCertificateId);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysUserCertificate::getUserId, userId);
        queryWrapper.eq(ObjectUtil.isNotNull(certificateType), SysUserCertificate::getCertificateType, certificateType);
        queryWrapper.like(ObjectUtil.isNotEmpty(certificateNo), SysUserCertificate::getCertificateNo, certificateNo);
        queryWrapper.like(ObjectUtil.isNotEmpty(issuingAuthority), SysUserCertificate::getIssuingAuthority, issuingAuthority);
        queryWrapper.eq(ObjectUtil.isNotNull(dateIssued), SysUserCertificate::getDateIssued, dateIssued);
        queryWrapper.eq(ObjectUtil.isNotNull(dateExpires), SysUserCertificate::getDateExpires, dateExpires);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), SysUserCertificate::getDelFlag, delFlag);

        return queryWrapper;
    }

}
