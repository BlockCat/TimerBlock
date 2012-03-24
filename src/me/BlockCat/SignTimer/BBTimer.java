package me.BlockCat.SignTimer;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Lever;

public class BBTimer {
	
	Player player;

	public BBTimer (Lever lever, double min, Block s2, Player p) {
		player = p;
		Timer timer = new Timer();
		
		int del = (int) (min*60*1000);
				
		timer.schedule(new Task(lever, s2), del);
	}
	
	public BBTimer (Lever lever, double min, Block s2) {
		player = null;
		Timer timer = new Timer();
		
		int del = (int) (min*60*1000);
				
		timer.schedule(new Task(lever, s2), del);
		
		
	}
	
	public class Task extends TimerTask {

		Lever lever;
		Block s2;
		
		
		public Task (Lever lever, Block s2) {
			
			this.lever = lever;
			this.s2 = s2;
		}
		
		@Override
		public void run() {
			lever.setPowered(false);
			s2.setTypeIdAndData(lever.getItemTypeId(), lever.getData(), true);
			
			s2.getState().update(true);
			BBListener.isTicking.remove(lever);
			if(player != null)
			player.sendMessage(ChatColor.AQUA + "Clock has stopped running.");
						
		}
		
	}
}
