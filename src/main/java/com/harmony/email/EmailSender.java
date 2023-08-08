package com.harmony.email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailSender {
    private String to; // 수신자
    private String subject; // 메일 제목
    private String message; // 내용
}
