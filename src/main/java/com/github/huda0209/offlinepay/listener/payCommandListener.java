package com.github.huda0209.offlinepay.listener;

import com.github.huda0209.offlinepay.OfflinePay;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.UUID;

public class payCommandListener implements Listener {
    private OfflinePay plugin;
    private Economy economy = OfflinePay.getEconomy();

    public payCommandListener(OfflinePay offlinePay) {
        this.plugin = offlinePay;
    }

    @EventHandler
    public void payCommandHandler(PlayerCommandPreprocessEvent event){

        String[] command =  event.getMessage().substring(1).split(" ");

        if(!OfflinePay.getMode()) return;
        if(!command[0].equalsIgnoreCase("pay")) return;
        if(command.length != 3) return;

        OfflinePlayer[] offlinePlayers = plugin.getServer().getOfflinePlayers();

        UUID toPlayerUUID = null;
        OfflinePlayer toPlayer;
        OfflinePlayer fromPlayer = event.getPlayer();

        for(int i=0;i<offlinePlayers.length;i++){
            if(offlinePlayers[i].getName()==null) continue;
            if(!offlinePlayers[i].getName().equalsIgnoreCase(command[1])) continue;
            toPlayerUUID=offlinePlayers[i].getUniqueId();
        }

        if(toPlayerUUID==null){
            event.getPlayer().sendMessage("§c支払い先プレイヤーのUUIDを取得できませんでした。支払い先プレイヤーにサーバーへログインするようにお願いしてください。");
            plugin.getLogger().warning("§cCan`t get UUID of toPlayer. Please tell toPlayer login the server.");
            return;
        };

        event.setCancelled(true);
        toPlayer = plugin.getServer().getOfflinePlayer(toPlayerUUID);

        if(!economy.hasAccount(fromPlayer) || !economy.hasAccount(toPlayer)){
            event.getPlayer().sendMessage("§c自身、若しくは相手プレイヤーの口座が存在しなかった為、処理ができませんでした。");
            plugin.getLogger().warning("§cfromPlayer or toPlayer didn't has Bank Account. So, I stop this process.");
            return;
        };

        double payAmount = -1;

        try{
            payAmount = Double.parseDouble(command[2]);
        }catch(Exception e){
            event.getPlayer().sendMessage("§c支払い金額を数値に変換できませんでした。数値のみを入れて再試行してください。");
            plugin.getLogger().warning("§ccan`t parse to double. So, I stop this process.");
            return;
        }

        if(payAmount<0){
            event.getPlayer().sendMessage("§c支払い金額がマイナスのため、処理できませんでした。正の数値で行ってください。");
            plugin.getLogger().warning("§cPay amount was Negative. So, I stop this process.");
            return;
        };
        if(economy.getBalance(fromPlayer) < payAmount){
            event.getPlayer().sendMessage("§c支払金額があなたの口座残高より多いため、処理できませんでした。");
            plugin.getLogger().warning("§cToo many pay amount. So, I stop this process.");
            return;
        };

        economy.withdrawPlayer(fromPlayer, payAmount);
        economy.depositPlayer(toPlayer, payAmount);

        event.getPlayer().sendMessage("§a"+toPlayer.getName() +"§cへ "+ payAmount + "円 支払いました。\n所持金 : "+economy.getBalance(fromPlayer)+"円");
        plugin.getLogger().info("§a"+fromPlayer.getName()+"§6 paid §c"+payAmount+"§6 to §a"+toPlayer.getName()+"§6.");
    }
}
