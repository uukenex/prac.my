package my.prac.api.games.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.prac.core.dto.Users;
import my.prac.core.prjgame1.service.Game1Service;

@Controller
public class GamePageController {
	static Logger logger = LoggerFactory.getLogger(GamePageController.class);
	@Resource(name = "core.prjgame1.Game1Service")
	Game1Service game1Service;

	/******************** Main games *********************/

	@RequestMapping(value = "/session/game1", method = RequestMethod.GET)
	public String game1(HttpSession session, Model model) {
		Users user = (Users) session.getAttribute("Users");
		if (user != null) {
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userNick", user.getUserNick());
		}
		return "session/games/game1";
	}

	@RequestMapping(value = "/session/game2", method = RequestMethod.GET)
	public String game2(HttpSession session, Model model,
			@RequestParam(required = false, defaultValue = "10") int cell) {
		if (cell > 50) {
			cell = 50;
		}
		if (cell < 10) {
			cell = 10;
		}
		Users user = (Users) session.getAttribute("Users");
		if (user != null) {
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userNick", user.getUserNick());
		}
		model.addAttribute("cell", cell);
		return "session/games/game2";
	}

	@RequestMapping(value = "/session/game3", method = RequestMethod.GET)
	public String game3(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "10") int cell)
			throws Exception {
		if (cell > 50) {
			cell = 50;
		}
		if (cell < 10) {
			cell = 10;
		}
		Users user = (Users) session.getAttribute("Users");
		if (user != null) {
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userNick", user.getUserNick());
		}
		model.addAttribute("cell", cell);
		return "session/games/game3";
	}

	@RequestMapping(value = "/session/game4", method = RequestMethod.GET)
	public String game4(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "10") int cell)
			throws Exception {
		Users user = (Users) session.getAttribute("Users");
		if (user != null) {
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userNick", user.getUserNick());
		}
		return "session/games/game4";
	}

	@RequestMapping(value = "/session/game5", method = RequestMethod.GET)
	public String game5(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "10") int cell)
			throws Exception {

		Users user = (Users) session.getAttribute("Users");
		if (user != null) {
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userNick", user.getUserNick());
		}
		return "session/games/game5";
	}

	@RequestMapping(value = "/session/game6", method = RequestMethod.GET)
	public String game6(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "10") int cell)
			throws Exception {

		Users user = (Users) session.getAttribute("Users");
		if (user != null) {
			model.addAttribute("userId", user.getUserId());
			model.addAttribute("userNick", user.getUserNick());
		}
		return "session/games/game6";
	}

	/******************** Main games end *********************/

	@RequestMapping(value = "/game1Rank", method = RequestMethod.GET)
	public List<HashMap<String, Object>> game1Rank(Model model) {
		List<HashMap<String, Object>> rankList = null;
		return rankList;
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
		return "session/games/game2_2";
	}

	@RequestMapping(value = "/session/game4_1", method = RequestMethod.GET)
	public String game4_1(Model model, @RequestParam(required = false, defaultValue = "10") int cell) throws Exception {
		return "session/games/game4_1";
	}

	@RequestMapping(value = "/session/game4_2", method = RequestMethod.GET)
	public String game4_2(Model model, @RequestParam(required = false, defaultValue = "10") int cell) throws Exception {
		return "session/games/game4_2";
	}

	@RequestMapping(value = "/session/ws", method = RequestMethod.GET)
	public String ws(Model model) throws Exception {
		return "session/games/ws";
	}

	@RequestMapping(value = "/game/rank", method = RequestMethod.GET)
	public String gameRank(Model model) throws Exception {
		return "session/games/rank";
	}

}