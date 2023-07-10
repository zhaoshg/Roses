package cn.stylefeng.roses.kernel.city.modular.mapper;

import cn.stylefeng.roses.kernel.city.modular.entity.Area;
import cn.stylefeng.roses.kernel.city.modular.pojo.AreaVo;
import cn.stylefeng.roses.kernel.city.modular.pojo.request.AreaRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 行政区域 Mapper 接口
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
public interface AreaMapper extends BaseMapper<Area> {

    List<AreaVo> customFindList(@Param("page") Page page, @Param("param") AreaRequest request);

}