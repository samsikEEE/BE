package com.sprta.samsike.domain.member;

public enum MemberRoleEnum {
    ROLE_CUSTOMER(Authrity.CUSTOMER),
    ROLE_OWNER(Authrity.OWNER),
    ROLE_MANAGER(Authrity.MANAGER),
    ROLE_MASTER(Authrity.MASTER);

    private final String authority;

    MemberRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authrity {
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String MASTER = "ROLE_MASTER";

    }
}
