package com.expense.controller;

import com.expense.dto.GroupRequest;
import com.expense.entity.Expense;
import com.expense.entity.Group;
import com.expense.repository.ExpenseRepository;
import com.expense.repository.GroupRepository;
import com.expense.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private ExpenseRepository expenseRepository;

    public GroupController(GroupRepository groupRepository, UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody GroupRequest groupRequest) {
        Group group = new Group();
        group.setName(groupRequest.getName());
        group.setMembers(userRepository.findAllById(groupRequest.getMemberIds()));
        groupRepository.save(group);

        return ResponseEntity.ok("Group created successfully");
    }

    // API to get all groups
    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // API to get group by ID
    @GetMapping("/{groupId}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if (group.isPresent()) {
            return ResponseEntity.ok(group.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<String> assignExpenseToGroup(@PathVariable Long groupId, @RequestBody ExpenseRequest expenseRequest) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            Expense expense = new Expense();
            expense.setDescription(expenseRequest.getDescription());
            expense.setAmount(expenseRequest.getAmount());
            expense.setPaidBy(userRepository.findById(expenseRequest.getUserId()).orElse(null));
            expense.setGroup(group);
            expense.setParticipants(userRepository.findAllById(expenseRequest.getParticipantIds()));

            expenseRepository.save(expense);
            return ResponseEntity.ok("Expense assigned to the group successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
