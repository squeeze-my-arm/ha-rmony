package com.harmony.boardUser;

public enum BoardUserEnum {
    USER(Authority.USER),  // 사용자 (board에 초대받은 사용자)
    ADMIN(Authority.ADMIN);  // 관리자 (board를 만든 사용자)

    private final String authority;

    BoardUserEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
