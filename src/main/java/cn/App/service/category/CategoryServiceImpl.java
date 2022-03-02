package cn.App.service.category;

import cn.App.dao.category.CategoryMapper;
import cn.App.entity.category;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private  CategoryMapper categoryMapper;
    @Override
    public List<category> categoryLevel1List() {
        return categoryMapper.categoryLevel1List();
    }

    @Override
    public List<category> categoryLevel2List(Integer id) {
        return categoryMapper.categoryLevel2List(id);
    }

    @Override
    public List<category> categoryLevel3List() {
        return categoryMapper.categoryLevel3List();
    }
}
