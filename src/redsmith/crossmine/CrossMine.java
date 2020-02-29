package redsmith.crossmine;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import com.vk2gpz.tokenenchant.api.EnchantHandler;
import com.vk2gpz.tokenenchant.api.InvalidTokenEnchantException;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;


public class CrossMine extends EnchantHandler {	
	
	public CrossMine(TokenEnchantAPI arg0) throws InvalidTokenEnchantException {
		super(arg0);
		super.loadConfig();
		api = arg0;
		
		try {
			checkConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		addConflict("Explosive");
	}
	

	ConsoleCommandSender console = Bukkit.getConsoleSender();
	TokenEnchantAPI api;

	
	File configFile = new File("plugins"+File.separator+"TokenEnchant"+File.separator+"config.yml");
	
	void checkConfig() throws Exception {
		
		FileConfiguration config = api.getConfig();
		
		if (!config.isConfigurationSection("Enchants.CrossMine")) {
			ConfigurationSection crossMineConfig = config.createSection("Enchants.CrossMine");
			crossMineConfig.createSection("event_map").set("BlockBreakEvent", "LOW");
			crossMineConfig.set("price", 10);
			crossMineConfig.set("max", 1);
			config.save(configFile);
			
			console.sendMessage("[CrossMine] - "+ChatColor.GREEN+"Added default values to TokeEnchant config.yml");
		}
		
	}
	
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		event.getPlayer().sendMessage("[CrossMine] - Mined "+ ChatColor.BOLD + ChatColor.RED + event.getBlock().getType().name());
		
	}
	 
}
