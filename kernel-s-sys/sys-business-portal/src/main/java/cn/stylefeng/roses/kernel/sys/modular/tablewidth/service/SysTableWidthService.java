package cn.stylefeng.roses.kernel.sys.modular.tablewidth.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.entity.SysTableWidth;
import cn.stylefeng.roses.kernel.sys.modular.tablewidth.pojo.request.SysTableWidthRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 业务中表的宽度 服务类
 *
 * @author fengshuonan
 * @date 2023/02/23 22:21
 */
public interface SysTableWidthService extends IService<SysTableWidth> {

	/**
     * 新增
     *
     * @param sysTableWidthRequest 请求参数
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    void setTableWidth(SysTableWidthRequest sysTableWidthRequest);

	/**
     * 删除
     *
     * @param sysTableWidthRequest 请求参数
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    void del(SysTableWidthRequest sysTableWidthRequest);

	/**
     * 编辑
     *
     * @param sysTableWidthRequest 请求参数
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    void edit(SysTableWidthRequest sysTableWidthRequest);

	/**
     * 查询详情
     *
     * @param sysTableWidthRequest 请求参数
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    SysTableWidth detail(SysTableWidthRequest sysTableWidthRequest);

	/**
     * 获取列表
     *
     * @param sysTableWidthRequest        请求参数
     * @return List<SysTableWidth>   返回结果
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    List<SysTableWidth> findList(SysTableWidthRequest sysTableWidthRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysTableWidthRequest              请求参数
     * @return PageResult<SysTableWidth>   返回结果
     * @author fengshuonan
     * @date 2023/02/23 22:21
     */
    PageResult<SysTableWidth> findPage(SysTableWidthRequest sysTableWidthRequest);

}