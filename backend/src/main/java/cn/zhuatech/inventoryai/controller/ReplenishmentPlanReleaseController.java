/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.inventoryai.controller;

import cn.zhuatech.inventoryai.common.ApiResponse;
import cn.zhuatech.inventoryai.service.ReplenishmentPlanReleaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/inventoryai")
public class ReplenishmentPlanReleaseController {
    private final ReplenishmentPlanReleaseService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ReplenishmentPlanReleaseController(ReplenishmentPlanReleaseService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/replenishment-plan-release")
    public ApiResponse<ReplenishmentPlanReleaseService.Assessment> assess(
            @Valid @RequestBody ReplenishmentPlanReleaseService.Request request) {
        return ApiResponse.ok(service.assess(request));
    }
}
