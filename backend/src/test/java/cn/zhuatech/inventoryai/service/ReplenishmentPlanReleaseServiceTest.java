/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.inventoryai.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ReplenishmentPlanReleaseServiceTest {
    private final ReplenishmentPlanReleaseService service = new ReplenishmentPlanReleaseService();
    @Test void releasesControlledPlan() {
        var result = service.assess(new ReplenishmentPlanReleaseService.Request("P1", true, true, true, true,
                true, true, false, false, true, true, true));
        assertThat(result.decision()).isEqualTo(ReplenishmentPlanReleaseService.Decision.RELEASE);
    }
    @Test void reviewsInventoryRisks() {
        var result = service.assess(new ReplenishmentPlanReleaseService.Request("P2", true, true, true, true,
                true, true, true, true, true, true, false));
        assertThat(result.actions()).hasSize(3);
    }
    @Test void blocksUncontrolledPlan() {
        var result = service.assess(new ReplenishmentPlanReleaseService.Request("P3", false, false, false, false,
                false, false, false, false, false, false, true));
        assertThat(result.blockers()).hasSize(8);
    }
}
