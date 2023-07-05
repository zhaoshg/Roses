package cn.stylefeng.roses.kernel.dict.modular.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.stylefeng.roses.kernel.dict.modular.entity.Area;
import cn.stylefeng.roses.kernel.dict.modular.pojo.AreaVo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.AreaRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import java.util.List;
/**
 * 【系统基础】-【行政区域表】 Mapper 接口
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
public interface AreaMapper extends BaseMapper<Area> {

    List<AreaVo> customFindList(@Param("page") Page page, @Param("param")AreaRequest request);

}