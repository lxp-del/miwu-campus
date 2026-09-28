package com.platform.lxp.admin.controller;
import com.platform.lxp.admin.utils.OssUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class OssController {

    @Autowired
    private OssUtil ossUtil;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return ossUtil.upload(file);
    }
}
