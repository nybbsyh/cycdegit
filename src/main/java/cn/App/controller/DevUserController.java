package cn.App.controller;

import cn.App.dao.category.CategoryMapper;
import cn.App.entity.category;
import cn.App.entity.devUser;
import cn.App.entity.dictionary;
import cn.App.entity.info;
import cn.App.service.DevUserService.DevUserService;
import cn.App.service.DictionService.DictionService;
import cn.App.service.category.CategoryService;
import cn.App.util.Constants;
import cn.App.util.PageSupport;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/devUser")
public class DevUserController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DevUserService devUserService;
    @Autowired
    private DictionService dictionService;

    @RequestMapping("devMain")
    public String devMain() {
        return "devlogin";
    }

    @RequestMapping("backendlogin")
    public String backendMain() {
        return "backendlogin";
    }

    /*拦截器跳转*/
    @RequestMapping("error")
    public String error() {
        return "BackError";
    }
    /*登录功能*/
    @RequestMapping("DevUserlogin")
    public String selsectDevUserByCode(@RequestParam String devCode, @RequestParam String devPassword, HttpServletRequest request
            , HttpSession session, Model model) {
        devUser dev = devUserService.selsectDevUserByCode(devCode);
        if (dev != null && dev.getDevPassword().equals(devPassword)) {
            session.setAttribute(Constants.USER_SESSION, dev);
            request.setAttribute("devUser", dev);
            /*  model.addAttribute("userSession",dev);*/
            return "/developer/main";
        } else {
            request.setAttribute("error", "用户名或密码错误");
            return "devlogin";/*转发*/
        }
    }

    /*退出*/
    @RequestMapping("signout")
    public String signout(HttpSession session) {
        session.removeAttribute(Constants.USER_SESSION);/*session失效*/
        return "devlogin";
    }

    /*App维护页面*/
    @RequestMapping("Appmaintain")
    public String Appmaintain() {
        return "/developer/appinfolist";
    }

    /*查看软件列表*/
    @RequestMapping("selectListInfo")
    public String selectListInfo(Integer queryCategoryLevel3, Integer queryCategoryLevel2, Integer queryCategoryLevel1, String querySoftwareName, Integer queryFlatformId, Integer queryStatus, @RequestParam(defaultValue = "1") Integer pageIndex, Model model) {
        /*一级菜单*/
        List<category> categoryLevel1List = categoryService.categoryLevel1List();
        List<category> categoryLevel2List = categoryService.categoryLevel2List(queryCategoryLevel1);
        List<category> categoryLevel3List = categoryService.categoryLevel2List(queryCategoryLevel2);
        List<info> infolist = devUserService.selectListInfo(queryCategoryLevel3, queryCategoryLevel2, queryCategoryLevel1, querySoftwareName, queryFlatformId, queryStatus, pageIndex, Constants.pageSize);
        /*获取状态信息*/
        List<dictionary> statusList = dictionService.getvalueName();
        List<dictionary> flatFormList = dictionService.getflatFormList();
        Integer totalCount = devUserService.getcount(queryCategoryLevel3, queryCategoryLevel2, queryCategoryLevel1, querySoftwareName, queryFlatformId, queryStatus);/*获取总记录数*/
        PageSupport support = new PageSupport();
        support.setPageSize(Constants.pageSize);
        support.setCurrentPageNo(pageIndex);
        support.setTotalCount(totalCount);
        model.addAttribute("categoryLevel2List", categoryLevel2List);
        model.addAttribute("categoryLevel1List", categoryLevel1List);
        model.addAttribute("categoryLevel3List", categoryLevel3List);
        model.addAttribute("flatFormList", flatFormList);
        model.addAttribute("statusList", statusList);
        model.addAttribute("appInfoList", infolist);
        model.addAttribute("currentPageNo", pageIndex);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPageCount", support.getTotalPageCount());
        model.addAttribute("queryStatus", queryStatus);
        model.addAttribute("queryFlatformId", queryFlatformId);
        model.addAttribute("querySoftwareName", querySoftwareName);
        model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
        return "/developer/appinfolist";
    }

    @RequestMapping("getListInfo")
    @ResponseBody
    public List<category> selectListInfo(Integer pid) {
        if (pid == null) {
            return categoryService.categoryLevel1List();
        }
        return categoryService.categoryLevel2List(pid);
    }

    @RequestMapping("oneListInfo")
    @ResponseBody
    public List<category> oneListInfo(Integer pid) {
        return categoryService.categoryLevel1List();
    }

    /*新增*/
    @RequestMapping("toaddListInfo")
    public String toaddListInfo() {
        return "/developer/appinfoadd";
    }

    @RequestMapping("addListInfo")
    public String addListInfo(info infos, HttpSession session, HttpServletRequest request,
                              @RequestParam(value = "a_logoPicPath", required = false) MultipartFile attach) {
        String a_logoPicPath = null;
        if (!attach.isEmpty()) {
            String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 500000;
            if (attach.getSize() > filesize) {//上传大小不得超过 500k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
                return "/developer/appinfoadd";
            } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {//上传图片格式不正确
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
                    return "/developer/appinfoadd";
                }
                a_logoPicPath = "/statics" + File.separator + "uploadfiles" + File.separator + fileName;
                System.out.println(a_logoPicPath);
            } else {
                /*  request.setAttribute("uploadFileError", " * 上传图片格式不正确");*/
                return "/developer/appinfoadd";
            }
        }
