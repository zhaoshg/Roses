package cn.stylefeng.roses.kernel.favorite.modular.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.favorite.modular.api.UserFavoriteApi;
import cn.stylefeng.roses.kernel.favorite.modular.entity.SysUserFavorite;
import cn.stylefeng.roses.kernel.favorite.modular.pojo.request.SysUserFavoriteRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户收藏信息服务类
 *
 * @author fengshuonan
 * @since 2023/11/21 22:09
 */
public interface SysUserFavoriteService extends IService<SysUserFavorite>, UserFavoriteApi {

    /**
     * 新增用户收藏信息
     *
     * @param sysUserFavoriteRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    void add(SysUserFavoriteRequest sysUserFavoriteRequest);

    /**
     * 删除用户收藏信息
     *
     * @param sysUserFavoriteRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    void del(SysUserFavoriteRequest sysUserFavoriteRequest);

    /**
     * 批量删除用户收藏信息
     *
     * @param sysUserFavoriteRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    void batchDelete(SysUserFavoriteRequest sysUserFavoriteRequest);

    /**
     * 编辑用户收藏信息
     *
     * @param sysUserFavoriteRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    void edit(SysUserFavoriteRequest sysUserFavoriteRequest);

    /**
     * 查询详情用户收藏信息
     *
     * @param sysUserFavoriteRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    SysUserFavorite detail(SysUserFavoriteRequest sysUserFavoriteRequest);

    /**
     * 获取用户收藏信息列表
     *
     * @param sysUserFavoriteRequest 请求参数
     * @return List<SysUserFavorite>  返回结果
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    List<SysUserFavorite> findList(SysUserFavoriteRequest sysUserFavoriteRequest);

    /**
     * 获取用户收藏信息分页列表
     *
     * @param sysUserFavoriteRequest 请求参数
     * @return PageResult<SysUserFavorite>   返回结果
     * @author fengshuonan
     * @since 2023/11/21 22:09
     */
    PageResult<SysUserFavorite> findPage(SysUserFavoriteRequest sysUserFavoriteRequest);

}
