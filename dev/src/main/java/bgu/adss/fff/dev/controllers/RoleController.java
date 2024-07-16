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

    /**
     * Establishes a new role in the system.
     * @param role The role containing the following data: <br>
     *             - {@code String name}: Role's name.<br>
     *             - {@code boolean isShiftManager}: Whether the role is a shift manager.
     * @return ResponseEntity containing the created role if successful, or a bad request status if the input is invalid.
     */
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto role) {
        Role roleEntity = map(role);
        return new ResponseEntity<>(map(service.createRole(roleEntity)), HttpStatus.CREATED);
    }

    /**
     * Fetches all roles in the system.
     * @return ResponseEntity containing an array of RoleDto or no content if none exist.
     */
    @GetMapping
    public ResponseEntity<RoleDto[]> getRoles() {
        RoleDto[] roles = new RoleDto[0];
        return ResponseEntity.ok().body(service.getRoles()
                .stream()
                .map(RoleMapper::map)
                .toList()
                .toArray(roles));
    }

    /**
     * Fetches a role by its name.
     * @param name
     * @return ResponseEntity containing the role as a RoleDto if it exists, or a not found status if it does not.
     */
    @GetMapping("/{name}")
    public ResponseEntity<RoleDto> getRole(@PathVariable("name") String name) {
        return ResponseEntity.ok(map(service.getRole(name)));
    }

    /**
     * Removes a role by its name.
     * @param name
     * @return ResponseEntity containing no content if successful, or a not found status if the role does not exist.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<?> removeRole(@PathVariable("name") String name) {
        service.removeRole(name);
        return ResponseEntity.noContent().build();
    }
}