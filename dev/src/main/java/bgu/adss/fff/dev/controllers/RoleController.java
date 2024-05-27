package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.controllers.mappers.RoleMapper;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static bgu.adss.fff.dev.controllers.mappers.RoleMapper.map;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService service;

    @Autowired
    public RoleController(RoleService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto role) {
        Role roleEntity = map(role);
        return new ResponseEntity<>(map(service.createRole(roleEntity)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<RoleDto[]> getRoles() {
        RoleDto[] roles = new RoleDto[0];
        return ResponseEntity.ok().body(service.getRoles()
                .stream()
                .map(RoleMapper::map)
                .toList()
                .toArray(roles));
    }

    @GetMapping("/{name}")
    public ResponseEntity<RoleDto> getRole(@PathVariable("name") String name) {
        return ResponseEntity.ok(map(service.getRole(name)));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> removeRole(@PathVariable("name") String name) {
        service.removeRole(name);
        return ResponseEntity.noContent().build();
    }
}