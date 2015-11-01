package com.tapwisdom.service.mobilenotifications.payload;


public class RequestAnswerPayload extends Payload {
    private String advisorId;

    public String getAdvisorId() {
        return advisorId;
    }

    public RequestAnswerPayload() {
    }

    public RequestAnswerPayload(String advisorId) {
        this.advisorId = advisorId;
    }
}
