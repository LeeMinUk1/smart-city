package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page<T> {
    private int totalRecords;
    private List<T> data;
}
