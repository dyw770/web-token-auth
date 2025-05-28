package cn.dyw.auth.demo.controller

import cn.dyw.auth.db.domain.SysUser
import cn.dyw.auth.db.service.ISysUserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * kotlin集成
 * @author dyw770
 * @since 2025-05-28
 */
@RestController
@RequestMapping("/kotlin")
class KotlinController(val userServer: ISysUserService) {

    /**
     * kotlin 接口
     */
    @GetMapping("/list")
    fun list(): Result<List<SysUser>> {
        return Result.success(userServer.list())
    }
}