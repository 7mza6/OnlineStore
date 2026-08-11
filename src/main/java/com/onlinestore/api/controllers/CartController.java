package com.onlinestore.api.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinestore.api.dto.CartItemRequest;
import com.onlinestore.api.entities.Cart;
import com.onlinestore.security.UserPrincipal;
import com.onlinestore.service.interfaces.CartService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart getMyCart(@AuthenticationPrincipal UserPrincipal principal) {
        return cartService.getOrCreateCartForCustomer(principal.getId());
    }

    @PostMapping("/items")
    public Cart addItem(@AuthenticationPrincipal UserPrincipal principal, @RequestBody CartItemRequest request) {
        return cartService.addItem(principal.getId(), request.getProductId(), request.getQuantity());
    }

    @PutMapping("/items/{productId}")
    public Cart updateItem(@AuthenticationPrincipal UserPrincipal principal, @PathVariable int productId,
            @RequestBody CartItemRequest request) {
        return cartService.updateItemQuantity(principal.getId(), productId, request.getQuantity());
    }

    @DeleteMapping("/items/{productId}")
    public Cart removeItem(@AuthenticationPrincipal UserPrincipal principal, @PathVariable int productId) {
        return cartService.removeItem(principal.getId(), productId);
    }
}
