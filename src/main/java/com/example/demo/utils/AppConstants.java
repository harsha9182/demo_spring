package com.example.demo.utils;

public class AppConstants {
    public static final int ACTION_ID_ALL_PAGES=1;

    public static final int ACTION_ID_ADD_EDIT_DELETE_VIEW_DBF=2;

    public static final int ACTION_ID_ADD_EDIT_DELETE_VIEW_SFR=3;

    public static final int ACTION_ID_ADD_EDIT_DELETE_VIEW_DBF_SFR=4;

    public static final int ACTION_ID_VIEW_DBF=5;

    public enum Zones {
        NORTH_ZONE(1),
        SOUTH_ZONE(2),
        CENTRAL_ZONE(3),
        EAST_ZONE(4);

        private final int code;

        Zones(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Zones fromCode(int code) {
            for (Zones type : Zones.values()) {
                if (type.getCode() == code) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid code: " + code);
        }
    }



}
