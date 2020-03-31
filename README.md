# CrossMine
Custom enchantment for TokenEnchant that mines blocks in a cross formation.

Permission to mine the blocks in the cross formation are checked against WorldGuard.
Drops from blocks broken by the plugin are directly added to the Player's inventory and are calculated by the server (not my code) and the currently held tool.

### Example demo

[![demo](https://img.youtube.com/vi/xy_UlGwgufM/0.jpg "Demo video on YouTube")](https://www.youtube.com/watch?v=xy_UlGwgufM)

### Configuration
+ Place the jar in the enchants folder and the default config file will be written to disk if one does not exist yet.
+ Stop the server and edit all configurations if you want to.
+ Cross size and activation chance are dictated by the expression you specify (javascript syntax)
+ On the next load TokenEnchant will see the config file for CrossMine.

### Download
Visit releases at: https://github.com/redsmith-dev/crossmine/releases
