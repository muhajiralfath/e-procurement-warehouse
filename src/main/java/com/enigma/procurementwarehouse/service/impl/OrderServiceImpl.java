package com.enigma.procurementwarehouse.service.impl;

import com.enigma.procurementwarehouse.entity.*;
import com.enigma.procurementwarehouse.model.request.OrderRequest;
import com.enigma.procurementwarehouse.model.response.OrderDetailResponse;
import com.enigma.procurementwarehouse.model.response.OrderResponse;
import com.enigma.procurementwarehouse.model.response.ProductResponse;
import com.enigma.procurementwarehouse.model.response.VendorResponse;
import com.enigma.procurementwarehouse.repository.OrderRepository;
import com.enigma.procurementwarehouse.service.*;
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
    private final ProductService productService;
    private final ReportDataService reportDataService;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse createNewTransaction(OrderRequest request) {
        // TODO 1: Validate vendor
        Vendor vendor = vendorService.getById(request.getVendorId());

        // TODO 2: Convert orderDetailRequest to orderDetail
        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(orderDetailRequest -> {
            // TODO 3: Validate Product Price
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());
            Product product = productService.getProductById(productPrice.getProduct().getId());

            // TODO : Add Order to Report Data
            ReportData report = ReportData.builder()
                    .productCode(product.getProductCode())
                    .date(LocalDateTime.now())
                    .vendorName(vendor.getName())
                    .productName(product.getName())
                    .category(product.getCategory().getName())
                    .price(productPrice.getPrice())
                    .quantity(orderDetailRequest.getQuantity())
                    .totalPrice(productPrice.getPrice() * orderDetailRequest.getQuantity())
                    .build();

            reportDataService.create(report);

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
                            // TODO 8: Convert vendor to vendorResponse (from productPrice)
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

        // TODO 10: Add Order to Entity Report


        // TODO 11: Convert orderDetail to orderDetailResponse
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
