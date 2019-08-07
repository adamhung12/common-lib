package me.xethh.libs.toolkits.webDto.core.general.page;

import me.xethh.libs.toolkits.exceptions.CommonException;

/**
 * @author xethhung
 * date 5/7/2018
 */
public class PageConfig {
    public static class PageConfigException extends CommonException {
        public PageConfigException(String message) {
            super(message);
        }
    }

    public PageConfig() {
    }

    public PageConfig(long page, long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    private long page = 0;
    private long pageSize = 0;

    public long from(){
        if(!valid().isValid()) throw new PageConfigException("Page config not correct, "+valid().message);
        return (page-1)*pageSize+1;
    }
    public long to(){
        if(!valid().isValid()) throw new PageConfigException("Page config not correct, "+valid().message);
        return page*pageSize;
    }
    public long to(long itemCount){
        if(!valid(itemCount).isValid()) throw new PageConfigException("Page config not correct, "+valid(itemCount).message);
        return Math.min(to(), itemCount);
    }

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
        if(!valid().isValid())
            return valid();
        else if(itemCount<=0)
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d, item count=%d] is not valid", pageSize, page, itemCount));
        else if(pageSize>Long.MAX_VALUE/(page-1))
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d, item count=%d] is not valid, page * pageSize > Long.MAX_VALUE", pageSize, page, itemCount));
        else if(((page-1)*pageSize+1)>=itemCount)
            return new PageConfigValidation(false, String.format("Page[page size=%d, page=%d, item count=%d] is not valid, page * pageSize > Long.MAX_VALUE", pageSize, page, itemCount));
        return new PageConfigValidation(true, "");
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public static class PageConfigValidation{
        public PageConfigValidation() {
        }

        public PageConfigValidation(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        private boolean valid;
        private String message;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
