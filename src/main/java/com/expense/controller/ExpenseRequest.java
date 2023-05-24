package com.expense.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {
    private String description;
    private double amount;
    private Long userId;
    private Long groupId;
    private List<Long> participantIds;
}
