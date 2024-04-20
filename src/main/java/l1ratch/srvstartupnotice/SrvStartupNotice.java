package l1ratch.srvstartupnotice;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
//import org.bukkit.event.server.ServerShutdownEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataOutputStream;

public class SvrStartupNotice extends JavaPlugin implements Listener {

    private String botToken = "YOUR_BOT_TOKEN";
    private String chatId = "YOUR_CHAT_ID";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        sendMessageToTelegram("Сервер Minecraft выключается...");
    }

    @EventHandler
    public void onServerStart(ServerLoadEvent event) {
        sendMessageToTelegram("Сервер Minecraft включился!");
    }

    private void sendMessageToTelegram(String message) {
        String urlString = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + message;

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
                Bukkit.getLogger().info("Сообщение успешно отправлено в канал Telegram");
            } else {
                Bukkit.getLogger().warning("Не удалось отправить сообщение в канал Telegram. Код ошибки: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
