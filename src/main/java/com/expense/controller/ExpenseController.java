package com.expense.controller;

import com.expense.entity.Expense;
import com.expense.entity.Group;
import com.expense.entity.User;
import com.expense.repository.ExpenseRepository;
import com.expense.repository.GroupRepository;
import com.expense.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody ExpenseRequest expenseRequest) {

        User currentUser = userRepository.findById(expenseRequest.getUserId()).orElse(null);
        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }


        Group group = groupRepository.findById(expenseRequest.getGroupId()).orElse(null);
        if (group == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group not found");
        }

        // Create a new Expense instance
        Expense expense = new Expense();
        expense.setDescription(expenseRequest.getDescription());
        expense.setAmount(expenseRequest.getAmount());
        expense.setPaidBy(currentUser);
        expense.setGroup(group);


        List<User> participants = userRepository.findAllById(expenseRequest.getParticipantIds());
        expense.setParticipants(participants);


        double totalParticipants = participants.size() + 1; // +1 for the user adding the expense
        double share = expense.getAmount() / totalParticipants;


        for (User participant : participants) {
            participant.setBalance(participant.getBalance() + share);
        }
        currentUser.setBalance(currentUser.getBalance() - (share * totalParticipants));


        expenseRepository.save(expense);
        userRepository.saveAll(participants);
        userRepository.save(currentUser);

        return ResponseEntity.ok("Expense added successfully");
    }
}