package me.xethh.libs.toolkits.webDto.core;

import me.xethh.libs.toolkits.webDto.core.response.general.page.PageConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class PageConfigTest {

    @Test
    public void baseTest()
    {
        PageConfig pc = new PageConfig(10, 1000);
        assertEquals(9001, pc.from());
        assertEquals(10000, pc.to());
        assertEquals(10, pc.getPage());
        assertEquals(1000, pc.getPageSize());

        pc = new PageConfig();
        try{
            pc.from();
            fail();
        }
        catch (PageConfig.PageConfigException ignored){
        }
        try{
            pc.to();
            fail();
        }
        catch (PageConfig.PageConfigException ignored){
        }
        assertEquals(0, pc.getPage());
        assertEquals(0, pc.getPageSize());

        pc = PageConfig.get().page(2).pageSize(222);
        assertEquals(223, pc.from());
        assertEquals(444, pc.to());
        assertEquals(430, pc.to(430));
        assertEquals(2, pc.getPage());
        assertEquals(222, pc.getPageSize());

        PageConfig.PageConfigValidation valid = null;
        valid = PageConfig.get().page(1).pageSize(200).valid();
        assertTrue(valid.isValid());

        valid = PageConfig.get().page(-1).pageSize(200).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(0).pageSize(200).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(-1).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(0).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(200).valid(-1);
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(200).valid(0);
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(2).pageSize(200).valid(200);
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(2).pageSize(200).valid(201);
        assertFalse(valid.isValid());
    }
}