package cn.App.service.category;

import cn.App.entity.category;

import java.util.List;

public interface CategoryService {
    /*获取第一级菜单*/
    List<category> categoryLevel1List();
    /*获取第二级菜单*/
    List<category>categoryLevel2List(Integer id);
    /*获取第三级菜单*/
    List<category>categoryLevel3List();
}
