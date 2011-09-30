/*
 *  Backup - CraftBukkit server Backup plugin (continued)
 *  Copyright (C) 2011 Domenic Horner <https://github.com/gamerx/Backup>
 *  Copyright (C) 2011 Lycano <https://github.com/gamerx/Backup>
 *
 *  Backup - CraftBukkit server Backup plugin (original author)
 *  Copyright (C) 2011 Kilian Gaertner <https://github.com/Meldanor/Backup>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.tgnx.bukkit.backup.listeners;

import net.tgnx.bukkit.backup.BackupMain;
import net.tgnx.bukkit.backup.config.Settings;
import net.tgnx.bukkit.backup.config.Strings;
import net.tgnx.bukkit.backup.threading.PrepareBackupTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;


/**
 * For manual backups
 * @author Kilian Gaertner
 */
public class CommandListener extends PlayerListener implements CommandExecutor {

    private PrepareBackupTask backupTask = null;
    private Settings settings;
    private final Plugin plugin;
    private Strings strings;

    public CommandListener (PrepareBackupTask backupTask, Settings settings, Plugin plugin) {
        this.backupTask = backupTask;
        this.settings = settings;
        this.plugin = plugin;
        this.strings = new Strings(plugin);
    }

    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        if((sender instanceof Player)) { // In-Game Command

            // Get player object
            Player player = (Player) sender;

            // Verify Permissions
            if (BackupMain.Permissions != null) { //permissions loaded
                 if(!BackupMain.Permissions.has(player, "backup.backup"))
                    player.sendMessage(strings.getString("norights"));
                 return true;
            } else if (settings.getBooleanProperty("onlyops") && !player.isOp()) {
                 player.sendMessage(strings.getString("norights"));
                 return true;
            }
        }
        
        if (label.equalsIgnoreCase("backup")) {
            backupTask.setAsManualBackup();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, backupTask);
        }
        
       return true;
    }
    
}