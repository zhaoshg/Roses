package cn.stylefeng.roses.kernel.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.api.SysUserServiceApi;
import cn.stylefeng.roses.kernel.sys.api.pojo.SimpleUserDTO;
import cn.stylefeng.roses.kernel.sys.api.pojo.UserOrgDTO;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService, SysUserServiceApi {

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

        // 只查询需要的字段
        wrapper.select(SysUser::getUserId, SysUser::getRealName, SysUser::getAccount, SysUser::getSex, SysUser::getStatusFlag, BaseEntity::getCreateTime);

        Page<SysUser> sysUserPage = this.page(PageFactory.defaultPage(), wrapper);

        // 遍历查询结果，增加对用户部门信息的返回
        for (SysUser record : sysUserPage.getRecords()) {
            record.setUserOrgDTO(this.getUserMainOrgInfo(record.getUserId()));
        }

        return PageResultFactory.createPageResult(sysUserPage);
    }

    @Override
    public List<SysUser> findList(SysUserRequest sysUserRequest) {
        LambdaQueryWrapper<SysUser> wrapper = this.createWrapper(sysUserRequest);
        return this.list(wrapper);
    }

    @Override
    public SimpleUserDTO getUserInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public UserOrgDTO getUserMainOrgInfo(Long userId) {
        return null;
    }

    @Override
    public List<UserOrgDTO> getUserOrgList(Long userId) {
        return null;
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
            queryWrapper.like(SysUser::getRealName, searchText);
            queryWrapper.or().like(SysUser::getAccount, searchText);
        }

        // 根据状态进行查询
        Integer statusFlag = sysUserRequest.getStatusFlag();
        queryWrapper.eq(ObjectUtil.isNotEmpty(statusFlag), SysUser::getStatusFlag, statusFlag);

        // 按用户排序字段排序
        queryWrapper.orderByAsc(SysUser::getUserSort);

        return queryWrapper;
    }

}