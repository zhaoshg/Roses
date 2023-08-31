package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.password.PasswordStoredEncryptApi;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.auth.api.pojo.password.SaltedEncryptResult;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantIdHolder;
import cn.stylefeng.roses.kernel.db.mp.tenant.holder.TenantSwitchHolder;
import cn.stylefeng.roses.kernel.file.api.FileInfoApi;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.callback.RemoveUserCallbackApi;
import cn.stylefeng.roses.kernel.sys.api.constants.SysConstants;
import cn.stylefeng.roses.kernel.sys.api.context.DataScopeContext;
import cn.stylefeng.roses.kernel.sys.api.enums.user.UserStatusEnum;
import cn.stylefeng.roses.kernel.sys.api.exception.enums.UserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.api.expander.SysConfigExpander;
import cn.stylefeng.roses.kernel.sys.api.pojo.user.*;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.sys.modular.user.enums.SysUserExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.user.factory.SysUserCreateFactory;
import cn.stylefeng.roses.kernel.sys.modular.user.mapper.SysUserMapper;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserRequest;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.response.PersonalInfo;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserOrgService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private PasswordStoredEncryptApi passwordStoredEncryptApi;

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private FileInfoApi fileInfoApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserRequest sysUserRequest) {
        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(sysUserRequest, sysUser);

        // 将密码加密存储到库中
        SaltedEncryptResult saltedEncryptResult = passwordStoredEncryptApi.encryptWithSalt(sysUser.getPassword());
        sysUser.setPassword(saltedEncryptResult.getEncryptPassword());
        sysUser.setPasswordSalt(saltedEncryptResult.getPasswordSalt());

        // 设置用户默认头像
        sysUser.setAvatar(FileConstants.DEFAULT_AVATAR_FILE_ID);

        this.save(sysUser);

        // 更新用户的任职信息
        sysUserOrgService.updateUserOrg(sysUser.getUserId(), sysUserRequest.getUserOrgList());

        // 添加用户一个默认角色
        sysUserRoleService.bindUserDefaultRole(sysUser.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 不能删除超级管理员
        if (YesOrNotEnum.Y.getCode().equals(sysUser.getSuperAdminFlag())) {
            throw new ServiceException(SysUserExceptionEnum.USER_CAN_NOT_DELETE_ADMIN);
        }

        // 删除用户的业务操作
        this.baseRemoveUser(CollectionUtil.set(false, sysUser.getUserId()));
    }

    @Override
    public void batchDel(SysUserRequest sysUserRequest) {

        // 判断被删除用户是否有管理员
        Set<Long> userIdList = sysUserRequest.getUserIdList();

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.in(SysUser::getUserId, userIdList);
        sysUserLambdaQueryWrapper.eq(SysUser::getSuperAdminFlag, YesOrNotEnum.Y.getCode());
        long adminCount = this.count(sysUserLambdaQueryWrapper);
        if (adminCount > 0) {
            throw new ServiceException(SysUserExceptionEnum.USER_CAN_NOT_DELETE_ADMIN);
        }

        // 删除用户的业务操作
        this.baseRemoveUser(userIdList);
    }

    @Override
    public void edit(SysUserRequest sysUserRequest) {
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 不能修改admin账号的超级管理员标识和账号
        if (SysConstants.ADMIN_USER_ACCOUNT.equals(sysUser.getAccount())) {
            if (!sysUser.getAccount().equals(sysUserRequest.getAccount())) {
                throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_ADMIN_ACCOUNT);
            }
            if (YesOrNotEnum.N.getCode().equals(sysUserRequest.getSuperAdminFlag())) {
                throw new ServiceException(SysUserExceptionEnum.CANT_CHANGE_ADMIN_FLAG);
            }
        }

        // copy属性
        BeanUtil.copyProperties(sysUserRequest, sysUser);

        // 编辑不能修改密码
        sysUser.setPassword(null);

        // 更新用户详情信息
        this.updateById(sysUser);

        // 更新用户的任职信息
        sysUserOrgService.updateUserOrg(sysUser.getUserId(), sysUserRequest.getUserOrgList());
    }

    @Override
    public SysUser detail(SysUserRequest sysUserRequest) {

        // 查询用户个人信息
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, sysUserRequest.getUserId());
        sysUserLambdaQueryWrapper.select(SysUser::getUserId, SysUser::getAvatar, SysUser::getAccount, SysUser::getUserSort,
                SysUser::getSuperAdminFlag, SysUser::getRealName, SysUser::getSex, SysUser::getBirthday, SysUser::getEmail,
                SysUser::getPhone, SysUser::getLastLoginIp, SysUser::getLoginCount, SysUser::getLastLoginTime, SysUser::getStatusFlag,
                BaseEntity::getCreateTime, BaseEntity::getUpdateTime);
        SysUser sysUser = this.getOne(sysUserLambdaQueryWrapper, false);

        // 获取用户的组织机构信息
        List<UserOrgDTO> userOrgList = sysUserOrgService.getUserOrgList(sysUser.getUserId());
        sysUser.setUserOrgDTOList(userOrgList);

        // 获取用户的角色信息
        List<Long> userRoleIdList = sysUserRoleService.getUserRoleIdList(sysUser.getUserId());
        sysUser.setRoleIdList(userRoleIdList);

        return sysUser;
    }

    @Override
    public List<SysUser> findList(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> wrapper = this.createWrapper(sysUserRequest);
        return this.list(wrapper);
    }

    @Override
    public PageResult<SysUser> findPage(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> wrapper = createWrapper(sysUserRequest);

        // 只查询需要的字段
        wrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getAccount, SysUser::getSex, SysUser::getStatusFlag,
                BaseEntity::getCreateTime);

        // 分页查询
        Page<SysUser> sysUserPage = this.page(PageFactory.defaultPage(), wrapper);

        // 遍历查询结果，增加对用户部门信息的返回
        for (SysUser record : sysUserPage.getRecords()) {
            record.setUserOrgDTO(sysUserOrgService.getUserMainOrgInfo(record.getUserId()));
        }

        return PageResultFactory.createPageResult(sysUserPage);
    }

    @Override
    public void updateStatus(SysUserRequest sysUserRequest) {

        // 如果是将用户禁用，检测判断不能禁用超级管理员用户
        boolean userSuperAdminFlag = this.getUserSuperAdminFlag(sysUserRequest.getUserId());
        if (userSuperAdminFlag) {
            throw new ServiceException(SysUserExceptionEnum.CANT_UPDATE_STATUS);
        }

        // 校验状态传值是否正确
        Integer statusFlag = sysUserRequest.getStatusFlag();
        UserStatusEnum.validateUserStatus(statusFlag);

        // 更新用户状态
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUser::getStatusFlag, sysUserRequest.getStatusFlag());
        updateWrapper.eq(SysUser::getUserId, sysUserRequest.getUserId());
        this.update(updateWrapper);

    }

    @Override
    public void resetPassword(SysUserRequest sysUserRequest) {

        // 只有超级管理员能重置密码
        if (!LoginContext.me().getSuperAdminFlag()) {
            throw new ServiceException(UserExceptionEnum.RESET_PASSWORD_ERROR);
        }

        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 获取系统配置的默认密码
        String password = SysConfigExpander.getDefaultPassWord();

        // 密码加密后，存储到数据库中
        SaltedEncryptResult saltedEncryptResult = passwordStoredEncryptApi.encryptWithSalt(password);
        sysUser.setPassword(saltedEncryptResult.getEncryptPassword());
        sysUser.setPasswordSalt(saltedEncryptResult.getPasswordSalt());

        this.updateById(sysUser);
    }

    @Override
    public PersonalInfo getPersonalInfo() {

        // 获取当前登录用户id
        Long userId = LoginContext.me().getLoginUser().getUserId();

        // 查询用户的详细信息
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getRealName, SysUser::getAccount, SysUser::getAvatar, SysUser::getEmail,
                SysUser::getPhone, SysUser::getSex, SysUser::getBirthday);
        SysUser sysUser = this.getOne(sysUserLambdaQueryWrapper, false);

        if (sysUser == null) {
            return new PersonalInfo();
        }

        PersonalInfo personalInfo = new PersonalInfo();
        BeanUtil.copyProperties(sysUser, personalInfo);
        return personalInfo;
    }

    @Override
    public void editInfo(SysUserRequest sysUserRequest) {

        // 获取当前登录用户的id
        sysUserRequest.setUserId(LoginContext.me().getLoginUser().getUserId());
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 填充更新用户的信息
        SysUserCreateFactory.fillUpdateInfo(sysUserRequest, sysUser);

        this.updateById(sysUser);
    }

    @Override
    public void editPassword(SysUserRequest sysUserRequest) {

        // 新密码与原密码相同
        if (sysUserRequest.getNewPassword().equals(sysUserRequest.getPassword())) {
            throw new ServiceException(SysUserExceptionEnum.USER_PWD_REPEAT);
        }

        // 获取当前用户的userId
        LoginUser loginUser = LoginContext.me().getLoginUser();
        sysUserRequest.setUserId(loginUser.getUserId());
        SysUser sysUser = this.querySysUser(sysUserRequest);

        // 原密码错误
        if (!passwordStoredEncryptApi.checkPasswordWithSalt(sysUserRequest.getPassword(), sysUser.getPasswordSalt(),
                sysUser.getPassword())) {
            throw new ServiceException(SysUserExceptionEnum.USER_PWD_ERROR);
        }

        // 设置新的加密后密码和盐
        SaltedEncryptResult saltedEncryptResult = passwordStoredEncryptApi.encryptWithSalt(sysUserRequest.getNewPassword());
        sysUser.setPassword(saltedEncryptResult.getEncryptPassword());
        sysUser.setPasswordSalt(saltedEncryptResult.getPasswordSalt());

        this.updateById(sysUser);
    }

    @Override
    public void editAvatar(SysUserRequest sysUserRequest) {
        // 新头像文件id
        Long fileId = sysUserRequest.getAvatar();

        // 从当前用户获取用户id
        LoginUser loginUser = LoginContext.me().getLoginUser();

        // 更新用户头像
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysUser::getUserId, loginUser.getUserId());
        wrapper.set(SysUser::getAvatar, fileId);
        this.update(wrapper);
    }

    @Override
    public SimpleUserDTO getUserInfoByUserId(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return null;
        }

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getRealName, SysUser::getAvatar, SysUser::getAccount);
        SysUser sysUser = this.getOne(sysUserLambdaQueryWrapper);
        if (sysUser == null) {
            return null;
        }

        SimpleUserDTO simpleUserDTO = new SimpleUserDTO();
        simpleUserDTO.setUserId(userId);
        simpleUserDTO.setRealName(sysUser.getRealName());
        simpleUserDTO.setAccount(sysUser.getAccount());

        // 获取头像文件id信息，转化为头像URL
        Long avatarFileId = sysUser.getAvatar();
        if (avatarFileId == null) {
            return null;
        }

        // 获取头像的访问地址
        String fileAuthUrl = fileInfoApi.getFileAuthUrl(avatarFileId);
        simpleUserDTO.setAvatarUrl(fileAuthUrl);

        return simpleUserDTO;
    }

    @Override
    public String getUserRealName(Long userId) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.select(SysUser::getRealName);
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        SysUser sysUser = this.getOne(sysUserLambdaQueryWrapper);
        if (sysUser == null) {
            return "";
        }
        return sysUser.getRealName();
    }

    @Override
    public UserValidateDTO getUserLoginValidateDTO(Long tenantId, String account) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getAccount, account);
        sysUserLambdaQueryWrapper.select(SysUser::getPassword, SysUser::getPasswordSalt, SysUser::getStatusFlag, SysUser::getUserId);

        // 单独填充租户id
        SysUser sysUserServiceOne;
        try {
            TenantIdHolder.set(tenantId);
            sysUserServiceOne = this.getOne(sysUserLambdaQueryWrapper, false);
        } finally {
            TenantIdHolder.remove();
        }

        if (sysUserServiceOne == null) {
            throw new ServiceException(SysUserExceptionEnum.ACCOUNT_NOT_EXIST);
        }

        return new UserValidateDTO(sysUserServiceOne.getUserId(), sysUserServiceOne.getPassword(), sysUserServiceOne.getPasswordSalt(),
                sysUserServiceOne.getStatusFlag());
    }

    @Override
    public UserValidateDTO getUserLoginValidateDTO(Long userId) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getPassword, SysUser::getPasswordSalt, SysUser::getStatusFlag, SysUser::getUserId);

        // 单独填充租户id
        SysUser sysUserServiceOne;
        try {
            TenantSwitchHolder.set(false);
            sysUserServiceOne = this.getOne(sysUserLambdaQueryWrapper, false);
        } finally {
            TenantSwitchHolder.remove();
        }

        if (sysUserServiceOne == null) {
            throw new ServiceException(SysUserExceptionEnum.ACCOUNT_NOT_EXIST);
        }

        return new UserValidateDTO(sysUserServiceOne.getUserId(), sysUserServiceOne.getPassword(), sysUserServiceOne.getPasswordSalt(),
                sysUserServiceOne.getStatusFlag());
    }

    @Override
    public void updateUserLoginInfo(Long userId, String ip) {

        if (ObjectUtil.isEmpty(userId) || ObjectUtil.isEmpty(ip)) {
            return;
        }

        // 获取原来的登录次数
        long loginCount = 0;
        LambdaQueryWrapper<SysUser> sysUserOriginLoginCountWrapper = new LambdaQueryWrapper<>();
        sysUserOriginLoginCountWrapper.eq(SysUser::getUserId, userId);
        sysUserOriginLoginCountWrapper.select(SysUser::getLoginCount);
        SysUser originUser = this.getOne(sysUserOriginLoginCountWrapper, false);
        if (originUser == null || originUser.getLoginCount() == null) {
            loginCount = 1;
        } else {
            loginCount = originUser.getLoginCount() + 1;
        }

        LambdaUpdateWrapper<SysUser> sysUserLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        sysUserLambdaUpdateWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaUpdateWrapper.set(SysUser::getLastLoginTime, new Date());
        sysUserLambdaUpdateWrapper.set(SysUser::getLastLoginIp, ip);
        sysUserLambdaUpdateWrapper.set(SysUser::getLoginCount, loginCount);
        this.update(sysUserLambdaUpdateWrapper);
    }

    @Override
    public boolean getUserSuperAdminFlag(Long userId) {

        if (ObjectUtil.isEmpty(userId)) {
            return false;
        }

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getSuperAdminFlag);
        SysUser result = this.getOne(sysUserLambdaQueryWrapper, false);

        if (result == null) {
            return false;
        }

        return YesOrNotEnum.Y.getCode().equals(result.getSuperAdminFlag());
    }

    @Override
    public List<Long> queryAllUserIdList() {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.select(SysUser::getUserId);
        List<SysUser> list = this.list(sysUserLambdaQueryWrapper);
        return list.stream().map(SysUser::getUserId).collect(Collectors.toList());
    }

    @Override
    public Boolean userExist(Long userId) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserId, userId);
        long count = this.count(wrapper);
        return count > 0;
    }

    @Override
    public OnlineUserItem getUserNameAccountInfo(Long userId) {

        if (userId == null) {
            return new OnlineUserItem();
        }

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserId, userId);
        wrapper.select(SysUser::getRealName, SysUser::getAccount);
        SysUser sysUser = this.getOne(wrapper, false);

        if (sysUser != null) {
            return new OnlineUserItem(userId, sysUser.getRealName(), sysUser.getAccount());
        } else {
            return new OnlineUserItem();
        }
    }

    @Override
    public List<OnlineUserItem> getUserNameAccountInfoListByCondition(List<OnlineUserItem> onlineUserItems, String searchText) {

        if (ObjectUtil.isEmpty(onlineUserItems) || ObjectUtil.isEmpty(searchText)) {
            return new ArrayList<>();
        }

        // 在线用户列表的id集合
        Set<Long> userIdList = onlineUserItems.stream().map(OnlineUserItem::getUserId).collect(Collectors.toSet());

        // 在这些id集合和查询条件中筛选符合条件的用户，并组装上他们的姓名和账号
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUser::getUserId, userIdList);
        wrapper.nested(wrap -> {
            wrap.like(SysUser::getRealName, searchText).or().like(SysUser::getAccount, searchText);
        });
        wrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getAccount);
        List<SysUser> sysUserList = this.list(wrapper);

        List<OnlineUserItem> resultList = new ArrayList<>();
        if (ObjectUtil.isEmpty(sysUserList)) {
            return resultList;
        }

        Map<Long, SysUser> userMap = sysUserList.stream().collect(Collectors.toMap(SysUser::getUserId, item -> item));

        // 从在线用户中查找包含这些key的元素
        for (OnlineUserItem onlineUserItem : onlineUserItems) {
            SysUser sysUser = userMap.get(onlineUserItem.getUserId());
            if (sysUser != null) {
                onlineUserItem.setRealName(sysUser.getRealName());
                onlineUserItem.setAccount(sysUser.getAccount());
                resultList.add(onlineUserItem);
            }
        }

        return resultList;
    }

    @Override
    public UserInfoDetailDTO getUserDetail(Long userId) {

        UserInfoDetailDTO result = new UserInfoDetailDTO();

        if (ObjectUtil.isEmpty(userId)) {
            return result;
        }

        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.eq(SysUser::getUserId, userId);
        sysUserLambdaQueryWrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getNickName, SysUser::getAccount,
                SysUser::getBirthday, SysUser::getSex, SysUser::getPhone, SysUser::getTel, SysUser::getEmail, SysUser::getSuperAdminFlag,
                SysUser::getStatusFlag, SysUser::getUserSort, SysUser::getMasterUserId);
        SysUser userInfo = this.getOne(sysUserLambdaQueryWrapper);

        if (userInfo != null) {
            BeanUtil.copyProperties(userInfo, result);
            return result;
        }

        return result;
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

        // 根据输入内容进行查询
        String searchText = sysUserRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.nested(wrap -> {
                wrap.like(SysUser::getRealName, searchText);
                wrap.or().like(SysUser::getAccount, searchText);
                wrap.or().like(SysUser::getPhone, searchText);
                wrap.or().like(SysUser::getTel, searchText);
            });
        }

        // 根据状态进行查询
        Integer statusFlag = sysUserRequest.getStatusFlag();
        queryWrapper.eq(ObjectUtil.isNotEmpty(statusFlag), SysUser::getStatusFlag, statusFlag);

        // 如果传递了组织机构id查询条件，则查询对应机构id下有哪些用户，再拼接用户查询条件
        if (ObjectUtil.isNotEmpty(sysUserRequest.getOrgIdCondition())) {
            List<Long> orgUserIdList = this.sysUserOrgService.getOrgUserIdList(sysUserRequest.getOrgIdCondition(), true);

            // 指定部门下没人，则直接返回一个不成立条件
            if (ObjectUtil.isEmpty(orgUserIdList)) {
                queryWrapper.in(SysUser::getUserId, -1L);
            } else {
                queryWrapper.in(SysUser::getUserId, orgUserIdList);
            }
        }

        // 数据权限范围控制
        Set<Long> dataScope = DataScopeContext.me().currentUserOrgScopeList();
        if (ObjectUtil.isNotEmpty(dataScope)) {
            Set<Long> userIdList = this.sysUserOrgService.getOrgUserIdList(dataScope);
            queryWrapper.in(SysUser::getUserId, userIdList);
        }

        // 按用户排序字段排序
        queryWrapper.orderByAsc(SysUser::getUserSort);

        return queryWrapper;
    }

    /**
     * 删除用户操作的基础业务
     *
     * @author fengshuonan
     * @since 2023/6/12 10:44
     */
    private void baseRemoveUser(Set<Long> userIdList) {
        // 校验是否有其他业务绑定了用户信息
        Map<String, RemoveUserCallbackApi> removeUserCallbackApiMap = SpringUtil.getBeansOfType(RemoveUserCallbackApi.class);
        for (RemoveUserCallbackApi removeUserCallbackApi : removeUserCallbackApiMap.values()) {
            removeUserCallbackApi.validateHaveUserBind(userIdList);
        }

        // 执行删除用户操作
        this.removeBatchByIds(userIdList);

        // 执行删除用户关联业务的操作
        for (RemoveUserCallbackApi removeUserCallbackApi : removeUserCallbackApiMap.values()) {
            removeUserCallbackApi.removeUserAction(userIdList);
        }
    }

}