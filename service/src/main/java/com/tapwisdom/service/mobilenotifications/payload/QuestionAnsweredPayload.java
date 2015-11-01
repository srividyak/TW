package com.tapwisdom.service.mobilenotifications.payload;

public class QuestionAnsweredPayload extends Payload {
    private String advisorId;

    public QuestionAnsweredPayload() {
    }

    public QuestionAnsweredPayload(String advisorId) {
        this.advisorId = advisorId;
    }

    public String getAdvisorId() {
        return advisorId;
    }
}
