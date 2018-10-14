package io.alauda.stater.microservice.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class JwtUserInfo implements Serializable {
    private String phone;
    private String mail;
    private String userId;
    private String loginName;
    private String displayName;
    private String avata;
}
