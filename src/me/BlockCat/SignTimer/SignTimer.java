package me.BlockCat.SignTimer;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class SignTimer extends JavaPlugin {

	@Override
	public void onEnable() {
		Logger.getLogger("Minecraft").info("[SignTimer] Enabled");
		this.getServer().getPluginManager().registerEvents(new BBListener(), this);
	}
}
