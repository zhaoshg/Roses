package cn.stylefeng.roses.kernel.sys.modular.org.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.org.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.sys.modular.org.pojo.request.HrOrgApproverRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 组织机构审批人 服务类
 *
 * @author fengshuonan
 * @date 2023/06/10 21:23
 */
public interface HrOrgApproverService extends IService<HrOrgApprover> {

	/**
     * 新增
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void add(HrOrgApproverRequest hrOrgApproverRequest);

	/**
     * 删除
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void del(HrOrgApproverRequest hrOrgApproverRequest);

	/**
     * 编辑
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    void edit(HrOrgApproverRequest hrOrgApproverRequest);

	/**
     * 查询详情
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    HrOrgApprover detail(HrOrgApproverRequest hrOrgApproverRequest);

	/**
     * 获取列表
     *
     * @param hrOrgApproverRequest        请求参数
     * @return List<HrOrgApprover>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    List<HrOrgApprover> findList(HrOrgApproverRequest hrOrgApproverRequest);

	/**
     * 获取列表（带分页）
     *
     * @param hrOrgApproverRequest              请求参数
     * @return PageResult<HrOrgApprover>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    PageResult<HrOrgApprover> findPage(HrOrgApproverRequest hrOrgApproverRequest);

}