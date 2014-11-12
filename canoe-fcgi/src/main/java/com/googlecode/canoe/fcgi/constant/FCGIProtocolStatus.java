package com.googlecode.canoe.fcgi.constant;

/**
 *
 * @author panzd
 */
public enum FCGIProtocolStatus {
    FCGI_REQUEST_COMPLETE {
        @Override
        public int getId()
        {
            return 0;
        }
    },
    FCGI_CANT_MPX_CONN {
        @Override
        public int getId()
        {
            return 1;
        }
    },
    FCGI_OVERLOADED{
        @Override
        public int getId()
        {
            return 2;
        }
    },
    FCGI_UNKNOWN_ROLE{
        @Override
        public int getId()
        {
            return 3;
        }
    };
    
    public abstract int getId();
    
    private static final FCGIProtocolStatus[] statusMap;
    static {
        statusMap = new FCGIProtocolStatus[4];
        for(FCGIProtocolStatus status : values())
        {
            statusMap[status.getId()] = status;
        }
    }

    public static FCGIProtocolStatus valueOf(int id)
    {
        return statusMap[id];
    }
}
