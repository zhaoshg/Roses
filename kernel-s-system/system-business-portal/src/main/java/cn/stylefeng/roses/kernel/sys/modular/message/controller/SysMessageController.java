package cn.stylefeng.roses.kernel.sys.modular.message.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.sys.modular.message.entity.SysMessage;
import cn.stylefeng.roses.kernel.sys.modular.message.pojo.request.SysMessageRequest;
import cn.stylefeng.roses.kernel.sys.modular.message.service.SysMessageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统消息控制器
 *
 * @author fengshuonan
 * @since 2024/01/12 17:31
 */
@RestController
@ApiResource(name = "系统消息")
public class SysMessageController {

    @Resource
    private SysMessageService sysMessageService;

    /**
     * 添加系统消息
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @PostResource(name = "添加系统消息", path = "/sysMessage/add")
    public ResponseData<SysMessage> add(@RequestBody @Validated(SysMessageRequest.add.class) SysMessageRequest sysMessageRequest) {
        sysMessageService.add(sysMessageRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除系统消息
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @PostResource(name = "删除系统消息", path = "/sysMessage/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysMessageRequest.delete.class) SysMessageRequest sysMessageRequest) {
        sysMessageService.del(sysMessageRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 批量删除系统消息
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @PostResource(name = "批量删除系统消息", path = "/sysMessage/batchDelete")
    public ResponseData<?> batchDelete(@RequestBody @Validated(BaseRequest.batchDelete.class) SysMessageRequest sysMessageRequest) {
        sysMessageService.batchDelete(sysMessageRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑系统消息
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @PostResource(name = "编辑系统消息", path = "/sysMessage/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysMessageRequest.edit.class) SysMessageRequest sysMessageRequest) {
        sysMessageService.edit(sysMessageRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看系统消息详情
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @GetResource(name = "查看系统消息详情", path = "/sysMessage/detail")
    public ResponseData<SysMessage> detail(@Validated(SysMessageRequest.detail.class) SysMessageRequest sysMessageRequest) {
        return new SuccessResponseData<>(sysMessageService.detail(sysMessageRequest));
    }

    /**
     * 获取系统消息列表
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @GetResource(name = "获取系统消息列表", path = "/sysMessage/list")
    public ResponseData<List<SysMessage>> list(SysMessageRequest sysMessageRequest) {
        return new SuccessResponseData<>(sysMessageService.findList(sysMessageRequest));
    }

    /**
     * 获取系统消息列表（带分页）
     *
     * @author fengshuonan
     * @since 2024/01/12 17:31
     */
    @GetResource(name = "获取系统消息列表（带分页）", path = "/sysMessage/page")
    public ResponseData<PageResult<SysMessage>> page(SysMessageRequest sysMessageRequest) {
        return new SuccessResponseData<>(sysMessageService.findPage(sysMessageRequest));
    }

}
