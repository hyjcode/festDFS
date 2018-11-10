package com.itheima.controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("upload")
public class UploadController {
    //案例一:传统方式上传,就是把图片上传到本服务器上
    //步骤说明:
    //1. 添加依赖  commons-fileupload  使用的这个组件上传
    //2. 在springmvc.xml配置文件中,配置上传解析器
    //3. 在Controller中编写上传逻辑(方法)
    //4. 编写index.jsp完成上传界面的编写


    @RequestMapping("pic")
    //MultipartFile:就是上传文件的类型数据
    //参数名可以随意,必须和form表单的name一样
    //fileName:自己定义可以让用户指定文件名,这个只是用户传递过来的
    public String upload(MultipartFile uploadFile, String fileName) throws IOException {
        //获取最终的文件名

        //获取上传文件的原始文件名
        String oriName = uploadFile.getOriginalFilename();
        //获取上传文件的类型(后缀)1.2.3.jpg -->  .jpg
        String extName = oriName.substring(oriName.lastIndexOf("."));

        //生成uuid,和用户指定的文件名进行组合,避免文件重名,把以前的文件覆盖
        //获取uuid,并且把-去掉了,并且文件的后缀也添加过来了,相当于:kasjfljasldfj.jpg
        String uuidName = UUID.randomUUID().toString().replace("-", "");

        //判断用户提供的文件名是否不为空,
        if (fileName != null && !"".equals(fileName)) {
            //如果不为空,才进行拼接
            fileName = fileName + "_" + uuidName + extName;
        } else {
            fileName = uuidName + extName;

        }

        //获取上传文件的存放地址,就是获取服务器的物理地址
        String basePath = this.getClass().getClassLoader().getResource("/").getPath();

        //有可能会有很多的图片,为了避免一个文件夹的图片过多,每天准备一个文件夹存放文明
        String datePath = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        File file = new File(basePath + "/" + datePath);

        //判断文件夹是否存在,
        if (!file.exists()) {
            //如果不存在,就创建
            file.mkdirs();
        }


        //把文件上传到指定的地址
        //这个一行代码是核心代码,上传用了,大家这一句搞定上传能成功
        //其实一行搞定:uploadFile.transferTo(new File("D:/itheima/1.jpg"));
        uploadFile.transferTo(new File(file, fileName));

        //返回成功页面
        return "success";
    }

    //案例二:跨服务器图片上传
    // 步骤:
    // 1. 开启老师提供的资料的Tomcat
    // 2. 工程添加依赖jersey
    // 3. 在Controller中编写上传逻辑,根据传统图片上传类似
    // 4. index.jsp编写表单


    //声明另外一台图片服务器,存放图片的地址
    private String imageUrl = "http://127.0.0.1:9090/uploads/";

    @RequestMapping("pic2")
    //MultipartFile:就是上传文件的类型数据
    //参数名可以随意,必须和form表单的name一样
    //fileName:自己定义可以让用户指定文件名,这个只是用户传递过来的
    public String upload2(MultipartFile uploadFile, String fileName) throws IOException {
        //获取最终的文件名

        //获取上传文件的原始文件名
        String oriName = uploadFile.getOriginalFilename();
        //获取上传文件的类型(后缀)1.2.3.jpg -->  .jpg
        String extName = oriName.substring(oriName.lastIndexOf("."));

        //生成uuid,和用户指定的文件名进行组合,避免文件重名,把以前的文件覆盖
        //获取uuid,并且把-去掉了,并且文件的后缀也添加过来了,相当于:kasjfljasldfj.jpg
        String uuidName = UUID.randomUUID().toString().replace("-", "");

        //判断用户提供的文件名是否不为空,
        if (fileName != null && !"".equals(fileName)) {
            //如果不为空,才进行拼接
            fileName = fileName + "_" + uuidName + extName;
        } else {
            fileName = uuidName + extName;

        }


        //使用jersey进行上传
        Client client = Client.create();
        //执行上传的路径
        WebResource resource = client.resource(imageUrl + fileName);
        //执行上传
        String result = resource.put(String.class, uploadFile.getBytes());

        System.out.println(result);


        //返回成功页面
        return "success";
    }

}
