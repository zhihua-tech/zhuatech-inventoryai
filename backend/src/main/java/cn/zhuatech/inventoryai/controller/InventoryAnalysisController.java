/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.inventoryai.controller;

import cn.zhuatech.inventoryai.common.ApiResponse;
import cn.zhuatech.inventoryai.service.InventoryAnalysisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/inventory")
@PreAuthorize("hasAnyRole('DOMAIN_USER','DOMAIN_OPERATOR','ADMIN')")
public class InventoryAnalysisController {
    private final InventoryAnalysisService service;
    public InventoryAnalysisController(InventoryAnalysisService service) { this.service = service; }
    @PostMapping("/optimize")
    public ApiResponse<InventoryAnalysisService.Result> optimize(@Valid @RequestBody InventoryAnalysisService.Request request) {
        return ApiResponse.ok("库存健康分析完成", service.optimize(request));
    }
}
