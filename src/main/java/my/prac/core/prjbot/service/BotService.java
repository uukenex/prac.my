package my.prac.core.prjbot.service;

import java.util.HashMap;
import java.util.List;

public interface BotService {
	public void insertBotWordSaveTx(HashMap<String, Object> hashMap)  throws Exception;

	public String selectBotWordSaveOne(HashMap<String, Object> hashMap);
	
	public List<String> selectBotLimitWordSaveAll(HashMap<String, Object> hashMap);
	public List<String> selectBotWordSaveAll(HashMap<String, Object> hashMap);
	public List<String> selectBotImgSaveAll(HashMap<String, Object> hashMap);	
	
	public int selectBotWordSaveMasterCnt(HashMap<String, Object> hashMap)  throws Exception;
	public void deleteBotWordSaveMasterTx(HashMap<String, Object> hashMap)  throws Exception;
	public void deleteBotWordSaveAllDeleteMasterTx(HashMap<String, Object> hashMap)  throws Exception;
	public void deleteBotWordSaveTx(HashMap<String, Object> hashMap)  throws Exception;
	
	public String selectBotImgSaveOne(String param);
	public void insertBotImgSaveOneTx(HashMap<String, Object> hashMap)  throws Exception;
	
	public String selectBotImgMch(HashMap<String, Object> hashMap);
	
	public String selectBotImgCharSave(String req);
	public HashMap<String,String> selectBotImgCharSaveI3(String res);
	public void insertBotImgCharSaveTx(HashMap<String, Object> hashMap)  throws Exception;
	
	
	public List<String> selectBotRaidSaveAll(HashMap<String, Object> hashMap);
	public void insertBotRaidSaveTx(HashMap<String, Object> hashMap)  throws Exception;
	
	public int selectSupporters(String userId);
	
	public int insertBotWordHisTx(HashMap<String, Object> hashMap);
	public List<String> selectRoomList(HashMap<String, Object> hashMap);
}

	
