package cn.stylefeng.roses.kernel.sys.modular.position.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.sys.modular.position.entity.HrPosition;
import cn.stylefeng.roses.kernel.sys.modular.position.enums.HrPositionExceptionEnum;
import cn.stylefeng.roses.kernel.sys.modular.position.mapper.HrPositionMapper;
import cn.stylefeng.roses.kernel.sys.modular.position.pojo.request.HrPositionRequest;
import cn.stylefeng.roses.kernel.sys.modular.position.service.HrPositionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 职位信息业务实现层
 *
 * @author fengshuonan
 * @date 2023/06/10 21:25
 */
@Service
public class HrPositionServiceImpl extends ServiceImpl<HrPositionMapper, HrPosition> implements HrPositionService {

    @Override
    public void add(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = new HrPosition();
        BeanUtil.copyProperties(hrPositionRequest, hrPosition);
        this.save(hrPosition);
    }

    @Override
    public void del(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = this.queryHrPosition(hrPositionRequest);
        this.removeById(hrPosition.getPositionId());
    }

    @Override
    public void edit(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = this.queryHrPosition(hrPositionRequest);
        BeanUtil.copyProperties(hrPositionRequest, hrPosition);
        this.updateById(hrPosition);
    }

    @Override
    public HrPosition detail(HrPositionRequest hrPositionRequest) {
        return this.queryHrPosition(hrPositionRequest);
    }

    @Override
    public PageResult<HrPosition> findPage(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> wrapper = this.createWrapper(hrPositionRequest);

        // 筛选主要属性
        wrapper.select(HrPosition::getPositionId, HrPosition::getPositionName, HrPosition::getPositionCode, HrPosition::getRemark, BaseEntity::getCreateTime);

        Page<HrPosition> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<HrPosition> findList(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> wrapper = this.createWrapper(hrPositionRequest);

        // 筛选主要属性
        wrapper.select(HrPosition::getPositionId, HrPosition::getPositionName, HrPosition::getPositionCode, HrPosition::getRemark, BaseEntity::getCreateTime);

        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    private HrPosition queryHrPosition(HrPositionRequest hrPositionRequest) {
        HrPosition hrPosition = this.getById(hrPositionRequest.getPositionId());
        if (ObjectUtil.isEmpty(hrPosition)) {
            throw new ServiceException(HrPositionExceptionEnum.HR_POSITION_NOT_EXISTED);
        }
        return hrPosition;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2023/06/10 21:25
     */
    private LambdaQueryWrapper<HrPosition> createWrapper(HrPositionRequest hrPositionRequest) {
        LambdaQueryWrapper<HrPosition> queryWrapper = new LambdaQueryWrapper<>();

        // 根据输入的搜索内容查询
        String searchText = hrPositionRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(HrPosition::getPositionName, searchText);
            queryWrapper.or().like(HrPosition::getPositionCode, searchText);
        }

        return queryWrapper;
    }

}