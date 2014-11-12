package com.googlecode.canoe.fcgi.constant;

/**
 *
 * @author panzd
 */
public enum FCGIRole {
    RESPONDER {
        @Override
        public int getId() {
            return 1;
        }
    },
    AUTHORIZER {
        @Override
        public int getId() {
            return 2;
        }
    },
    FILTER{
        @Override
        public int getId() {
            return 3;
        }
    };

    abstract public int getId();
    private static final FCGIRole[] roleMap;
    static {
        roleMap = new FCGIRole[4];
        for(FCGIRole role : values())
        {
            roleMap[role.getId()] = role;
        }
    }

    public static FCGIRole valueOf(int id)
    {
        return roleMap[id];
    }
}
