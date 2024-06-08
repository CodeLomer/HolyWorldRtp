package com.github.codelomer.holyworldrtp.factory;

import com.github.codelomer.holyworldrtp.api.impl.PsPluginAPI;
import com.github.codelomer.holyworldrtp.api.impl.WGPluginAPI;

public class APIFactory {
    private final WGPluginAPI wgPluginAPI;
    private final PsPluginAPI psPluginAPI;

    public APIFactory() {
        wgPluginAPI = new WGPluginAPI();
        psPluginAPI = new PsPluginAPI();
        wgPluginAPI.loadPlugin();
        psPluginAPI.loadPlugin();
    }

    public WGPluginAPI getWgPluginAPI() {
        return wgPluginAPI;
    }

    public PsPluginAPI getPsPluginAPI() {
        return psPluginAPI;
    }
}
