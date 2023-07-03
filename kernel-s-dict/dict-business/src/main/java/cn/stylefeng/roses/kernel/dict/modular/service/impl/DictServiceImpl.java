/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.dict.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.dict.api.exception.DictException;
import cn.stylefeng.roses.kernel.dict.api.exception.enums.DictExceptionEnum;
import cn.stylefeng.roses.kernel.dict.api.pojo.DictDetail;
import cn.stylefeng.roses.kernel.dict.api.pojo.SimpleDictUpdateParam;
import cn.stylefeng.roses.kernel.dict.modular.entity.SysDict;
import cn.stylefeng.roses.kernel.dict.modular.factory.DictFactory;
import cn.stylefeng.roses.kernel.dict.modular.mapper.DictMapper;
import cn.stylefeng.roses.kernel.dict.modular.pojo.TreeDictInfo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.DictRequest;
import cn.stylefeng.roses.kernel.dict.modular.service.DictService;
import cn.stylefeng.roses.kernel.dict.modular.service.DictTypeService;
import cn.stylefeng.roses.kernel.pinyin.api.PinYinApi;
import cn.stylefeng.roses.kernel.rule.constants.SymbolConstant;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.rule.tree.buildpids.PidStructureBuildUtil;
import cn.stylefeng.roses.kernel.rule.tree.factory.DefaultTreeBuildFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 基础字典 服务实现类
 *
 * @author fengshuonan
 * @since 2020/12/26 22:36
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, SysDict> implements DictService {

    @Resource
    private PinYinApi pinYinApi;

    @Resource
    private DictTypeService dictTypeService;

    @Override
    public List<TreeDictInfo> getTreeDictList(DictRequest dictRequest) {

        // 获取字典类型下所有的字典
        List<SysDict> sysDictList = this.findList(dictRequest);
        if (ObjectUtil.isEmpty(sysDictList)) {
            return new ArrayList<>();
        }

        // 构造树节点信息
        ArrayList<TreeDictInfo> treeDictInfos = new ArrayList<>();
        for (SysDict sysDict : sysDictList) {
            TreeDictInfo treeDictInfo = new TreeDictInfo();
            treeDictInfo.setDictId(sysDict.getDictId());
            treeDictInfo.setDictParentId(sysDict.getDictParentId());
            treeDictInfo.setDictCode(sysDict.getDictCode());
            treeDictInfo.setDictName(sysDict.getDictName());
            treeDictInfo.setDictSort(sysDict.getDictSort());
            treeDictInfos.add(treeDictInfo);
        }

        // 构建菜单树
        return new DefaultTreeBuildFactory<TreeDictInfo>().doTreeBuild(treeDictInfos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DictRequest dictRequest) {

        // 校验字典重复，同一个字典类型下不能有重复的字典编码或者字典名称
        this.validateRepeat(dictRequest, false);

        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(dictRequest, sysDict);

        // 填充字典的拼音
        sysDict.setDictNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDict.getDictName()));

        // 填充字典的pids
        String pids = this.createPids(sysDict.getDictParentId());
        sysDict.setDictPids(pids);

        this.save(sysDict);
    }

    @Override
    public void del(DictRequest dictRequest) {
        this.removeById(dictRequest.getDictId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(DictRequest dictRequest) {

        // 校验字典重复
        this.validateRepeat(dictRequest, true);

        SysDict sysDict = this.querySysDict(dictRequest);
        BeanUtil.copyProperties(dictRequest, sysDict);

        // 不能修改字典类型、编码和字典的上下级关系（上下级关系和顺序，通过更新字典树接口更方便）
        sysDict.setDictTypeId(null);
        sysDict.setDictCode(null);
        sysDict.setDictParentId(null);

        // 填充拼音
        sysDict.setDictNamePinyin(pinYinApi.parseEveryPinyinFirstLetter(sysDict.getDictName()));

        // 填充pids
        //String pids = this.createPids(sysDict.getDictParentId());
        //sysDict.setDictPids(pids);

        this.updateById(sysDict);
    }

    @Override
    public SysDict detail(DictRequest dictRequest) {
        return this.querySysDict(dictRequest);
    }

    @Override
    public List<SysDict> findList(DictRequest dictRequest) {

        LambdaQueryWrapper<SysDict> wrapper = this.createWrapper(dictRequest);

        // 只查询有用字段
        wrapper.select(SysDict::getDictName, SysDict::getDictCode, SysDict::getDictSort, SysDict::getDictId, SysDict::getDictParentId);

        return this.list(wrapper);
    }

    @Override
    public void removeByDictTypeId(Long dictTypeId) {
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeId, dictTypeId);
        this.remove(sysDictLambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictTree(DictRequest dictRequest) {

        // 获取字典树的结构
        List<SysDict> totalDictStructure = dictRequest.getTotalDictStructure();

        // 调整字典的顺序
        DictFactory.updateSort(totalDictStructure, 1);

        // 填充树节点的parentId字段
        DictFactory.fillParentId(-1L, totalDictStructure);

        // 平行展开树形结构，准备从新整理pids
        List<SysDict> totalDictList = new ArrayList<>();
        DictFactory.collectTreeTasks(totalDictStructure, totalDictList);

        // 从新整理上下级结构，整理id和pid关系
        PidStructureBuildUtil.createPidStructure(totalDictList);

        // 更新菜单的sort字段、pid字段和pids字段这3个字段
        this.updateBatchById(totalDictList);
    }

    @Override
    public String getDictName(String dictTypeCode, String dictCode) {

        // 获取字典类型编码对应的字典类型id
        Long dictTypeId = dictTypeService.getDictTypeIdByDictTypeCode(dictTypeCode);

        // 查询字典类型下有哪些字典
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeId, dictTypeId);
        sysDictLambdaQueryWrapper.eq(SysDict::getDictCode, dictCode);
        sysDictLambdaQueryWrapper.select(SysDict::getDictName);
        List<SysDict> list = this.list(sysDictLambdaQueryWrapper);

        // 如果查询不到字典，则返回空串
        if (list.isEmpty()) {
            return StrUtil.EMPTY;
        }

        // 字典code存在多个重复的，返回空串并打印错误日志
        if (list.size() > 1) {
            log.error(DictExceptionEnum.DICT_CODE_REPEAT.getUserTip(), "", dictCode);
            return StrUtil.EMPTY;
        }

        return list.get(0).getDictName();
    }

    @Override
    public List<SimpleDict> getDictDetailsByDictTypeCode(String dictTypeCode, String searchText) {

        // 获取字典类型编码对应的字典类型id
        Long dictTypeId = dictTypeService.getDictTypeIdByDictTypeCode(dictTypeCode);

        if (dictTypeId == null) {
            return new ArrayList<>();
        }

        // 查询字典的列表
        DictRequest dictRequest = new DictRequest();
        dictRequest.setDictTypeId(dictTypeId);
        dictRequest.setSearchText(searchText);
        LambdaQueryWrapper<SysDict> wrapper = this.createWrapper(dictRequest);
        wrapper.select(SysDict::getDictId, SysDict::getDictName, SysDict::getDictCode);
        List<SysDict> dictList = this.list(wrapper);

        if (dictList.isEmpty()) {
            return new ArrayList<>();
        }

        // 转化成响应结果
        ArrayList<SimpleDict> simpleDictList = new ArrayList<>();
        for (SysDict sysDict : dictList) {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setCode(sysDict.getDictCode());
            simpleDict.setName(sysDict.getDictName());
            simpleDict.setId(sysDict.getDictId());
            simpleDictList.add(simpleDict);
        }

        return simpleDictList;
    }

    @Override
    public void deleteByDictId(Long dictId) {
        this.removeById(dictId);
    }

    @Override
    public DictDetail getDictByDictId(Long dictId) {
        if (dictId == null) {
            return new DictDetail();
        }

        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictId, dictId);
        sysDictLambdaQueryWrapper.select(SysDict::getDictName, SysDict::getDictCode, SysDict::getDictId, SysDict::getDictSort);
        SysDict sysDict = this.getOne(sysDictLambdaQueryWrapper, false);

        if (sysDict == null) {
            return new DictDetail();
        } else {
            return new DictDetail(sysDict.getDictId(), sysDict.getDictCode(), sysDict.getDictName(), sysDict.getDictSort());
        }
    }

    @Override
    public String getDictNameByDictId(Long dictId) {
        if (dictId == null) {
            return "";
        }

        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictId, dictId);
        sysDictLambdaQueryWrapper.select(SysDict::getDictName);
        SysDict sysDict = this.getOne(sysDictLambdaQueryWrapper, false);

        if (sysDict == null) {
            return "";
        } else {
            return sysDict.getDictName();
        }
    }

    @Override
    public void simpleAddDict(SimpleDictUpdateParam simpleDictUpdateParam) {

        // 组装添加字典的参数
        DictRequest dictRequest = new DictRequest();
        BeanUtil.copyProperties(simpleDictUpdateParam, dictRequest);

        // 设置为启用
        dictRequest.setStatusFlag(StatusEnum.ENABLE.getCode());

        // 添加字典
        this.add(dictRequest);
    }

    @Override
    public void simpleEditDict(SimpleDictUpdateParam simpleDictUpdateParam) {

        // 组装编辑字典的参数
        DictRequest dictRequest = new DictRequest();
        BeanUtil.copyProperties(simpleDictUpdateParam, dictRequest);

        // 更新字典
        this.edit(dictRequest);
    }

    @Override
    public String getPinyin(String name) {
        if (ObjectUtil.isNotEmpty(name)) {
            name = pinYinApi.parsePinyinString(name);
        }
        return name;
    }

    /**
     * 获取详细信息
     *
     * @author chenjinlong
     * @since 2021/1/13 10:50
     */
    private SysDict querySysDict(DictRequest dictRequest) {
        SysDict sysDict = this.getById(dictRequest.getDictId());
        if (ObjectUtil.isNull(sysDict)) {
            throw new DictException(DictExceptionEnum.DICT_NOT_EXISTED, dictRequest.getDictId());
        }
        return sysDict;
    }

    /**
     * 构建wrapper
     *
     * @author chenjinlong
     * @since 2021/1/13 10:50
     */
    private LambdaQueryWrapper<SysDict> createWrapper(DictRequest dictRequest) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();

        // 根据名称或者编码进行查询
        String searchText = dictRequest.getSearchText();
        if (ObjectUtil.isNotEmpty(searchText)) {
            queryWrapper.like(SysDict::getDictName, searchText);
            queryWrapper.or().like(SysDict::getDictCode, searchText);
            queryWrapper.or().like(SysDict::getDictNamePinyin, searchText);
        }

        // 根据字典类型id查询字典
        queryWrapper.eq(ObjectUtil.isNotEmpty(dictRequest.getDictTypeId()), SysDict::getDictTypeId, dictRequest.getDictTypeId());

        // 根据字典类型编码查询
        if (StrUtil.isNotBlank(dictRequest.getDictTypeCode())) {

            // 根据字典类型编码，获取字典类型的id
            Long dictTypeId = dictTypeService.getDictTypeIdByDictTypeCode(dictRequest.getDictTypeCode());
            if (dictTypeId != null) {
                queryWrapper.eq(SysDict::getDictTypeId, dictTypeId);
            } else {
                // 字典类型不存在，则查询一个不存在的字典类型id
                queryWrapper.eq(SysDict::getDictTypeId, -1L);
            }
        }

        // 排序
        queryWrapper.orderByAsc(SysDict::getDictSort);

        return queryWrapper;
    }

    /**
     * 检查添加和编辑字典是否有重复的编码和名称
     *
     * @author fengshuonan
     * @since 2021/5/12 16:58
     */
    private void validateRepeat(DictRequest dictRequest, boolean editFlag) {

        // 检验同字典类型下是否有一样的编码
        LambdaQueryWrapper<SysDict> sysDictLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysDictLambdaQueryWrapper.eq(SysDict::getDictTypeId, dictRequest.getDictTypeId());
        sysDictLambdaQueryWrapper.eq(SysDict::getDictCode, dictRequest.getDictCode());
        if (editFlag) {
            sysDictLambdaQueryWrapper.ne(SysDict::getDictId, dictRequest.getDictId());
        }
        long count = this.count(sysDictLambdaQueryWrapper);
        if (count > 0) {
            throw new DictException(DictExceptionEnum.DICT_CODE_REPEAT, dictRequest.getDictTypeId(), dictRequest.getDictCode());
        }

        // 检验同字典类型下是否有一样的名称
        LambdaQueryWrapper<SysDict> dictNameWrapper = new LambdaQueryWrapper<>();
        dictNameWrapper.eq(SysDict::getDictTypeId, dictRequest.getDictTypeId());
        dictNameWrapper.eq(SysDict::getDictName, dictRequest.getDictName());
        if (editFlag) {
            dictNameWrapper.ne(SysDict::getDictId, dictRequest.getDictId());
        }
        long dictNameCount = this.count(dictNameWrapper);
        if (dictNameCount > 0) {
            throw new DictException(DictExceptionEnum.DICT_NAME_REPEAT, dictRequest.getDictTypeId(), dictRequest.getDictCode());
        }
    }

    /**
     * 创建字典的pids的值
     * <p>
     * 如果pid是顶级节点，pids = 【[-1],】
     * <p>
     * 如果pid不是顶级节点，pids = 【父菜单的pids,[pid],】
     *
     * @author fengshuonan
     * @since 2023/6/27 17:24
     */
    private String createPids(Long dictParentId) {
        if (dictParentId.equals(TreeConstants.DEFAULT_PARENT_ID)) {
            return SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
        } else {
            //获取父字典
            LambdaQueryWrapper<SysDict> dictWrapper = new LambdaQueryWrapper<>();
            dictWrapper.eq(SysDict::getDictId, dictParentId);
            dictWrapper.select(SysDict::getDictPids);
            SysDict parentDictInfo = this.getOne(dictWrapper, false);
            if (parentDictInfo == null) {
                return SymbolConstant.LEFT_SQUARE_BRACKETS + TreeConstants.DEFAULT_PARENT_ID + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
            } else {
                // 组装pids
                return parentDictInfo.getDictPids() + SymbolConstant.LEFT_SQUARE_BRACKETS + dictParentId + SymbolConstant.RIGHT_SQUARE_BRACKETS + SymbolConstant.COMMA;
            }
        }
    }

}
