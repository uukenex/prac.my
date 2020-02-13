package my.prac.api.games.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import my.prac.core.prjgame1.service.Game1Service;

@Controller
public class GameController {
	static Logger logger = LoggerFactory.getLogger(GameController.class);
	@Resource(name = "core.prjgame1.Game1Service")
	Game1Service game1Service;

	@RequestMapping(value = "/game1", method = RequestMethod.GET)
	public String game1(Model model) {
		return "games/game1";
	}

	@RequestMapping(value = "/game1Rank", method = RequestMethod.GET)
	public List<HashMap<String, Object>> game1Rank(Model model) {
		List<HashMap<String, Object>> rankList = null;
		return rankList;
	}

	// 댓글달기 ajax
	@RequestMapping(value = "/game1/saveGame1Cnt", method = RequestMethod.POST)
	public @ResponseBody String ajaxreply(@RequestParam String mediaCode, @RequestParam String ip,
			@RequestParam int cnt) {
		String resultMsg = "0";
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("mediaCode", mediaCode);
		hashMap.put("ip", ip);
		hashMap.put("cnt", cnt);
		try {
			game1Service.saveGame1CntTx(hashMap);
		} catch (Exception e) {
			resultMsg = "save ERR";
		}

		return resultMsg;
	}

	@RequestMapping(value = "/game2", method = RequestMethod.GET)
	public String game2(Model model, @RequestParam(required = false, defaultValue = "10") int cell) {
		if (cell > 50) {
			cell = 50;
		}
		if (cell < 10) {
			cell = 10;
		}
		model.addAttribute("cell", cell);
		return "games/game2";
	}

	@RequestMapping(value = "/game2_2", method = RequestMethod.GET)
	public String game2_2(Model model, @RequestParam(required = false, defaultValue = "10") int cell) {
		if (cell > 50) {
			cell = 50;
		}
		if (cell < 10) {
			cell = 10;
		}
		model.addAttribute("cell", cell);
		return "games/game2_2";
	}

	@RequestMapping(value = "/game3", method = RequestMethod.GET)
	public String game3(Model model, @RequestParam(required = false, defaultValue = "10") int cell) throws Exception {
		if (cell > 50) {
			cell = 50;
		}
		if (cell < 10) {
			cell = 10;
		}

		model.addAttribute("rank", game1Service.selectGame3Cnt());
		model.addAttribute("cell", cell);
		return "games/game3";
	}

	// 댓글달기 ajax
	@RequestMapping(value = "/game3/saveGame3Cnt", method = RequestMethod.POST)
	public @ResponseBody String game3ajax(@RequestParam String userName, @RequestParam String mediaCode,
			@RequestParam String ip, @RequestParam int cnt) {
		String resultMsg = "0";
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("userName", userName);
		hashMap.put("mediaCode", mediaCode);
		hashMap.put("ip", ip);
		hashMap.put("cnt", cnt);
		try {
			game1Service.saveGame3CntTx(hashMap);
		} catch (Exception e) {
			resultMsg = "save ERR";
		}

		return resultMsg;
	}

}