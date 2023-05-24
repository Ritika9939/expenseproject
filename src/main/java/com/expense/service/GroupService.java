package com.expense.service;

import com.expense.entity.Group;
import com.expense.repository.GroupRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Group createGroup(Group newGroup) {
        return groupRepository.save(newGroup);
    }

    public Optional<Group> updateGroup(Long id, Group updatedGroup) {
        Optional<Group> existingGroup = groupRepository.findById(id);

        if (existingGroup.isPresent()) {
            updatedGroup.setId(id);
            return Optional.of(groupRepository.save(updatedGroup));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteGroup(Long id) {
        Optional<Group> group = groupRepository.findById(id);

        if (group.isPresent()) {
            groupRepository.delete(group.get());
            return true;
        } else {
            return false;
        }
    }
}

