package cjay.ai.cjreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryResponse {

    private long todayCount;
    private long scheduledCount;
    private long allCount;
    private long completedCount;
    private long flaggedCount;
}
