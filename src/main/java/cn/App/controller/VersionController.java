package cn.App.controller;

import cn.App.entity.devUser;
import cn.App.entity.info;
import cn.App.entity.version;
import cn.App.service.DevUserService.DevUserService;
import cn.App.service.VersionService.VersionService;
import cn.App.util.Constants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Version")
public class VersionController {
    @Autowired
    private VersionService versionService;
   @Autowired
   private DevUserService devUserService;
    @RequestMapping("devMain")
    public String findmassion(Integer id, Model model) {
        List<version> versionslist = versionService.findmassion(id);

        model.addAttribute("appVersionList",versionslist);
        model.addAttribute("appVersionId",id);
        return "/developer/appversionadd";
    }
    @RequestMapping("addVersion")
    public String addVersion(version vers, HttpSession session, HttpServletRequest request,
                             @RequestParam(value = "a_downloadLink", required = false) MultipartFile attach) {
        System.out.println(vers.getVersionNo());
        System.out.println(vers.getAppId());
            String a_downloadLink = null;
        String DownloadLink = null;
        String ApkFileName =null;
            if (!attach.isEmpty()) {
                String path = request.getSession().getServletContext().getRealPath("/statics" + File.separator + "uploadfiles");
                String oldFileName = attach.getOriginalFilename();//原文件名
                String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
                int filesize = 500000000;
                if (attach.getSize() > filesize) {//上传大小不得超过 500k
                    request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
                    return "/developer/appversionadd";
                } else if (prefix.equalsIgnoreCase("apk")) {//上传图片格式不正确
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                    File targetFile = new File(path, fileName);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        /* request.setAttribute("uploadFileError", " * 上传失败！");*/
                        return "/developer/appversionadd";
                    }
                    ApkFileName = fileName;
                    a_downloadLink = "/statics" + File.separator + "uploadfiles" + File.separator + fileName;
                    System.out.println(a_downloadLink);
                } else {
                    /*  request.setAttribute("uploadFileError", " * 上传图片格式不正确");*/
                    return "/developer/appversionadd";
                }
            }
//判断文件是否为空 设置了文件的保存路径

        vers.setCreatedBy(((devUser)session.getAttribute(Constants.USER_SESSION)).getId());
            vers.setCreationDate(new Date());
            vers.setApkLocPath(a_downloadLink);
           vers.setApkFileName(ApkFileName);
           vers.setDownloadLink(a_downloadLink);
       Integer count=versionService.addVersion(vers);
       if (count>0){
           devUserService.UpdateappId(vers.getAppId());
           return "forward:/devUser/selectListInfo";
       }else {
           return "/developer/appversionadd";
       }
    }
    /*获取当前版本信息*/
    @RequestMapping("toupdateVersions")
    public String toupdateVersions(Integer aid,Integer vid ,Model model) {
        List<version> versionslist = versionService.findVersion(aid);
        version versionslists = versionService.findbanben(aid,vid);
        model.addAttribute("appVersionList",versionslist);
        model.addAttribute("appVersion",versionslists);
      /*  model.addAttribute("appVersionId",id);*/
        return "/developer/appversionmodify";
    }
    /*修改最新版本*/
    @RequestMapping("updateVersions")
    public String updateVersions(version vers, HttpSession session, HttpServletRequest request,
                                 @RequestParam(value = "attach", required = false) MultipartFile attach) {
        /**/
        System.out.println(vers.getVersionNo());
        System.out.println(vers.getAppId());
        String a_downloadLink = null;
        String ApkFileName =null;
        if (!attach.isEmpty()) {
            String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 500000000;
            if (attach.getSize() > filesize) {//上传大小不得超过 500k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
                return "/developer/appversionmodify";
            } else if (prefix.equalsIgnoreCase("apk")) {//上传图片格式不正确
                String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                File targetFile = new File(path, fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                //保存
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    /* request.setAttribute("uploadFileError", " * 上传失败！");*/
                    return "forward:/Version/toupdateVersions";
                }
                        ApkFileName = ""+fileName;
                        a_downloadLink = "/statics" + File.separator + "uploadfiles" + File.separator + fileName;
                System.out.println(a_downloadLink);
            } else {
                /*  request.setAttribute("uploadFileError", " * 上传图片格式不正确");*/
                return "/developer/appversionmodify";
            }
        }
//判断文件是否为空 设置了文件的保存路径
        vers.setModifyBy(((devUser)session.getAttribute(Constants.USER_SESSION)).getId());
        vers.setModifyDate(new Date());
        System.out.println(vers.getModifyDate());
        System.out.println(vers.getModifyBy());
        vers.setApkLocPath(a_downloadLink);
        vers.setApkFileName(ApkFileName);
        vers.setDownloadLink(a_downloadLink);
        System.out.println(vers.getDownloadLink());
        Integer count=versionService.updateBanBen(vers);
        if (count>0){
            /*devUserService.UpdateappId(vers.getAppId());*/
            return "forward:/devUser/selectListInfo";
        }else {
            return "/developer/appversionmodify";
        }
    }
    /*删除*/
    @RequestMapping("deleteInfo")
    @ResponseBody
    public Object deleteInfo(Integer id){
        Map<String,Object> map = new HashMap();
            if ( versionService.Deleat(id)){
                versionService.DeleteVersion(id);
               map.put("delResult","true");
            }else if (!versionService.Deleat(id)){
                map.put("delResult","false");
            }else {
                map.put("delResult","notexist");
            }
      return map;
    }
    /*查看*/
    @RequestMapping("viewInfo")
    public String viewInfo(Integer id,Model model) {
       info ins =devUserService.selectinfoid(id);
        List<version> versionslist = versionService.findVersion(id);
        model.addAttribute("appVersionList",versionslist);
       model.addAttribute("appInfo",ins);
        return "developer/appinfoview" ;
    }
    /*safdaf*/
    /*删除安装包*/
    @RequestMapping("UpdateApk")
    @ResponseBody
    public Object UpdateApk(version vers) {
        Map<String,Object> map = new HashMap<>();
        if (versionService.UpdateApk(vers)>0) {
            map.put("result","success");
            return map;
        } else {
            map.put("result","failed");
            return map;
        }
    }

}
