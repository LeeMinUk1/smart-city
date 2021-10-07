package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Notice {
    private String id;
    private String title;
    private String registered;
    private String readCount;
    private String content;
    private String attachmentUrl;
    private String attachmentName;
}
