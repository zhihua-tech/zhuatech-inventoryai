/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.inventoryai.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReplenishmentPlanReleaseService {
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (!request.sourceDataFresh()) blockers.add("库存与需求源数据过期");
        if (!request.inventoryReconciled()) blockers.add("账面库存与实物库存未对账");
        if (!request.supplierCapacityConfirmed()) blockers.add("供应商产能或配额未确认");
        if (!request.leadTimeValidated()) blockers.add("采购与运输提前期未验证");
        if (!request.safetyStockProtected()) blockers.add("安全库存约束未生效");
        if (!request.budgetApproved()) blockers.add("补货预算未批准");
        if (!request.overrideOwnerAssigned()) blockers.add("计划调整责任人未指定");
        if (!request.finalApprovalComplete()) blockers.add("补货计划审批未完成");
        if (!blockers.isEmpty()) {
            actions.add("阻断计划下发并完成库存、供应与预算校验");
            return new Assessment(Decision.BLOCKED, blockers, actions);
        }
        if (request.stockoutRiskHigh() || request.excessInventoryRiskHigh() || !request.rollbackReady()) {
            if (request.stockoutRiskHigh()) actions.add("人工复核缺货风险和优先分配策略");
            if (request.excessInventoryRiskHigh()) actions.add("复核滞销、保质期和库存资金占用");
            if (!request.rollbackReady()) actions.add("准备撤单、改量和供应商沟通方案");
            return new Assessment(Decision.PLANNER_REVIEW, blockers, actions);
        }
        actions.add("批准补货计划下发并监控到货、缺货和库存周转");
        return new Assessment(Decision.RELEASE, blockers, actions);
    }

    public record Request(@NotBlank String planId, boolean sourceDataFresh, boolean inventoryReconciled,
                          boolean supplierCapacityConfirmed, boolean leadTimeValidated,
                          boolean safetyStockProtected, boolean budgetApproved, boolean stockoutRiskHigh,
                          boolean excessInventoryRiskHigh, boolean overrideOwnerAssigned,
                          boolean finalApprovalComplete, boolean rollbackReady) {}
    public record Assessment(Decision decision, List<String> blockers, List<String> actions) {}
    public enum Decision { RELEASE, PLANNER_REVIEW, BLOCKED }
}
