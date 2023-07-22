package com.enigma.procurementwarehouse.model.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateVendoreRequest {
    private String vendorId;
    private String vendorName;
    private String address;
    private String phone;
}
