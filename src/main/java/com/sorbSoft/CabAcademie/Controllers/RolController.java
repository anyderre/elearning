package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Entities.Error.MessageResponse;
import com.sorbSoft.CabAcademie.Entities.Rol;
import com.sorbSoft.CabAcademie.Services.Dtos.Info.RolInfo;
import com.sorbSoft.CabAcademie.Services.Dtos.ViewModel.RolViewModel;
import com.sorbSoft.CabAcademie.Services.RolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dany on 20/05/2018.
 */
@RestController
@RequestMapping("/api/role")
public class RolController {
    @Autowired
    private RolServices roleService;

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<RolViewModel> getRole(@PathVariable Long id){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        RolViewModel vm= roleService.getRoleViewModel(id);
        if(vm==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<RolViewModel> getRoleViewModel(){
        RolViewModel vm= roleService.getRoleViewModel(null);
        if(vm==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(vm, HttpStatus.OK);
    }

    @GetMapping(value = "/all" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rol>> findAllRoles(){
        List<Rol> roles= roleService.fetchAllRole();
        if(roles.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping(value = "/info" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RolInfo>> findRoleInfo(){
        List<RolInfo> roles= roleService.getRolInfo();
        if(roles.isEmpty())
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping(value= "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public  ResponseEntity<MessageResponse> saveRole(@Valid @RequestBody RolViewModel role){
        Pair<String, Rol> result = roleService.saveRole(role);
        if(result.getSecond() == null)
            return new ResponseEntity<>(MessageResponse.of(result.getFirst()), HttpStatus.CONFLICT);
        return  new ResponseEntity<>(MessageResponse.of(result.getFirst()), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> deleteRole(@PathVariable Long id ){
        if(id<0)
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(roleService.fetchRole(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        roleService.deleteRol(id);
        return new ResponseEntity<>("Role with "+ id+" Deleted!", HttpStatus.OK);
    }
}
