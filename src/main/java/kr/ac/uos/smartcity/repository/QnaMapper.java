package kr.ac.uos.smartcity.repository;

import kr.ac.uos.smartcity.model.NoticeListCondition;
import kr.ac.uos.smartcity.model.Qna;
import kr.ac.uos.smartcity.model.QnaUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QnaMapper {

    void add(Qna qna);

    void update(Qna qna);

    void delete(int id);

    int listCount(NoticeListCondition condition);

    List<Qna> list(NoticeListCondition condition);

    Qna get(int id);

    void updateReadCount(int id);

    int unreadQnaCount(String userId);

    int getReadQna(QnaUser qnaUser);

    void addReadQna(QnaUser qnaUser);
}
