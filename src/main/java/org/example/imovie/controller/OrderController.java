package org.example.imovie.controller;

import jakarta.validation.Valid;
import org.example.imovie.dto.BuyTicketRequest;
import org.example.imovie.dto.OrderResponse;
import org.example.imovie.dto.RespCode;
import org.example.imovie.dto.ResultJsonObject;
import org.example.imovie.entity.Order;
import org.example.imovie.entity.User;
import org.example.imovie.exception.BusinessException;
import org.example.imovie.service.OrderService;
import org.example.imovie.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResultJsonObject> getOrderList(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        User user = getCurrentUser(authentication);

        return ResponseEntity.ok(ResultJsonObject.success(
                orderService.getOrderList(user.getId(), PageRequest.of(page, size, Sort.by("orderTime").descending()))
                        .map(this::toOrderResponse)
        ));
    }

    @PostMapping("/buy")
    public ResponseEntity<ResultJsonObject> buyTicket(
            Authentication authentication,
            @Valid @RequestBody BuyTicketRequest request) {

        User user = getCurrentUser(authentication);
        Order order = orderService.saveOrder(request.getScheduleId(), user.getId(), request.getQuantity());
        return ResponseEntity.ok(ResultJsonObject.success(toOrderResponse(order)));
    }

    private User getCurrentUser(Authentication authentication) {
        String account = authentication.getName();
        return userService.findByAccount(account)
                .orElseThrow(() -> new BusinessException(RespCode.USER_NOT_FOUND));
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTicketNo(),
                order.getSchedule().getFilm().getName(),
                order.getSchedule().getTheater().getName(),
                order.getSchedule().getShowTime(),
                order.getPrice(),
                order.getQuantity(),
                order.getStatus().getMessage(),
                order.getOrderTime(),
                order.getPayTime()
        );
    }
}
