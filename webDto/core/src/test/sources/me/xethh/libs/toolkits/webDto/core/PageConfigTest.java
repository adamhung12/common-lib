package me.xethh.libs.toolkits.webDto.core;

import me.xethh.libs.toolkits.webDto.core.response.general.page.PageConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PageConfigTest {

    @Test
    public void baseTest()
    {
        PageConfig pc = new PageConfig(10, 1000);
        assertEquals(10, pc.getPage());
        assertEquals(1000, pc.getPageSize());

        pc = new PageConfig();
        assertEquals(0, pc.getPage());
        assertEquals(0, pc.getPageSize());

        pc = PageConfig.get().page(2).pageSize(222)
        assertEquals(2, pc.getPage());
        assertEquals(222, pc.getPageSize());

        assertTrue( true );
    }
}