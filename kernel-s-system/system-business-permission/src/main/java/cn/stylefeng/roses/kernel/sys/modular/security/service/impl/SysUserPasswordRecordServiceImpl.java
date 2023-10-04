package cn.stylefeng.roses.kernel.sys.modular.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.security.entity.SysUserPasswordRecord;
import cn.stylefeng.roses.kernel.sys.modular.security.enums.SysUserPasswordRecordExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.security.mapper.SysUserPasswordRecordMapper;
import cn.stylefeng.roses.kernel.sys.modular.security.pojo.request.SysUserPasswordRecordRequest;
import cn.stylefeng.roses.kernel.sys.modular.security.service.SysUserPasswordRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户历史密码记录业务实现层
 *
 * @author fengshuonan
 * @date 2023/10/04 23:28
 */
@Service
public class SysUserPasswordRecordServiceImpl extends ServiceImpl<SysUserPasswordRecordMapper, SysUserPasswordRecord> implements SysUserPasswordRecordService {

	@Override
    public void add(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        SysUserPasswordRecord sysUserPasswordRecord = new SysUserPasswordRecord();
        BeanUtil.copyProperties(sysUserPasswordRecordRequest, sysUserPasswordRecord);
        this.save(sysUserPasswordRecord);
    }

    @Override
    public void del(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        SysUserPasswordRecord sysUserPasswordRecord = this.querySysUserPasswordRecord(sysUserPasswordRecordRequest);
        this.removeById(sysUserPasswordRecord.getRecordId());
    }

    @Override
    public void edit(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        SysUserPasswordRecord sysUserPasswordRecord = this.querySysUserPasswordRecord(sysUserPasswordRecordRequest);
        BeanUtil.copyProperties(sysUserPasswordRecordRequest, sysUserPasswordRecord);
        this.updateById(sysUserPasswordRecord);
    }

    @Override
    public SysUserPasswordRecord detail(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        return this.querySysUserPasswordRecord(sysUserPasswordRecordRequest);
    }

    @Override
    public PageResult<SysUserPasswordRecord> findPage(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        LambdaQueryWrapper<SysUserPasswordRecord> wrapper = createWrapper(sysUserPasswordRecordRequest);
        Page<SysUserPasswordRecord> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    public List<SysUserPasswordRecord> findList(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        LambdaQueryWrapper<SysUserPasswordRecord> wrapper = this.createWrapper(sysUserPasswordRecordRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    private SysUserPasswordRecord querySysUserPasswordRecord(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        SysUserPasswordRecord sysUserPasswordRecord = this.getById(sysUserPasswordRecordRequest.getRecordId());
        if (ObjectUtil.isEmpty(sysUserPasswordRecord)) {
            throw new ServiceException(SysUserPasswordRecordExceptionEnum.SYS_USER_PASSWORD_RECORD_NOT_EXISTED);
        }
        return sysUserPasswordRecord;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/10/04 23:28
     */
    private LambdaQueryWrapper<SysUserPasswordRecord> createWrapper(SysUserPasswordRecordRequest sysUserPasswordRecordRequest) {
        LambdaQueryWrapper<SysUserPasswordRecord> queryWrapper = new LambdaQueryWrapper<>();

        Long recordId = sysUserPasswordRecordRequest.getRecordId();
        Long userId = sysUserPasswordRecordRequest.getUserId();
        String historyPassword = sysUserPasswordRecordRequest.getHistoryPassword();
        String historyPasswordSalt = sysUserPasswordRecordRequest.getHistoryPasswordSalt();
        String updatePasswordTime = sysUserPasswordRecordRequest.getUpdatePasswordTime();

        queryWrapper.eq(ObjectUtil.isNotNull(recordId), SysUserPasswordRecord::getRecordId, recordId);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysUserPasswordRecord::getUserId, userId);
        queryWrapper.like(ObjectUtil.isNotEmpty(historyPassword), SysUserPasswordRecord::getHistoryPassword, historyPassword);
        queryWrapper.like(ObjectUtil.isNotEmpty(historyPasswordSalt), SysUserPasswordRecord::getHistoryPasswordSalt, historyPasswordSalt);
        queryWrapper.eq(ObjectUtil.isNotNull(updatePasswordTime), SysUserPasswordRecord::getUpdatePasswordTime, updatePasswordTime);

        return queryWrapper;
    }

}
