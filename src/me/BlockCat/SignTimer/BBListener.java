package me.BlockCat.SignTimer;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;

public class BBListener implements Listener {

	private static final BlockFace[] faces =  new BlockFace[]{BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	public static HashMap<Lever,Boolean> isTicking = new HashMap<Lever, Boolean>();

	@EventHandler
	public void onRedstoneChange(BlockRedstoneEvent e) {
		Block block = null;

		block = e.getBlock();
		int startX = block.getX()-1;
		int startY = block.getY()-1;
		int startZ = block.getZ()-1;
		int endX = block.getX()+1;
		int endY = block.getY()+1;
		int endZ = block.getZ()+1;
		for(int x = startX; x <= endX; x++)
			for(int y = startY; y <= endY; y++)
				for(int z = startZ; z <= endZ; z++)
					if(block.getWorld().getBlockAt(x,y,z).getType() == Material.WALL_SIGN)
					{
						Block b = block.getWorld().getBlockAt(x,y,z);
						Sign sign = (Sign) b.getState();

						if (b.isBlockPowered()){
							if(sign.getLine(0).equalsIgnoreCase("[Clock]")) {
								if (sign.getLine(1).equalsIgnoreCase("")) {
									return;
								}
								double min = Double.parseDouble(sign.getLine(1));
								BlockFace s1 = null;
								Block s2 = null;
								for (BlockFace face: faces) {
									s1 = face;
									s2 = sign.getBlock().getRelative(s1).getRelative(s1);
									if (s2.getType() != Material.LEVER) {
										continue;
									}

									if (s2 != null) {						
										Lever lever = (Lever) s2.getState().getData();
										if(!isTicking.containsKey(lever)) {



											new BBTimer(lever, min, s2, null);

											lever.setPowered(true);
											s2.setTypeIdAndData(lever.getItemTypeId(), lever.getData(), true);
											s2.getState().update(true);

											isTicking.put(lever, true);
										}
									}
								}
							}
						}
					}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Block b = null;

		Player player = e.getPlayer();

		if(e.getAction() == Action.RIGHT_CLICK_BLOCK ) {
			if(e.getClickedBlock().getType() == Material.WALL_SIGN)
				if (player.isOp()) {
					b = e.getClickedBlock();
					Sign sign = (Sign) b.getState();

					if(sign.getLine(0).equalsIgnoreCase("[Clock]")) {
						if (sign.getLine(1).equalsIgnoreCase("")) {
							player.sendMessage(ChatColor.RED + "There is no time given.");
							return;
						}
						double min = Double.parseDouble(sign.getLine(1));
						BlockFace s1 = null;
						Block s2 = null;
						for (BlockFace face: faces) {
							s1 = face;
							s2 = sign.getBlock().getRelative(s1).getRelative(s1);
							if (s2.getType() != Material.LEVER) {
								continue;
							}

							if (s2 != null) {						
								Lever lever = (Lever) s2.getState().getData();
								if(!isTicking.containsKey(lever)) {

									player.sendMessage(ChatColor.GREEN + "Clock started and runs for: "+min+" Minutes.");

									new BBTimer(lever, min, s2, player);

									lever.setPowered(true);
									s2.setTypeIdAndData(lever.getItemTypeId(), lever.getData(), true);
									s2.getState().update(true);

									isTicking.put(lever, true);
								}
								else {
									player.sendMessage(ChatColor.RED + "Clock is already running.");
								}
							}
						}
					}
				}
		}

	}

	@EventHandler
	public void onSignChange(SignChangeEvent e){ 
		Block placed = e.getBlock();
		Player placer = e.getPlayer();

		if(e.getLine(0).equalsIgnoreCase("[Clock]")) {

			if (!placer.isOp()) {
				placer.sendMessage(ChatColor.RED + "You are not allowed to place [Clock] signs.");
				e.setCancelled(true);
				placed.setTypeId(0);
				return;
			}
			else if (e.getLine(1).equalsIgnoreCase("")) {
				placer.sendMessage(ChatColor.RED + "You have not specified the timer time.");
				e.setCancelled(true);
				placed.setTypeId(0);
				return;
			}
			placer.sendMessage(ChatColor.GREEN + "[Clock] Sign placed!");

		}
	}
}
