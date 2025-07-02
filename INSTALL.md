# 安装指南

## 系统要求
- 操作系统：Windows 10/11, macOS 10.15+, Linux (64位)
- 内存：至少4GB RAM
- 存储空间：至少500MB可用空间

## 安装步骤

### 1. 下载软件
从GitHub Releases页面下载最新版本的Expense-Manager。

### 2. 安装依赖
在终端中运行以下命令安装必要的依赖：
```bash
pip install -r requirements.txt
```

### 3. 配置环境
- 确保Python 3.8+已安装。
- 配置环境变量以包含Python和依赖库的路径。

### 4. 启动软件
```bash
python main.py
```

## 故障排除

### 1. 无法启动软件
- 检查Python是否已正确安装并添加到环境变量中。
- 确保所有依赖项已正确安装。
- 查看错误日志以获取更多信息。

### 2. 依赖安装失败
- 确保你拥有管理员权限。
- 尝试使用`pip install --user -r requirements.txt`命令。
- 如果问题依旧，请在GitHub上提交问题。

### 3. 兼容性问题
- 确保你的操作系统和Python版本符合系统要求。
- 如果遇到兼容性问题，请尝试在虚拟环境中运行软件。

## 联系支持
如果你在安装过程中遇到任何问题，请参考FAQ文档或在GitHub上提交问题。
