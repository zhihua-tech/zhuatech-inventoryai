# InventoryAI API

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录并获取 JWT |
| GET | `/api/admin/dashboard` | 库存健康运营中心 |
| GET | `/api/admin/work-orders` | 库存优化任务 |
| GET | `/api/shopfloor/dashboard` | 库存分析师工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交库存反馈 |
| POST | `/api/ai/inventory/optimize` | 覆盖天数、补货点、滞销风险与动作建议 |
| POST | `/api/shopfloor/ai-risk-assessment` | AI 功能上线风险初筛 |

除登录外均需 `Authorization: Bearer <token>`。
