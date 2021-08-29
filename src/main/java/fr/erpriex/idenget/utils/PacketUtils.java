package fr.erpriex.idenget.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.response.MinecraftProfilePropertiesResponse;
import com.mojang.util.UUIDTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketUtils {

    private static Map<UUID, Property> uidSkins = new HashMap<>();

    public static GameProfile fillProfileProperties(GameProfile profile, UUID skinId, boolean requireSecure) throws Exception {
        if(!uidSkins.containsKey(skinId)) {
            MinecraftSessionService sessionService = ((CraftServer)Bukkit.getServer()).getServer().aD();

            YggdrasilAuthenticationService auth = ((YggdrasilMinecraftSessionService)sessionService).getAuthenticationService();

            URL url = HttpAuthenticationService.constantURL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDTypeAdapter.fromUUID(skinId));

            url = HttpAuthenticationService.concatenateURL(url, "unsigned=" + !requireSecure);
            Method MAKE_REQUEST = null;
            try {
                MAKE_REQUEST = YggdrasilAuthenticationService.class.getDeclaredMethod("makeRequest", URL.class, Object.class, Class.class);
                MAKE_REQUEST.setAccessible(true);
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            MinecraftProfilePropertiesResponse response = (MinecraftProfilePropertiesResponse)MAKE_REQUEST.invoke(auth, url, null, MinecraftProfilePropertiesResponse.class);
            if(response == null)
                return profile;

            uidSkins.put(skinId, response.getProperties().values().iterator().next());
        }

        PropertyMap map = new PropertyMap();

        map.put("textures", uidSkins.get(skinId));

        GameProfile result = new GameProfile(profile.getId(), profile.getName());
        result.getProperties().putAll(map);
        profile.getProperties().putAll(map);

        return result;
    }

}
