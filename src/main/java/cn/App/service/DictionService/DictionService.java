package cn.App.service.DictionService;

import cn.App.entity.dictionary;

import java.util.List;

public interface DictionService {
    List<dictionary> getvalueName();
    /*查询所属平台*/
    List<dictionary> getflatFormList();
    /*查询所属平台*/
    List<dictionary> getdyNamicflatFormList(String typeCode);
}
