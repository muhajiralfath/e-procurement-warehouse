package com.enigma.procurementwarehouse.model.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class VendorResponse {
    private String id;
    private String name;
    private String address;

    private String mobilePhone;
}
