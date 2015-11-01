package com.tapwisdom.service.mobilenotifications.payload;

public class CreditsPayload extends Payload {
    private String credits;

    public CreditsPayload(String credits) {
        this.credits = credits;
    }

    public CreditsPayload() {

    }

    public String getCredits() {
        return credits;
    }
}
