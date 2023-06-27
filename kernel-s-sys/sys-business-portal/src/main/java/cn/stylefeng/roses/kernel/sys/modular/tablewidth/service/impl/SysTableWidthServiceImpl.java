package cn.stylefeng.roses.kernel.sys.modular.tablewidth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.entity.SysTableWidth;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.enums.SysTableWidthExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.mapper.SysTableWidthMapper;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo.request.SysTableWidthRequest;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.service.SysTableWidthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务中表的宽度业务实现层
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
@Service
public class SysTableWidthServiceImpl extends ServiceImpl<SysTableWidthMapper, SysTableWidth> implements SysTableWidthService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setTableWidth(SysTableWidthRequest sysTableWidthRequest) {
        SysTableWidth sysTableWidth = new SysTableWidth();
        BeanUtil.copyProperties(sysTableWidthRequest, sysTableWidth);

        // 当前用户id
        Long currentUserId = LoginContext.me().getLoginUser().getUserId();

        // 如果是保存个人的信息，设置上用户id
        if (FieldTypeEnum.USER.getCode().equals(sysTableWidthRequest.getFieldType())) {
            sysTableWidth.setUserId(currentUserId);
        }

        // 如果设置的是全局的，则把个人配置的全都删掉
        else {
            LambdaQueryWrapper<SysTableWidth> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysTableWidth::getFieldBusinessCode, sysTableWidthRequest.getFieldBusinessCode());
            this.remove(queryWrapper);
        }

        // 先删除再保存
        String fieldBusinessCode = sysTableWidthRequest.getFieldBusinessCode();
        Integer fieldType = sysTableWidthRequest.getFieldType();
        Long userId = sysTableWidthRequest.getUserId();

        LambdaQueryWrapper<SysTableWidth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotEmpty(fieldBusinessCode), SysTableWidth::getFieldBusinessCode, fieldBusinessCode);
        queryWrapper.eq(ObjectUtil.isNotEmpty(fieldType), SysTableWidth::getFieldType, fieldType);
        queryWrapper.eq(ObjectUtil.isNotEmpty(userId), SysTableWidth::getUserId, userId);
        this.remove(queryWrapper);

        this.save(sysTableWidth);
    }

    @Override
    public void del(SysTableWidthRequest sysTableWidthRequest) {
        SysTableWidth sysTableWidth = this.querySysTableWidth(sysTableWidthRequest);
        this.removeById(sysTableWidth.getTableWidthId());
    }

    @Override
    public void edit(SysTableWidthRequest sysTableWidthRequest) {
        SysTableWidth sysTableWidth = this.querySysTableWidth(sysTableWidthRequest);
        BeanUtil.copyProperties(sysTableWidthRequest, sysTableWidth);
        this.updateById(sysTableWidth);
    }

    @Override
    public SysTableWidth detail(SysTableWidthRequest sysTableWidthRequest) {

        // 获取当前人，在这个业务下，是否有生成过配置
        LambdaQueryWrapper<SysTableWidth> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTableWidth::getUserId, LoginContext.me().getLoginUser().getUserId());
        wrapper.eq(SysTableWidth::getFieldBusinessCode, sysTableWidthRequest.getFieldBusinessCode());

        SysTableWidth sysTableWidth = this.getOne(wrapper, false);

        // 如果存在配置，则直接返回
        if (sysTableWidth != null) {
            return sysTableWidth;
        }

        // 如果不存在配置，则去查找当前项目是否存在全局的配置
        LambdaQueryWrapper<SysTableWidth> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(SysTableWidth::getFieldBusinessCode, sysTableWidthRequest.getFieldBusinessCode());
        wrapper2.eq(SysTableWidth::getFieldType, FieldTypeEnum.TOTAL.getCode());
        SysTableWidth totalTableWidth = this.getOne(wrapper2, false);
        if (totalTableWidth != null) {
            return totalTableWidth;
        }

        // 如果没有个人配置，也没有全局配置，返回空
        return new SysTableWidth();
    }

    @Override
    public PageResult<SysTableWidth> findPage(SysTableWidthRequest sysTableWidthRequest) {
        LambdaQueryWrapper<SysTableWidth> wrapper = createWrapper(sysTableWidthRequest);
        Page<SysTableWidth> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SysTableWidth> findList(SysTableWidthRequest sysTableWidthRequest) {
        LambdaQueryWrapper<SysTableWidth> wrapper = this.createWrapper(sysTableWidthRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    private SysTableWidth querySysTableWidth(SysTableWidthRequest sysTableWidthRequest) {
        SysTableWidth sysTableWidth = this.getById(sysTableWidthRequest.getTableWidthId());
        if (ObjectUtil.isEmpty(sysTableWidth)) {
            throw new ServiceException(SysTableWidthExceptionEnum.SYS_TABLE_WIDTH_NOT_EXISTED);
        }
        return sysTableWidth;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    private LambdaQueryWrapper<SysTableWidth> createWrapper(SysTableWidthRequest sysTableWidthRequest) {
        LambdaQueryWrapper<SysTableWidth> queryWrapper = new LambdaQueryWrapper<>();

        Long tableWidthId = sysTableWidthRequest.getTableWidthId();
        String fieldBusinessCode = sysTableWidthRequest.getFieldBusinessCode();
        Integer fieldType = sysTableWidthRequest.getFieldType();
        Long userId = sysTableWidthRequest.getUserId();
        String tableWidthJson = sysTableWidthRequest.getTableWidthJson();

        queryWrapper.eq(ObjectUtil.isNotNull(tableWidthId), SysTableWidth::getTableWidthId, tableWidthId);
        queryWrapper.like(ObjectUtil.isNotEmpty(fieldBusinessCode), SysTableWidth::getFieldBusinessCode, fieldBusinessCode);
        queryWrapper.eq(ObjectUtil.isNotNull(fieldType), SysTableWidth::getFieldType, fieldType);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysTableWidth::getUserId, userId);
        queryWrapper.like(ObjectUtil.isNotEmpty(tableWidthJson), SysTableWidth::getTableWidthJson, tableWidthJson);

        return queryWrapper;
    }

}