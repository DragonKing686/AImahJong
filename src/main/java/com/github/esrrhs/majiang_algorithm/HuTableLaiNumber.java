package com.github.esrrhs.majiang_algorithm;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HuTableLaiNumber {
    public static ConcurrentHashMap<Long, List<HuTableInfo>> table = new ConcurrentHashMap<>();
    public static String ziname[] = new String[]
            { "1万", "2万", "3万", "4万", "5万", "6万", "7万", "8万", "9万" };

    public static void gen()
    {
        HuCommon.table = table;
        HuCommon.N = 9;
        HuCommon.NAME = "laiNumber";
        HuCommon.CARD = ziname;
        HuCommon.huLian = false;
        HuCommon.gen();
    }

    public static void load()
    {
        table.clear();
        HuCommon.table = table;
        HuCommon.N = 9;
        HuCommon.NAME = "laiNumber";
        HuCommon.CARD = ziname;
        HuCommon.huLian = false;
        HuCommon.load();
    }

    public static void load(List<String> lines)
    {
        table.clear();
        HuCommon.table = table;
        HuCommon.N = 9;
        HuCommon.NAME = "laiNumber";
        HuCommon.CARD = ziname;
        HuCommon.huLian = false;
        HuCommon.load(lines);
    }
}
