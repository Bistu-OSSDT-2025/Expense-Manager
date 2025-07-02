# 💰 Expense-Manager - 轻量级记账应用

> 一个简洁易用的移动端记账应用，帮助用户轻松记录收支、管理分类预算
## 简介:出于传统记账方式繁琐且不可移动性，我们提出了一个用于移动端的记账方式设想，致力于开发一个简单的移动端记账应用，允许用户添加收入和支出，并显示每月的预算总结，并包含分类功能，例如日常开销、餐饮、娱乐等。

## ✨ 功能特性
- **快速记账**：一键添加收入/支出，支持金额、分类、日期、备注
- **智能分类**：餐饮、购物、交通、娱乐等预设分类（支持自定义）
- **预算管理**：实时显示本月支出总额与预算对比
- **数据可视化**：月度收支趋势图表
- **本地存储**：采用 SQLite 数据库保存数据，无需网络
- **暗色模式**：支持深色/浅色主题切换

## 📥 安装指南
### 环境要求


### 运行步骤

## 🚀 快速使用（用户指引）
1. **添加记录**：点击底部 ➕ 按钮
2. **选择类型**：收入（绿色）或支出（红色）
3. **填写信息**：金额 + 分类 + 备注
4. **查看统计**：首页顶部显示本月预算进度
5. **切换月份**：左右滑动顶部日历区域

![](screenshots/add-expense.png) <!-- 操作截图示例 -->

## 🛠️ 技术栈
- **语言**: Kotlin (主) + Java
- **数据库**: SQLite + [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **架构**: MVVM (Model-View-ViewModel)
- **依赖注入**: [Hilt](https://dagger.dev/hilt/)
- **图表**: [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- **异步**: Kotlin Coroutines + Flow

## 🤝 如何贡献
欢迎通过 Issues 和 Pull Requests 参与改进！  
👉 详见 [credits](https://github.com/Bistu-OSSDT-2025/Expense-Manager/blob/xie%E2%80%98s/CREDITS)

## 📄 许可证
本项目采用 Apache Lisence 2.0 协议 - 详情见 [lisence](https://github.com/Bistu-OSSDT-2025/Expense-Manager/blob/main/LICENSE)文件

## 👨‍💻 开发团队
| 成员 | 角色 |  
| 李婧 | DevOps |  
| 张思琪 | 前端开发 |  
| 常小北/孙星宜 | 后端/数据库 |  
| 谢萌晶 | QA & 文档 |  

> 此项目为北京信息科技大学2025年小学期《开源软件开发技术》课程第16组的课程作业
