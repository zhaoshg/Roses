package cn.stylefeng.roses.kernel.dict.modular.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.constants.TreeConstants;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.dict.modular.entity.Area;
import cn.stylefeng.roses.kernel.dict.modular.enums.AreaExceptionEnum;
import cn.stylefeng.roses.kernel.dict.modular.mapper.AreaMapper;
import cn.stylefeng.roses.kernel.dict.modular.pojo.AreaVo;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.AreaRequest;
import cn.stylefeng.roses.kernel.dict.modular.service.AreaService;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 【系统基础】-【行政区域表】业务实现层
 *
 * @author LiYanJun
 * @date 2023/07/05 18:12
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

	@Override
	public void add(AreaRequest areaRequest) {
		Area area = new Area();
		BeanUtil.copyProperties(areaRequest, area);
		this.save(area);
	}

	@Override
	public void del(AreaRequest areaRequest) {
		Area area = this.queryArea(areaRequest);
		this.removeById(area.getAreaId());
	}

	@Override
	public void edit(AreaRequest areaRequest) {
		Area area = this.queryArea(areaRequest);
		BeanUtil.copyProperties(areaRequest, area);
		this.updateById(area);
	}

	@Override
	public Area detail(AreaRequest areaRequest) {
		return this.queryArea(areaRequest);
	}

	@Override
	public PageResult<AreaVo> findPage(AreaRequest areaRequest) {
		Page<AreaVo> page = PageFactory.defaultPage();
		List<AreaVo> list = baseMapper.customFindList(page, areaRequest);
		PageResult<AreaVo> pageResult = PageResultFactory.createPageResult(page.setRecords(list));
		return pageResult;
	}

	@Override
	public List<Area> findList(AreaRequest areaRequest) {

		LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();
		String parentId = areaRequest.getParentId();
		if(ObjectUtil.isEmpty(parentId)) {
			parentId=TreeConstants.DEFAULT_PARENT_ID.toString();
		} 
		queryWrapper.select(Area::getAreaId,Area::getAreaName,Area::getAreaCode);
		queryWrapper.eq(Area::getParentId, parentId);
		queryWrapper.orderByAsc(Area::getAreaSort);
		return this.list(queryWrapper);
	}

	/**
	 * 获取信息
	 *
	 * @author LiYanJun
	 * @date 2023/07/05 18:12
	 */
	private Area queryArea(AreaRequest areaRequest) {
		Area area = this.getById(areaRequest.getAreaId());
		if (ObjectUtil.isEmpty(area)) {
			throw new ServiceException(AreaExceptionEnum.AREA_NOT_EXISTED);
		}
		return area;
	}

	/**
	 * 创建查询wrapper
	 *
	 * @author LiYanJun
	 * @date 2023/07/05 18:12
	 */
	private LambdaQueryWrapper<Area> createWrapper(AreaRequest areaRequest) {
		LambdaQueryWrapper<Area> queryWrapper = new LambdaQueryWrapper<>();

		Long areaId = areaRequest.getAreaId();
		String areaCode = areaRequest.getAreaCode();
		String areaName = areaRequest.getAreaName();
		String parentId = areaRequest.getParentId();
		Integer areaLevel = areaRequest.getAreaLevel();
		BigDecimal areaSort = areaRequest.getAreaSort();
		String delFlag = areaRequest.getDelFlag();
		String areaPids = areaRequest.getAreaPids();

		queryWrapper.eq(ObjectUtil.isNotNull(areaId), Area::getAreaId, areaId);
		queryWrapper.like(ObjectUtil.isNotEmpty(areaCode), Area::getAreaCode, areaCode);
		queryWrapper.like(ObjectUtil.isNotEmpty(areaName), Area::getAreaName, areaName);
		queryWrapper.like(ObjectUtil.isNotEmpty(parentId), Area::getParentId, parentId);
		queryWrapper.eq(ObjectUtil.isNotNull(areaLevel), Area::getAreaLevel, areaLevel);
		queryWrapper.eq(ObjectUtil.isNotNull(areaSort), Area::getAreaSort, areaSort);
		queryWrapper.like(ObjectUtil.isNotEmpty(delFlag), Area::getDelFlag, delFlag);
		queryWrapper.like(ObjectUtil.isNotEmpty(areaPids), Area::getAreaPids, areaPids);

		return queryWrapper;
	}

}