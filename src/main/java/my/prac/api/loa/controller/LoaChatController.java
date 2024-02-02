package my.prac.api.loa.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

import my.prac.core.util.PropsUtil;

@Controller
public class LoaChatController {
	static Logger logger = LoggerFactory.getLogger(LoaChatController.class);

	final String lostArkKey = "bearer "+PropsUtil.getProperty("keys","loaKey");

	// final String lostArkAPIurl =
	// "https://developer-lostark.game.onstove.com/armories/characters/일어난다람쥐/equipment";
	final String lostArkAPIurl = "https://developer-lostark.game.onstove.com";

	@RequestMapping(value = "/loa/chat", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> chatApplication(
			@RequestParam(required = true)  String param0,
			@RequestParam(required = false) String param1) {
		

		try {
			System.out.println(param0 + " " + param1);
			String val = autoResponse(param0,param1);
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

	
	String autoResponse(String param0,String param1) throws Exception {
		String val="";
		int randNum;
		switch (param0) {
			case "/비실":
			case "/비실이":
				randNum = (int) (Math.random() * 10);
				System.out.println(randNum);
				switch(randNum) {
				case 0:
				case 1:
					val="안넣어두대!";
					break;
				case 2:
				case 3:
				case 4:
				case 5:
					val="아니거든요";
					break;
				case 6:
				case 7:
				case 8:
				case 9:
					val="아닌데";
					break;
				}
				
				break;
			case "/돔돔":
			case "/돔돔쨩":
				val="비실이 아냐";
				break;
				
			case "/택티컬모코코":
				val="사라진 길드원입니다";
				break;	
			case "/두목":
				val="용인피주먹";
				break;
			case "/우고":
				val="도굴단장";
				break;
			case "/도굴단장":
				val="우고";
				break;
			case "/퇴근":
				val="쥰내 신나는 텍스트";
				break;
			case "/나랑꽃":
				val="네 수령님";
				break;
			case "/수령님":
				val="네 나랑꽃님";
				break;
				
				
			case "/모험섬":
				val = calendarSearch();
				break;
			case "/장비":
				if(param1!=null && !param1.equals("")) {
					try {
						val = equipmentSearch(param1);
					}catch (Exception e) {
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
					}
				}
				break;
			case "/초월":
				if(param1!=null && !param1.equals("")) {
					try {
						val = limitSearch(param1);
					}catch (Exception e) {
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
					}
					
				}
				break;	
			case "/항협":
			case "/항해":
			case "/항해협동":
				val = shipSearch();
				break;
			case "/날씨":
				if(param1!=null && !param1.equals("")) {
					val = weatherSearch(param1);
				}
				break;
			
			default:
				//System.out.println(param1);
				break;
			
		}
		
		return val;
	}
	
	String shipSearch() throws Exception {
		String retMsg="오늘의 항협";
		String retMsg1="";
		String retMsg2="";
		String retMsg3="";
		
		String paramUrl = lostArkAPIurl + "/gamecontents/calendar";
		
		String returnData = connect_process(paramUrl);
		List<Map<String, Object>> data_list = new ObjectMapper().readValue(returnData, new TypeReference<List<Map<String, Object>>>() {
		});
		
		for(Map<String,Object> data_sub_list : data_list) {
			if(data_sub_list.get("CategoryName").equals("항해")) {
				if(data_sub_list.get("StartTimes")!=null) {
					List<String> start_time_list = (List<String>)data_sub_list.get("StartTimes");
					for(String time : start_time_list) {
						if(time.equals(StringToDate()+"T19:30:00")) {
							retMsg1 = retMsg1 +data_sub_list.get("ContentsName").toString()+", ";
						}
						if(time.equals(StringToDate()+"T21:30:00")) {
							retMsg2 = retMsg2 +data_sub_list.get("ContentsName").toString()+", ";
						}
						if(time.equals(StringToDate()+"T23:30:00")) {
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
		// +는 %2B로 치환한다
		String paramUrl = lostArkAPIurl + "/gamecontents/calendar";
		
		String returnData = connect_process(paramUrl);
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
							if(time.equals(StringToDate()+"T09:00:00")) {
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
							if(time.equals(StringToDate()+"T19:00:00")) {
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

	
	String limitSearch(String userId) throws Exception {
		String ordUserId=userId;
		userId = URLEncoder.encode(userId, "UTF-8");
		// +는 %2B로 치환한다
		String paramUrl = lostArkAPIurl + "/armories/characters/" + userId + "?filters=equipment";

		String returnData = connect_process(paramUrl);
		
		HashMap<String, Object> rtnMap = new ObjectMapper().readValue(returnData,new TypeReference<Map<String, Object>>() {
		});

		List<Map<String, Object>> armoryEquipment;
		try {
			armoryEquipment = (List<Map<String, Object>>) rtnMap.get("ArmoryEquipment");
		}catch(Exception e){
			throw new Exception("E0003");
		}
		
		String [] elixerList = {"강맹","달인","신념","회심","선각자","선봉대","행운","진군","칼날방패"};
		List<String> equipElixerList = new ArrayList<>();
		
		String resMsg = ordUserId+" 초월정보";

		String resEquip = "";
		String totLmit ="";
		int totElixir =0;
		
		boolean newEnhanceYn=false;
		
		
		for (Map<String, Object> equip : armoryEquipment) {
			switch (equip.get("Type").toString()) {
			case "무기":
			case "투구":
			case "상의":
			case "하의":
			case "장갑":
			case "어깨":
				HashMap<String, Object> tooltip = new ObjectMapper().readValue((String) equip.get("Tooltip"),
						new TypeReference<Map<String, Object>>() {
						});
				

				HashMap<String, Object> element_005;
				HashMap<String, Object> element_008;
				HashMap<String, Object> element_008_value;
				HashMap<String, Object> element_008_value1;
				HashMap<String, Object> element_008_value2;
				HashMap<String, Object> element_008_value3;
				HashMap<String, Object> element_009;
				HashMap<String, Object> element_009_value;
				HashMap<String, Object> element_009_value1;
				HashMap<String, Object> element_009_value2;
				HashMap<String, Object> element_009_value3;
				HashMap<String, Object> element_010;
				HashMap<String, Object> element_010_value;
				HashMap<String, Object> element_010_value1;
				HashMap<String, Object> element_010_value2;
				HashMap<String, Object> element_010_value3;
				switch (equip.get("Type").toString()) {
				case "무기":
					break;
				case "투구":
				case "상의":
				case "하의":
				case "장갑":
				case "어깨":
					
					/* 상급제련 */
					element_005 = (HashMap<String, Object>) tooltip.get("Element_005");
					if(element_005.toString().indexOf("상급 재련")>=0) {
						newEnhanceYn = true;
					}
					
					
					if(newEnhanceYn) {
						newEnhanceYn=false;
						
						element_009 = (HashMap<String, Object>) tooltip.get("Element_009");
						if(element_009.toString().indexOf("초월") < 0) {
							throw new Exception("E0002");
						}
						element_009_value = (HashMap<String, Object>) element_009.get("value");
						element_009_value1 = (HashMap<String, Object>) element_009_value.get("Element_000");
						element_009_value2 = (HashMap<String, Object>) element_009_value1.get("contentStr");
						element_009_value3 = (HashMap<String, Object>) element_009_value2.get("Element_001");

						totLmit = Jsoup.parse((String) element_009_value3.get("contentStr")).text().replaceAll("[^0-9]", "");
						
						resEquip = resEquip + "</br>"+"["+equip.get("Type").toString()+"]" + Jsoup.parse((String) element_009_value1.get("topStr")).text()+" ◈ ";
						resEquip = resEquip.replaceAll("\\[초월\\]", "");
						
						element_010 = (HashMap<String, Object>) tooltip.get("Element_010");
						element_010_value = (HashMap<String, Object>) element_010.get("value");
						element_010_value1 = (HashMap<String, Object>) element_010_value.get("Element_000");
						element_010_value2 = (HashMap<String, Object>) element_010_value1.get("contentStr");

						String elixerFind;
						
						element_010_value3 = (HashMap<String, Object>) element_010_value2.get("Element_000");
						elixerFind = Jsoup.parse((String) element_010_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						resEquip = resEquip+ elixerFind+" ";
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
						
						
						element_010_value3 = (HashMap<String, Object>) element_010_value2.get("Element_001");
						elixerFind = Jsoup.parse((String) element_010_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						resEquip = resEquip+ elixerFind+" ";
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
						
						
					}else {
						element_008 = (HashMap<String, Object>) tooltip.get("Element_008");
						if(element_008.toString().indexOf("초월") < 0) {
							throw new Exception("E0002");
						}
						element_008_value = (HashMap<String, Object>) element_008.get("value");
						element_008_value1 = (HashMap<String, Object>) element_008_value.get("Element_000");
						element_008_value2 = (HashMap<String, Object>) element_008_value1.get("contentStr");
						element_008_value3 = (HashMap<String, Object>) element_008_value2.get("Element_001");

						totLmit = Jsoup.parse((String) element_008_value3.get("contentStr")).text().replaceAll("[^0-9]", "");
						
						resEquip = resEquip + "</br>"+"["+equip.get("Type").toString()+"]" + Jsoup.parse((String) element_008_value1.get("topStr")).text()+" ◈ ";
						resEquip = resEquip.replaceAll("\\[초월\\]", "");
						
						element_009 = (HashMap<String, Object>) tooltip.get("Element_009");
						element_009_value = (HashMap<String, Object>) element_009.get("value");
						element_009_value1 = (HashMap<String, Object>) element_009_value.get("Element_000");
						element_009_value2 = (HashMap<String, Object>) element_009_value1.get("contentStr");

						String elixerFind;
						
						element_009_value3 = (HashMap<String, Object>) element_009_value2.get("Element_000");
						elixerFind = Jsoup.parse((String) element_009_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						resEquip = resEquip+ elixerFind+" ";
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
						
						
						element_009_value3 = (HashMap<String, Object>) element_009_value2.get("Element_001");
						elixerFind = Jsoup.parse((String) element_009_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						
						resEquip = resEquip+ elixerFind+" ";
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
					}
					
					
					
					break;
					
				}
				default:
				continue;
			}
		}
		
		String elixerField="";
		int cnt =0 ;
		for(String elixer:elixerList) {
			cnt += Collections.frequency(equipElixerList, elixer);
			//System.out.println(cnt);
			if(cnt > 1) { // 회심2 를 회심으로 표기 
				elixerField = elixerField + elixer;
				break;
			}else {
				continue;
			}
			
		}
		
		resEquip=resEquip.replaceAll("  ", " ");
		//resEquip=resEquip.replaceAll("공격력", "공");
		
		
		resMsg = resMsg + "</br>"+"초월합 : " + totLmit + " 엘릭서합 : " + totElixir + "(" + elixerField+")";
		resMsg = resMsg + "</br>";
		resMsg = resMsg +  resEquip;
		
		return resMsg;
	}
	
	String filterTextForElixer(String txt) {
		txt=txt.replaceAll(" ", "");
		txt=txt.replaceAll("\\[공용\\]", "");
		txt=txt.replaceAll("\\[하의\\]", "");
		txt=txt.replaceAll("\\[장갑\\]", "");
		txt=txt.replaceAll("\\[어깨\\]", "");
		txt=txt.replaceAll("\\[투구\\]", "");
		txt=txt.replaceAll("\\[상의\\]", "");
		txt=txt.replaceAll("\\(혼돈\\)", "");
		txt=txt.replaceAll("\\(질서\\)", "");
		txt=txt.replaceAll("Lv.", "");
		txt=txt.replaceAll("회피의달인", "회달");
		txt=txt.replaceAll("탈출의달인", "탈달");
		txt=txt.replaceAll("폭발물달인", "폭달");
		txt=txt.replaceAll("생명의축복", "생축");
		txt=txt.replaceAll("자원의축복", "자축");
		txt=txt.replaceAll("최대생명력", "최생");
		txt=txt.replaceAll("무기공격력", "무공");
		return txt;
	}
	
	
	
	String equipmentSearch(String userId) throws Exception {
		String ordUserId=userId;
		userId = URLEncoder.encode(userId, "UTF-8");
		// +는 %2B로 치환한다
		String paramUrl = lostArkAPIurl + "/armories/characters/" + userId + "?filters=equipment";

		String returnData = connect_process(paramUrl);
		
		HashMap<String, Object> rtnMap = new ObjectMapper().readValue(returnData,new TypeReference<Map<String, Object>>() {
		});

		List<Map<String, Object>> armoryEquipment;
		try {
			armoryEquipment = (List<Map<String, Object>>) rtnMap.get("ArmoryEquipment");
		}catch(Exception e){
			throw new Exception("E0003");
		}
		String [] setList = {"악몽","환각","지배","사멸","갈망","배신","파괴","구원","매혹"};
		String [] elixerList = {"강맹","달인","신념","회심","선각자","선봉대","행운","진군","칼날방패"};
		
		List<String> equipSetList = new ArrayList<>();
		List<String> equipElixerList = new ArrayList<>();
		
		String weaponQualityValue="";
		double armorQualityValue=0;
		
		double tmpLv=0;
		double avgLv=0;
		
		
		String resMsg = ordUserId+" 장비정보";

		String enhanceLv="";
		String newEnhanceLv="";
		String newEnhanceInfo="";
		String totLmit ="";
		int totElixir =0;

		boolean newEnhanceYn=false;
		
		for (Map<String, Object> equip : armoryEquipment) {
			switch (equip.get("Type").toString()) {
			case "무기":
			case "투구":
			case "상의":
			case "하의":
			case "장갑":
			case "어깨":
				HashMap<String, Object> tooltip = new ObjectMapper().readValue((String) equip.get("Tooltip"),
						new TypeReference<Map<String, Object>>() {
						});
				
				
				HashMap<String, Object> element_000 = (HashMap<String, Object>) tooltip.get("Element_000");
				
				
				
				String setFind = Jsoup.parse((String) element_000.get("value")).text();
				if(equip.get("Type").toString().equals("무기")) {
					enhanceLv = setFind.replaceAll("[^0-9]", "");
				}
				
				for(String set:setList) {
					if(setFind.indexOf(set) >= 0) {
						equipSetList.add(set);
					}
				}
				
				HashMap<String, Object> element_001 = (HashMap<String, Object>) tooltip.get("Element_001");
				HashMap<String, Object> element_001_value = (HashMap<String, Object>) element_001.get("value");

				HashMap<String, Object> element_005;
				HashMap<String, Object> element_008;
				HashMap<String, Object> element_008_value;
				HashMap<String, Object> element_008_value1;
				HashMap<String, Object> element_008_value2;
				HashMap<String, Object> element_008_value3;
				HashMap<String, Object> element_009;
				HashMap<String, Object> element_009_value;
				HashMap<String, Object> element_009_value1;
				HashMap<String, Object> element_009_value2;
				HashMap<String, Object> element_009_value3;
				HashMap<String, Object> element_010;
				HashMap<String, Object> element_010_value;
				HashMap<String, Object> element_010_value1;
				HashMap<String, Object> element_010_value2;
				HashMap<String, Object> element_010_value3;
				switch (equip.get("Type").toString()) {
				case "무기":
					/* 무기품질 */
					weaponQualityValue = element_001_value.get("qualityValue").toString();
					/* 아이템레벨 */
					tmpLv = Integer.parseInt(Jsoup.parse((String) element_001_value.get("leftStr2")).text().replaceAll("[^0-9]|[0-9]\\)$", ""));
					if(tmpLv < 1610) {
						throw new Exception("E0001");
					}
					avgLv = avgLv+tmpLv;
					
					element_005 = (HashMap<String, Object>) tooltip.get("Element_005");
					if(element_005.toString().indexOf("상급 재련")>=0) {
						newEnhanceInfo = Jsoup.parse((String) element_005.get("value")).text();
						newEnhanceInfo = filterTextForElixer(newEnhanceInfo);
					}
					
					break;
				case "투구":
				case "상의":
				case "하의":
				case "장갑":
				case "어깨":
					/* 방어구품질 */
					armorQualityValue = armorQualityValue + Integer.parseInt(element_001_value.get("qualityValue").toString());
					/* 아이템레벨 */
					tmpLv = Integer.parseInt(Jsoup.parse((String) element_001_value.get("leftStr2")).text().replaceAll("[^0-9]|[0-9]\\)$", ""));
					avgLv = avgLv  + tmpLv;
					
					/* 상급제련 */
					element_005 = (HashMap<String, Object>) tooltip.get("Element_005");
					if(element_005.toString().indexOf("상급 재련")>=0) {
						newEnhanceYn = true;
					}
					
					if(newEnhanceYn) {
						newEnhanceYn=false;
						newEnhanceLv = Jsoup.parse((String) element_005.get("value")).text().replaceAll("[^0-9]", "");
						/*009 : 초월*/
						/** 초월 로직 시작*/
						element_009 = (HashMap<String, Object>) tooltip.get("Element_009");
						if(element_009.toString().indexOf("초월") < 0) {
							throw new Exception("E0002");
						}
						element_009_value = (HashMap<String, Object>) element_009.get("value");
						element_009_value1 = (HashMap<String, Object>) element_009_value.get("Element_000");
						element_009_value2 = (HashMap<String, Object>) element_009_value1.get("contentStr");
						element_009_value3 = (HashMap<String, Object>) element_009_value2.get("Element_001");

						totLmit = Jsoup.parse((String) element_009_value3.get("contentStr")).text().replaceAll("[^0-9]", "");
						
						/** 초월 로직 끝*/
						/*009: 엘릭서*/
						/** 엘릭서 로직 시작*/
						element_010 = (HashMap<String, Object>) tooltip.get("Element_010");
						element_010_value = (HashMap<String, Object>) element_010.get("value");
						element_010_value1 = (HashMap<String, Object>) element_010_value.get("Element_000");
						element_010_value2 = (HashMap<String, Object>) element_010_value1.get("contentStr");

						String elixerFind;
						
						element_010_value3 = (HashMap<String, Object>) element_010_value2.get("Element_000");
						elixerFind = Jsoup.parse((String) element_010_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
						
						element_010_value3 = (HashMap<String, Object>) element_010_value2.get("Element_001");
						elixerFind = Jsoup.parse((String) element_010_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
						/** 엘릭서 로직 끝 */
						
					}else {
						/*008 : 초월*/
						/** 초월 로직 시작*/
						element_008 = (HashMap<String, Object>) tooltip.get("Element_008");
						if(element_008.toString().indexOf("초월") < 0) {
							throw new Exception("E0002");
						}
						element_008_value = (HashMap<String, Object>) element_008.get("value");
						element_008_value1 = (HashMap<String, Object>) element_008_value.get("Element_000");
						element_008_value2 = (HashMap<String, Object>) element_008_value1.get("contentStr");
						element_008_value3 = (HashMap<String, Object>) element_008_value2.get("Element_001");

						totLmit = Jsoup.parse((String) element_008_value3.get("contentStr")).text().replaceAll("[^0-9]", "");
						
						/** 초월 로직 끝*/
						/*009: 엘릭서*/
						/** 엘릭서 로직 시작*/
						element_009 = (HashMap<String, Object>) tooltip.get("Element_009");
						element_009_value = (HashMap<String, Object>) element_009.get("value");
						element_009_value1 = (HashMap<String, Object>) element_009_value.get("Element_000");
						element_009_value2 = (HashMap<String, Object>) element_009_value1.get("contentStr");

						String elixerFind;
						
						element_009_value3 = (HashMap<String, Object>) element_009_value2.get("Element_000");
						elixerFind = Jsoup.parse((String) element_009_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
						
						element_009_value3 = (HashMap<String, Object>) element_009_value2.get("Element_001");
						elixerFind = Jsoup.parse((String) element_009_value3.get("contentStr").toString().split("<br>")[0]).text();
						elixerFind = filterTextForElixer(elixerFind);
						totElixir += Integer.parseInt(elixerFind.replaceAll("[^1-5]", ""));
						
						for(String elixer:elixerList) {
							if(elixerFind.indexOf(elixer) >= 0) {
								equipElixerList.add(elixer);
							}
						}
						/** 엘릭서 로직 끝 */
					}
					break;
					
				}
				default:
				continue;
			}
		}
		
		String setField="";
		String elixerField="";
		
		for(String set:setList) {
			int cnt0=0;
			cnt0 += Collections.frequency(equipSetList, set);
			if(cnt0 > 0) {
				setField = setField+cnt0+set;
			}
			
		}
		int cnt1=0;
		for(String elixer:elixerList) {
			cnt1 += Collections.frequency(equipElixerList, elixer);
			if(cnt1 > 1) { // 회심2 를 회심으로 표기 
				elixerField = elixerField + elixer;
				break;
			}else {
				continue;
			}
			
		}
		
		resMsg = resMsg + "</br>"+"ItemLV : "+ String.format("%.2f", (avgLv/6));
		resMsg = resMsg + "</br>"+"↪무기 : "+enhanceLv+"강, 무품 : "+weaponQualityValue+""; 
		
		if(!newEnhanceInfo.equals("")) {
			resMsg = resMsg + "</br>"+"↪무기 "+newEnhanceInfo; 
		}
		
		resMsg = resMsg + "</br>"+"↪세트 : "+setField;
		resMsg = resMsg + "</br>"+"↪초월합 : " + totLmit + " 엘릭서합: " + totElixir + "(" + elixerField+")";
		return resMsg;
	}

	
	String connect_process(String paramUrl) throws Exception {
		List<Map<String, Object>>rtnMap = new ArrayList<>();
		
		URL url = new URL(paramUrl);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// POST 요청을 위해 기본값이 false인 setDoOutput을 true로

		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", lostArkKey);

		// 결과 코드가 200이라면 성공
		int responseCode = conn.getResponseCode();
		System.out.println("### getAccessToken responseCode : " + responseCode);

		// 서버로부터 데이터 읽어오기
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		}

		return sb.toString();
	}
	
	
	
	String weatherSearch(String area) throws Exception {
		HashMap<String, Object> rtnMap = new HashMap<>();

		String retMsg = "";
		String errMsg = "불러올 수 없는 지역이거나 지원되지 않는 지역입니다.</br> ↪ex)00시00구00동 (띄어쓰기없이)";
		try {
			setSSL();
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
	
	public void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	public String StringToDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = dateFormat.format(new Date());
        
        return dateTime;
	}
}
