package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginHistory {
    private String id;
    private String name;
    private String department;
    private String ip;
    private String loggedIn;
}
