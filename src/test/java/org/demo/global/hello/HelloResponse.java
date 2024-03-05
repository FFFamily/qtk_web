package org.demo.global.hello;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelloResponse {
    private String id;

}
