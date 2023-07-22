package com.enigma.procurementwarehouse.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {

    private String productId;

    @NotBlank(message = "product name is required")
    private String productName;

    @NotNull(message = "price is required")
    @Min(value = 0, message = "price must be greater than equal 0")
    private Long price;

    @NotNull(message = "stock is required")
    @Min(value = 0, message = "stock must be greater than equal 0")
    private Integer stock;

    @NotBlank(message = "storeId is required")
    private String vendorId;
}
