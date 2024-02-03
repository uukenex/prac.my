package my.prac.api.loa.controller;

import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import my.prac.core.util.LoaApiParser;
import my.prac.core.util.LoaApiUtils;

@Controller
public class LoaChatController {
	static Logger logger = LoggerFactory.getLogger(LoaChatController.class);

	// final String lostArkAPIurl =
	// "https://developer-lostark.game.onstove.com/armories/characters/일어난다람쥐/equipment";
	final String lostArkAPIurl = "https://developer-lostark.game.onstove.com";

	@RequestMapping(value = "/loa/chat", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> chatApplication(
			@RequestParam(required = true)  String param0,
			@RequestParam(required = false) String param1,
			@RequestParam(required = true) String room) {
		

		try {
			System.out.println(param0 + " " + param1);
			String val = autoResponse(param0,param1,room);
			if(val!=null&&!val.equals("")) {
				HashMap<String, Object> rtnMap = new HashMap<>();
				rtnMap.put("data", val);
				return rtnMap;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	//roomName은 https://cafe.naver.com/msgbot/2067 수정본 참조
	String autoResponse(String param0,String param1,String roomName) throws Exception {
		String val="";
		int randNum;
		
		if(roomName.equals("로아냥떼")) {
			switch (param0) {

			case "/택티컬모코코":
				val = "사라진 길드원입니다";
				break;
			case "/두목":
				val = "용인피주먹";
				break;
			case "/우고":
				val = "도굴단장";
				break;
			case "/도굴단장":
				val = "우고";
				break;
			case "/퇴근":
				val = "쥰내 신나는 텍스트";
				break;

			}
		}else if(roomName.equals("내 갈길 간다")) {
			switch (param0) {
			
			case "/비실": case "/비실이":
				randNum = (int) (Math.random() * 10);
				switch (randNum) {
				case 0: case 1:
					val = "안넣어두대!";
					break;
				case 2: case 3:	case 4:	case 5:
					val = "아니거든요";
					break;
				case 6:	case 7:	case 8:	case 9:
					val = "아닌데";
					break;
				}

				break;
			case "/돔돔": case "/돔돔쨩":
				val = "비실이 아냐";
				break;
			}
		}
		
		switch (param0) {
			case "/모험섬":
				val = calendarSearch();
				break;
			case "/장비":
				if(param1!=null && !param1.equals("")) {
					try {
						val = equipmentSearch(param1);
						//val = tttt(param1);
					}catch (Exception e) {
						val = errorCodeMng(e);
					}
				}
				break;
			case "/초월":
				if(param1!=null && !param1.equals("")) {
					try {
						val = limitSearch(param1);
					}catch (Exception e) {
						val = errorCodeMng(e);
					}
					
				}
				break;	
			case "/항협": case "/항해": case "/항해협동":
				val = shipSearch();
				break;
			case "/날씨":
				if(param1!=null && !param1.equals("")) {
					val = weatherSearch(param1);
				}
				break;
			
			default:
				break;
			
		}
		
		return val;
	}
	
	String errorCodeMng(Exception e) {
		String val="";
		if(e != null && e.getMessage()!=null) {
			switch(e.getMessage()) {
			case "E0001":
				val = "레벨이 1610 보다 낮습니다";
				break;
			case "E0002":
				val = "초월 검색 오류";
				break;
			case "E0003":
				val = "캐릭터명 검색 오류";
				break;
			default:
				val = "ID오류이거나 엘릭서/초월이 모두있어야 검색가능합니다";
				e.printStackTrace();
				break;
			}
		}else {
			val = "ID오류이거나 엘릭서/초월이 모두있어야 검색가능합니다";
			e.printStackTrace();
		}
		return val;
	}
	
	String shipSearch() throws Exception {
		String retMsg="오늘의 항협";
		String retMsg1="";
		String retMsg2="";
		String retMsg3="";
		
		String paramUrl = lostArkAPIurl + "/gamecontents/calendar";
		
		String returnData = LoaApiUtils.connect_process(paramUrl);
		List<Map<String, Object>> data_list = new ObjectMapper().readValue(returnData, new TypeReference<List<Map<String, Object>>>() {});
		
		for(Map<String,Object> data_sub_list : data_list) {
			if(data_sub_list.get("CategoryName").equals("항해")) {
				if(data_sub_list.get("StartTimes")!=null) {
					List<String> start_time_list = (List<String>)data_sub_list.get("StartTimes");
					for(String time : start_time_list) {
						if(time.equals(LoaApiUtils.StringToDate()+"T19:30:00")) {
							retMsg1 = retMsg1 +data_sub_list.get("ContentsName").toString()+", ";
						}
						if(time.equals(LoaApiUtils.StringToDate()+"T21:30:00")) {
							retMsg2 = retMsg2 +data_sub_list.get("ContentsName").toString()+", ";
						}
						if(time.equals(LoaApiUtils.StringToDate()+"T23:30:00")) {
							retMsg3 = retMsg3 +data_sub_list.get("ContentsName").toString()+", ";
						}
					}
				}
			}
		}
		
		retMsg1 = "<br>(오후 7:30)"+ retMsg1;
		retMsg2 = "<br>(오후 9:30)"+ retMsg2;
		retMsg3 = "<br>(오후11:30)"+ retMsg3;
		
		retMsg = retMsg+retMsg1+retMsg2+retMsg3;
		
		return retMsg;
	}
	String calendarSearch() throws Exception {
		String retMsg="오늘의 모험 섬";
		String retMsg1="";
		String retMsg2="";
		
		int cnt = 0; 
		String paramUrl = lostArkAPIurl + "/gamecontents/calendar";
		
		String returnData = LoaApiUtils.connect_process(paramUrl);
		List<Map<String, Object>> data_list = new ObjectMapper().readValue(returnData, new TypeReference<List<Map<String, Object>>>() {
		});
		
		for(Map<String,Object> data_sub_list : data_list) {
			if(data_sub_list.get("CategoryName").equals("모험 섬")) {
				List<Map<String, Object>> rewardItemsList = (List<Map<String, Object>>)data_sub_list.get("RewardItems");
				for(Map<String, Object> rewardItem : rewardItemsList) {
					
					if(rewardItem.get("StartTimes")!=null) {
						List<String> start_time_list = (List<String>)rewardItem.get("StartTimes");
						for(String time : start_time_list) {
							//if(time.equals("2024-01-28"+"T09:00:00")) {
							if(time.equals(LoaApiUtils.StringToDate()+"T09:00:00")) {
								switch(rewardItem.get("Name").toString()) {
									case "전설 ~ 고급 카드 팩 III":
										retMsg1 = retMsg1 + "</br>";
										retMsg1 = retMsg1 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg1+="카드";
										cnt++;
										break;
									case "실링":
										retMsg1 = retMsg1 + "</br>";
										retMsg1 = retMsg1 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg1+="실링";
										cnt++;
										break;
									case "대양의 주화 상자":
										retMsg1 = retMsg1 + "</br>";
										retMsg1 = retMsg1 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg1+="주화";
										cnt++;
										break;
									case "골드":
										retMsg1 = retMsg1 + "</br>";
										retMsg1 = retMsg1 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg1+="♣골드♣";
										cnt++;
										break;
									default:
										continue;
								}
							}
							//if(time.equals("2024-01-28"+"T19:00:00")) {
							if(time.equals(LoaApiUtils.StringToDate()+"T19:00:00")) {
								switch(rewardItem.get("Name").toString()) {
									case "전설 ~ 고급 카드 팩 III":
										retMsg2 = retMsg2 + "</br>";
										retMsg2 = retMsg2 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg2+="카드";
										cnt++;
										break;
									case "실링":
										retMsg2 = retMsg2 + "</br>";
										retMsg2 = retMsg2 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg2+="실링";
										cnt++;
										break;
									case "대양의 주화 상자":
										retMsg2 = retMsg2 + "</br>";
										retMsg2 = retMsg2 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg2+="주화";
										cnt++;
										break;
									case "골드":
										retMsg2 = retMsg2 + "</br>";
										retMsg2 = retMsg2 + data_sub_list.get("ContentsName").toString()+" : ";
										retMsg2+="♣골드♣";
										cnt++;
										break;
									default:
										continue;
								}
							}
						}
					}
				}
			}
		}
		
		if(cnt>=6) {
			retMsg1 = "</br>☆(오전)"+ retMsg1;
			retMsg2 = "</br>★(오후)"+ retMsg2;
		}
		
		retMsg = retMsg+retMsg1+retMsg2;

		return retMsg;
	}

	
	String tttt(String userId) throws Exception {
		String ordUserId=userId;
		userId = URLEncoder.encode(userId, "UTF-8");
		// +는 %2B로 치환한다
		String paramUrl = lostArkAPIurl + "/armories/characters/" + userId + "?filters=equipment";
		String returnData = LoaApiUtils.connect_process(paramUrl);
		HashMap<String, Object> rtnMap = new ObjectMapper().readValue(returnData,new TypeReference<Map<String, Object>>() {});

		List<Map<String, Object>> armoryEquipment;
		try {
			armoryEquipment = (List<Map<String, Object>>) rtnMap.get("ArmoryEquipment");
		}catch(Exception e){
			throw new Exception("E0003");
		}
		
		
		for (Map<String, Object> equip : armoryEquipment) {
			switch (equip.get("Type").toString()) {
			case "무기":
			case "투구":
			case "상의":
			case "하의":
			case "장갑":
			case "어깨":
				HashMap<String, Object> tooltip = new ObjectMapper().readValue((String) equip.get("Tooltip"),new TypeReference<Map<String, Object>>() {});
				HashMap<String, Object> maps = LoaApiParser.findElement(tooltip);
				System.out.println(maps);
			}
		}
		
		return "";
	}
	
	
	String limitSearch(String userId) throws Exception {
		String ordUserId=userId;
		userId = URLEncoder.encode(userId, "UTF-8");
		// +는 %2B로 치환한다
		String paramUrl = lostArkAPIurl + "/armories/characters/" + userId + "?filters=equipment";
		String returnData = LoaApiUtils.connect_process(paramUrl);
		HashMap<String, Object> rtnMap = new ObjectMapper().readValue(returnData,new TypeReference<Map<String, Object>>() {});

		List<Map<String, Object>> armoryEquipment;
		try {
			armoryEquipment = (List<Map<String, Object>>) rtnMap.get("ArmoryEquipment");
		}catch(Exception e){
			throw new Exception("E0003");
		}
		
		String [] elixirList = LoaApiParser.getElixirList();
		List<String> equipElixirList = new ArrayList<>();
		
		String resMsg = ordUserId+" 초월정보";

		String resEquip = "";
		String totLmit ="";
		int totElixir =0;
		
		for (Map<String, Object> equip : armoryEquipment) {
			switch (equip.get("Type").toString()) {
			case "투구":
			case "상의":
			case "하의":
			case "장갑":
			case "어깨":
				HashMap<String, Object> tooltip = new ObjectMapper().readValue((String) equip.get("Tooltip"),new TypeReference<Map<String, Object>>() {});
				HashMap<String, Object> maps = LoaApiParser.findElement(tooltip);
				HashMap<String, Object> limit_element = (HashMap<String, Object>)maps.get("limit_element");
				HashMap<String, Object> elixir_element = (HashMap<String, Object>)maps.get("elixir_element");
				
				//초월 정보 출력
				totLmit = LoaApiParser.parseLimit(limit_element);
				resEquip = resEquip + "</br>"+equip.get("Type").toString()+"▶" + LoaApiParser.parseLimitForLimit(limit_element)+" ◈ ";
				resEquip = LoaApiUtils.filterText(resEquip);

				//엘릭서 정보 출력 
				totElixir +=LoaApiParser.parseElixirForEquip(equipElixirList,elixir_element);
				resEquip  +=LoaApiParser.parseElixirForLimit(equipElixirList,elixir_element);
				
				break;
				default:
				continue;
			}
		}
		
		String elixirField="";
		int cnt =0 ;
		for(String elixir:elixirList) {
			cnt += Collections.frequency(equipElixirList, elixir);
			if(cnt > 1) { // 회심2 를 회심으로 표기 
				elixirField = elixirField + elixir;
				break;
			}else {
				continue;
			}
			
		}
		resEquip=resEquip.replaceAll("  ", " ");
		
		resMsg = resMsg + "</br>"+"초월합 : " + totLmit + " 엘릭서합 : " + totElixir + "(" + elixirField+")";
		resMsg = resMsg + "</br>";
		resMsg = resMsg +  resEquip;
		
		return resMsg;
	}
	
	
	String equipmentSearch(String userId) throws Exception {
		String ordUserId=userId;
		userId = URLEncoder.encode(userId, "UTF-8");
		// +는 %2B로 치환한다
		String paramUrl = lostArkAPIurl + "/armories/characters/" + userId + "?filters=equipment";
		String returnData = LoaApiUtils.connect_process(paramUrl);
		HashMap<String, Object> rtnMap = new ObjectMapper().readValue(returnData,new TypeReference<Map<String, Object>>() {});

		List<Map<String, Object>> armoryEquipment;
		try {
			armoryEquipment = (List<Map<String, Object>>) rtnMap.get("ArmoryEquipment");
		}catch(Exception e){
			throw new Exception("E0003");
		}
		
		List<String> equipSetList = new ArrayList<>();
		List<String> equipElixirList = new ArrayList<>();
		
		String weaponQualityValue="";
		double armorQualityValue=0;
		
		double tmpLv=0;
		double avgLv=0;
		
		
		String resMsg = ordUserId+" 장비정보";

		String enhanceLv="";
		String newEnhanceInfo="";
		String totLmit ="";
		int totElixir =0;

		for (Map<String, Object> equip : armoryEquipment) {
			HashMap<String, Object> tooltip = new ObjectMapper().readValue((String) equip.get("Tooltip"),new TypeReference<Map<String, Object>>() {});
			HashMap<String, Object> maps = LoaApiParser.findElement(tooltip);
			HashMap<String, Object> weapon_element = (HashMap<String, Object>)maps.get("weapon_element");
			HashMap<String, Object> item_level_element = (HashMap<String, Object>)maps.get("item_level_element");
			HashMap<String, Object> item_level_element_dt = new HashMap<>();
			HashMap<String, Object> new_refine_element = (HashMap<String, Object>)maps.get("new_refine_element");
			HashMap<String, Object> limit_element = (HashMap<String, Object>)maps.get("limit_element");
			HashMap<String, Object> elixir_element = (HashMap<String, Object>)maps.get("elixir_element");
			
			
			//악세들은 레벨파싱에서 에러가남 
			switch (equip.get("Type").toString()) {
			case "무기":case "투구": case "상의": case "하의": case "장갑": case "어깨":
				String setFind = Jsoup.parse((String) weapon_element.get("value")).text();
				if(equip.get("Type").toString().equals("무기")) {
					enhanceLv = setFind.replaceAll("[^0-9]", "");
				}
				
				for(String set:LoaApiParser.getSetList()) {
					if(setFind.indexOf(set) >= 0) {
						equipSetList.add(set);
					}
				}
				
				item_level_element_dt = (HashMap<String, Object>) item_level_element.get("value");
				
				/* 아이템레벨 */
				tmpLv = Integer.parseInt(Jsoup.parse((String) item_level_element_dt.get("leftStr2")).text().replaceAll("[^0-9]|[0-9]\\)$", ""));
				avgLv = avgLv+tmpLv;
				
				/* 아이템레벨 */
				if(tmpLv < 1610) {
					throw new Exception("E0001");
				}
			}
			
			
			
			switch (equip.get("Type").toString()) {
			case "무기":
				/* 무기품질 */
				weaponQualityValue = item_level_element_dt.get("qualityValue").toString();
				if(new_refine_element.size()>0) {
					newEnhanceInfo = Jsoup.parse((String) new_refine_element.get("value")).text();
					newEnhanceInfo = LoaApiUtils.filterText(newEnhanceInfo);
				}
				
				break;
			case "투구": case "상의": case "하의": case "장갑": case "어깨":
				/* 방어구품질 */
				//armorQualityValue = armorQualityValue + Integer.parseInt(item_level_element_dt.get("qualityValue").toString());
				//초월
				totLmit = LoaApiParser.parseLimit(limit_element);
				//엘릭서
				totElixir +=LoaApiParser.parseElixirForEquip(equipElixirList, elixir_element);
				break;
			default:
			continue;
			}
		}
		
		String setField="";
		String elixerField="";
		
		for(String set:LoaApiParser.getSetList()) {
			int cnt0=0;
			cnt0 += Collections.frequency(equipSetList, set);
			if(cnt0 > 0) {
				setField = setField+cnt0+set;
			}
			
		}
		int cnt1=0;
		for(String elixer: LoaApiParser.getElixirList()) {
			cnt1 += Collections.frequency(equipElixirList, elixer);
			if(cnt1 > 1) { // 회심2 를 회심으로 표기 
				elixerField = elixerField + elixer;
				break;
			}else {
				continue;
			}
			
		}
		
		resMsg = resMsg + "</br>"+"ItemLv : "+ String.format("%.2f", (avgLv/6));
		resMsg = resMsg + "</br>"+"↪무기 : "+enhanceLv+"강, 무품 : "+weaponQualityValue+""; 
		
		if(!newEnhanceInfo.equals("")) {
			resMsg = resMsg + "</br>"+"↪무기 "+newEnhanceInfo; 
		}
		
		resMsg = resMsg + "</br>"+"↪세트 : "+setField;
		resMsg = resMsg + "</br>"+"↪초월합 : " + totLmit + " 엘릭서합: " + totElixir + "(" + elixerField+")";
		return resMsg;
	}

	
	
	
	
	String weatherSearch(String area) throws Exception {
		HashMap<String, Object> rtnMap = new HashMap<>();

		String retMsg = "";
		String errMsg = "불러올 수 없는 지역이거나 지원되지 않는 지역입니다.</br> ↪ex)00시00구00동 (띄어쓰기없이)";
		try {
			LoaApiUtils.setSSL();
			String WeatherURL = "https://m.search.naver.com/search.naver?&query=날씨+" + area;
			Document doc = Jsoup.connect(WeatherURL).get();
			String cur_temp = doc.select(".weather_info ._today .temperature_text strong").text();
			String weather = doc.select(".weather_info ._today .before_slash").text();
			String diff_temp = doc.select(".weather_info ._today .temperature_info .temperature").text();// 어제와 온도차이
			String v1 = doc.select(".weather_info ._today .summary_list .sort:eq(0) .desc").text();// 체감
			String v2 = doc.select(".weather_info ._today .summary_list .sort:eq(1) .desc").text();// 습도
			String v3 = doc.select(".weather_info ._today .summary_list .sort:eq(2) .desc").text();// 풍속
			
			
			if(cur_temp.equals("")) {
				return errMsg;
			}
			
			retMsg += "오늘날씨 : " + weather;
			retMsg += "</br>↪현재온도 : " + cur_temp;
			retMsg += "</br>↪체감온도 : " + v1;
			retMsg += "</br>↪습도 : " + v2;
			retMsg += "</br>↪풍속 : " + v3;
			retMsg += "</br>↪현재 " + area + "의 온도는 " + cur_temp + " 이며 어제보다 " + diff_temp;
		} catch (Exception e) {
			e.printStackTrace();
			retMsg = errMsg;
		}
		rtnMap.put("data", retMsg);
		return retMsg;
	}
	
	
}
