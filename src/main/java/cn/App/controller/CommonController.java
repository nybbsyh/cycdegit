package cn.App.controller;

import cn.App.entity.*;
import cn.App.service.BackendUserService.BackUserService;
import cn.App.service.DevUserService.DevUserService;
import cn.App.service.DictionService.DictionService;
import cn.App.service.VersionService.VersionService;
import cn.App.service.category.CategoryService;
import cn.App.util.Constants;
import cn.App.util.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private BackUserService backUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DevUserService devUserService;
    @Autowired
    private DictionService dictionService;
    @Autowired
    private VersionService versionService;
    /*登录功能*/
    @RequestMapping("DevUserlogin")
    public String selsectDevUserByCode(@RequestParam String userCode, @RequestParam String userPassword, HttpServletRequest request
            , HttpSession session, Model model) {
        backendUser backen = backUserService.login(userCode);
        if (backen != null && backen.getUserPassword().equals(userPassword)) {
            session.setAttribute(Constants.USER_SESSION,backen);
            request.setAttribute("devUser", backen);
           /* model.addAttribute("userSession",backen);*/
            return "backend/main";
        } else {
            request.setAttribute("error", "用户名或密码错误");
            return "backendlogin";/*转发*/
        }
    }

    /*退出*/
    @RequestMapping("signout")
    public String signout(HttpSession session) {
        session.removeAttribute(Constants.USER_SESSION);/*session失效*/
        return "backendlogin";
    }

    /*查看审核列表*/
    @RequestMapping("appcheck")
    public String appcheck(Integer queryCategoryLevel3, Integer queryCategoryLevel2, Integer queryCategoryLevel1, String querySoftwareName, Integer queryFlatformId, @RequestParam(defaultValue = "1") Integer pageIndex, Model model) {
        /*一级菜单*/
        List<category> categoryLevel1List = categoryService.categoryLevel1List();
        List<category> categoryLevel2List = categoryService.categoryLevel2List(queryCategoryLevel1);
        List<category> categoryLevel3List = categoryService.categoryLevel2List(queryCategoryLevel2);
        List<info> infolist = devUserService.selectListInfo(queryCategoryLevel3, queryCategoryLevel2, queryCategoryLevel1, querySoftwareName, queryFlatformId, 1, pageIndex, Constants.pageSize);
        /*获取状态信息*/
        List<dictionary> statusList = dictionService.getvalueName();
        List<dictionary> flatFormList = dictionService.getflatFormList();
        Integer totalCount = devUserService.getcount(queryCategoryLevel3, queryCategoryLevel2, queryCategoryLevel1, querySoftwareName, queryFlatformId, 1);/*获取总记录数*/
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
        model.addAttribute("queryFlatformId", queryFlatformId);
        model.addAttribute("querySoftwareName", querySoftwareName);
        model.addAttribute("queryCategoryLevel1", queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2", queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3", queryCategoryLevel3);
        return  "backend/applist";
    }
    /*审核*/
    /*退出*/
    @RequestMapping("appchecks")
    public String appchecks(Integer aid,Integer vid ,Model model) {
        info ins =devUserService.selectinfoid(aid);
        version versionslists = versionService.findbanben(aid,vid);
        model.addAttribute("appVersion",versionslists);
        System.out.println(versionslists.getDownloadLink());
        model.addAttribute("appInfo",ins);
        return "backend/appcheck";
    }
    /*上架下架*/
    @RequestMapping("update")
    public String modifystate(info ins) {
        if (backUserService.updateStatus(ins)) {
            return  "forward:/common/appcheck";
        }else {
            return  "backend/appcheck";
        }
    }


}
