/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.inventoryai.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/** 结合库存覆盖、交期、滞销和保质期信号生成库存动作。 */
@Service
public class InventoryAnalysisService {
    public Result optimize(Request request) {
        BigDecimal available = request.onHand().add(request.inbound());
        BigDecimal coverDays = request.averageDailyDemand().signum() == 0 ? new BigDecimal("999")
            : available.divide(request.averageDailyDemand(), 1, RoundingMode.HALF_UP);
        BigDecimal reorderPoint = request.averageDailyDemand().multiply(BigDecimal.valueOf(request.leadTimeDays() + 3L));
        int reorderQty = reorderPoint.subtract(available).max(BigDecimal.ZERO).setScale(0, RoundingMode.CEILING).intValue();
        int slowMovingScore = 0;
        if (coverDays.compareTo(new BigDecimal("90")) > 0) slowMovingScore += 40;
        if (request.daysSinceLastSale() > 45) slowMovingScore += 35;
        if (request.shelfLifeRemainingDays() > 0 && request.shelfLifeRemainingDays() < 30) slowMovingScore += 30;
        if (request.marginRate().compareTo(new BigDecimal("0.10")) < 0) slowMovingScore += 10;
        slowMovingScore = Math.min(100, slowMovingScore);
        String action = reorderQty > 0 ? "REPLENISH" : slowMovingScore >= 70 ? "LIQUIDATE"
            : coverDays.compareTo(new BigDecimal("60")) > 0 ? "REDUCE" : "HOLD";
        List<String> reasons = new ArrayList<>();
        if (reorderQty > 0) reasons.add("库存低于交期需求与安全缓冲");
        if (coverDays.compareTo(new BigDecimal("90")) > 0) reasons.add("库存覆盖天数超过 90 天");
        if (request.daysSinceLastSale() > 45) reasons.add("商品连续较长时间无销售");
        if (request.shelfLifeRemainingDays() > 0 && request.shelfLifeRemainingDays() < 30) reasons.add("临近保质期或有效期");
        if (reasons.isEmpty()) reasons.add("库存、需求和周转保持健康");
        return new Result(request.skuCode(), coverDays, reorderPoint.setScale(0, RoundingMode.CEILING),
            reorderQty, slowMovingScore, action, reasons, "LIQUIDATE".equals(action));
    }

    public record Request(@NotBlank String skuCode,
                          @DecimalMin("0") BigDecimal onHand,
                          @DecimalMin("0") BigDecimal inbound,
                          @DecimalMin("0") BigDecimal averageDailyDemand,
                          @Min(1) int leadTimeDays, @Min(0) int daysSinceLastSale,
                          @Min(0) int shelfLifeRemainingDays,
                          @DecimalMin("0") @DecimalMax("1") BigDecimal marginRate) {}
    public record Result(String skuCode, BigDecimal inventoryCoverDays, BigDecimal reorderPoint,
                         int recommendedReorderQuantity, int slowMovingRiskScore,
                         String action, List<String> reasons, boolean approvalRequired) {}
}
