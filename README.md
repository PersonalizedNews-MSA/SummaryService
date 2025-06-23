요약서비스 입니다.

local 동작시 main/java/ resources/application-prod.yml 만들고

spring:
  application:
    name: demo


  ai:
    openai:
      api-key: ${OPEN_AI_API_KEY}
      
      chat:
        options:

          model: gpt-4o

api key 추가 하면 실행 됩니다.
