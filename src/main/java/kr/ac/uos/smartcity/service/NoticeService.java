package kr.ac.uos.smartcity.service;

import kr.ac.uos.smartcity.model.Notice;
import kr.ac.uos.smartcity.model.NoticeListCondition;
import kr.ac.uos.smartcity.repository.NoticeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NoticeService {
    private static final SimpleDateFormat REGISTERED_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final NoticeMapper mapper;

    public NoticeService(NoticeMapper mapper) {
        this.mapper = mapper;
    }

    public void add(Notice notice) {
        notice.setRegistered(REGISTERED_DATE_FORMAT.format(new Date()));
        mapper.add(notice);
    }

    public void update(Notice notice) {
        notice.setRegistered(REGISTERED_DATE_FORMAT.format(new Date()));
        mapper.update(notice);
    }

    public void delete(int id) {
        mapper.delete(id);
    }

    public int listCount(NoticeListCondition condition) {
        return mapper.listCount(condition);
    }

    public List<Notice> list(NoticeListCondition condition) {
        return mapper.list(condition);
    }

    public Notice get(int id) {
        return mapper.get(id);
    }

    public void updateReadCount(int id) {
        mapper.updateReadCount(id);
    }
}
