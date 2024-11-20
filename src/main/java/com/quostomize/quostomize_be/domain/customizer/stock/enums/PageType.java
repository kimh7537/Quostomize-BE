package com.quostomize.quostomize_be.domain.customizer.stock.enums;

public enum PageType {

    CREATE_STOCK("CREATE_STOCK", "/stock/create"),
    MY_STOCK("MY_STOCK", "/stock/my"),
    LINK_ACCOUNT("LINK_ACCOUNT", "/stock/link");

    private final String code;
    private final String path;

    PageType(String code, String path) {
        this.code = code;
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public String getPath() {
        return path;
    }
}
