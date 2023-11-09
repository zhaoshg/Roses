package cn.stylefeng.roses.kernel.sys.modular.user.service;

import cn.stylefeng.roses.kernel.sys.modular.user.entity.SysUserCertificate;
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
     * 更新用户的证书信息
     *
     * @param userId                 用户id集合
     * @param sysUserCertificateList 请求参数
     * @author fengshuonan
     * @since 2023/11/09 22:44
     */
    void updateUserCertificate(Long userId, List<SysUserCertificate> sysUserCertificateList);

}
