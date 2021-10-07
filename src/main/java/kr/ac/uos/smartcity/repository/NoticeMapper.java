package kr.ac.uos.smartcity.repository;

import kr.ac.uos.smartcity.model.Notice;
import kr.ac.uos.smartcity.model.NoticeListCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    void add(Notice notice);

    void update(Notice notice);

    void delete(int id);

    int listCount(NoticeListCondition condition);

    List<Notice> list(NoticeListCondition condition);

    Notice get(int id);

    void updateReadCount(int id);
}
