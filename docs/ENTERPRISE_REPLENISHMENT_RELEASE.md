# 企业级智能补货计划发布

`POST /api/enterprise/inventoryai/replenishment-plan-release` 检查数据新鲜度、库存对账、供应能力、提前期、安全库存、预算、缺货与积压风险、责任人、审批和撤回方案，返回 `RELEASE / PLANNER_REVIEW / BLOCKED`。

生产使用时应与 ERP、WMS、采购和供应商协同系统对接，并对建议转订单的全过程保留版本、人工调整和审批记录。
