package cn.stylefeng.roses.kernel.config.modular.service;

import cn.stylefeng.roses.kernel.config.api.constants.ConfigConstants;
import cn.stylefeng.roses.kernel.config.modular.pojo.param.SysConfigTypeParam;
import cn.stylefeng.roses.kernel.dict.api.DictApi;
import cn.stylefeng.roses.kernel.dict.api.DictTypeApi;
import cn.stylefeng.roses.kernel.dict.api.pojo.SimpleDictUpdateParam;
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

    @Resource
    private DictTypeApi dictTypeApi;

    @Resource
    private SysConfigService sysConfigService;

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

        // 查询字典类型
        Long dictTypeId = dictTypeApi.getDictTypeIdByDictTypeCode(ConfigConstants.CONFIG_GROUP_DICT_TYPE_CODE);

        // 配置类型信息转化为新增字典的参数信息
        SimpleDictUpdateParam simpleDictAddParam = new SimpleDictUpdateParam();
        simpleDictAddParam.setDictTypeId(dictTypeId);
        simpleDictAddParam.setDictName(sysConfigTypeParam.getConfigTypeName());
        simpleDictAddParam.setDictCode(sysConfigTypeParam.getConfigTypeCode());
        simpleDictAddParam.setDictSort(sysConfigTypeParam.getConfigTypeSort());

        // 新增一个字典
        dictApi.simpleAddDict(simpleDictAddParam);
    }

    /**
     * 更新字典类型
     *
     * @author fengshuonan
     * @since 2023/6/28 17:32
     */
    public void edit(SysConfigTypeParam sysConfigTypeParam) {

        // 查询字典类型
        Long dictTypeId = dictTypeApi.getDictTypeIdByDictTypeCode(ConfigConstants.CONFIG_GROUP_DICT_TYPE_CODE);

        // 配置类型信息转化为编辑字典的参数信息
        SimpleDictUpdateParam simpleDictAddParam = new SimpleDictUpdateParam();
        simpleDictAddParam.setDictId(sysConfigTypeParam.getConfigTypeId());
        simpleDictAddParam.setDictTypeId(dictTypeId);
        simpleDictAddParam.setDictName(sysConfigTypeParam.getConfigTypeName());
        simpleDictAddParam.setDictCode(sysConfigTypeParam.getConfigTypeCode());
        simpleDictAddParam.setDictSort(sysConfigTypeParam.getConfigTypeSort());

        // 编辑字典
        dictApi.simpleEditDict(simpleDictAddParam);
    }

}
