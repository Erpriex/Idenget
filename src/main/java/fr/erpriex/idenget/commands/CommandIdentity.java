package fr.erpriex.idenget.commands;

import fr.erpriex.idenget.Idenget;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandIdentity implements CommandExecutor {

    private Idenget main;

    public CommandIdentity(Idenget main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("identity")){

                if(args.length == 0) {
                    if(main.getIdentities().containsKey(player.getUniqueId())) {
                        main.getIdentities().remove(player.getUniqueId());
                        sendConfirmMessage(player);
                    }else {
                        player.sendMessage("§cSyntaxe : /identity <pseudo>");
                    }
                }

                if(args.length >= 1) {
                    StringBuilder bc = new StringBuilder();
                    for(String part : args) {
                        bc.append(part);
                    }
                    String name = bc.toString();
                    if(name.length() > 16) {
                        player.sendMessage("§7C'est un peu long comme pseudo ça :o");
                        return true;
                    }

                    main.getIdentities().put(player.getUniqueId(), name);

                    sendConfirmMessage(player);
                }


                return true;
            }
        }
        return false;
    }

    private void sendConfirmMessage(Player player) {
        player.sendMessage(" \n" + main.getPrefix() + " §eChangement pris en compte !\nVous devez vous déconnecter puis vous reconnecter sur le serveur afin d'appliquer les modifications\n ");
    }
}
