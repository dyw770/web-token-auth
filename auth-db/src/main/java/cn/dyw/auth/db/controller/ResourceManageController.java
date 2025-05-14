package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.message.rq.ResourceSearchRq;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.message.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author dyw770
 * @since 2025-05-14
 */
@Validated
@RestController
@RequestMapping("${app.auth.jdbc.api-context-path:/admin}/resource")
public class ResourceManageController {

    private final ISysApiResourceService apiResourceService;

    public ResourceManageController(ISysApiResourceService apiResourceService) {
        this.apiResourceService = apiResourceService;
    }

    /**
     * 资源列表
     * @param rq 请求参数
     * @return 结果
     */
    @PostMapping("list")
    public Result<Page<SysApiResource>> search(@RequestBody @Validated ResourceSearchRq rq) {
        Page<SysApiResource> page = apiResourceService.lambdaQuery()
                .likeRight(StringUtils.isNotBlank(rq.getApiPath()), SysApiResource::getApiPath, rq.getApiPath())
                .like(StringUtils.isNotBlank(rq.getDescription()), SysApiResource::getDescription, rq.getDescription())
                .eq(StringUtils.isNotBlank(rq.getApiMethod()), SysApiResource::getApiMethod, rq.getApiMethod())
                .eq(ObjectUtils.isNotEmpty(rq.getMatchType()), SysApiResource::getMatchType, rq.getMatchType())
                .page(rq.toPage());
        return Result.createSuccess(page);
    }
    
    /**
     * 保存资源
     * @param resource 资源
     * @return 结果
     */
    @PostMapping("save")
    public Result<Void> save(@RequestBody SysApiResource resource) {
        apiResourceService.save(resource);
        return Result.createSuccess();
    }

    /**
     * 启用禁用资源
     * @param enable 启用状态
     * @param resourceId api 资源ID
     * @return 结果
     */
    @GetMapping("/enable/{resourceId}/{enable}")
    public Result<Void> enable(@PathVariable("enable") @NotNull Boolean enable,
                               @PathVariable("resourceId") @NotNull Integer resourceId) {
        apiResourceService.lambdaUpdate()
                .eq(SysApiResource::getId, resourceId)
                .set(SysApiResource::getEnable, enable)
                .update();
        return Result.createSuccess();
    }
    
    /**
     * 更新资源
     * @param resource 源
     * @return 结果
     */
    @PostMapping("update")
    public Result<Void> update(@RequestBody SysApiResource resource) {
        apiResourceService.updateById(resource);
        return Result.createSuccess();
    }
    
    /**
     * 删除资源
     * @param resource 资源
     * @return 结果
     */
    @PostMapping("delete")
    public Result<Void> delete(@RequestBody SysApiResource resource) {
        apiResourceService.removeById(resource);
        return Result.createSuccess();
    }
}
