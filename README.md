# CrossMine
Custom enchantment for TokenEnchant that mines blocks in a cross formation.

Permission to mine the blocks in the cross formation are checked against WorldGuard.
Drops from blocks broken by the plugin are directly added to the Player's inventory and are calculated by the server (not my code) and the currently held tool.

### Configuration
+ Place the jar in the enchants folder and the default config file will be written to disk if one does not exist yet.
+ Stop the server and edit all configurations if you want to. Each level will add a block to the length of the 3D cross.
+ Next time around TokenEnchant will see the config file for CrossMine.