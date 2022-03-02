package cn.App.dao.category;

import cn.App.entity.category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    /*获取第一级菜单*/
    List<category>categoryLevel1List();
    /*获取第二级菜单*/
    List<category>categoryLevel2List(@Param("parentId") Integer queryCategoryLevel1);
    /*获取第三级菜单*/
    List<category>categoryLevel3List();
}
