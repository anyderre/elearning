package com.sorbSoft.CabAcademie.Controllers;

import com.sorbSoft.CabAcademie.Services.VoucherService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voucher")
@Log4j2
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @GetMapping(value = "/create")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Crete new voucher, Role:ROLE_SUPER_ADMIN, ROLE_ADMIN")
    public ResponseEntity<String> createVoucher(){
        return new ResponseEntity<>(voucherService.createVoucher(), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    @ApiOperation(value = "Get all vouchers, Role:ROLE_SUPER_ADMIN")
    public ResponseEntity<List<String>> getAllVouchers(){
        return new ResponseEntity<>(voucherService.getAllVouchers(), HttpStatus.OK);
    }

    @GetMapping("/verify/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Add user by School/Org Admin, Role:ROLE_ADMIN")
    public  ResponseEntity<Boolean> verify(@PathVariable String voucherId) {
        if (voucherService.verify(voucherId)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping("/apply/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Add user by School/Org Admin, Role:ROLE_ADMIN")
    public  ResponseEntity<Boolean> apply(@PathVariable String voucherId) {
        if (voucherService.apply(voucherId)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @DeleteMapping("/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Add user by School/Org Admin, Role:ROLE_ADMIN")
    public  ResponseEntity<Boolean> delete(@PathVariable String voucherId) {
        if (voucherService.delete(voucherId)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }

}
