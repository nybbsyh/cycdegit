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
                String oldFileName = attach.getOriginalFilename();//????????????
                String prefix = FilenameUtils.getExtension(oldFileName);//???????????????
                int filesize = 500000000;
                if (attach.getSize() > filesize) {//???????????????????????? 500k
                    request.setAttribute("uploadFileError", " * ???????????????????????? 500k");
                    return "/developer/appversionadd";
                } else if (prefix.equalsIgnoreCase("apk")) {//???????????????????????????
                    String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                    File targetFile = new File(path, fileName);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    //??????
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        /* request.setAttribute("uploadFileError", " * ???????????????");*/
                        return "/developer/appversionadd";
                    }
                    ApkFileName = fileName;
                    a_downloadLink = "/statics" + File.separator + "uploadfiles" + File.separator + fileName;
                    System.out.println(a_downloadLink);
                } else {
                    /*  request.setAttribute("uploadFileError", " * ???????????????????????????");*/
                    return "/developer/appversionadd";
                }
            }
//???????????????????????? ??????????????????????????????

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
    /*????????????????????????*/
    @RequestMapping("toupdateVersions")
    public String toupdateVersions(Integer aid,Integer vid ,Model model) {
        List<version> versionslist = versionService.findVersion(aid);
        version versionslists = versionService.findbanben(aid,vid);
        model.addAttribute("appVersionList",versionslist);
        model.addAttribute("appVersion",versionslists);
      /*  model.addAttribute("appVersionId",id);*/
        return "/developer/appversionmodify";
    }
    /*??????????????????*/
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
            String oldFileName = attach.getOriginalFilename();//????????????
            String prefix = FilenameUtils.getExtension(oldFileName);//???????????????
            int filesize = 500000000;
            if (attach.getSize() > filesize) {//???????????????????????? 500k
                request.setAttribute("uploadFileError", " * ???????????????????????? 500k");
                return "/developer/appversionmodify";
            } else if (prefix.equalsIgnoreCase("apk")) {//???????????????????????????
                String fileName = System.currentTimeMillis() + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                File targetFile = new File(path, fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                //??????
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    /* request.setAttribute("uploadFileError", " * ???????????????");*/
                    return "forward:/Version/toupdateVersions";
                }
                        ApkFileName = ""+fileName;
                        a_downloadLink = "/statics" + File.separator + "uploadfiles" + File.separator + fileName;
                System.out.println(a_downloadLink);
            } else {
                /*  request.setAttribute("uploadFileError", " * ???????????????????????????");*/
                return "/developer/appversionmodify";
            }
        }
//???????????????????????? ??????????????????????????????
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
    /*??????*/
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
    /*??????*/
    @RequestMapping("viewInfo")
    public String viewInfo(Integer id,Model model) {
       info ins =devUserService.selectinfoid(id);
        List<version> versionslist = versionService.findVersion(id);
        model.addAttribute("appVersionList",versionslist);
       model.addAttribute("appInfo",ins);
        return "developer/appinfoview" ;
    }
    /*safdaf*/
    /*???????????????*/
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
