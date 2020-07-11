package cn.itcast.web.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Date;

public class MyString2DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        if (StringUtils.isNotBlank(source)){
            try {
                return DateUtils.parseDate(source,"yyyy-MM-dd");
            } catch (ParseException e) {
            }
        }
        return null;
    }
}
