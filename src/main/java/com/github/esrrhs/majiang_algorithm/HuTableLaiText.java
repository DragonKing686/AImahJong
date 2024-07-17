package com.github.esrrhs.majiang_algorithm;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HuTableLaiText {
    public static ConcurrentHashMap<Long, List<HuTableInfo>> table = new ConcurrentHashMap<>();
    public static String ziname[] = new String[]
            { "东", "南", "西", "北", "中", "发", "白" };

    public static void gen()
    {
        HuCommon.table = table;
        HuCommon.N = 7;
        HuCommon.NAME = "laiText";
        HuCommon.CARD = ziname;
        HuCommon.huLian = false;
        HuCommon.gen();
    }

    public static void load()
    {
        table.clear();
        HuCommon.table = table;
        HuCommon.N = 7;
        HuCommon.NAME = "laiText";
        HuCommon.CARD = ziname;
        HuCommon.huLian = false;
        HuCommon.load();
    }

    public static void load(List<String> lines)
    {
        table.clear();
        HuCommon.table = table;
        HuCommon.N = 7;
        HuCommon.NAME = "laiText";
        HuCommon.CARD = ziname;
        HuCommon.huLian = false;
        HuCommon.load(lines);
    }
}
