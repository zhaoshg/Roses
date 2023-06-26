package cn.stylefeng.roses.kernel.sys.modular.userapp.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.userapp.entity.PortalUserApp;
import cn.stylefeng.roses.kernel.sys.modular.userapp.pojo.request.PortalUserAppRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户常用功能 服务类
 *
 * @author fengshuonan
 * @date 2023/06/26 21:25
 */
public interface PortalUserAppService extends IService<PortalUserApp> {

	/**
     * 新增
     *
     * @param portalUserAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    void add(PortalUserAppRequest portalUserAppRequest);

	/**
     * 删除
     *
     * @param portalUserAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    void del(PortalUserAppRequest portalUserAppRequest);

	/**
     * 编辑
     *
     * @param portalUserAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    void edit(PortalUserAppRequest portalUserAppRequest);

	/**
     * 查询详情
     *
     * @param portalUserAppRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    PortalUserApp detail(PortalUserAppRequest portalUserAppRequest);

	/**
     * 获取列表
     *
     * @param portalUserAppRequest        请求参数
     * @return List<PortalUserApp>   返回结果
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    List<PortalUserApp> findList(PortalUserAppRequest portalUserAppRequest);

	/**
     * 获取列表（带分页）
     *
     * @param portalUserAppRequest              请求参数
     * @return PageResult<PortalUserApp>   返回结果
     * @author fengshuonan
     * @date 2023/06/26 21:25
     */
    PageResult<PortalUserApp> findPage(PortalUserAppRequest portalUserAppRequest);

}