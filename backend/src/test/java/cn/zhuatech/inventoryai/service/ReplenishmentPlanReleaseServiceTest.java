/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.inventoryai.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class ReplenishmentPlanReleaseServiceTest {
    private final ReplenishmentPlanReleaseService service = new ReplenishmentPlanReleaseService();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void releasesControlledPlan() {
        var result = service.assess(new ReplenishmentPlanReleaseService.Request("P1", true, true, true, true,
                true, true, false, false, true, true, true));
        assertThat(result.decision()).isEqualTo(ReplenishmentPlanReleaseService.Decision.RELEASE);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void reviewsInventoryRisks() {
        var result = service.assess(new ReplenishmentPlanReleaseService.Request("P2", true, true, true, true,
                true, true, true, true, true, true, false));
        assertThat(result.actions()).hasSize(3);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksUncontrolledPlan() {
        var result = service.assess(new ReplenishmentPlanReleaseService.Request("P3", false, false, false, false,
                false, false, false, false, false, false, true));
        assertThat(result.blockers()).hasSize(8);
    }
}
