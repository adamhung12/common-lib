package me.xethh.libs.toolkits.webDto.core.response.general.page;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author xethhung
 * @date 5/7/2018
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageConfig {
    private long page = 0;
    private long pageSize = 0;

    public static PageConfig get(){
        return new PageConfig();
    }

    public PageConfig page(long page){
        this.page = page;
        return this;
    }

    public PageConfig pageSize(long pageSize){
        this.pageSize = pageSize;
        return this;
    }

    public PageConfigValidation valid(){
        if(page<=0)
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d] is not valid", pageSize, page));
        else if(pageSize<=0)
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d] is not valid", pageSize, page));
        return new PageConfigValidation(true, "");
    }

    public PageConfigValidation valid(long itemCount){
        if(!valid().isValid)
            return valid();
        else if(itemCount<=0)
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d, item count=%d] is not valid", pageSize, page, itemCount));
        else if(pageSize>Long.MAX_VALUE/(page-1))
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d, item count=%d] is not valid, page * pageSize > Long.MAX_VALUE", pageSize, page, itemCount));
        else if((page-1)*pageSize>itemCount)
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d, item count=%d] is not valid, page * pageSize > Long.MAX_VALUE", pageSize, page, itemCount));
        return new PageConfigValidation(true, "");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PageConfigValidation{
        private Boolean isValid;
        private String message;
    }

}
