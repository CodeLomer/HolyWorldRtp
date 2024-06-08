package com.github.codelomer.holyworldrtp.command;

import com.github.codelomer.holyworldrtp.HolyWorldRtp;
import com.github.codelomer.holyworldrtp.config.other.impl.MessagesConfig;
import com.github.codelomer.holyworldrtp.factory.APIFactory;
import com.github.codelomer.holyworldrtp.factory.ConfigFactory;
import com.github.codelomer.holyworldrtp.rtp.Rtp;
import com.github.codelomer.holyworldrtp.rtp.specific.impl.BaseRtp;
import com.github.codelomer.holyworldrtp.rtp.primitive.impl.CustomRtp;
import com.github.codelomer.holyworldrtp.rtp.specific.impl.PlayerRtp;
import com.github.codelomer.holyworldrtp.rtp.primitive.impl.StandardRtp;
import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import com.github.codelomer.holyworldrtp.util.BukkitUtilities;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class RtpCMD extends AbstractCommand {
    private final HolyWorldRtp plugin;
    private final MessagesConfig messagesConfig;
    private final ConfigFactory configFactory;
    private final APIFactory apiFactory;

    public RtpCMD(@NonNull String command, @NonNull HolyWorldRtp plugin, @NonNull ConfigFactory configFactory, @NonNull APIFactory apiFactory) {
        super(command, plugin);
        this.plugin = plugin;
        this.messagesConfig = configFactory.getMessagesConfig();
        this.configFactory = configFactory;
        this.apiFactory = apiFactory;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof Player player && !BukkitUtilities.hasPermission(sender, "holy.rtp.reload")) {
                    BukkitLogger.logColorText(messagesConfig.getErrorArgumentsText(), player);
                    return;
                }
                plugin.reloadPlugin();
                BukkitLogger.logFine("плагин успешно перезагрузился", sender);
                return;
            }
            if (!(sender instanceof Player player)) return;
            if (args[0].equalsIgnoreCase("player")) {
                PlayerRtp playerRtp = new PlayerRtp(configFactory, player.getUniqueId(), plugin);
                if (!playerRtp.playerExistsInServer()) return;
                executeTeleport(playerRtp);
                return;
            }
            if (args[0].equalsIgnoreCase("base")) {
                BaseRtp baseRtp = new BaseRtp(configFactory, player.getUniqueId(), apiFactory, plugin);
                if (!baseRtp.dependenciesEnabled()) return;
                executeTeleport(baseRtp);
                return;
            }
            if (args[0].equalsIgnoreCase("help")) {
                BukkitLogger.logColorText(messagesConfig.getHelpText(), sender);
                return;
            }
            CustomRtp customRtp = new CustomRtp(configFactory, player.getUniqueId(), plugin);
            if (customRtp.isTypeExists(args[0].toLowerCase(Locale.ENGLISH))) {
                executeTeleport(customRtp);
                return;
            }
            BukkitLogger.logColorText(messagesConfig.getErrorArgumentsText(), player);
            return;
        }
        if (args.length == 0 && sender instanceof Player player) {
            StandardRtp standardRtp = new StandardRtp(configFactory, player.getUniqueId(), plugin);
            executeTeleport(standardRtp);
            return;
        }
        BukkitLogger.logColorText(messagesConfig.getErrorArgumentsText(), sender);
    }


    private void executeTeleport(@NonNull Rtp rtp) {
        if (!rtp.hasPermission()) return;
        if (!rtp.canTeleport()) return;
        rtp.findLocationAndTeleport();
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String[] args) {
        if (args.length == 1) {
            return List.of("help");
        }
        return null;
    }
}
