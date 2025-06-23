package com.mini2.SummaryService.service;

import com.mini2.SummaryService.common.exception.CustomException;
import com.mini2.SummaryService.common.exception.ErrorCode;
import com.mini2.SummaryService.dto.response.NewsSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final OpenAiChatModel openAiChatModel;
    public NewsSummaryResponse getSummary(String link) {
        try {
            Document doc = Jsoup.connect(link).get();
            Elements article;
            if (link.contains("https://n.news.naver.com") || link.contains("https://news.naver.com")) {
                article = doc.select("article[id^=dic_area]");
            } else {
                article = doc.select("div[class=_article_content]");
            }
            if (!article.isEmpty()) {
                article.select("strong, span, div, em, img, script, style, br").remove();

                String plainText = article.text();

                if (plainText.length() > 5000) {
                    plainText = plainText.substring(0, 5000);
                }

                String prompt = String.format("""
                        아래는 뉴스 기사 본문입니다:
                        ---
                        %s
                        ---

                        이 기사를 한국어로 약 800자 내외로 요약해 주세요.
                        핵심 인물, 사건, 발언, 맥락을 포함해 주세요.
                        간결하고 상세한 뉴스 요약문 형식으로 작성해 주세요.
                        """, plainText);

                String response = openAiChatModel.call(prompt);

                return NewsSummaryResponse.builder()
                        .summary(response)
                        .build();
            }
            return null;
        } catch (Exception e) {
            log.error("[News Service] getSummary", e);
            throw new CustomException(ErrorCode.NEWS_PARSING_ERROR);
        }
    }
}
