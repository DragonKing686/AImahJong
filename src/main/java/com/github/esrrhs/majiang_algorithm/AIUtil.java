package com.github.esrrhs.majiang_algorithm;

import java.util.*;

/**
 * Created by bjzhaoxin on 2018/4/2.
 */
public class AIUtil
{
	public static double calc(List<Integer> input, List<Integer> guiCard, Boolean extHu) {
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

		List<Integer> ting = HuUtil.isTingCard(cards, guiNum);
		if (!ting.isEmpty()) {
			return ting.size() * 10;
		}

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

		List<List<AITableInfo>> tmp = new ArrayList<>();
		List<List<AITableInfo>> tmpExtHu = new ArrayList<>();

		List<AITableInfo> wanAITableInfo = AITable.table.get(wan_key);
		tmp.add(wanAITableInfo);
		List<AITableInfo> huTableInfos = AITableLaiNumber.table.get(wan_key);
		tmpExtHu.add(huTableInfos);

		List<AITableInfo> tongAITableInfo = AITable.table.get(tong_key);
		tmp.add(tongAITableInfo);
		List<AITableInfo> huTableInfos2 = AITableLaiNumber.table.get(tong_key);
		tmpExtHu.add(huTableInfos2);

		List<AITableInfo> tiaoAITableInfo = AITable.table.get(tiao_key);
		tmp.add(tiaoAITableInfo);
		List<AITableInfo> huTableInfos3 = AITableLaiNumber.table.get(tiao_key);
		tmpExtHu.add(huTableInfos3);

		List<AITableInfo> fengAITableInfo = AITableFeng.table.get(feng_key);
		tmp.add(fengAITableInfo);

		List<AITableInfo> jianAITableInfo = AITableJian.table.get(jian_key);
		tmp.add(jianAITableInfo);

		List<AITableInfo> huTableInfos4 = AITableText.table.get(zi_key);
		tmpExtHu.add(huTableInfos4);

		List<Double> ret = new ArrayList<>();
		calcAITableInfo(ret, tmp, 0, false, 0.d);

		if (extHu) {
			calcAITableInfo2(ret, tmpExtHu, 0, false, 0.d);
		}
		Double d = Collections.max(ret);
		return d;
	}

	public static double calc(List<Integer> input, List<Integer> guiCard) {
		return calc(input, guiCard, false);
	}

	private static void calcAITableInfo2(List<Double> ret, List<List<AITableInfo>> tmp, int index, boolean jiang,
										double cur) {
		if (index >= tmp.size()) {
			if (!jiang) {
				ret.add(cur);
			}
			return;
		}
		List<AITableInfo> aiTableInfos = tmp.get(index);
		for (AITableInfo aiTableInfo : aiTableInfos) {
			if (!aiTableInfo.jiang){
				calcAITableInfo2(ret, tmp, index + 1, aiTableInfo.jiang, cur + aiTableInfo.p);
			}
		}
	}

	private static void calcAITableInfo(List<Double> ret, List<List<AITableInfo>> tmp, int index, boolean jiang,
			double cur)
	{
		if (index >= tmp.size())
		{
			if (jiang)
			{
				ret.add(cur);
			}
			return;
		}
		List<AITableInfo> aiTableInfos = tmp.get(index);
		for (AITableInfo aiTableInfo : aiTableInfos)
		{
			if (jiang)
			{
				if (aiTableInfo.jiang == false)
				{
					calcAITableInfo(ret, tmp, index + 1, jiang, cur + aiTableInfo.p);
				}
			}
			else
			{
				calcAITableInfo(ret, tmp, index + 1, aiTableInfo.jiang, cur + aiTableInfo.p);
			}
		}
	}

	public static int outAI(List<Integer> input, List<Integer> guiCard, boolean extHu) {
		int ret = 0;
		double max = Double.MIN_VALUE;
		Set<Integer> processed = new HashSet<>();

		for (Integer c : input) {
			if (!processed.contains(c)) {
				processed.add(c);
				if (!guiCard.contains(c)) {
					List<Integer> tmp = new ArrayList<>(input);
					tmp.remove(c);
					double score = calc(tmp, guiCard, extHu);
					if (score > max) {
						max = score;
						ret = c;
					}
				}
			}
		}
		return ret;
	}


