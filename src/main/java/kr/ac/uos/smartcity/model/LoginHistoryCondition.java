package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginHistoryCondition {
    private String beginDate;
    private String endDate;
    private String searchType;
    private String keyword;
    private int limit;
    private int offset;
}
