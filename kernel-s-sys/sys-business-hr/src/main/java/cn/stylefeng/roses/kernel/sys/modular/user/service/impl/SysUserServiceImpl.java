package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统用户业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

	@Override
    public void add(SysUserRequest sysUserRequest) {
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserRequest, sysUser);
        this.save(sysUser);
    }

    @Override
    public void del(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);
        this.removeById(sysUser.getUserId());
    }

    @Override
    public void edit(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);
        BeanUtil.copyProperties(sysUserRequest, sysUser);
        this.updateById(sysUser);
    }

    @Override
    public SysUser detail(SysUserRequest sysUserRequest) {
        return this.querySysUser(sysUserRequest);
    }

    @Override
    public PageResult<SysUser> findPage(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> wrapper = createWrapper(sysUserRequest);
        Page<SysUser> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysUser> findList(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> wrapper = this.createWrapper(sysUserRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private SysUser querySysUser(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.getById(sysUserRequest.getUserId());
        if (ObjectUtil.isEmpty(sysUser)) {
            throw new ServiceException(SysUserExceptionEnum.SYS_USER_NOT_EXISTED);
        }
        return sysUser;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:26
     */
    private LambdaQueryWrapper<SysUser> createWrapper(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        Long userId = sysUserRequest.getUserId();
        String realName = sysUserRequest.getRealName();
        String nickName = sysUserRequest.getNickName();
        String account = sysUserRequest.getAccount();
        String password = sysUserRequest.getPassword();
        Long avatar = sysUserRequest.getAvatar();
        String birthday = sysUserRequest.getBirthday();
        String sex = sysUserRequest.getSex();
        String email = sysUserRequest.getEmail();
        String phone = sysUserRequest.getPhone();
        String tel = sysUserRequest.getTel();
        String superAdminFlag = sysUserRequest.getSuperAdminFlag();
        Integer statusFlag = sysUserRequest.getStatusFlag();
        Integer loginCount = sysUserRequest.getLoginCount();
        String lastLoginIp = sysUserRequest.getLastLoginIp();
        String lastLoginTime = sysUserRequest.getLastLoginTime();
        String masterUserId = sysUserRequest.getMasterUserId();
        String expandField = sysUserRequest.getExpandField();
        Long versionFlag = sysUserRequest.getVersionFlag();
        String delFlag = sysUserRequest.getDelFlag();
        Long tenantId = sysUserRequest.getTenantId();

        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysUser::getUserId, userId);
        queryWrapper.like(ObjectUtil.isNotEmpty(realName), SysUser::getRealName, realName);
        queryWrapper.like(ObjectUtil.isNotEmpty(nickName), SysUser::getNickName, nickName);
        queryWrapper.like(ObjectUtil.isNotEmpty(account), SysUser::getAccount, account);
        queryWrapper.like(ObjectUtil.isNotEmpty(password), SysUser::getPassword, password);
        queryWrapper.eq(ObjectUtil.isNotNull(avatar), SysUser::getAvatar, avatar);
        queryWrapper.eq(ObjectUtil.isNotNull(birthday), SysUser::getBirthday, birthday);
        queryWrapper.like(ObjectUtil.isNotEmpty(sex), SysUser::getSex, sex);
        queryWrapper.like(ObjectUtil.isNotEmpty(email), SysUser::getEmail, email);
        queryWrapper.like(ObjectUtil.isNotEmpty(phone), SysUser::getPhone, phone);
        queryWrapper.like(ObjectUtil.isNotEmpty(tel), SysUser::getTel, tel);
        queryWrapper.like(ObjectUtil.isNotEmpty(superAdminFlag), SysUser::getSuperAdminFlag, superAdminFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(statusFlag), SysUser::getStatusFlag, statusFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(loginCount), SysUser::getLoginCount, loginCount);
        queryWrapper.like(ObjectUtil.isNotEmpty(lastLoginIp), SysUser::getLastLoginIp, lastLoginIp);
        queryWrapper.eq(ObjectUtil.isNotNull(lastLoginTime), SysUser::getLastLoginTime, lastLoginTime);
        queryWrapper.like(ObjectUtil.isNotEmpty(masterUserId), SysUser::getMasterUserId, masterUserId);
        queryWrapper.like(ObjectUtil.isNotEmpty(expandField), SysUser::getExpandField, expandField);
        queryWrapper.eq(ObjectUtil.isNotNull(versionFlag), SysUser::getVersionFlag, versionFlag);
        queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), SysUser::getDelFlag, delFlag);
        queryWrapper.eq(ObjectUtil.isNotNull(tenantId), SysUser::getTenantId, tenantId);

        return queryWrapper;
    }

}