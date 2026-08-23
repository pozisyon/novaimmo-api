package com.novaimmo.demo.admin;

import com.novaimmo.demo.admin.dto.AdminDashboardResponse;

import com.novaimmo.demo.admin.dto.AdminUserResponse;
import com.novaimmo.demo.payment.dto.PaymentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;


    public AdminController(
            AdminService adminService
    ) {
        this.adminService =
                adminService;
    }


    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard() {

        return adminService
                .getDashboard();
    }
    @GetMapping("/payments")
    public List<PaymentResponse> findAllPayments() {

        return adminService
                .findAllPayments();
    }



}