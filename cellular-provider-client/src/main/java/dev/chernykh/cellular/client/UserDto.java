package dev.chernykh.cellular.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private Long tariffId;
}
