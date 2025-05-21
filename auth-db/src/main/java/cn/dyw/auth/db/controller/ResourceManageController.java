package cn.dyw.auth.db.controller;

import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.domain.SysApiResourceAuth;
import cn.dyw.auth.db.message.rq.*;
import cn.dyw.auth.db.service.ISysApiResourceAuthService;
import cn.dyw.auth.db.service.ISysApiResourceService;
import cn.dyw.auth.event.AuthChangedApplicationEvent;
import cn.dyw.auth.message.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-14
 */
@Validated
@RestController
@RequestMapping("${app.auth.api-context-path:/admin}/resource")
public class ResourceManageController {

    private final ISysApiResourceService apiResourceService;

    private final ISysApiResourceAuthService apiResourceAuthService;
    
    private final ApplicationContext applicationContext;

    public ResourceManageController(ISysApiResourceService apiResourceService, 
                                    ISysApiResourceAuthService apiResourceAuthService, 
                                    ApplicationContext applicationContext) {
        this.apiResourceService = apiResourceService;
        this.apiResourceAuthService = apiResourceAuthService;
        this.applicationContext = applicationContext;
    }
    
    @GetMapping("/auth/refresh")
    public Result<Void> refresh() {
        AuthChangedApplicationEvent event = new AuthChangedApplicationEvent(new Object());
        applicationContext.publishEvent(event);
        return Result.createSuccess();
    }

    /**
     * 资源列表
     *
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
     * 获取资源详情
     *
     * @param resourceId 资源ID
     * @return 资源详情
     */
    @GetMapping("/{resourceId}")
    public Result<SysApiResource> get(@PathVariable("resourceId") @NotNull @Min(1) Integer resourceId) {
        SysApiResource resource = apiResourceService.getById(resourceId);
        return Result.createSuccess(resource);
    }

    /**
     * 保存资源
     *
     * @param resource 资源
     * @return 结果
     */
    @PostMapping("save")
    public Result<Void> save(@RequestBody @Validated ResourceSaveRq resource) {
        SysApiResource apiResource = new SysApiResource();
        BeanUtils.copyProperties(resource, apiResource);
        apiResource.setUpdateTime(LocalDateTime.now());
        apiResource.setCreateTime(LocalDateTime.now());
        apiResource.setEnable(false);
        apiResourceService.save(apiResource);
        return Result.createSuccess();
    }

    /**
     * 启用禁用资源
     *
     * @param enable     启用状态
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
     *
     * @param rq 源
     * @return 结果
     */
    @PostMapping("update")
    public Result<Void> update(@RequestBody @Validated ResourceUpdateRq rq) {
        SysApiResource apiResource = new SysApiResource();
        BeanUtils.copyProperties(rq, apiResource);
        apiResource.setUpdateTime(LocalDateTime.now());
        apiResource.setEnable(null);
        apiResourceService.updateById(apiResource);
        return Result.createSuccess();
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     * @return 结果
     */
    @DeleteMapping("delete/{resourceId}")
    public Result<Void> delete(@PathVariable("resourceId") @NotNull @Min(1) Integer resourceId) {
        apiResourceService.removeById(resourceId);
        return Result.createSuccess();
    }

    /**
     * 获取资源的授权列表
     * @param resourceId 资源ID
     * @return 授权列表
     */
    @GetMapping("/auth/{resourceId}")
    public Result<List<SysApiResourceAuth>> authList(@PathVariable("resourceId") @NotNull @Min(1) Integer resourceId) {
        List<SysApiResourceAuth> list = apiResourceAuthService.lambdaQuery()
                .eq(SysApiResourceAuth::getApiResourceId, resourceId)
                .list();
        return Result.createSuccess(list);
    }
    
    /**
     * 添加资源授权
     * @param auth 授权
     * @return 结果
     */
    @PostMapping("/auth/add")
    public Result<Void> authAdd(@RequestBody @Validated ApiResourceAuthAddRq auth) {
        SysApiResourceAuth resourceAuth = new SysApiResourceAuth();
        BeanUtils.copyProperties(auth, resourceAuth);
        resourceAuth.setAuthTime(LocalDateTime.now());
        resourceAuth.setExpiredTime(LocalDateTime.now());
        apiResourceAuthService.save(resourceAuth);
        return Result.createSuccess();
    }
    
    /**
     * 更新资源授权
     * @param auth 授权
     * @return 结果
     */
    @PostMapping("/auth/update")
    public Result<Void> authUpdate(@RequestBody @Validated ApiResourceAuthUpdateRq auth) {
        apiResourceAuthService.lambdaUpdate()
                .eq(SysApiResourceAuth::getAuthId, auth.getAuthId())
                .set(SysApiResourceAuth::getApiResourceId, auth.getApiResourceId())
                .set(SysApiResourceAuth::getAuthType, auth.getAuthType())
                .set(SysApiResourceAuth::getAuthObject, auth.getAuthObject())
                .update();
        return Result.createSuccess();
    }
    
    /**
     * 删除资源授权
     * @param authId 授权id
     * @return 删除结果
     */
    @DeleteMapping("/auth/delete/{authId}")
    public Result<Void> authDelete(@PathVariable @NotNull @Min(1) Integer authId) {
        apiResourceAuthService.removeById(authId);
        return Result.createSuccess();
    }
}
