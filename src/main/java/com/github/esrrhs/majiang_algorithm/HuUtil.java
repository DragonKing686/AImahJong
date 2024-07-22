package com.github.esrrhs.majiang_algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by bjzhaoxin on 2017/12/4.
 */
public class HuUtil {
    public static boolean isHu(List<Integer> input, int guiCard, boolean extHu) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }
        int guiNum = cards.get(guiCard - 1);
        cards.set(guiCard - 1, 0);
        boolean otherHU = otherHu(input, guiCard, extHu);
        return isHuCard(cards, guiNum, extHu) || otherHU;
    }

    public static boolean isHuExtra(List<Integer> input, List<Integer> guiCard, int extra) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }

        int guiNum = 0;
        for (int gui : guiCard) {
            guiNum += cards.get(gui - 1);
            cards.set(gui - 1, 0);
        }

        if (extra != 0) {
            cards.set(extra - 1, cards.get(extra - 1) + 1);
        }

        return isHuCard(cards, guiNum, false);
    }

    public static boolean isHuCard(List<Integer> cards, int guiNum, boolean extHu) {
        long wan_key = 0;
        long tong_key = 0;
        long tiao_key = 0;
        long feng_key = 0;
        long jian_key = 0;
        long zi_key = 0;

        for (int i = MaJiangDef.WAN1; i <= MaJiangDef.WAN9; i++) {
            int num = cards.get(i - 1);
            wan_key = wan_key * 10 + num;
        }
        for (int i = MaJiangDef.TONG1; i <= MaJiangDef.TONG9; i++) {
            int num = cards.get(i - 1);
            tong_key = tong_key * 10 + num;
        }
        for (int i = MaJiangDef.TIAO1; i <= MaJiangDef.TIAO9; i++) {
            int num = cards.get(i - 1);
            tiao_key = tiao_key * 10 + num;
        }
        for (int i = MaJiangDef.FENG_DONG; i <= MaJiangDef.FENG_BEI; i++) {
            int num = cards.get(i - 1);
            feng_key = feng_key * 10 + num;
        }
        for (int i = MaJiangDef.JIAN_ZHONG; i <= MaJiangDef.JIAN_BAI; i++) {
            int num = cards.get(i - 1);
            jian_key = jian_key * 10 + num;
        }
        // 字牌
        for (int i = MaJiangDef.FENG_DONG; i <= MaJiangDef.JIAN_BAI; i++) {
            int num = cards.get(i - 1);
            zi_key = zi_key * 10 + num;
        }

        List<List<HuTableInfo>> tmp = new ArrayList<>();
        List<List<HuTableInfo>> tmpExtHu = new ArrayList<>();
        if (wan_key != 0) {
            List<HuTableInfo> wanHuTableInfo = HuTable.table.get(wan_key);
            tmp.add(wanHuTableInfo);
            List<HuTableInfo> huTableInfos = HuTableLaiNumber.table.get(wan_key);
            tmpExtHu.add(huTableInfos);
        }
        if (tong_key != 0) {
            List<HuTableInfo> tongHuTableInfo = HuTable.table.get(tong_key);
            tmp.add(tongHuTableInfo);
            List<HuTableInfo> huTableInfos = HuTableLaiNumber.table.get(tong_key);
            tmpExtHu.add(huTableInfos);
        }
        if (tiao_key != 0) {
            List<HuTableInfo> tiaoHuTableInfo = HuTable.table.get(tiao_key);
            tmp.add(tiaoHuTableInfo);
            List<HuTableInfo> huTableInfos = HuTableLaiNumber.table.get(tiao_key);
            tmpExtHu.add(huTableInfos);
        }
        if (feng_key != 0) {
            List<HuTableInfo> fengHuTableInfo = HuTableFeng.table.get(feng_key);
            tmp.add(fengHuTableInfo);
        }
        if (jian_key != 0) {
            List<HuTableInfo> jianHuTableInfo = HuTableJian.table.get(jian_key);
            tmp.add(jianHuTableInfo);
        }
        if (zi_key != 0) {
            List<HuTableInfo> jianHuTableInfo = HuTableLaiText.table.get(zi_key);
            tmpExtHu.add(jianHuTableInfo);
        }

        boolean isHu = true;
        List<List<HuTableInfo>> tmp1 = new ArrayList<>();
        for (List<HuTableInfo> huTableInfos : tmp) {
            if (huTableInfos == null) {
                isHu = false;
                break;
            }
            List<HuTableInfo> tmp2 = new ArrayList<>();
            for (HuTableInfo huTableInfo : huTableInfos) {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                    tmp2.add(huTableInfo);
                }
            }
            if (tmp2.isEmpty()) {
                isHu = false;
                break;
            }
            tmp1.add(tmp2);
        }

        boolean isHuExt = true;
        List<List<HuTableInfo>> tmp2 = new ArrayList<>();
        for (List<HuTableInfo> huTableInfos : tmpExtHu) {
            if (huTableInfos == null) {
                isHuExt = false;
                break;
            }
            List<HuTableInfo> tmp3 = new ArrayList<>();
            for (HuTableInfo huTableInfo : huTableInfos) {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                    tmp3.add(huTableInfo);
                }
            }
            if (tmp3.isEmpty()) {
                isHuExt = false;
                break;
            }
            tmp2.add(tmp3);
        }
        boolean normalHU = isHu && isHuTableInfo(tmp1, 0, guiNum, false);
        boolean extHU = isHuExt && isHuTableInfo2(tmp2, 0, guiNum, false);
        return extHu ? normalHU || extHU : normalHU;
    }

    private static boolean isHuTableInfo2(List<List<HuTableInfo>> tmp, int index, int guiNum, boolean jiang) {
        if (index >= tmp.size()) {
            return (guiNum % 3 == 0 && jiang == true) || (guiNum % 3 == 2 && jiang == false);
        }
        List<HuTableInfo> huTableInfos = tmp.get(index);
        for (HuTableInfo huTableInfo : huTableInfos) {
            if (jiang) {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum && huTableInfo.jiang == false) {
                    if (isHuTableInfo(tmp, index + 1, guiNum - huTableInfo.needGui, jiang)) {
                        return true;
                    }
                }
            }
            else {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                    if (isHuTableInfo(tmp, index + 1, guiNum - huTableInfo.needGui, huTableInfo.jiang)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isHuTableInfo(List<List<HuTableInfo>> tmp, int index, int guiNum, boolean jiang) {
        if (index >= tmp.size()) {
            return true;
        }
        List<HuTableInfo> huTableInfos = tmp.get(index);
        for (HuTableInfo huTableInfo : huTableInfos) {
            if (jiang) {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum && huTableInfo.jiang == false) {
                    if (isHuTableInfo(tmp, index + 1, guiNum - huTableInfo.needGui, jiang)) {
                        return true;
                    }
                }
            }
            else {
                if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                    if (isHuTableInfo(tmp, index + 1, guiNum - huTableInfo.needGui, huTableInfo.jiang)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<Integer> isTing(List<Integer> input, int guiCard, boolean extHu) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }
        int guiNum = cards.get(guiCard - 1);
        cards.set(guiCard - 1, 0);
        List<Integer> tingCard = new ArrayList<>();
        tingCard.addAll(isTingCard(cards, guiNum));
        if (extHu) {
            tingCard.addAll(otherTing(input,  guiCard, extHu));
        }
        return tingCard;
    }

    private static List<Integer> otherTing(List<Integer> tmp, int guiCard, boolean extHu) {
        if (tmp.size() != 13) {
            return new ArrayList<>();
        }
        List<Integer> result = new ArrayList<>();
        result.addAll(HuUtil.tingQiDui(tmp, guiCard));
        if (extHu) {
            // 有字牌的麻将，例如乐平麻将,十三幺
            result.addAll(HuUtil.tingThirthen(tmp, guiCard));
            // 清一色
            result.addAll(HuUtil.tingQingColor(tmp, guiCard));
        }
        return result;
    }

    private static List<Integer> tingQingColor(List<Integer> tmp, int guiCard) {
        List<Integer> result = new ArrayList<>();
        List<Integer> guiList = new ArrayList<>();
        guiList.add(guiCard);
        for (int i = MaJiangDef.WAN1; i <= MaJiangDef.JIAN_BAI ; i++) {
            List<Integer> all = new ArrayList<>(tmp);
            all.add(i);
            if (isQingColor(all,guiList, false) == 1) {
                result.add(i);
            }
        }
        return result;
    }

    private static List<Integer> tingThirthen(List<Integer> tmp, int guiCard) {
        List<Integer> result = new ArrayList<>();
        List<Integer> guiList = new ArrayList<>();
        guiList.add(guiCard);
        for (int i = MaJiangDef.WAN1; i <= MaJiangDef.JIAN_BAI ; i++) {
            List<Integer> all = new ArrayList<>(tmp);
            all.add(i);
            if (isThirthen(all, guiList, false) == 1) {
                result.add(i);
            }
        }
        return result;
    }

    private static List<Integer> tingQiDui(List<Integer> tmp, int guiCard) {
        List<Integer> result = new ArrayList<>();
        List<Integer> guiList = new ArrayList<>();
        guiList.add(guiCard);
        for (int i = MaJiangDef.WAN1; i <= MaJiangDef.JIAN_BAI ; i++) {
            List<Integer> all = new ArrayList<>(tmp);
            all.add(i);
            if (isQiDui(all, guiList, false) == 1) {
                result.add(i);
            }
        }
        return result;
    }

    public static List<Integer> isTingExtra(List<Integer> input, List<Integer> guiCard) {
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < MaJiangDef.MAX_NUM; i++) {
            cards.add(0);
        }
        for (int c : input) {
            cards.set(c - 1, cards.get(c - 1) + 1);
        }

        int guiNum = 0;
        for (int gui : guiCard) {
            guiNum += cards.get(gui - 1);
            cards.set(gui - 1, 0);
        }

        return isTingCard(cards, guiNum);
    }

    public static List<Integer> isTingCard(List<Integer> cards, int guiNum) {
        long wan_key = 0;
        long tong_key = 0;
        long tiao_key = 0;
        long feng_key = 0;
        long jian_key = 0;
        long zi_key = 0;

        for (int i = MaJiangDef.WAN1; i <= MaJiangDef.WAN9; i++) {
            int num = cards.get(i - 1);
            wan_key = wan_key * 10 + num;
        }
        for (int i = MaJiangDef.TONG1; i <= MaJiangDef.TONG9; i++) {
            int num = cards.get(i - 1);
            tong_key = tong_key * 10 + num;
        }
        for (int i = MaJiangDef.TIAO1; i <= MaJiangDef.TIAO9; i++) {
            int num = cards.get(i - 1);
            tiao_key = tiao_key * 10 + num;
        }
        for (int i = MaJiangDef.FENG_DONG; i <= MaJiangDef.FENG_BEI; i++) {
            int num = cards.get(i - 1);
            feng_key = feng_key * 10 + num;
        }
        for (int i = MaJiangDef.JIAN_ZHONG; i <= MaJiangDef.JIAN_BAI; i++) {
            int num = cards.get(i - 1);
            jian_key = jian_key * 10 + num;
        }
        for (int i = MaJiangDef.FENG_DONG; i <= MaJiangDef.JIAN_BAI; i++) {
            int num = cards.get(i - 1);
            zi_key = zi_key * 10 + num;
        }

        List<Integer> tmpType = new ArrayList<>();
        List<Integer> tmpTypeLai = new ArrayList<>();
        List<List<HuTableInfo>> tmpTing = new ArrayList<>();
        List<List<HuTableInfo>> tmpTingLai = new ArrayList<>();
        List<List<HuTableInfo>> tmp = new ArrayList<>();
        List<List<HuTableInfo>> tmpLai = new ArrayList<>();

        List<HuTableInfo> wanHuTableInfo = HuTable.table.get(wan_key);
        List<HuTableInfo> wanHuTableInfo2 = HuTableLaiNumber.table.get(wan_key);
        if (wanHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTingLai.add(wanHuTableInfo2);
        tmpTing.add(wanHuTableInfo);
        if (wan_key != 0) {
            tmpType.add(MaJiangDef.TYPE_WAN);
            tmpTypeLai.add(MaJiangDef.TYPE_WAN);
            tmp.add(wanHuTableInfo);
            tmpLai.add(wanHuTableInfo2);
        }
        List<HuTableInfo> tongHuTableInfo = HuTable.table.get(tong_key);
        List<HuTableInfo> tongHuTableInfo2 = HuTableLaiNumber.table.get(tong_key);
        if (tongHuTableInfo == null) {
            return new ArrayList<>();
        }
        if (tongHuTableInfo2 == null) {
            return new ArrayList<>();
        }
        tmpTingLai.add(tongHuTableInfo2);
        tmpTing.add(tongHuTableInfo);
        if (tong_key != 0) {
            tmpType.add(MaJiangDef.TYPE_TONG);
            tmp.add(tongHuTableInfo);
            tmpLai.add(tongHuTableInfo2);
            tmpTypeLai.add(MaJiangDef.TYPE_TONG);
        }
        List<HuTableInfo> tiaoHuTableInfo = HuTable.table.get(tiao_key);
        List<HuTableInfo> tiaoHuTableInfo2 = HuTableLaiNumber.table.get(tiao_key);
        if (tiaoHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTingLai.add(tiaoHuTableInfo2);
        tmpTing.add(tiaoHuTableInfo);
        if (tiao_key != 0) {
            tmpType.add(MaJiangDef.TYPE_TIAO);
            tmp.add(tiaoHuTableInfo);
            tmpLai.add(tiaoHuTableInfo2);
            tmpTypeLai.add(MaJiangDef.TYPE_TIAO);
        }
        List<HuTableInfo> fengHuTableInfo = HuTableFeng.table.get(feng_key);
        if (fengHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTing.add(fengHuTableInfo);
        if (feng_key != 0) {
            tmpType.add(MaJiangDef.TYPE_FENG);
            tmp.add(fengHuTableInfo);
        }
        List<HuTableInfo> jianHuTableInfo = HuTableJian.table.get(jian_key);
        if (jianHuTableInfo == null) {
            return new ArrayList<>();
        }
        tmpTing.add(jianHuTableInfo);
        if (jian_key != 0) {
            tmpType.add(MaJiangDef.TYPE_JIAN);
            tmp.add(jianHuTableInfo);
        }
        List<HuTableInfo> ziHuTableInfo = HuTableLaiText.table.get(zi_key);
        tmpTingLai.add(ziHuTableInfo);
        if (zi_key != 0) {
            tmpTypeLai.add(MaJiangDef.TYPE_ZI);
            tmpLai.add(ziHuTableInfo);
        }

        List<Integer> ret = new ArrayList<>();
        for (int type = MaJiangDef.TYPE_WAN; type <= MaJiangDef.TYPE_JIAN; type++) {
            List<HuTableInfo> huTableInfos = tmpTing.get(type - 1);
            int[] cache = new int[9];
            for (HuTableInfo huTableInfo : huTableInfos) {
                if (huTableInfo.hupai != null && huTableInfo.needGui <= guiNum) {
                    boolean cached = true;
                    for (int j = 0; j < huTableInfo.hupai.length; j++) {
                        if (huTableInfo.hupai[j] > 0 && cache[j] == 0) {
                            cached = false;
                            break;
                        }
                    }

                    if (!cached && isTingHuTableInfo(tmpType, tmp, 0, guiNum - huTableInfo.needGui, huTableInfo.jiang,
                            type)) {
                        for (int j = 0; j < huTableInfo.hupai.length; j++) {
                            if (huTableInfo.hupai[j] > 0) {
                                if (cache[j] == 0) {
                                    ret.add(MaJiangDef.toCard(type, j));
                                }
                                cache[j]++;
                            }
                        }
                    }
                }
            }
        }
        for (int type = MaJiangDef.TYPE_WAN; type <= MaJiangDef.TYPE_ZI; type++) {
            List<HuTableInfo> huTableInfos = tmpTingLai.get(type - 1);
            int[] cache = new int[9];
            if (huTableInfos == null) {
                continue;
            }
            for (HuTableInfo huTableInfo : huTableInfos) {
                if (huTableInfo.hupai != null && huTableInfo.needGui <= guiNum) {
                    boolean cached = true;
                    for (int j = 0; j < huTableInfo.hupai.length; j++) {
                        if (huTableInfo.hupai[j] > 0 && cache[j] == 0) {
                            cached = false;
                            break;
                        }
                    }

                    if (!cached && isTingHuTableInfo(tmpTypeLai, tmpLai, 0, guiNum - huTableInfo.needGui, huTableInfo.jiang,
                            type)) {
                        for (int j = 0; j < huTableInfo.hupai.length; j++) {
                            if (huTableInfo.hupai[j] > 0) {
                                if (cache[j] == 0) {
                                    ret.add(MaJiangDef.toCard(type, j));
                                }
                                cache[j]++;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    private static boolean isTingHuTableInfo(List<Integer> tmpType, List<List<HuTableInfo>> tmp, int index, int guiNum,
                                             boolean jiang, int tingType) {
        if (index >= tmp.size()) {
            return guiNum == 0 && jiang == true;
        }
        if (tmpType.get(index) == tingType) {
            return isTingHuTableInfo(tmpType, tmp, index + 1, guiNum, jiang, tingType);
        }
        List<HuTableInfo> huTableInfos = tmp.get(index);
        if (huTableInfos == null) {
            return false;
        }
        for (HuTableInfo huTableInfo : huTableInfos) {
            if (huTableInfo.hupai == null && huTableInfo.needGui <= guiNum) {
                if (jiang) {
                    if (huTableInfo.jiang == false) {
                        if (isTingHuTableInfo(tmpType, tmp, index + 1, guiNum - huTableInfo.needGui, jiang, tingType)) {
                            return true;
                        }
                    }
                }
                else {
                    if (isTingHuTableInfo(tmpType, tmp, index + 1, guiNum - huTableInfo.needGui, huTableInfo.jiang,
                            tingType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean otherHu(List<Integer> tmp, int guiCard, boolean extHu) {
        if (tmp.size() != 14) {
            return false;
        }
        List<Integer> guiList = new ArrayList<>();
        guiList.add(guiCard);
        double qidui = HuUtil.isQiDui(tmp, guiList, false);
        if (qidui == 1) {
            return true;
        }
        if (extHu) {
            // 有字牌的麻将，例如乐平麻将,十三幺
            double thirthen = HuUtil.isThirthen(tmp, guiList, false);
            if (thirthen == 1) {
                return true;
            }
            // 清一色
            double qingColor = HuUtil.isQingColor(tmp, guiList, false);
            if (qingColor == 1) {
                return true;
            }
        }
        return false;
    }

    public static double isQiDui(List<Integer> tmp, List<Integer> guiCard, boolean isScore) {
        // 统计每种牌的出现次数
        Map<Integer, Integer> cardCount = new HashMap<>();
        int guiCount = 0;

        // 统计鬼牌数量
        for (Integer gui : guiCard) {
            for (Integer card : tmp) {
                if (Objects.equals(card, gui)) {
                    guiCount++;
                }
            }
        }
        // 移除鬼牌
        tmp = tmp.stream().filter(item -> !item.equals(guiCard.get(0))).collect(Collectors.toList());

        // 统计手牌中每种牌的数量
        for (Integer card : tmp) {
            cardCount.put(card, cardCount.getOrDefault(card, 0) + 1);
        }

        // 统计对子数量
        int pairCount = 0;
        for (int count : cardCount.values()) {
            pairCount += (count == 2 ? 1 : 0);
        }

        if (isScore) {
            if (pairCount + guiCount >= 5) {
                return (pairCount + guiCount - 1);
            }
            return 0;

        }
        else {
            if (pairCount + guiCount == 7) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    public static double isQiDui(List<Integer> tmp, List<Integer> guiCard) {
       return isQiDui(tmp, guiCard, true);
    }

    public static double isThirthen(List<Integer> tmp, List<Integer> guiCard, boolean isScore) {
        int countSingle = 0;
        int countDouble = 0;
        int guiCount = 0;

        // 统计鬼牌数量
        for (Integer gui : guiCard) {
            for (Integer card : tmp) {
                if (Objects.equals(card, gui)) {
                    guiCount++;
                }
            }
        }
        // 移除鬼牌
        tmp = tmp.stream().filter(item -> !item.equals(guiCard.get(0))).collect(Collectors.toList());

        Set<Integer> tmpSet = new HashSet<>(tmp);
        if (tmpSet.size() < 10) {
            return 0;
        }
        // 使用一个哈希表来存储每个数字出现的次数
        Map<Integer, Integer> countMap = new HashMap<>();

        // 遍历列表，统计每个数字的出现次数
        for (int num : tmp) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        // 检查是否有数字出现次数大于2次
        for (int countTemp : countMap.values()) {
            if (countTemp > 2) {
                return 0;
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            if (tmp.get(i) == MaJiangDef.WAN1 || tmp.get(i) == MaJiangDef.WAN9
                    || tmp.get(i) == MaJiangDef.TONG1 || tmp.get(i) == MaJiangDef.TONG9
                    || tmp.get(i) == MaJiangDef.TIAO1 || tmp.get(i) == MaJiangDef.TIAO9
                    || tmp.get(i) == MaJiangDef.FENG_DONG || tmp.get(i) == MaJiangDef.FENG_NAN || tmp.get(i) == MaJiangDef.FENG_XI || tmp.get(i) == MaJiangDef.FENG_BEI
                    || tmp.get(i) == MaJiangDef.JIAN_ZHONG || tmp.get(i) == MaJiangDef.JIAN_FA || tmp.get(i) == MaJiangDef.JIAN_BAI) {
                if (countMap.get(tmp.get(i)) != null && countMap.get(tmp.get(i)) == 1) {
                    countSingle++;
                }
                else {
                    countDouble++;
                }
            }
        }
        if (countDouble > 2) {
            return 0;
        }

        if (isScore) {
            if (countSingle + countDouble + guiCount >= 10) {
                return countSingle + countDouble + guiCount - 6;
            }
            return 0;
        }
        else {
            if (countSingle + countDouble + guiCount == 14) {
                return 1;
            }
            return 0;
        }
    }

    public static double isThirthen(List<Integer> tmp, List<Integer> guiCard) {
       return isThirthen(tmp, guiCard, true);
    }

    public static double isQingColor(List<Integer> tmp, List<Integer> guiCard, boolean isScore) {
        int guiCount = 0;
        int wan_key = 0;
        int tong_key = 0;
        int tiao_key = 0;
        // 统计鬼牌数量
        for (Integer gui : guiCard) {
            for (Integer card : tmp) {
                if (Objects.equals(card, gui)) {
                    guiCount++;
                }
            }
        }
        // 移除鬼牌
        tmp = tmp.stream().filter(item -> !item.equals(guiCard.get(0))).collect(Collectors.toList());

        for (int i = 0; i < tmp.size(); i++) {
            if (tmp.get(i) >= MaJiangDef.WAN1 && tmp.get(i) <= MaJiangDef.WAN9) {
                wan_key++;
            }
            else if (tmp.get(i) >= MaJiangDef.TONG1 && tmp.get(i) <= MaJiangDef.TONG9) {
                tong_key++;
            }
            else if (tmp.get(i) >= MaJiangDef.TIAO1 && tmp.get(i) <= MaJiangDef.TIAO9) {
                tiao_key++;
            }
        }

        if (isScore) {
            if (wan_key + guiCount >= 10) {
                return wan_key + guiCount - 6;
            }
            if (tong_key + guiCount >= 10) {
                return tong_key + guiCount - 6;
            }
            if (tiao_key + guiCount >= 10) {
                return tiao_key + guiCount - 6;
            }
            return 0;
        }
        else {
            if (wan_key + guiCount == 14) {
                return 1;
            }
            if (tong_key + guiCount == 14) {
                return 1;
            }
            if (tiao_key + guiCount == 14) {
                return 1;
            }
            return 0;
        }
    }

    public static double isQingColor(List<Integer> tmp, List<Integer> guiCard) {
       return isQingColor(tmp, guiCard, true);
    }

    public static void gen() {
//		HuTableJian.gen();
//		HuTableFeng.gen();
//		HuTable.gen();
        HuTableLaiNumber.gen();
        HuTableLaiText.gen();
    }

    public synchronized static void load() {
        HuTableJian.load();
        HuTableFeng.load();
        HuTableLaiNumber.load();
        HuTableLaiText.load();
        HuTable.load();
    }

    public static void testHu() {
        String init = "1万,4万,2条,6条,9条,1筒,5筒,9筒,东,南,白,发";
        String gui = "中";
        List<Integer> cards = MaJiangDef.stringToCards(init);
        System.out.println(HuUtil.isHu(cards, MaJiangDef.stringToCard(gui), true));
    }

    public static void testTing() {
        String init = "1万,9万,1筒,9筒,1条,9条,北,东,南,西,发,白,中";
        String gui = "中";
        List<Integer> cards = MaJiangDef.stringToCards(init);
        System.out.println(MaJiangDef.cardsToString(HuUtil.isTing(cards, MaJiangDef.stringToCard(gui), true)));
        System.out.println(MaJiangDef.cardsToString(HuUtil.isTingExtra(cards, MaJiangDef.stringToCards(gui))));
    }

    public static void main(String[] args) {
        // 需要生成文件时 加上gen()
//		gen();
        load();
//		testHu();
        testTing();
    }

}