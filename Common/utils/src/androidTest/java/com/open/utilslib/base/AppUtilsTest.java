package com.open.utilslib.base;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AppUtilsTest {

    @Test
    public void compareVersion() {
        String[] large = new String[]{"1.0.1",
                "1.0.11",
                "1.0.12.rc.11"};
        String[] small = new String[]{"1.0.0",
                "1.0.01",
                "1.0.12.rc.10"};

        int len = large.length;
        for (int i = 0; i < len; i++) {
            try {
                assertTrue(AppUtils.compareVersion(large[i], small[i]) > 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}