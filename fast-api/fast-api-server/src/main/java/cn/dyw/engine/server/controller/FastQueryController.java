package cn.dyw.engine.server.controller;

import cn.dyw.auth.message.Result;
import cn.dyw.engine.core.context.DynamicFilterParameter;
import cn.dyw.engine.core.exec.ExecResult;
import cn.dyw.engine.server.message.rq.ExecParameterRq;
import cn.dyw.engine.server.service.IQueryService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 * 查询接口
 *
 * @author dyw770
 * @since 2026-03-27
 */
@RestController
@RequestMapping("${app.fast-api.api-path:/query}")
public class FastQueryController {

    private final IQueryService queryService;

    public final String DEFAULT_PATH_PREFIX = "/query";

    @Value("${app.fast-api.api-path:/query}")
    private String apiPathPrefix;
    
    public FastQueryController(IQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * 执行查询
     *
     * @param rq 请求参数
     * @return 执行结果
     */
    @PostMapping("/**")
    public Result<ExecResult> postQuery(@RequestBody ExecParameterRq rq, HttpServletRequest request) {
        String apiPath = getApiPath(request);
        return Result.createSuccess(queryService.execApi(rq, apiPath));
    }

    /**
     * 执行查询
     *
     * @param map 参数
     * @return 执行结果
     */
    @GetMapping("/**")
    public Result<ExecResult> getQuery(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        String apiPath = getApiPath(request);
        ExecParameterRq rq = new ExecParameterRq();
        rq.setParameters(
                map.entrySet()
                        .stream()
                        .map(item ->
                                new DynamicFilterParameter(item.getKey(), item.getValue()))
                        .toList()
        );
        return Result.createSuccess(queryService.execApi(rq, apiPath));
    }
    

    private String getApiPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        int i = StringUtils.indexOf(requestPath, apiPathPrefix);
        return requestPath.substring(i + apiPathPrefix.length());
    }
}
