package cn.App.service.DictionService;

import cn.App.dao.DevUser.DevUserMapper;
import cn.App.dao.dictionary.DictionMapper;
import cn.App.entity.dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("dictionService")
public class DictionServiceImpl implements DictionService{
    @Autowired
    private DictionMapper dictionMapper;
    @Override
    public List<dictionary> getvalueName() {
        return dictionMapper.getvalueName();
    }

    @Override
    public List<dictionary> getflatFormList() {
        return dictionMapper.getflatFormList();
    }

    @Override
    public List<dictionary> getdyNamicflatFormList(String typeCode) {
        return dictionMapper.getdyNamicflatFormList(typeCode);
    }
}
