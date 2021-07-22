package com.github.huda0209.offlinepay.command;

import com.github.huda0209.offlinepay.OfflinePay;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

    private OfflinePay plugin;
    public CommandHandler(OfflinePay offlinePay){
        this.plugin = offlinePay;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("OfflinePay.Admin")){
            sender.sendMessage("§9[" + plugin.getDescription().getName() + "]"+command.getPermissionMessage());
            return true;
        }

        if(args.length!=1) return false;

        if(args[0].equalsIgnoreCase("mode")){
            OfflinePay.setMode(!OfflinePay.getMode());
            sender.sendMessage("§6モードを"+(OfflinePay.getMode()?"§a":"§c")+OfflinePay.getMode()+"§6に設定しました。");
            plugin.getLogger().info("§6Set mode "+(OfflinePay.getMode()?"§a":"§c")+OfflinePay.getMode()+"§r.");
            return true;
        }
        if(args[0].equalsIgnoreCase("showmode")){
            sender.sendMessage("§6現在のモードは、"+(OfflinePay.getMode()?"§a":"§c")+OfflinePay.getMode()+"§6です。");
            plugin.getLogger().info("§6Mode is "+(OfflinePay.getMode()?"§a":"§c")+OfflinePay.getMode()+"§r.");
            return true;
        }
        return false;
    }
}
