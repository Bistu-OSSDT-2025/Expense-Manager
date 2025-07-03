package com.example.keepb.bean;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String id;
    private String userId;
    private double amount;          // 金额
    private int type;               // 1-收入，2-支出  
    private String category;        // 分类
    private String description;     // 描述
    private String createTime;      // 时间
    private String aiAnalysis;      // AI分析

    public Transaction() {}

    public Transaction(String userId, double amount, int type, String category, String description, String createTime) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.createTime = createTime;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    
    public String getAiAnalysis() { return aiAnalysis; }
    public void setAiAnalysis(String aiAnalysis) { this.aiAnalysis = aiAnalysis; }
    
    // 便捷方法
    public boolean isIncome() { return type == 1; }
    public boolean isExpense() { return type == 2; }
    public String getTypeString() { return type == 1 ? "收入" : "支出"; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", createTime='" + createTime + '\'' +
                ", aiAnalysis='" + aiAnalysis + '\'' +
                '}';
    }
} 