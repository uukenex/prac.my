package my.prac.core.prjbot.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import my.prac.core.prjbot.dao.BotDAO;
import my.prac.core.prjbot.service.BotService;

@Service("core.prjbot.BotService")
public class BotServiceImpl implements BotService {
	
	@Resource(name = "core.prjbot.BotDAO")
	BotDAO botDAO;
	
	public void insertBotWordSaveTx(HashMap<String, Object> hashMap) throws Exception{
		if(botDAO.insertBotWordSave(hashMap)< 1) {
			throw new Exception("저장 실패");
		}
	}

	public String selectBotWordSaveOne(HashMap<String, Object> hashMap) {
		return botDAO.selectBotWordSaveOne(hashMap);
	}
	
	public List<String> selectBotWordSaveAll(HashMap<String, Object> hashMap){
		return botDAO.selectBotWordSaveAll(hashMap);
	}

	
}