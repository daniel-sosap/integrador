package com.integracion.sdp.dto;

public class CommentInfo {


    private String id;
    private int replyCount;
    private String content;

    public CommentInfo(String id, int replyCount, String content) {
        this.id = id;
        this.replyCount = replyCount;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public String getContent() {
        return content;
    }
}

