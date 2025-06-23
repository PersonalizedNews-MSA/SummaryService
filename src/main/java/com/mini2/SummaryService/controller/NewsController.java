package com.mini2.SummaryService.controller;


import com.mini2.SummaryService.common.exception.CustomException;
import com.mini2.SummaryService.common.exception.ErrorCode;
import com.mini2.SummaryService.dto.request.NewsSummaryRequest;
import com.mini2.SummaryService.dto.response.NewsSummaryResponse;
import com.mini2.SummaryService.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "News API", description = "뉴스 조회 및 AI 요약 관련 API")
public class NewsController {
    private final NewsService newsService;
    @Operation(summary = "뉴스 링크 본문 AI 요약", description = "뉴스 링크를 AI가 요약하여 제공합니다.")
//    @PostMapping("/news/summary")
    @PostMapping("/api/summary/v1")
    public ResponseEntity<NewsSummaryResponse> getSummary(@RequestBody NewsSummaryRequest request){
        NewsSummaryResponse response = newsService.getSummary(request.getLink());
        if(response == null)
            throw new CustomException(ErrorCode.NEWS_PARSING_ERROR);
        return ResponseEntity.ok(response);
    }

}
