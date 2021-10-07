package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
    private String id;
    private String name;
    private String department;
    private String type;
    private String password;
    private String role;
    private boolean suspended;
    private boolean deleted;
    private String created;
}
