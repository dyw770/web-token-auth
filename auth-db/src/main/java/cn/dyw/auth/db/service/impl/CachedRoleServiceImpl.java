package cn.dyw.auth.db.service.impl;

import cn.dyw.auth.db.model.ParentRoleDto;
import cn.dyw.auth.db.model.RoleDto;
import cn.dyw.auth.db.service.ICachedRoleService;
import cn.dyw.auth.db.service.ISysRoleService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dyw770
 * @since 2025-06-06
 */
@Service
public class CachedRoleServiceImpl implements ICachedRoleService {
    
    private final ISysRoleService roleService;

    public CachedRoleServiceImpl(ISysRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public List<RoleDto> roleTree() {
        List<RoleDto> roleDtoList = roleService.roleList();

        Map<String, RoleDto> dtoMap = roleDtoList
                .stream()
                .collect(
                        Collectors
                                .toMap(RoleDto::getRoleCode, dto -> dto)
                );

        return roleDtoList
                .stream()
                .peek(dto -> {
                    if (StringUtils.isNotBlank(dto.getParentRoleCode())) {
                        RoleDto roleDto = dtoMap.get(dto.getParentRoleCode());
                        if (ObjectUtils.isNotEmpty(roleDto)) {
                            roleDto.addChildren(dto);
                        }
                    }
                }).filter(
                        dto -> StringUtils.isBlank(dto.getParentRoleCode())
                ).collect(Collectors.toList());
    }

    @Override
    public List<ParentRoleDto> parentRoleList() {
        List<RoleDto> roleDtoList = roleService.roleList();

        Map<String, RoleDto> dtoMap = roleDtoList
                .stream()
                .collect(
                        Collectors
                                .toMap(RoleDto::getRoleCode, dto -> dto)
                );

        return roleDtoList.stream()
                .map(roleDto -> {
                    ParentRoleDto parentRoleDto = new ParentRoleDto();
                    parentRoleDto.setRoleCode(roleDto.getRoleCode());
                    parentRoleDto.setParentRoleCode(new ArrayList<>());

                    parentRoleDto.getParentRoleCode().add(roleDto.getRoleCode());

                    Queue<RoleDto> queue = new LinkedList<>();
                    queue.add(roleDto);
                    while (!queue.isEmpty()) {
                        RoleDto poll = queue.poll();
                        if (!StringUtils.isBlank(poll.getParentRoleCode())) {
                            parentRoleDto.getParentRoleCode().add(poll.getParentRoleCode());
                            RoleDto dto = dtoMap.get(poll.getParentRoleCode());
                            if (ObjectUtils.isNotEmpty(dto)) {
                                queue.add(dto);
                            }
                        }
                    }
                    return parentRoleDto;
                }).toList();
    }
}
