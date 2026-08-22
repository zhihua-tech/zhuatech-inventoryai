/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.inventoryai;

import cn.zhuatech.inventoryai.service.InventoryAnalysisService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class InventoryAnalysisServiceTests {
    private final InventoryAnalysisService service = new InventoryAnalysisService();
    @Test void replenishesWhenLeadTimeDemandExceedsStock() {
        var result = service.optimize(new InventoryAnalysisService.Request("SKU-01", new BigDecimal("80"), new BigDecimal("20"), new BigDecimal("30"), 7, 1, 365, new BigDecimal("0.30")));
        assertThat(result.action()).isEqualTo("REPLENISH");
        assertThat(result.recommendedReorderQuantity()).isPositive();
    }
    @Test void liquidatesAgedExcessInventory() {
        var result = service.optimize(new InventoryAnalysisService.Request("SKU-02", new BigDecimal("1000"), BigDecimal.ZERO, new BigDecimal("5"), 5, 80, 20, new BigDecimal("0.08")));
        assertThat(result.action()).isEqualTo("LIQUIDATE");
        assertThat(result.approvalRequired()).isTrue();
    }
}
