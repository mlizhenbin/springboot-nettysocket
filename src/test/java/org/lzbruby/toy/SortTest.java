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
     * 插入排序
     */
    @Test
    public void insertionSort() {
        int[] v = {2, 3, 1, 3, 2, 6, 7, 8, 23, 27, 2, 3, 1, 0, 20};
        logger.info("冒泡排序：小——>大");
        for (int i = 1; i < v.length; i++) {
            //只能从当前索引往前循环，因为索引前的数组皆为有序的，索引只要确定当前索引的数据的为止即可
            for (int j = i; j > 0 && v[j] < v[j - 1]; j--) {
                int temp = v[j];
                v[j] = v[j - 1];
                v[j - 1] = temp;
            }
            logger.info("index={}, v={}", i, ArrayUtils.toString(v));
        }

        int[] v1 = {2, 3, 1, 3, 2, 6, 7, 8, 23, 27, 2, 3, 1, 0, 20};
        logger.info("冒泡排序：小——>大");
        for (int i = 1; i < v1.length; i++) {
            //只能从当前索引往前循环，因为索引前的数组皆为有序的，索引只要确定当前索引的数据的为止即可
            for (int j = i; j > 0 && v1[j] > v1[j - 1]; j--) {
                int temp = v1[j];
                v1[j] = v1[j - 1];
                v1[j - 1] = temp;
            }
            logger.info("index={}, v1={}", i, ArrayUtils.toString(v1));
        }
    }

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
