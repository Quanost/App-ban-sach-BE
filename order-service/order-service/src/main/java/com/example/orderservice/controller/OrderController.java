package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.repo.OrderRepository;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    private static final String SERVICE_A = "serviceA";
    int count=1;
    int countCir=1;

    @Autowired
    public OrderController(OrderService orderService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        Order order = orderService.getOrderById(id);
//        if (order != null) {
//            // Gọi API của User Service để lấy thông tin người dùng
//            User user = restTemplate.getForObject("http://user-service/users/" + order.getUserId(), User.class);
//            order.setUser(user);
//
//            // Gọi API của Book Service để lấy thông tin sách trong đơn hàng
//            List<OrderItem> orderItems = order.getItems();
//            for (OrderItem item : orderItems) {
//                Book book = restTemplate.getForObject("http://book-service/books/" + item.getBookId(), Book.class);
//                item.setBook(book);
//            }
//
//            return ResponseEntity.ok(order);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


    @CircuitBreaker(name = SERVICE_A)
    @Retry(name = SERVICE_A, fallbackMethod = "serviceAFallback")
    @RateLimiter(name = SERVICE_A)
    @PostMapping
    public String createOrder(@RequestBody OrderRequest orderRequest) {

////        // Thêm logic xử lý tạo đơn hàng (nếu cần)
//        Order newOrder = orderService.createOrder(orderRequest);
////        System.out.println("So luong:"+ orderRequest.);
////        Order newOrder = new Order();
//        return ResponseEntity.ok(newOrder);
        System.out.println("Retry method called " + count++ + " times at " + new Date());
        return  orderService.createOrder(orderRequest);
    }

    @GetMapping("/getAllOrder")
    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    @GetMapping("/getAllOrder/{orderId}")
    public Optional<Order> getOrderById(@PathVariable(value = "orderId") Long orderId){
        return orderRepository.findById(orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder != null) {
            order.setId(id);
            Order updatedOrder = orderService.updateOrder(order);
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    public String serviceAFallback(Exception e) {
        return "This service Book connect failed";
    }
}
