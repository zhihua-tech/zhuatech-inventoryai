/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.inventoryai.controller;

import cn.zhuatech.inventoryai.common.ApiResponse;
import cn.zhuatech.inventoryai.service.ReplenishmentPlanReleaseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enterprise/inventoryai")
public class ReplenishmentPlanReleaseController {
    private final ReplenishmentPlanReleaseService service;
    public ReplenishmentPlanReleaseController(ReplenishmentPlanReleaseService service) { this.service = service; }
    @PostMapping("/replenishment-plan-release")
    public ApiResponse<ReplenishmentPlanReleaseService.Assessment> assess(
            @Valid @RequestBody ReplenishmentPlanReleaseService.Request request) {
        return ApiResponse.ok(service.assess(request));
    }
}
