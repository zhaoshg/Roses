package cn.stylefeng.roses.kernel.sys.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.enums.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.sys.modular.role.enums.SysRoleExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.role.mapper.SysRoleMapper;
import cn.stylefeng.roses.kernel.sys.modular.role.pojo.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.sys.modular.role.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public void add(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // 设置角色默认的数据范围，默认查看全部
        sysRole.setDataScopeType(DataScopeTypeEnum.ALL.getCode());

        this.save(sysRole);
    }

    @Override
    public void del(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        this.removeById(sysRole.getRoleId());
    }

    @Override
    public void edit(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        BeanUtil.copyProperties(sysRoleRequest, sysRole);
        this.updateById(sysRole);
    }

    @Override
    public SysRole detail(SysRoleRequest sysRoleRequest) {
        return this.querySysRole(sysRoleRequest);
    }

    @Override
    public PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(sysRoleRequest);

        // 只查询需要的字段
        wrapper.select(SysRole::getRoleName, SysRole::getRoleCode, SysRole::getRoleSort, SysRole::getRoleId, BaseEntity::getCreateTime);

        Page<SysRole> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysRole> findList(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = this.createWrapper(sysRoleRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private SysRole querySysRole(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.getById(sysRoleRequest.getRoleId());
        if (ObjectUtil.isEmpty(sysRole)) {
            throw new ServiceException(SysRoleExceptionEnum.SYS_ROLE_NOT_EXISTED);
        }
        return sysRole;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:29
     */
    private LambdaQueryWrapper<SysRole> createWrapper(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // 根据名称模糊搜索
        String searchText = sysRoleRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysRole::getRoleName, searchText);
            queryWrapper.or().like(SysRole::getRoleCode, searchText);
        }

        // 排序字段
        queryWrapper.orderByAsc(SysRole::getRoleSort);

        return queryWrapper;
    }

}