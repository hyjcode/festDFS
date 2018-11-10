package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    //通过application.properties文件中的key, 获取它的value然后给下面这个变量赋值
    @Value("${FILE_SERVER_URL}")
    private String fileServer;

    /**
     * MultipartFile是springMvc提供的接收上传的文件的接口, 里面有大量的我们需要的方法可以直接调用
     * @param file 上传的文件
     * @return
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            //创建分布式文件系统工具类对象
            FastDFSClient fastClient = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //上传文件并返回上传后的文件名和路径
            String path = fastClient.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            System.out.println("====path====" + path);
            System.out.println("=====url====" + fileServer + path);
            return new Result(true, fileServer + path);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败!");
        }

    }
}
