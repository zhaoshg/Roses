package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserCertificate;
import cn.stylefeng.roses.kernel.sys.modular.user.pojo.request.SysUserCertificateRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户证书服务类
 *
 * @author fengshuonan
 * @since 2023/11/09 22:44
 */
public interface SysUserCertificateService extends IService<SysUserCertificate> {

    /**
     * 新增用户证书
     *
     * @param sysUserCertificateRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    void add(SysUserCertificateRequest sysUserCertificateRequest);

    /**
     * 删除用户证书
     *
     * @param sysUserCertificateRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    void del(SysUserCertificateRequest sysUserCertificateRequest);

    /**
     * 批量删除用户证书
     *
     * @param sysUserCertificateRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    void batchDelete(SysUserCertificateRequest sysUserCertificateRequest);

    /**
     * 编辑用户证书
     *
     * @param sysUserCertificateRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    void edit(SysUserCertificateRequest sysUserCertificateRequest);

    /**
     * 查询详情用户证书
     *
     * @param sysUserCertificateRequest 请求参数
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    SysUserCertificate detail(SysUserCertificateRequest sysUserCertificateRequest);

    /**
     * 获取用户证书列表
     *
     * @param sysUserCertificateRequest         请求参数
     * @return List<SysUserCertificate>  返回结果
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    List<SysUserCertificate> findList(SysUserCertificateRequest sysUserCertificateRequest);

    /**
     * 获取用户证书分页列表
     *
     * @param sysUserCertificateRequest                请求参数
     * @return PageResult<SysUserCertificate>   返回结果
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    PageResult<SysUserCertificate> findPage(SysUserCertificateRequest sysUserCertificateRequest);

}