//判断文件是否为空 设置了文件的保存路径
        infos.setCreatedBy(((devUser) session.getAttribute(Constants.USER_SESSION)).getId());
        infos.setCreationDate(new Date());
        infos.setLogoPicPath(a_logoPicPath);
        System.out.println(infos.getAPKName());
        System.out.println(infos.getAppInfo());
        System.out.println(infos.getCreationDate());
        if (devUserService.addSoftware(infos) > 0) {
            return "forward:/devUser/selectListInfo";
        }
        return "/developer/appinfoadd";
    }

    /*获取状态信息*/
    @RequestMapping("getdyNamicflatFormList")
    @ResponseBody
    public Object getdyNamicflatFormList(String tcode) {
        return dictionService.getdyNamicflatFormList(tcode);
    }

    @RequestMapping("findAPK")
    @ResponseBody
    public Object findAPK(String APKName) {
        Map<String, Object> objectMap = new HashMap<>();
        if (devUserService.findApk(APKName) == null) {
            objectMap.put("APKName", "noexist");
        } else if (devUserService.findApk(APKName) != null) {
            objectMap.put("APKName", "exist");
        } else if (devUserService.findApk(APKName) != null) {
            objectMap.put("APKName", "empty");
        }
        /*利用fastJSON*/
        return JSONArray.toJSONString(objectMap);
    }

    /*修改之前的查看*/
    /*新增*/
    @RequestMapping("appinfomodify")
    public String selectinfoid(Integer id, Model model) {
        System.out.println(id);
        info appInfo = devUserService.selectinfoid(id);
        model.addAttribute("appInfo", appInfo);
        return "/developer/appinfomodify";
    }

    /*修改*/
    @RequestMapping("updateinfomodify")
    public String updateinfomodify(info infos, HttpSession session,HttpServletRequest request, @RequestParam(value = "attach", required = false) MultipartFile attach) {
        String a_downloadLink = null;
        String ApkFileName =null;
        if (!attach.isEmpty()) {
            String path = request.getSession().getServletContext().getRealPath("statics" + File.separator + "uploadfiles");
            String oldFileName = attach.getOriginalFilename();//原文件名
            String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
            int filesize = 500000000;
            if (attach.getSize() > filesize) {//上传大小不得超过 500k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
                return "/developer/appinfomodify";
            } else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {//上传图片格式不正确
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
                    return "/developer/appinfomodify";
                }
                ApkFileName = ""+fileName;
                a_downloadLink = "/statics" + File.separator + "uploadfiles" + File.separator + fileName;
                System.out.println(a_downloadLink);
            } else {
                /*  request.setAttribute("uploadFileError", " * 上传图片格式不正确");*/
                return "/developer/appinfomodify";
            }
        }
        infos.setModifyBy(((devUser) session.getAttribute(Constants.USER_SESSION)).getId());
        infos.setModifyDate(new Date());
        infos.setLogoPicPath(a_downloadLink);
        System.out.println(infos.getLogoPicPath());
        if (devUserService.updateinfoid(infos) > 0) {
            return "forward:/devUser/selectListInfo";
        } else {
            return "/developer/appinfomodify";
        }
    }

    /*上架下架*/
    @RequestMapping("/{id}/modifystate")
    @ResponseBody
    public Object modifystate(@PathVariable Integer id) {
        info infos = devUserService.findstatus(id);
        System.out.println(infos.getStatus());
        Map<String, Object> map = new HashMap<>();
        /*  非法操作*/
        if (infos == null) {
            map.put("errorCode", "exception000001");
            return map;
        }
        if (infos.getStatus() == 1 || infos.getStatus() == 3) {
            map.put("errorCode", "param000001");
            return map;
        }
        /*  上架*/
        if (infos.getStatus() == 5 || infos.getStatus() == 2) {
            if (devUserService.modifystate(id, 4) > 0) {
                map.put("errorCode", "0");
                map.put("resultMsg", "success");
                return map;
            }
        }

        /*sdsaf*/
        //下架操作
      /*  if (info.getStatus()==4){
            if (appInfoService.updateAppStatus(5,id)){
                result.put("errorCode","0");
                result.put("resultMsg","success");
                return result;
            }
        }*/
        /*下架*/
        if (infos.getStatus() == 4) {
            devUserService.modifystate(id, 5);
            map.put("errorCode", "0");
            map.put("resultMsg", "success");
            return map;
        }
        //失败则返回 失败
        map.put("errorCode", "0");
        map.put("resultMsg", "failed");
        return map;
    }

    /*删除图片*/
    @RequestMapping("updateimg")
    @ResponseBody
    public Object updateimg(info infos) {
        Map<String,Object> map = new HashMap<>();
        if (devUserService.Updateimg(infos) > 0) {
            map.put("result","success");
            return map;
        } else {
            map.put("result","failed");
            return map;
        }
    }
}
