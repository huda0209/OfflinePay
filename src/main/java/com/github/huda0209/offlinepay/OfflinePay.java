package com.github.huda0209.offlinepay;

import com.github.huda0209.offlinepay.command.CommandHandler;
import com.github.huda0209.offlinepay.listener.payCommandListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public final class OfflinePay extends JavaPlugin implements CommandExecutor {

    final String pluginName = this.getDescription().getName();
    private static Configuration config;
    private static boolean pluginMode = false;
    private static Economy econ = null;


    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();
        pluginMode = config.getBoolean("PluginMode");

        if(!pluginMode){
            getLogger().info("Mode of "+pluginName + " was FALSE now.");
        }

        if (!setupEconomy() ) {
            getLogger().warning("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new payCommandListener(this),this);
        getCommand("offlinepay").setExecutor(new CommandHandler(this));

        String[] EnableMessage = {"=============================","Plugin Name : "+pluginName ,"Author : "+ this.getDescription().getAuthors(),"============================="};
        for (String s : EnableMessage) {
            getLogger().info(s);
        }
    }

    @Override
    public void onDisable() {
        config.set("PluginMode",pluginMode);
        this.saveConfig();
        getLogger().info(pluginName+"was disable.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().info("1");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().info("2");
            return false;
        }
        getLogger().info("3");
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static boolean getMode(){
        return pluginMode;
    }

    public static void setMode(boolean bool){
        pluginMode = bool;
    }
}
