package cn.stylefeng.roses.kernel.sys.modular.org.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.api.pojo.org.CompanyDeptDTO;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrganization;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrganizationRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 组织机构信息 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
public interface HrOrganizationService extends IService<HrOrganization> {

    /**
     * 新增
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void add(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 删除
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void del(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 批量删除组织机构
     *
     * @author fengshuonan
     * @since 2023/6/11 16:59
     */
    void batchDelete(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 编辑
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void edit(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 查询详情
     *
     * @param hrOrganizationRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    HrOrganization detail(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 获取列表
     *
     * @param hrOrganizationRequest 请求参数
     * @return List<HrOrganization>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    List<HrOrganization> findList(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 获取列表（带分页）
     *
     * @param hrOrganizationRequest 请求参数
     * @return PageResult<HrOrganization>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    PageResult<HrOrganization> findPage(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 通用获取组织机构树
     * <p>
     * ps：用在获取用户管理和组织机构管理界面左侧树
     *
     * @author fengshuonan
     * @since 2023/6/11 10:40
     */
    List<HrOrganization> commonOrgTree(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 根据组织机构id，获取对应的具体的公司和部门信息
     *
     * @param orgId 组织机构id
     * @return 公司和部门信息
     * @author fengshuonan
     * @since 2023/6/12 15:42
     */
    CompanyDeptDTO getCompanyDeptInfo(Long orgId);

    /**
     * 获取组织机构对应的公司信息
     *
     * @param hrOrganization 被查询的组织机构
     * @return 单独返回公司信息
     * @author fengshuonan
     * @since 2023/6/12 16:09
     */
    CompanyDeptDTO getOrgCompanyInfo(HrOrganization hrOrganization);

}