package kr.ac.uos.smartcity.controller;

import kr.ac.uos.smartcity.model.*;
import kr.ac.uos.smartcity.service.NoticeService;
import kr.ac.uos.smartcity.service.QnaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api/qna")
public class QnaController {
    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private final QnaService qnaService;
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public QnaController(QnaService service) {
        this.qnaService = service;
    }

    @PostMapping("/add")
    public Response<Boolean> add(
            Qna qna,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) throws IOException {
        System.out.println(attachment.getOriginalFilename());
        saveFile(qna, attachment);
        this.qnaService.add(qna);
        return new Response<>(true);
    }

    @PostMapping("/update")
    public Response<Boolean> update(@RequestBody Qna qna) {
        this.qnaService.update(qna);
        return new Response<>(true);
    }

    @PostMapping("/delete")
    public Response<Boolean> delete(@RequestParam int id) {
        this.qnaService.delete(id);
        return new Response<>(true);
    }

    @PostMapping("/list")
    public Page<Qna> list(@RequestBody NoticeListCondition condition) {
        Page<Qna> page = new Page<Qna>();
        page.setTotalRecords(this.qnaService.listCount(condition));
        page.setData(this.qnaService.list(condition));
        return page;
    }

    @GetMapping("/get/{userId}/{qnaId}")
    public Qna get(@PathVariable String userId, @PathVariable int qnaId) {
        Qna qna = this.qnaService.get(qnaId);
        this.qnaService.updateReadCount(qnaId);

        QnaUser qnaUser = new QnaUser(userId, qnaId);
        if (this.qnaService.getReadQna(qnaUser) == 0) {
            this.qnaService.addReadQna(qnaUser);
        }
        return qna;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable int id, HttpServletRequest request) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("download: " + id);
        Qna qna = this.qnaService.get(id);
        System.out.println(uploadPath);
        System.out.println(qna.getAttachmentName());
        System.out.println(qna.getAttachmentUrl());

        File file = new File(uploadPath + "/" + qna.getAttachmentUrl());
        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        String fileName = URLEncoder.encode(qna.getAttachmentName(), "UTF-8");
        fileName = URLDecoder.decode(fileName, "ISO8859_1");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(resource);
    }

    @GetMapping("/unreadCount")
    public int unreadQnaCount(String userId) {
        return this.qnaService.unreadQnaCount(userId);
    }

    private void saveFile(Qna qna, MultipartFile file) throws IOException {
        System.out.println(file);
        if (file == null) {
            return;
        }

        String originalFilename = file.getOriginalFilename();
        qna.setAttachmentName(originalFilename);

        String savedFileName = getUuidFileName(originalFilename);
        qna.setAttachmentUrl(savedFileName);

        File newFile = new File(savedFileName);
        file.transferTo(newFile);
    }

    public static String getUuidFileName(String fileName) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        fileName = fileName.replaceAll(" ", "_");
        int idx = fileName.lastIndexOf(".");
        if (idx < 0) {
            fileName = fileName + "_" + FILE_DATE_FORMAT.format(timestamp);
        } else {
            fileName = fileName.substring(0, idx) + "_" + FILE_DATE_FORMAT.format(timestamp) + fileName.substring(idx);
        }
        return fileName;
    }
}
