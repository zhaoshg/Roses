package cn.stylefeng.roses.kernel.sys.modular.org.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
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
     * @param hrOrgApproverRequest 请求参数
     * @return List<HrOrgApprover>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    List<HrOrgApprover> findList(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 获取列表（带分页）
     *
     * @param hrOrgApproverRequest 请求参数
     * @return PageResult<HrOrgApprover>   返回结果
     * @author fengshuonan
     * @date 2023/06/10 21:23
     */
    PageResult<HrOrgApprover> findPage(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 获取所有审批人类型列表
     *
     * @return 审批人类型列表
     * @author fengshuonan
     * @since 2022/9/26 10:18
     */
    List<SimpleDict> getApproverTypeList();

    /**
     * 获取组织机构绑定的审批人列表
     *
     * @author fengshuonan
     * @since 2023/6/11 15:23
     */
    List<HrOrgApprover> getOrgApproverList(HrOrgApproverRequest hrOrgApproverRequest);

}