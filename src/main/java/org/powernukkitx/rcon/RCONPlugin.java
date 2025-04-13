package org.powernukkitx.rcon;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.Config;

public class RCONPlugin extends PluginBase {
    private RCON rcon;
    private TaskHandler task;

    @Override
    public void onLoad() {
        saveResource("config.yml");
    }

    @Override
    public void onEnable() {
        Config conf = new Config(getDataFolder() + "/config.yml");
        this.rcon = new RCON(this.getServer(), conf.getString("rconPassword"), conf.getString("rconAddress", "0.0.0.0"), conf.getInt("rconPort", 1234));

        getLogger().info("RCON is running on " + conf.getString("rconAddress") + ":" + conf.getInt("rconPort"));

        task = getServer().getScheduler().scheduleRepeatingTask(new Task() {
            @Override
            public void onRun(int i) {
                if(rcon != null) rcon.check();
            }
        }, 1);
    }

    @Override
    public void onDisable() {
        if(this.rcon != null) this.rcon.close();
        if(task != null && !task.isCancelled()) task.cancel();
    }
}