	public static boolean chiAI(List<Integer> input, List<Integer> guiCard, int card, int card1, int card2)
	{
		if (guiCard.contains(card) || guiCard.contains(card1) || guiCard.contains(card2))
		{
			return false;
		}

		if (Collections.frequency(input, card1) < 1 || Collections.frequency(input, card2) < 1)
		{
			return false;
		}

		double score = calc(input, guiCard);

		List<Integer> tmp = new ArrayList<>(input);
		tmp.remove((Integer) card1);
		tmp.remove((Integer) card2);
		double scoreNew = calc(tmp, guiCard);

		return scoreNew >= score;
	}

	public static ArrayList<Integer> chiAI(List<Integer> input, List<Integer> guiCard, int card)
	{
		ArrayList<Integer> ret = new ArrayList<>();
		if (guiCard.contains(card))
		{
			return ret;
		}

		double score = calc(input, guiCard);
		double scoreNewMax = 0;

		int card1 = 0;
		int card2 = 0;

		if (Collections.frequency(input, card - 2) > 0 && Collections.frequency(input, card - 1) > 0
				&& MaJiangDef.type(card) == MaJiangDef.type(card - 2)
				&& MaJiangDef.type(card) == MaJiangDef.type(card - 1))
		{
			List<Integer> tmp = new ArrayList<>(input);
			tmp.remove((Integer) (card - 2));
			tmp.remove((Integer) (card - 1));
			double scoreNew = calc(tmp, guiCard);
			if (scoreNew > scoreNewMax)
			{
				scoreNewMax = scoreNew;
				card1 = card - 2;
				card2 = card - 1;
			}
		}

		if (Collections.frequency(input, card - 1) > 0 && Collections.frequency(input, card + 1) > 0
				&& MaJiangDef.type(card) == MaJiangDef.type(card - 1)
				&& MaJiangDef.type(card) == MaJiangDef.type(card + 1))
		{
			List<Integer> tmp = new ArrayList<>(input);
			tmp.remove((Integer) (card - 1));
			tmp.remove((Integer) (card + 1));
			double scoreNew = calc(tmp, guiCard);
			if (scoreNew > scoreNewMax)
			{
				scoreNewMax = scoreNew;
				card1 = card - 1;
				card2 = card + 1;
			}
		}

		if (Collections.frequency(input, card + 1) > 0 && Collections.frequency(input, card + 2) > 0
				&& MaJiangDef.type(card) == MaJiangDef.type(card + 1)
				&& MaJiangDef.type(card) == MaJiangDef.type(card + 2))
		{
			List<Integer> tmp = new ArrayList<>(input);
			tmp.remove((Integer) (card + 1));
			tmp.remove((Integer) (card + 2));
			double scoreNew = calc(tmp, guiCard);
			if (scoreNew > scoreNewMax)
			{
				scoreNewMax = scoreNew;
				card1 = card + 1;
				card2 = card + 2;
			}
		}

		if (scoreNewMax > score)
		{
			ret.add(card1);
			ret.add(card2);
		}

		return ret;
	}

	public static boolean pengAI(List<Integer> input, List<Integer> guiCard, int card, double award)
	{
		if (guiCard.contains(card))
		{
			return false;
		}

		if (Collections.frequency(input, card) < 2)
		{
			return false;
		}

		double score = calc(input, guiCard);

		List<Integer> tmp = new ArrayList<>(input);
		tmp.remove((Integer) card);
		tmp.remove((Integer) card);
		double scoreNew = calc(tmp, guiCard);

		return scoreNew + award >= score;
	}

