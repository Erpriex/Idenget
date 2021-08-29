package fr.erpriex.idenget;

import fr.erpriex.idenget.commands.CommandIdentity;
import fr.erpriex.idenget.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Idenget extends JavaPlugin {

    private String prefix = "§f[§bIdenget§f]§r";
    private Map<UUID, String> identities = new HashMap<>();

    @Override
    public void onEnable() {
        getCommand("identity").setExecutor(new CommandIdentity(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    public String getPrefix(){
        return prefix;
    }
    public Map<UUID, String> getIdentities(){
        return identities;
    }
}
