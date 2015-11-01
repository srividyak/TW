package com.tapwisdom.service.mobilenotifications.payload;

public class QnAUpVotedPayload extends Payload {
    private String qnaSessionId;
    private String questionId;

    public QnAUpVotedPayload() {
    }

    public QnAUpVotedPayload(String qnaSessionId, String questionId) {
        this.qnaSessionId = qnaSessionId;
        this.questionId = questionId;
    }

    public String getQnaSessionId() {
        return qnaSessionId;
    }

    public String getQuestionId() {
        return questionId;
    }
}
