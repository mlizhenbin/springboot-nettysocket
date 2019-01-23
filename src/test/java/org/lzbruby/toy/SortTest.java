package org.lzbruby.toy;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lizhenbin on 19-1-23.
 */
public class SortTest {

    /**
     * sl4j
     */
    private static final Logger logger = LoggerFactory.getLogger(SortTest.class);


    /**
     * 冒泡
     */
    @Test
    public void bubbleSort() {
        int[] v = {2, 3, 1, 3, 2, 6, 7, 8, 23, 27, 2, 3, 1, 0, 20};
        logger.info("冒泡排序：小——>大");
        for (int i = 0; i < v.length; i++) {

            for (int j = i + 1; j < v.length; j++) {

                if (v[i] > v[j]) {
                    int tmp = v[i];
                    v[i] = v[j];
                    v[j] = tmp;
                }
            }

            logger.info("序号" + i + " = " + ArrayUtils.toString(v));
        }

        int[] v1 = {2, 3, 1, 3, 2, 6, 7, 8, 23, 27, 2, 3, 1, 0, 20};
        logger.info("冒泡排序：大——>小");
        for (int i = 0; i < v1.length; i++) {

            for (int j = i + 1; j < v1.length; j++) {
                if (v1[i] < v1[j]) {
                    int tmp = v1[i];
                    v1[i] = v1[j];
                    v1[j] = tmp;
                }
            }
            logger.info("序号" + i + " = " + ArrayUtils.toString(v1));
        }
    }
}
