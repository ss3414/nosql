package com.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "user")
public class User {

    /* 主键由MongoDB自动生成 */
    private String id;

    private String name;

    private String password;

}
