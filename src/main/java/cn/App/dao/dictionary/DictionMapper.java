package cn.App.dao.dictionary;

import cn.App.entity.dictionary;

import java.util.List;

public interface DictionMapper {
    /*查询状态*/
    List<dictionary> getvalueName();
    /*查询所属平台*/
    List<dictionary> getflatFormList();
    /*查询所属平台*/
    List<dictionary> getdyNamicflatFormList(String typeCode);
}
