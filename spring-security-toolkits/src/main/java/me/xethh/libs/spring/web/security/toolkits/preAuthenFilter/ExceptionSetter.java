package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter;

import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModel;

import javax.servlet.http.HttpServletResponse;

public interface ExceptionSetter {
    void setException(HttpServletResponse httpServletResponse, GeneralExceptionModel exceptionModel);
}
