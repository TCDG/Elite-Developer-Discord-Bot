package com.collectivedev.edb.api;

import com.collectivedev.edb.api.persist.AbstractDataStore;

public abstract class IBot {
    
    public abstract AbstractDataStore getDataStore();
}