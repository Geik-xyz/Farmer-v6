package xyz.geik.ciftci;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;

import net.md_5.bungee.api.ChatColor;
import xyz.geik.ciftci.DataSource.DatabaseQueries;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiType;
import xyz.geik.ciftci.Utils.NPC.npc.NPCEventHandler;

public class Main extends JavaPlugin {

	public static Main instance;

	public static ApiType API = ApiType.NULL;

	public static boolean autoSell = false;

	public static boolean autoCollector = false;

	public static boolean spawnerKiller = false;

	public static WrappedSignedProperty DEFAULT_SKIN;

	public static boolean isShutdowned = false;

	public static int multiplier = 1;

	/**
	 * NPC Protocol manager & show distance
	 */
	public static final int NPC_RADIUS = 15;
	public ProtocolManager manager;

	/**
	 * Load tasks
	 */
	@Override
	public void onLoad() {

		instance = this;

		manager = ProtocolLibrary.getProtocolManager();

	}

	/**
	 * Enable tasks
	 */
	public void onEnable() {

		onEnableShortcut.getWorkingPlugin();

		npcEnable();

		licenseCheck();

	}

	/**
	 * Disable method which save cache sync
	 */
	public void onDisable() {
		Bukkit.getConsoleSender()
				.sendMessage(Main.color("&6Çiftçi &cÇiftçi verileri kayıt ediliyor. Lütfen bekleyin..."));
		for (String s : FarmerManager.farmerCache.keySet())
			DatabaseQueries.leaveEvent(s, FarmerManager.farmerCache.get(s));

	}

	/**
	 * Color util convert text to colored string
	 * 
	 * @param text
	 * @return
	 */
	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	/**
	 * Old License Check modified to all true
	 * 
	 */
	private void licenseCheck() {

		Bukkit.getConsoleSender().sendMessage(Main.color("&6&l		CIFTCI 		&b"));

		Bukkit.getConsoleSender().sendMessage(Main.color("&aDeveloped by &2Geik"));

		Bukkit.getConsoleSender().sendMessage(Main.color("&aDiscord: &2discord.gg/h283guX"));

		Bukkit.getConsoleSender().sendMessage(Main.color("&aWeb: &2https://geik.xyz"));

		autoSell = true;
		Bukkit.getConsoleSender().sendMessage(Main.color("&6&lCIFTCI&e&lADDON &aOtoSatış modülü bulundu."));

		autoCollector = true;
		Bukkit.getConsoleSender().sendMessage(Main.color("&6&lCIFTCI&e&lADDON &aOtoToplama modülü bulundu."));

		spawnerKiller = true;
		Bukkit.getConsoleSender().sendMessage(Main.color("&6&lCIFTCI&e&lADDON &aSpawner Öldürücü modülü bulundu."));

		new onEnableShortcut();

	}

	/**
	 * NPC Listener ProtocolLib PacketWrapper
	 * 
	 */
	public void npcEnable() {

		manager.addPacketListener(new PacketAdapter(this, ListenerPriority.LOW, PacketType.Play.Client.USE_ENTITY) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				try {
					if (event.isCancelled()) {
						return;
					}

					PacketContainer packet = event.getPacket();
					int target = packet.getIntegers().read(0);
					if (!FarmerManager.NPCList.contains(target))
						return;

					boolean isVerBigger1_17 = Manager.isVerBigger1_17();

					Object action;
					if (isVerBigger1_17)
						action = packet.getEnumEntityUseActions().read(0);

					else
						action = packet.getEntityUseActions().read(0);

					if (isVerBigger1_17
							&& ((WrappedEnumEntityUseAction) action).getAction() == EntityUseAction.INTERACT)
						return;

					else if (!isVerBigger1_17 && action == EnumWrappers.EntityUseAction.INTERACT)
						return;

					// TODO Executed API & Not Executed API
					@SuppressWarnings("unused")
					boolean hasExecuted = FarmerManager.farmerCache.values().stream().anyMatch(storage -> {
						if (storage.getNPC().getEntityId() == target) {
							event.setCancelled(true);
							Bukkit.getScheduler().runTask(Main.this, () -> {
								storage.getNPC().eventHandlers.stream().forEach(handler -> {
									if (isVerBigger1_17) {
										handler.onInteract(storage.getNPC(), event.getPlayer(),
												((WrappedEnumEntityUseAction) action)
														.getAction() == EntityUseAction.ATTACK
																? NPCEventHandler.InteractType.LEFT_CLICK
																: NPCEventHandler.InteractType.RIGHT_CLICK);
									} else {
										handler.onInteract(storage.getNPC(), event.getPlayer(),
												action == EnumWrappers.EntityUseAction.ATTACK
														? NPCEventHandler.InteractType.LEFT_CLICK
														: NPCEventHandler.InteractType.RIGHT_CLICK);
									}
								});
							});
							return true;
						} else
							return false;
					});

				}

				catch (NullPointerException e1) {
					e1.printStackTrace();
				}

			}
		});

	}

}