	public static boolean gangAI(List<Integer> input, List<Integer> guiCard, int card, double award)
	{
		if (guiCard.contains(card))
		{
			return false;
		}

		if (Collections.frequency(input, card) < 3)
		{
			return false;
		}

		double score = calc(input, guiCard);

		List<Integer> tmp = new ArrayList<>(input);
		tmp.remove((Integer) card);
		tmp.remove((Integer) card);
		tmp.remove((Integer) card);
		tmp.remove((Integer) card);
		double scoreNew = calc(tmp, guiCard);

		return scoreNew + award >= score;
	}

	public static void testOut()
	{
		String init = "3筒,5筒,1万,4万,9万,7筒,1条,4条,9条,东,西,南,北,发";
		String guiStr = "中";
		List<Integer> cards = MaJiangDef.stringToCards(init);
		List<Integer> gui = MaJiangDef.stringToCards(guiStr);

		int out = outAI(cards, gui, true);
		System.out.println(MaJiangDef.cardToString(out));
	}

	public static void testChi()
	{
		String init = "2筒,4筒,4筒,5筒";
		String guiStr = "1万";
		List<Integer> cards = MaJiangDef.stringToCards(init);
		List<Integer> gui = MaJiangDef.stringToCards(guiStr);

		System.out.println(chiAI(cards, gui, MaJiangDef.stringToCard("6筒"), MaJiangDef.stringToCard("5筒"),
				MaJiangDef.stringToCard("4筒")));
		System.out.println(MaJiangDef.cardsToString(chiAI(cards, gui, MaJiangDef.stringToCard("3筒"))));
	}

	public static void testPeng()
	{
		String init = "1万,2万,2万,1条,1条,2筒,4筒,4筒";
		String guiStr = "1万";
		List<Integer> cards = MaJiangDef.stringToCards(init);
		List<Integer> gui = MaJiangDef.stringToCards(guiStr);

		System.out.println(pengAI(cards, gui, MaJiangDef.stringToCard("1条"), 0.d));
	}

	public static void testGang()
	{
		String init = "1万,2万,2万,2万,3万,4万,4筒,4筒";
		String guiStr = "1万";
		List<Integer> cards = MaJiangDef.stringToCards(init);
		List<Integer> gui = MaJiangDef.stringToCards(guiStr);

		System.out.println(gangAI(cards, gui, MaJiangDef.stringToCard("2万"), 1.d));
	}

	public static void gen()
	{
//		AITableJian.gen();
//		AITableFeng.gen();
//		AITable.gen();
		AITableLaiNumber.gen();
//		AITableText.gen();
	}

	public synchronized static void load()
	{
		AITableJian.load();
		AITableFeng.load();
		AITable.load();
		AITableLaiNumber.load();
		AITableText.load();
	}

	private static void testHu()
	{
		ArrayList<Integer> total = new ArrayList<>();
		for (int i = MaJiangDef.WAN1; i <= MaJiangDef.JIAN_BAI; i++)
		{
			total.add(i);
			total.add(i);
			total.add(i);
			total.add(i);
		}
		Collections.shuffle(total);

		ArrayList<Integer> cards = new ArrayList<>();
		for (int i = 0; i < 14; i++)
		{
			cards.add(total.remove(0));
		}

		Collections.sort(cards);
		System.out.println("before " + MaJiangDef.cardsToString(cards));

		List<Integer> gui = new ArrayList<>();

		int step = 0;
		while (!total.isEmpty())
		{
			if (HuUtil.isHuExtra(cards, gui, 0))
			{
				Collections.sort(cards);
				System.out.println("after " + MaJiangDef.cardsToString(cards));
				System.out.println("step " + step);
				break;
			}else {
				Collections.sort(cards);
				System.out.println("手牌: " + MaJiangDef.cardsToString(cards));
			}
			step++;
			int out = outAI(cards, gui, true);
			cards.remove((Integer) out);
			Integer remove = total.remove(0);
			cards.add(remove);
			System.out.println("打出:" + MaJiangDef.cardToString(out) + "    摸进:" + MaJiangDef.cardToString(remove));
		}
	}

	public static void main(String[] args)
	{
		// 需要生成文件时 加上gen()
		gen();
//		HuUtil.load();
//		load();
//		testOut();
//		testChi();
//		testPeng();
//		testGang();
//		testHu();
	}

}
