package cn.stylefeng.roses.kernel.favorite.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.favorite.modular.entity.SysUserFavorite;
import cn.stylefeng.roses.kernel.favorite.modular.enums.SysUserFavoriteExceptionEnum;
import cn.stylefeng.roses.kernel.favorite.modular.mapper.SysUserFavoriteMapper;
import cn.stylefeng.roses.kernel.favorite.modular.pojo.request.SysUserFavoriteRequest;
import cn.stylefeng.roses.kernel.favorite.modular.service.SysUserFavoriteService;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户收藏信息业务实现层
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
@Service
public class SysUserFavoriteServiceImpl extends ServiceImpl<SysUserFavoriteMapper, SysUserFavorite> implements SysUserFavoriteService {

	@Override
    public void add(SysUserFavoriteRequest sysUserFavoriteRequest) {
        SysUserFavorite sysUserFavorite = new SysUserFavorite();
        BeanUtil.copyProperties(sysUserFavoriteRequest, sysUserFavorite);
        this.save(sysUserFavorite);
    }

    @Override
    public void del(SysUserFavoriteRequest sysUserFavoriteRequest) {
        SysUserFavorite sysUserFavorite = this.querySysUserFavorite(sysUserFavoriteRequest);
        this.removeById(sysUserFavorite.getFavoriteId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(SysUserFavoriteRequest sysUserFavoriteRequest) {
        this.removeByIds(sysUserFavoriteRequest.getBatchDeleteIdList());
    }

    @Override
    public void edit(SysUserFavoriteRequest sysUserFavoriteRequest) {
        SysUserFavorite sysUserFavorite = this.querySysUserFavorite(sysUserFavoriteRequest);
        BeanUtil.copyProperties(sysUserFavoriteRequest, sysUserFavorite);
        this.updateById(sysUserFavorite);
    }

    @Override
    public SysUserFavorite detail(SysUserFavoriteRequest sysUserFavoriteRequest) {
        return this.querySysUserFavorite(sysUserFavoriteRequest);
    }

    @Override
    public PageResult<SysUserFavorite> findPage(SysUserFavoriteRequest sysUserFavoriteRequest) {
        LambdaQueryWrapper<SysUserFavorite> wrapper = createWrapper(sysUserFavoriteRequest);
        Page<SysUserFavorite> pageList = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(pageList);
    }

    @Override
    public List<SysUserFavorite> findList(SysUserFavoriteRequest sysUserFavoriteRequest) {
        LambdaQueryWrapper<SysUserFavorite> wrapper = this.createWrapper(sysUserFavoriteRequest);
        return this.list(wrapper);
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    private SysUserFavorite querySysUserFavorite(SysUserFavoriteRequest sysUserFavoriteRequest) {
        SysUserFavorite sysUserFavorite = this.getById(sysUserFavoriteRequest.getFavoriteId());
        if (ObjectUtil.isEmpty(sysUserFavorite)) {
            throw new ServiceException(SysUserFavoriteExceptionEnum.SYS_USER_FAVORITE_NOT_EXISTED);
        }
        return sysUserFavorite;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    private LambdaQueryWrapper<SysUserFavorite> createWrapper(SysUserFavoriteRequest sysUserFavoriteRequest) {
        LambdaQueryWrapper<SysUserFavorite> queryWrapper = new LambdaQueryWrapper<>();

        Long favoriteId = sysUserFavoriteRequest.getFavoriteId();
        String favType = sysUserFavoriteRequest.getFavType();
        Long userId = sysUserFavoriteRequest.getUserId();
        Long businessId = sysUserFavoriteRequest.getBusinessId();
        String favTime = sysUserFavoriteRequest.getFavTime();

        queryWrapper.eq(ObjectUtil.isNotNull(favoriteId), SysUserFavorite::getFavoriteId, favoriteId);
        queryWrapper.like(ObjectUtil.isNotEmpty(favType), SysUserFavorite::getFavType, favType);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), SysUserFavorite::getUserId, userId);
        queryWrapper.eq(ObjectUtil.isNotNull(businessId), SysUserFavorite::getBusinessId, businessId);
        queryWrapper.eq(ObjectUtil.isNotNull(favTime), SysUserFavorite::getFavTime, favTime);

        return queryWrapper;
    }

}
