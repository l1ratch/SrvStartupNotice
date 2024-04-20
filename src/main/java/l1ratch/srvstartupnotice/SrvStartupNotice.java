package l1ratch.srvstartupnotice;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
//import org.bukkit.event.server.ServerShutdownEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataOutputStream;

public class SrvStartupNotice extends JavaPlugin implements Listener {

    private String botToken = getConfig().getString("botToken");
    private String chatId = getConfig().getString("chatId");
    private String serverId = getConfig().getString("serverId");

    @Override
    public void onEnable() {
        final File f = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!f.exists()) {
            saveDefaultConfig();
            //Bukkit.getLogger().info("Файл конфигурации не был найден! Создаю новый...");
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        sendMessageToTelegram(getConfig().getString("serverDisable"));
    }

    @EventHandler
    public void onServerStart(ServerLoadEvent event) {
        sendMessageToTelegram(getConfig().getString("serverEnable"));
    }

    private void sendMessageToTelegram(String message) {
        String urlString = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + message + serverId;

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Bukkit.getLogger().info("The message was successfully sent to the Telegram channel!");
            } else {
                Bukkit.getLogger().warning("The message could not be sent to the Telegram channel. Error code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
