package fr.erpriex.idenget.listeners;

import com.mojang.authlib.GameProfile;
import fr.erpriex.idenget.Idenget;
import fr.erpriex.idenget.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PlayerJoinListener implements Listener {

    private Idenget main;

    public PlayerJoinListener(Idenget main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(main.getIdentities().containsKey(player.getUniqueId())) {

            String identity = main.getIdentities().get(player.getUniqueId());

            try {
                Method getHandle = player.getClass().getMethod("getHandle", (Class<?>[]) null);
                Object entityPlayer = getHandle.invoke(player);
                Class<?> entityHuman = entityPlayer.getClass().getSuperclass();
                Field bH = entityHuman.getDeclaredField("bH");
                bH.setAccessible(true);
                bH.set(entityPlayer, PacketUtils.fillProfileProperties(new GameProfile(player.getUniqueId(), identity), Bukkit.getOfflinePlayer(identity).getUniqueId(), true));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.hidePlayer(player);
                    players.showPlayer(player);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.sendMessage(" \n" + main.getPrefix() + " §aVous devenez maintenant §e" + player.getName() + " §a:o\n ");
        }
    }

}
