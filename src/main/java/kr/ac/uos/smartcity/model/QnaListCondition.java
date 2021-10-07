package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QnaListCondition {
    private String keyword;
    private int limit;
    private int offset;
}
