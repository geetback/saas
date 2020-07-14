package cn.itcast.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class QiniuYunUtils {

    public static String upload(byte[] bys){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());//使用空间所在的区域
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "ikm2zL7Xsfn0GrgW4ET1nXsA2UpU45SFJ1aFOYBg";
        String secretKey = "U5214hUEWQBi9eu_uPbyyeOPvvm57CkEOokIN4yV";
        String bucket = "saas-28";
        String url = "http://qdfvj90pq.bkt.clouddn.com";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        //byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bys, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            url=url+"/"+putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

        return url;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("e:\\1.jpg");
        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);

        System.out.println(upload(byt));
    }

}
