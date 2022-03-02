package cn.App.util;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*实现converter接口的时候，第一类型是请求中入参的类型，第二的是转换后的目标类型*/
public class StringToDateConvrter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        Date result =null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
           result = simpleDateFormat.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
