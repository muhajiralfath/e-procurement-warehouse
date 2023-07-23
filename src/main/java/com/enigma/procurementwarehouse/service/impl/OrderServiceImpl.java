package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.Vendor;
import com.enigma.procurementwarehouse.entity.Order;
import com.enigma.procurementwarehouse.entity.OrderDetail;
import com.enigma.procurementwarehouse.entity.ProductPrice;
import com.enigma.procurementwarehouse.model.request.OrderRequest;
import com.enigma.procurementwarehouse.model.response.*;
import com.enigma.procurementwarehouse.repository.OrderRepository;
import com.enigma.procurementwarehouse.service.VendorService;
import com.enigma.procurementwarehouse.service.OrderService;
import com.enigma.procurementwarehouse.service.ProductPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final VendorService vendorService;
    private final ProductPriceService productPriceService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse createNewTransaction(OrderRequest request) {
        // TODO 1: Validate vendor
        Vendor vendor = vendorService.getById(request.getCustomerId());

        // TODO 2: Convert orderDetailRequest to orderDetail
        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(orderDetailRequest -> {
            // TODO 3: Validate Product Price
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());

            return OrderDetail.builder()
                    .productPrice(productPrice)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        // TODO 4: Create new order
        Order order = Order.builder()
                .vendor(vendor)
                .transDate(LocalDateTime.now())
                .orderDetails(orderDetails)
                .build();
        orderRepository.saveAndFlush(order);

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
            // TODO 5: Set order from orderDetail after creating new order
            orderDetail.setOrder(order);

            // TODO 6: Change the stock from the purchased quantity
            ProductPrice currentProductPrice = orderDetail.getProductPrice();
            currentProductPrice.setStock(currentProductPrice.getStock() - orderDetail.getQuantity());

            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    // TODO 7: Convert product to productResponse (from productPrice)
                    .product(ProductResponse.builder()
                            .id(currentProductPrice.getProduct().getId())
                            .productName(currentProductPrice.getProduct().getName())
                            .price(currentProductPrice.getPrice())
                            .stock(currentProductPrice.getStock())
                            // TODO 8: Convert Store to storeResponse (from productPrice)
                            .vendorResponse(VendorResponse.builder()
                                    .id(currentProductPrice.getVendor().getId())
                                    .name(currentProductPrice.getVendor().getName())
                                    .address(currentProductPrice.getVendor().getAddress())
                                    .build())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        // TODO 9: Convert vendor to vendorResponse
        VendorResponse vendorResponse = VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .build();

        // TODO 10: Convert orderDetail to orderDetailResponse
        return OrderResponse.builder()
                .orderId(order.getId())
                .vendorResponse(vendorResponse)
                .transDate(order.getTransDate())
                .orderDetails(orderDetailResponses)
                .build();
    }

    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
            orderDetail.setOrder(order);
            ProductPrice currentProductPrice = orderDetail.getProductPrice();

            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    .product(ProductResponse.builder()
                            .id(currentProductPrice.getProduct().getId())
                            .productName(currentProductPrice.getProduct().getName())
                            .price(currentProductPrice.getPrice())
                            .stock(currentProductPrice.getStock())
                            .vendorResponse(VendorResponse.builder()
                                    .id(currentProductPrice.getVendor().getId())
                                    .name(currentProductPrice.getVendor().getName())
                                    .address(currentProductPrice.getVendor().getAddress())
                                    .build())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        Vendor vendor = order.getVendor();
        VendorResponse vendorResponse = VendorResponse.builder()
                .id(vendor.getId())
                .name(vendor.getName())
                .build();

        return OrderResponse.builder()
                .orderId(order.getId())
                .vendorResponse(vendorResponse)
                .transDate(order.getTransDate())
                .orderDetails(orderDetailResponses)
                .build();
    }

    @Override
    public List<OrderResponse> getAllTransaction(String vendorName) {
        Specification<Order> specification = (root, query, criteriaBuilder) -> {
            Join<Order, Vendor> vendor = root.join("vendor");
            if (vendorName != null) {
                Predicate name = criteriaBuilder.like(criteriaBuilder.lower(vendor.get("name")), vendorName.toLowerCase() + "%");
                return query.where(name).getRestriction();
            }

            return query.getRestriction();
        };
        List<Order> orders = orderRepository.findAll(specification);

        return orders.stream().map(order -> {
            List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
                orderDetail.setOrder(order);
                ProductPrice currentProductPrice = orderDetail.getProductPrice();

                return OrderDetailResponse.builder()
                        .orderDetailId(orderDetail.getId())
                        .quantity(orderDetail.getQuantity())
                        .product(ProductResponse.builder()
                                .id(currentProductPrice.getProduct().getId())
                                .productName(currentProductPrice.getProduct().getName())
                                .price(currentProductPrice.getPrice())
                                .stock(currentProductPrice.getStock())
                                .vendorResponse(VendorResponse.builder()
                                        .id(currentProductPrice.getVendor().getId())
                                        .name(currentProductPrice.getVendor().getName())
                                        .address(currentProductPrice.getVendor().getAddress())
                                        .build())
                                .build())
                        .build();
            }).collect(Collectors.toList());

            Vendor vendor = order.getVendor();
            VendorResponse vendorResponse = VendorResponse.builder()
                    .id(vendor.getId())
                    .name(vendor.getName())
                    .build();

            return OrderResponse.builder()
                    .orderId(order.getId())
                    .vendorResponse(vendorResponse)
                    .transDate(order.getTransDate())
                    .orderDetails(orderDetailResponses)
                    .build();
        }).collect(Collectors.toList());
    }
}
