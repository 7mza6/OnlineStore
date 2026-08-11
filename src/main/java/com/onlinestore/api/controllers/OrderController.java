package com.onlinestore.api.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinestore.api.dto.PagedResult;
import com.onlinestore.api.entities.Order;
import com.onlinestore.security.UserPrincipal;
import com.onlinestore.service.interfaces.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order placeOrder(@AuthenticationPrincipal UserPrincipal principal) {
        return orderService.placeOrder(principal.getId());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PagedResult<Order> myOrders(@AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.findByCustomerId(principal.getId(), page, size);
    }

    @GetMapping("/my/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order myOrderById(@AuthenticationPrincipal UserPrincipal principal, @PathVariable int id) {
        return orderService.findByIdForCustomer(id, principal.getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PagedResult<Order> allOrders(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.findAll(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Order orderById(@PathVariable int id) {
        return orderService.findByIdForAdmin(id);
    }
}
