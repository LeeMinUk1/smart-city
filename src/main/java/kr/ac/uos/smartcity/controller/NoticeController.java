package kr.ac.uos.smartcity.controller;

import kr.ac.uos.smartcity.model.*;
import kr.ac.uos.smartcity.service.LoginHistoryService;
import kr.ac.uos.smartcity.service.NoticeService;
import kr.ac.uos.smartcity.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private final NoticeService noticeService;
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public NoticeController(NoticeService service) {
        this.noticeService = service;
    }

    @PostMapping("/add")
    public Response<Boolean> add(
            Notice notice,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) throws IOException {
        System.out.println(attachment.getOriginalFilename());
        saveFile(notice, attachment);
        this.noticeService.add(notice);
        return new Response<>(true);
    }

    @PostMapping("/update")
    public Response<Boolean> update(@RequestBody Notice user) {
        this.noticeService.update(user);
        return new Response<>(true);
    }

    @PostMapping("/delete")
    public Response<Boolean> delete(@RequestParam int id) {
        this.noticeService.delete(id);
        return new Response<>(true);
    }

    @PostMapping("/list")
    public Page<Notice> list(@RequestBody NoticeListCondition condition) {
        Page<Notice> page = new Page<Notice>();
        page.setTotalRecords(this.noticeService.listCount(condition));
        page.setData(this.noticeService.list(condition));
        return page;
    }

    @GetMapping("/get")
    public Notice get(@RequestParam int id) {
        Notice notice = this.noticeService.get(id);
        this.noticeService.updateReadCount(id);
        return notice;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable int id, HttpServletRequest request) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("download: " + id);
        Notice notice = this.noticeService.get(id);
        System.out.println(uploadPath);
        System.out.println(notice.getAttachmentName());
        System.out.println(notice.getAttachmentUrl());

        File file = new File(uploadPath + "/" + notice.getAttachmentUrl());
        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        String fileName = URLEncoder.encode(notice.getAttachmentName(), "UTF-8");
        fileName = URLDecoder.decode(fileName, "ISO8859_1");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(resource);
    }

    private void saveFile(Notice notice, MultipartFile file) throws IOException {
        System.out.println(file);
        if (file == null) {
            return;
        }

        String originalFilename = file.getOriginalFilename();
        notice.setAttachmentName(originalFilename);

        String savedFileName = getUuidFileName(originalFilename);
        notice.setAttachmentUrl(savedFileName);

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
