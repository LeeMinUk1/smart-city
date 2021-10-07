package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserListCondition {
    private boolean suspendedOrDeleted;
    private String type;
    private String searchType;
    private String keyword;
    private int limit;
    private int offset;
}
