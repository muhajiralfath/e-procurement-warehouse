package com.enigma.procurementwarehouse.service;

import com.enigma.procurementwarehouse.entity.Role;
import com.enigma.procurementwarehouse.entity.constant.ERole;

public interface RoleService {

    Role getOrSave(ERole role);

}
