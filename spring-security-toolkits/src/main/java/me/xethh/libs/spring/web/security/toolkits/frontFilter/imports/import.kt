package me.xethh.libs.spring.web.security.toolkits.frontFilter.imports

import me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstLayerFilter
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.AppNameProvider
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.DefaultAppNameProvider
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.NoneAppNameProvider
import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@Import(EnableFirstLayerFilterConfig::class)
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class EnableFirstLayerFilter


class EnableFirstLayerFilterConfig{
    @Autowired lateinit var firstLayerFilterProperties:FirstLayerFilterProperties
    @Bean
    fun appNameProvider(): AppNameProvider {
        when (firstLayerFilterProperties.appNameConfig.type!!) {
            FirstFilterProperties.AppNameConfig.BuildType.Default -> return DefaultAppNameProvider()
            FirstFilterProperties.AppNameConfig.BuildType.None -> return NoneAppNameProvider()
            FirstFilterProperties.AppNameConfig.BuildType.Custom -> throw RuntimeException("app name provider not supported")
        }
    }

    @Autowired
    fun firstLayerFilter() : FirstLayerFilter{
        return FirstLayerFilter()
    }

}

@EnableConfigurationProperties(FirstLayerFilterProperties::class)
class FirstLayerFilterProperties{
    var appNameConfig: FirstFilterProperties.AppNameConfig = FirstFilterProperties.AppNameConfig.getDefault()
}