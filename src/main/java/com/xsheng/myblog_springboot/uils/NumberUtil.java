package com.xsheng.myblog_springboot.uils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * fw练习生
 * </p>
 *
 * @author sheng
 * @since 2023/8/26 17:40:39
 */
public class NumberUtil {

    /**
     * 获取从 minNum -> maxNum 之间的n个数的列表
     * @param minNnm 最小数
     * @param maxNum 最大数
     * @param num 数量
     * @return 列表
     */
    public static List<Integer> getRandomNumberList(Integer minNnm,Integer maxNum,Integer num) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0 ; i < num ; i += maxNum - minNnm){
            list.addAll(randomCommon(minNnm, maxNum, maxNum -minNnm));
        }
        return list;
    }

    /**
     * 随机指定范围内N个不重复的数
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     * @return min-max 乱序 不重复数组
     */
    public static List<Integer> randomCommon(int min, int max, int n) {
        List<Integer> integers = new ArrayList<Integer>();
        if (min < 0) {
            min = 0;
        }
        if ((max - min) + 1 < n) {
            n = (max - min) + 1;
        }
        if (max < min) {
            max = min;
        }
        if (max < 0 || n < 0) {
            return integers;
        }
        for (int i = 1; i <= n; i++) {
            int randomNumber = (int) Math.round(Math.random() * (max - min) + min);
            if (integers.contains(randomNumber)) {
                i--;
                continue;
            } else {
                integers.add(randomNumber);
            }
        }
        return integers;
    }
}

