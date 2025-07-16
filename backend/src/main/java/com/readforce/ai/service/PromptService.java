package com.readforce.ai.service;

import org.springframework.stereotype.Service;

import com.readforce.ai.dto.AiGeneratePassageRequestDto;
import com.readforce.common.enums.CategoryEnum;
import com.readforce.common.enums.LanguageEnum;
import com.readforce.common.enums.TypeEnum;
import com.readforce.passage.entity.Language;
import com.readforce.passage.entity.Level;
import com.readforce.passage.entity.Passage;
import com.readforce.passage.service.LevelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromptService {

	private final LevelService levelService;

	public String gernerateTestVocabularyPrompt(Language language, Level level) {

		String prompt = "";
  
		switch(language.getLanguageName()) {
  
		case KOREAN:
		prompt = String.format("""
				당신은 한국어 어휘의 전문가입니다.
                         '난이도 %d (%s)' 수준의 단어 데이터를 생성해야 합니다.
   
                         ## 중요 규칙
                         - 반드시 **하나의 JSON 객체** 형식으로만 응답해야 합니다.
                         - 절대로 JSON 배열(리스트) 형식인 `[ ]` 로 감싸서 반환하면 안 됩니다.
   
                         ## 올바른 응답 형식 (JSON 객체):
                         {
                           "title": "단어",
                           "content": "단어의 뜻",
                           "level": "난이도 1 (초등 저학년)"
                         }
   
                         ## 잘못된 응답 형식 (JSON 배열):
                         [
                           {
                             "title": "단어",
                             "content": "단어의 뜻",
                             "level": "난이도 1 (초등 저학년)"
                           }
                         ]
   
                         이제 규칙에 맞춰 '난이도 %d (%s)' 수준의 단어를 생성해 주세요.
				""", 
				level.getLevelNumber(), 
				level.getVocabularyLevel(), 
				level.getLevelNumber(), 
				level.getVocabularyLevel());
		break;
     
		default:
        
		}
  
		return prompt;

	}
	
	public String generateTestFactualPassageAndQuestionPrompt(Language language, Level level) {
	      
		String prompt = "";
  
		switch(language.getLanguageName()) {
  
			case KOREAN:
				prompt = String.format("""
						당신은 한국어 '사실적 이해' 문제 출제 전문가입니다.
                      '난이도 %d (%s)' 수준의 짧은 글을 생성하고, 글의 내용과 일치하는 내용을 찾는 객관식 문제를 JSON 형식으로 생성해 주세요.
                      정답 1개와 매력적인 오답 3개를 포함하고, 정답의 위치는 무작위로 설정해 주세요.
      
                      요청 형식:
                      {
                           "title": "여기에 지문 제목을 생성해주세요.",
                           "content": "여기에 지문 내용을 생성해주세요.",
                           "question": "생성된 질문",
                           "choiceList": ["선택지 1", "선택지 2", "선택지 3", "선택지 4"],
                        "correctAnswerIndex": 정답_선택지의_인덱스 (0-3),
                          "explanation": {
                             "correct": "정답 해설...",
                             "incorrect": "오답 해설..."
                            }
                      }
						""", level.getLevelNumber(), level.getVocabularyLevel());
				break;

			default:
            
		}
      
		return prompt;
      
	}
	
	public String generateTestInferentialPassageAndQuestionPrompt(Language language, Level level) {
	      
		String prompt = "";
	      
		switch(language.getLanguageName()) {
	      
			case KOREAN:
				prompt = String.format("""
						당신은 한국어 '추론적 이해' 문제 출제 전문가입니다.
                         '난이도 %d (%s)' 수준의 짧은 글을 생성하고, 글의 내용을 바탕으로 추론할 수 있는 내용을 찾는 객관식 문제를 JSON 형식으로 생성해 주세요.
                         정답 1개와 매력적인 오답 3개를 포함하고, 정답의 위치는 무작위로 설정해 주세요.

                         요청 형식:
                         {
                              "title": "여기에 지문 제목을 생성해주세요.",
                              "content": "여기에 지문 내용을 생성해주세요.",
                              "question": "생성된 질문",
                              "choiceList": ["선택지 1", "선택지 2", "선택지 3", "선택지 4"],
                           "correctAnswerIndex": 정답_선택지의_인덱스 (0-3),
                             "explanation": {
                                "correct": "정답 해설...",
                                "incorrect": "오답 해설..."
                               }
                         }
						""", level.getLevelNumber(), level.getVocabularyLevel());
				break;
	            
			default:
	     
		}
	      
		return prompt;
	      
	}
	
	public String generatePassagePrompt(AiGeneratePassageRequestDto aiGeneratePassageRequestDto) {

		String languageString = getLanguageString(aiGeneratePassageRequestDto.getLanguage());
		    
		String categoryString = getCategoryString(
				aiGeneratePassageRequestDto.getLanguage(),
				aiGeneratePassageRequestDto.getCategory()
		);
		String typeString = getTypeString(
				aiGeneratePassageRequestDto.getLanguage(),
				aiGeneratePassageRequestDto.getCategory(),
				aiGeneratePassageRequestDto.getType()
		);

		Level level = levelService.getLevelByLevel(aiGeneratePassageRequestDto.getLevel());

		String prompt = "";

		switch (aiGeneratePassageRequestDto.getCategory()) {

			case NEWS:
				
				switch (aiGeneratePassageRequestDto.getLanguage()) {

		        	case KOREAN:
		        		prompt = String.format("""
		        				당신은 %s 분야를 다루는 전문 기자입니다. 이번 기사 주제는 '%s'입니다.
	                            지금부터 다음 조건에 맞춰 뉴스 기사를 작성해 주세요.

	                            1. **언어:** %s
	                            2. **카테고리:** %s
	                            3. **유형:** %s
	                            4. **난이도:** 전체 10의 난이도 중 %d
	                               - **어휘:** %s 수준
	                               - **문장 구조:** %s
	                               - **내용:** '%s' 주제에 맞는 실제 사례나 최근 이슈를 포함해 주세요.
	                               - **분량:** 제목 포함 %d개 문단으로 구성해 주세요.

	                            5. **출력 형식:** 아래 JSON 형식으로만 응답해 주세요. 다른 설명은 포함하지 마세요.

	                            {
	                              "title": "기사 제목",
	                              "content": "기사 본문 내용. 문단 구분을 위해 '\\n\\n'를 사용하세요."
	                            }	        				
		        				""",
		                            typeString,
		                            typeString,
		                            languageString,
		                            categoryString,
		                            typeString,
		                            level.getLevelNumber(),
		                            level.getVocabularyLevel(),
		                            level.getSentenceStructure(),
		                            typeString,
		                            level.getParagraphCount()
		        				);
		        		break;
		        	
		        	default:
		        		prompt = "지원하지 않는 언어입니다.";
		        		break;
		            }
		            break;
			
			default:
				prompt = "지원하지 않는 카테고리입니다.";
				break;
		    }
		
		return prompt;
		
	}
	
	public String generateQuestionPrompt(Passage passage) {

		String prompt = "";

		switch(passage.getLanguage().getLanguageName()) {

			case KOREAN:

				prompt = String.format("""
						당신은 문해력 평가 전문가입니다. 주어진 텍스트와 그 텍스트의 난이도에 맞춰, 학생의 이해도를 정확히 측정할 수 있는 문제와 상세한 해설을 생성해 주세요.
               
               1.  **주어진 텍스트:**
                   {%s}
               
               2.  **텍스트의 난이도:** 전체 10의 난이도 중 {%d}
               
               3.  **생성 조건:**
                   * **질문 유형:** {%s}에 해당하는 질문 유형으로 문제를 생성해야 합니다.
                   * **문제 수:** {%d}
                   * **선택지 구성:** 문제의 난이도에 맞게 오답 선택지의 매력도를 조절해 주세요. (예: 난이도가 높을수록 오답이 더 정교하고 미묘해야 함)
                   * **해설 생성:** 정답의 근거와 오답이 틀린 이유를 명확하게 설명하는 해설을 포함해 주세요.
               
               4.  **출력 형식:** 아래의 JSON 배열 형식에 맞춰서, 다른 설명 없이 JSON 데이터만 출력해 주세요.
               
               [
                 {
                   "question": "문제 내용",
                   "choiceList": ["선택지 1", "선택지 2", "선택지 3", "선택지 4"],
                   "correctAnswerIndex": 0,
                   "explanation": {
                     "correct": "정답 해설...",
                     "incorrect": "오답 해설..."
                   }
                 }
               ]
						""", 
		            passage.getContent(), 
		            passage.getLevel().getLevelNumber(), 
		            passage.getLevel().getQuestionType(),
		            3);
				break;
				
			default:
		}

		return prompt;

	}
	
	public String gernerateTestVocabularyQuestionPrompt(Language language, Level level, String title, String content) {
	      
		String prompt = "";
      
		switch(language.getLanguageName()) {
      
			case KOREAN:
				prompt = String.format("""
						 당신은 한국어 어휘 문제 출제 전문가입니다.
                  '난이도 %d (%s)' 수준의 단어인 '%s'의 의미를 묻는 객관식 문제를 생성해 주세요.
                  아래 예시 문장을 문제에 반드시 포함하고, 정답 1개와 매력적인 오답 3개를 포함한 총 4개의 선택지를 만들어 주세요.
                  정답의 위치는 무작위로 설정해 주세요.
         
                  - 단어: %s
                  - 예시 문장: %s
         
                  반드시 아래의 JSON 형식으로만 응답해 주세요:
                  {
                        "question": "생성된 질문",
                        "choiceList": ["선택지 1", "선택지 2", "선택지 3", "선택지 4"],
                        "correctAnswerIndex": 정답_선택지의_인덱스 (0-3),
                        "explanation": {
                             "correct": "정답 해설...",
                           "incorrect": "오답 해설..."
                          }
                  }
						""", level.getLevelNumber(), level.getVocabularyLevel(), title, title, content);
				default:
		}


		return prompt;

	}
	
	private String getTypeString(LanguageEnum language, CategoryEnum category, TypeEnum type) {

	      switch(language) {
	      
	         case KOREAN:
	            
	            switch(category) {
	            
	               case NEWS:
	                  
	                  switch(type) {
	                  
	                        case POLITICS: 
	                           return "정부 정책, 법안, 선거";
	                        case ECONOMY: 
	                           return "경제 지표, 기업, 시장";
	                        case SOCIETY:
	                           return "교육, 환경, 사건";
	                        case LIFE_AND_CULTURE: 
	                           return "문화·라이프스타일, 건강";
	                        case IT_AND_SCIENCE: 
	                           return "AI, 우주, 기술 혁신";
	                        case WORLD: 
	                           return "국제 사건, 글로벌 이슈";
	                        case SPORTS: 
	                           return "경기 결과, 선수";
	                        case ENTERTAINMENT: 
	                           return "영화, 스타 활동";
	                        default:
	                  }
	                  
	               break;
	               
	               default:
	            
	            }
	            
	            break;
	            
	         case ENGLISH:
	            
	         case JAPANESE:
	      
	         default:
	      
	      }

	      return null;

	   }
	
	private String getCategoryString(LanguageEnum language, CategoryEnum category) {

	      switch(language) {
	      
	         case KOREAN:
	            
	            switch(category) {
	            
	            case NEWS:
	               return "뉴스";
	               
	            case NOVEL:
	               return "소설";
	               
	            case FAIRY_TALE:
	               return "동화";
	               
	            default:
	            
	            }
	            
	         case ENGLISH:
	      
	         case JAPANESE:
	      
	         default:
	            
	      }
	      return null;
	   }


	   private String getLanguageString(LanguageEnum language) {

	      switch(language) {
	      
	         case KOREAN:
	            return "한국어";
	      
	         case ENGLISH:
	            return "Englisgh";
	            
	         case JAPANESE:
	            return "日本語";
	            
	         default:
	            
	      }
	      
	      return null;
	      
	   }


	   
	
}
