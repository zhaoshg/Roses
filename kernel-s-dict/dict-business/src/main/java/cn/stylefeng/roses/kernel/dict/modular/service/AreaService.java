package cn.stylefeng.roses.kernel.dict.modular.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.dict.modular.entity.Area;
import cn.stylefeng.roses.kernel.dict.modular.pojo.AreaVo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.AreaRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【系统基础】-【行政区域表】 服务类
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
public interface AreaService extends IService<Area> {

 /**
     * 新增
     *
     * @param areaRequest 请求参数
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    void add(AreaRequest areaRequest);

 /**
     * 删除
     *
     * @param areaRequest 请求参数
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    void del(AreaRequest areaRequest);

 /**
     * 编辑
     *
     * @param areaRequest 请求参数
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    void edit(AreaRequest areaRequest);

 /**
     * 查询详情
     *
     * @param areaRequest 请求参数
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    Area detail(AreaRequest areaRequest);

 /**
     * 获取列表
     *
     * @param areaRequest        请求参数
     * @return List<Area>   返回结果
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    List<Area> findList(AreaRequest areaRequest);

 /**
     * 获取列表（带分页）
     *
     * @param areaRequest              请求参数
     * @return PageResult<Area>   返回结果
     * @author LiYanJun
     * @date 2023/07/05 18:12
     */
    PageResult<AreaVo> findPage(AreaRequest areaRequest);

}