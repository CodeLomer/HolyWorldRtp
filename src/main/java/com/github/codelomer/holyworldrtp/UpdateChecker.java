package com.github.codelomer.holyworldrtp;


import com.github.codelomer.holyworldrtp.util.BukkitLogger;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateChecker {
    private static final Pattern VERSION_PATTERN = Pattern.compile("\"\"tag_name\":\"([^\"]+)\"\"");
    private static final String CURRENT_VERSION = "1.0.0";
    private final JavaPlugin plugin;
    private final String githubAuthor;
    private final String repositoryName;

    public UpdateChecker(@NonNull JavaPlugin plugin, @NonNull String githubAuthor, @NonNull String repositoryName){

        this.plugin = plugin;
        this.githubAuthor = githubAuthor;
        this.repositoryName = repositoryName;
    }

    public void checkUpdate(){
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try(InputStream inputStream = new URL("https://api.github.com/repos/"+githubAuthor+"/"+repositoryName+"/releases/latest").openStream()) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
                String data = scanner.hasNext() ? scanner.next() : "";
                Matcher matcher = VERSION_PATTERN.matcher(data);
                if(matcher.find()){
                    String latestVersion = matcher.group(1);
                    if(!latestVersion.equalsIgnoreCase(CURRENT_VERSION)){
                        BukkitLogger.logError("The current version of the plugin is outdated, please update it. Download link -");
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}

