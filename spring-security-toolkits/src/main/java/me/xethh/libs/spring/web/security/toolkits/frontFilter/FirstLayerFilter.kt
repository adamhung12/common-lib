package me.xethh.libs.spring.web.security.toolkits.frontFilter

import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties
import me.xethh.libs.toolkits.logging.WithLogger
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class FirstLayerFilter() : GenericFilterBean(), WithLogger {
    @Autowired
    internal var properties: FirstFilterProperties? = null

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterchain: FilterChain?) {
        filterchain?.doFilter(request, response)
    }
}