package com.github.esrrhs.majiang_algorithm;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AITableText {
    public static ConcurrentHashMap<Long, List<AITableInfo>> table = new ConcurrentHashMap<>();
    public static String ziname[] = new String[]
            { "东", "南", "西", "北", "中", "发", "白"  };

    public static void gen()
    {
        AICommon.table = table;
        AICommon.N = 7;
        AICommon.NAME = "laiText";
        AICommon.CARD = ziname;
        AICommon.huLian = false;
        AICommon.baseP = 28.d / 136;
        AICommon.gen();
    }

    public static void load()
    {
        table.clear();
        AICommon.table = table;
        AICommon.N = 7;
        AICommon.NAME = "laiText";
        AICommon.CARD = ziname;
        AICommon.huLian = false;
        AICommon.baseP = 28.d / 136;
        AICommon.load();
    }

    public static void load(List<String> lines)
    {
        table.clear();
        AICommon.table = table;
        AICommon.N = 7;
        AICommon.NAME = "laiText";
        AICommon.CARD = ziname;
        AICommon.huLian = false;
        AICommon.baseP = 28.d / 136;
        AICommon.load(lines);
    }
}
