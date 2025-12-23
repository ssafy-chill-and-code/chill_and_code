package com.ssafy.chillandcode.model.dto.post;

/**
 * HashtagRank는 해시태그별 게시글 수 집계 결과를 담는 DTO 입니다.
 */
public class HashtagRank {
    private String hashtag;
    private Long count;

    public HashtagRank() {}

    public HashtagRank(String hashtag, Long count) {
        this.hashtag = hashtag;
        this.count = count;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

