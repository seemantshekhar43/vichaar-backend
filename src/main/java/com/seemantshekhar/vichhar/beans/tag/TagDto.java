package com.seemantshekhar.vichhar.beans.tag;

import lombok.*;

import java.util.List;

public class TagDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagList{
        List<String> tags;
    }
}
