package redsmith.crossmine;

import java.io.File;
import java.nio.file.Files;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.vk2gpz.tokenenchant.api.EnchantInfo;
import com.vk2gpz.tokenenchant.api.InvalidTokenEnchantException;
import com.vk2gpz.tokenenchant.api.PotionHandler;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;


public class CrossMine extends PotionHandler {	
	
	public CrossMine(TokenEnchantAPI arg0) throws InvalidTokenEnchantException {
		super(arg0);
		super.loadConfig();
		
		try {
			worldguard = WorldGuardPlugin.inst();
			container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		} catch (Exception e) {
			console.sendMessage(ChatColor.DARK_RED+"[CrossMine] - Failed to get instance of WorldGuard! ("+ChatColor.RED + e.toString() + ChatColor.DARK_RED+")");
		}

		
		checkConfig();
	}
	
	
	ConsoleCommandSender console = Bukkit.getConsoleSender();
	WorldGuardPlugin worldguard;
	RegionContainer container;
	
	
	File configFile = new File("plugins"+File.separator+"TokenEnchant"+File.separator+"enchants"+File.separator+"CrossMine_config.yml");
	
	void checkConfig() {
		if (!configFile.exists()) {
			try {
				Files.copy(getClass().getResourceAsStream("config.yml"), configFile.toPath());
				console.sendMessage(ChatColor.GREEN+"[CrossMine] - Wrote default CrossMine_config.yml");
				console.sendMessage(ChatColor.GREEN+"[CrossMine] - Reload might be needed to activate CrossMine.");
			} catch (Exception e) {
				console.sendMessage(ChatColor.DARK_RED+"[CrossMine] - Failed to write default config file! ("+ChatColor.RED + e.toString() + ChatColor.DARK_RED+")");
			}
		}
		
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block block = event.getBlock();
		EnchantInfo enchantment = hasCE(player);
		
		if (enchantment != null && enchantment.getHandler().getName().equalsIgnoreCase("CrossMine")) {
			if (canBreak(player, block)) {
				process(player, block, enchantment.getLevel());
			}
		}
	}
	
	
	private void process(Player player, Block block, int level) {
		
		Block[] scheduledBlocks = new Block[level*6];
		Block[] candidateBlocks = new Block[level*6];
		
		for (int i = 0; i < level; i++) {
			candidateBlocks[i*6]   = block.getRelative((i+1), 0, 0);
			candidateBlocks[i*6+1] = block.getRelative(-(i+1), 0, 0);
			candidateBlocks[i*6+2] = block.getRelative(0, (i+1), 0);
			candidateBlocks[i*6+3] = block.getRelative(0, -(i+1), 0);
			candidateBlocks[i*6+4] = block.getRelative(0, 0, (i+1));
			candidateBlocks[i*6+5] = block.getRelative(0, 0, -(i+1));
		}
		
		int scheduledCount = 0;
		boolean[] results = batchCanBreak(player, candidateBlocks);
		for (int i = 0; i < results.length; i++) {
			if (results[i]) {
				scheduledBlocks[scheduledCount] = candidateBlocks[i];
				scheduledCount++;
			}
		}
		
		PlayerInventory inventory = player.getInventory();
		
		for (int i = 0; i < scheduledCount; i++) {
			
			scheduledBlocks[i].getDrops(inventory.getItemInMainHand()).iterator().forEachRemaining(new Consumer<ItemStack>(){
				@Override
				public void accept(ItemStack t) {
					inventory.addItem(t);
				}
			});
			
			scheduledBlocks[i].setType(Material.AIR);
		}
	}
	
	
	
	public boolean canBreak(Player player, Block block) {
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		RegionQuery query = container.createQuery();
		
		return query.testState(BukkitAdapter.adapt(block.getLocation()), localPlayer, Flags.BUILD);
	}
	
	public boolean[] batchCanBreak(Player player, Block[] blocks) {
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		RegionQuery query = container.createQuery();
		
		boolean[] results = new boolean[blocks.length];
		for (int i = 0; i < blocks.length; i++) {
			results[i] = query.testState(BukkitAdapter.adapt(blocks[i].getLocation()), localPlayer, Flags.BUILD);
		}
		return results;
	}
	 
}
