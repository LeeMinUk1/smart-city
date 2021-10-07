package kr.ac.uos.smartcity.service;

import kr.ac.uos.smartcity.model.Notice;
import kr.ac.uos.smartcity.model.NoticeListCondition;
import kr.ac.uos.smartcity.model.Qna;
import kr.ac.uos.smartcity.model.QnaUser;
import kr.ac.uos.smartcity.repository.NoticeMapper;
import kr.ac.uos.smartcity.repository.QnaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class QnaService {
    private static final SimpleDateFormat REGISTERED_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final QnaMapper mapper;

    public QnaService(QnaMapper mapper) {
        this.mapper = mapper;
    }

    public void add(Qna qna) {
        qna.setRegistered(REGISTERED_DATE_FORMAT.format(new Date()));
        mapper.add(qna);
    }

    public void update(Qna qna) {
        qna.setRegistered(REGISTERED_DATE_FORMAT.format(new Date()));
        mapper.update(qna);
    }

    public void delete(int id) {
        mapper.delete(id);
    }

    public int listCount(NoticeListCondition condition) {
        return mapper.listCount(condition);
    }

    public List<Qna> list(NoticeListCondition condition) {
        return mapper.list(condition);
    }

    public Qna get(int id) {
        return mapper.get(id);
    }

    public void updateReadCount(int id) {
        mapper.updateReadCount(id);
    }

    public int unreadQnaCount(String userId) {
        return mapper.unreadQnaCount(userId);
    }

    public int getReadQna(QnaUser qnaUser) {
        return mapper.getReadQna(qnaUser);
    }

    public void addReadQna(QnaUser qnaUser) {
        mapper.addReadQna(qnaUser);
    }
}
