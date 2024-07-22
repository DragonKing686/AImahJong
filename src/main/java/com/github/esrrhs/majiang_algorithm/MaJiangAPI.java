package com.github.esrrhs.majiang_algorithm;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class MaJiangAPI {

    private static final Logger logger = LoggerFactory.getLogger(MaJiangAPI.class);


    static {
        HuUtil.load();
        AIUtil.load();
    }

    // 判断是否胡牌了
    @PostMapping("/isHu")
    public JSONObject isHu(@RequestBody JSONObject body) {
        String cards = body.getString("cards");
        String gui = body.getString("gui");
        Boolean extHu = body.getBoolean("extHu");
        List<Integer> cardsList = MaJiangDef.stringToCards(cards);
        Collections.sort(cardsList);
        boolean hu = HuUtil.isHu(cardsList, MaJiangDef.stringToCard(gui), extHu);
        JSONObject result = new JSONObject();
        result.put("resultCode", "0");
        result.put("resultData", hu);
        return result;
    }

    //判断听什么牌
    @PostMapping("/isTing")
    public JSONObject isTing(@RequestBody JSONObject body) {
        String cards = body.getString("cards");
        String gui = body.getString("gui");
        Boolean extHu = body.getBoolean("extHu");
        List<Integer> cardsList = MaJiangDef.stringToCards(cards);
        Collections.sort(cardsList);
        List<Integer> integerList = HuUtil.isTing(cardsList, MaJiangDef.stringToCard(gui), extHu);
        String cardsToString = MaJiangDef.cardsToString(integerList);
        JSONObject result = new JSONObject();
        result.put("resultCode", "0");
        result.put("resultData", cardsToString);
        return result;
    }

    //判断上家打出的牌用什么牌吃
    @PostMapping("/chiWhat")
    public JSONObject chiWhat(@RequestBody JSONObject body) {
        String cards = body.getString("cards");
        String gui = body.getString("gui");
        String other = body.getString("other");
        List<Integer> cardsList = MaJiangDef.stringToCards(cards);
        List<Integer> guiList = MaJiangDef.stringToCards(gui);
        Collections.sort(cardsList);
        ArrayList<Integer> integers = AIUtil.chiAI(cardsList, guiList, MaJiangDef.stringToCard(other));
        String cardsToString = MaJiangDef.cardsToString(integers);
        JSONObject result = new JSONObject();
        result.put("resultCode", "0");
        result.put("resultData", cardsToString);
        return result;
    }

    //判断上家打出的牌用当前选择的牌能不能吃，或者建不建议吃
    @PostMapping("/canChi")
    public JSONObject canChi(@RequestBody JSONObject body) {
        String cards = body.getString("cards");
        String gui = body.getString("gui");
        String other = body.getString("other");
        String[] split = other.split(",");
        List<Integer> cardsList = MaJiangDef.stringToCards(cards);
        List<Integer> guiList = MaJiangDef.stringToCards(gui);
        Collections.sort(cardsList);
        Boolean integers = AIUtil.chiAI(cardsList, guiList, MaJiangDef.stringToCard(split[0]),
                MaJiangDef.stringToCard(split[1]), MaJiangDef.stringToCard(split[2]));
        JSONObject result = new JSONObject();
        result.put("resultCode", "0");
        result.put("resultData", integers);
        return result;
    }

    // 判断其他人打出的牌能不能碰
    @PostMapping("/canPeng")
    public JSONObject canPeng(@RequestBody JSONObject body) {
        String cards = body.getString("cards");
        String gui = body.getString("gui");
        String other = body.getString("other");
        List<Integer> cardsList = MaJiangDef.stringToCards(cards);
        List<Integer> guiList = MaJiangDef.stringToCards(gui);
        boolean pengAI = AIUtil.pengAI(cardsList, guiList, MaJiangDef.stringToCard(other), 0.d);
        JSONObject result = new JSONObject();
        result.put("resultCode", "0");
        result.put("resultData", pengAI);
        return result;
    }

    // 判断其他人打出的牌能不能杠
    @PostMapping("/canGang")
    public JSONObject canGang(@RequestBody JSONObject body) {
        String cards = body.getString("cards");
        String gui = body.getString("gui");
        String other = body.getString("other");
        List<Integer> cardsList = MaJiangDef.stringToCards(cards);
        List<Integer> guiList = MaJiangDef.stringToCards(gui);
        boolean gangAI = AIUtil.gangAI(cardsList, guiList, MaJiangDef.stringToCard(other), 0.d);
        JSONObject result = new JSONObject();
        result.put("resultCode", "0");
        result.put("resultData", gangAI);
        return result;
    }

    // 摸到手牌之后该如何出牌，默认14张
    @PostMapping("/smartOut")
    public JSONObject smartOut(@RequestBody JSONObject body) {
        String cards = body.getString("cards");
        String gui = body.getString("gui");
        Boolean extHu = body.getBoolean("extHu");
        logger.info("Received request: cards={}, gui={}", cards, gui);
        List<Integer> cardsList = MaJiangDef.stringToCards(cards);
        List<Integer> guiList = MaJiangDef.stringToCards(gui);
        long start = System.currentTimeMillis();
        int outAI = AIUtil.outAI(cardsList, guiList, extHu == null || extHu);
        long end = System.currentTimeMillis();

        String string = MaJiangDef.cardToString(outAI);

        logger.info("AI computation result: {}, time taken: {} ms", string, (end - start));
        JSONObject result = new JSONObject();
        result.put("resultCode", "0");
        result.put("resultData", string);
        return result;
    }
}
