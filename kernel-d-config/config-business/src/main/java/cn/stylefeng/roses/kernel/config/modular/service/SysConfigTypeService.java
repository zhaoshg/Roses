package cn.stylefeng.roses.kernel.config.modular.service;

import cn.stylefeng.roses.kernel.config.api.constants.ConfigConstants;
import cn.stylefeng.roses.kernel.config.modular.pojo.param.SysConfigTypeParam;
import cn.stylefeng.roses.kernel.dict.api.DictApi;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置类型业务
 *
 * @author fengshuonan
 * @since 2023/6/28 16:52
 */
@Service
public class SysConfigTypeService {

    @Resource
    private DictApi dictApi;

    /**
     * 查询系统配置类型列表
     * <p>
     * 查询字典类型为config_group的所有字典列表
     *
     * @author fengshuonan
     * @since 2023/6/28 16:56
     */
    public List<SimpleDict> getSysConfigTypeList(SysConfigTypeParam sysConfigTypeParam) {
        return dictApi.getDictDetailsByDictTypeCode(ConfigConstants.CONFIG_GROUP_DICT_TYPE_CODE, sysConfigTypeParam.getSearchText());
    }

    /**
     * 新增系统配置类型
     * <p>
     * 针对字典类型编码为config_group的字典增加一个条目
     *
     * @author fengshuonan
     * @since 2023/6/28 17:07
     */
    public void add(SysConfigTypeParam sysConfigTypeParam) {

    }

}
