package xyz.geik.ciftci.DataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import xyz.geik.ciftci.Main;
import xyz.geik.ciftci.Utils.FarmerManager;
import xyz.geik.ciftci.Utils.Manager;
import xyz.geik.ciftci.Utils.onEnableShortcut;
import xyz.geik.ciftci.Utils.API.ApiFun;
import xyz.geik.ciftci.Utils.Cache.Farmer;
import xyz.geik.ciftci.Utils.Cache.ConfigItems;
import xyz.geik.ciftci.Utils.Cache.StorageAndValues;
import xyz.geik.ciftci.Utils.NPC.npc.impl.NPCImpl;

public class DatabaseQueries {

	private static String TABLE_NAME = "Tablo";

	public static void createTable() {

		try (Connection connection = ConnectionPool.getConnection();
				Statement statement = connection.createStatement()) {

			statement.addBatch("CREATE TABLE IF NOT EXISTS `" + TABLE_NAME + "`\n" + "(\n"
					+ " `Owner`          varchar(30) NOT NULL UNIQUE,\n" + " `farmerLvl`          int DEFAULT 1,\n"
					+ " `farmerID`          int DEFAULT 0\n," + " `sellingStatus`          int DEFAULT 1,\n"
					+ " `autoSell`          int DEFAULT 0,\n" + " `autoCollect`          int DEFAULT 0,\n"
					+ " `spawnerKill`          int DEFAULT 0,\n" + " `farmerSkin`          varchar(30) DEFAULT NULL,\n"
					+ " `farmerLocation`          varchar(64) DEFAULT null\n" + ");");

			statement.executeBatch();

		}

		catch (SQLException e) {
		}

	}

	@SuppressWarnings("deprecation")
	public static Farmer getFarmerInformations(String uuid) {

		String SQL_QUERY = "SELECT Owner, farmerLvl, farmerID, sellingStatus, farmerLocation, autoSell, autoCollect, spawnerKill FROM Tablo WHERE Owner = ?";

		int farmerLevel = 0;

		int farmerID = 96456;

		int sellingStatus = 0;

		Location placedLocation = null;

		boolean autoSell = false;

		boolean autoCollect = false;

		boolean spawnerKill = false;

		String owner = uuid;
		if (onEnableShortcut.USE_OWNER)
			owner = ApiFun.getOwnerViaID(uuid).toString();
		// TODO
		List<OfflinePlayer> playerList = new ArrayList<OfflinePlayer>();
		playerList.add(Bukkit.getOfflinePlayer(UUID.fromString(owner)));

		for (String members : Manager.getLore("data", "data." + uuid))
			playerList.add(Bukkit.getOfflinePlayer(members));

		try (Connection con = ConnectionPool.getConnection()) {

			PreparedStatement pst = con.prepareStatement(SQL_QUERY);

			pst.setString(1, String.valueOf(uuid));

			ResultSet resultSet = pst.executeQuery();

			if (resultSet.next()) {

				farmerLevel = resultSet.getInt("farmerLvl");

				farmerID = resultSet.getInt("farmerID");

				sellingStatus = resultSet.getInt("sellingStatus");

				placedLocation = Manager.getLocationFromString(resultSet.getString("farmerLocation"));

				if (resultSet.getInt("autoSell") == 1)
					autoSell = true;

				if (resultSet.getInt("autoCollect") == 1 || onEnableShortcut.autoCollectWithoutFarmer)
					autoCollect = true;

				if (resultSet.getInt("spawnerKill") == 1 || onEnableShortcut.spawnerKillerWithoutFarmer)
					spawnerKill = true;

			}

			resultSet.close();

			pst.close();

			Farmer storage = new Farmer(uuid, farmerLevel, farmerID, sellingStatus, placedLocation, playerList,
					Manager.getText("lang", "FarmerSkin"), autoSell, autoCollect, spawnerKill);

			return storage;

		} catch (SQLException e1) {
			return null;
		}

	}

	@SuppressWarnings("deprecation")
	public static void insertAllFarmers() {

		String SQL_QUERY = "SELECT * FROM Tablo";

		String farmerSkin = Manager.getText("lang", "FarmerSkin");

		try (Connection con = ConnectionPool.getConnection()) {

			PreparedStatement pst = con.prepareStatement(SQL_QUERY);

			ResultSet resultSet = pst.executeQuery();

			while (resultSet.next()) {

				try {

					int farmerLevel = 0;

					int farmerID = 96456;

					int sellingStatus = 0;

					Location placedLocation = null;

					boolean autoSell = false;

					boolean autoCollect = false;

					boolean spawnerKill = false;

					String uuid = resultSet.getString("Owner");

					String owner = uuid;

					if (onEnableShortcut.USE_OWNER)
						owner = ApiFun.getOwnerViaID(uuid).getUniqueId().toString();

					List<OfflinePlayer> playerList = new ArrayList<OfflinePlayer>();
					playerList.add(Bukkit.getOfflinePlayer(UUID.fromString(owner)));

					for (String members : Manager.getLore("data", "data." + uuid))
						playerList.add(Bukkit.getOfflinePlayer(members));

					HashMap<ConfigItems, Integer> itemValues = new HashMap<ConfigItems, Integer>();

					farmerLevel = resultSet.getInt("farmerLvl");

					farmerID = resultSet.getInt("farmerID");

					if (farmerID == 96456)
						continue;

					sellingStatus = resultSet.getInt("sellingStatus");

					if (resultSet.getString("farmerSkin") != null)
						farmerSkin = resultSet.getString("farmerSkin");

					placedLocation = Manager.getLocationFromString(resultSet.getString("farmerLocation"));

					if (resultSet.getInt("autoSell") == 1) {

						if (onEnableShortcut.getPermissions().playerHas(placedLocation.getWorld().getName(),
								Bukkit.getOfflinePlayer(UUID.fromString(owner)), "ciftci.autosell"))
							autoSell = true;
					}

					if (resultSet.getInt("autoCollect") == 1 || onEnableShortcut.autoCollectWithoutFarmer) {
						
						if (onEnableShortcut.getPermissions().playerHas(placedLocation.getWorld().getName(),
								Bukkit.getOfflinePlayer(UUID.fromString(owner)), "ciftci.autocollect"))
							autoCollect = true;
						
					}

					if (resultSet.getInt("spawnerKill") == 1 || onEnableShortcut.spawnerKillerWithoutFarmer)
					{
						
						if (onEnableShortcut.getPermissions().playerHas(placedLocation.getWorld().getName(),
								Bukkit.getOfflinePlayer(UUID.fromString(owner)), "ciftci.spawnerkill"))
							spawnerKill = true;
						
					}

					for (ConfigItems items : FarmerManager.STORED_ITEMS.values()) {

						String product = items.getItemMaterial().toLowerCase();

						if (items.getDamage() != 0)
							product = items.getItemMaterial().toLowerCase() + "_" + items.getDamage();

						int amount = resultSet.getInt(product);

						itemValues.put(items, amount);

					}

					Farmer storage = new Farmer(uuid, farmerLevel, farmerID, sellingStatus, placedLocation, playerList,
							farmerSkin, autoSell, autoCollect, spawnerKill);

					StorageAndValues values = new StorageAndValues(storage, itemValues, null);

					if (storage.getFarmerID() != 96456) {

						if (storage.getNpcLocation() != null) {

							NPCImpl npc = FarmerManager.createFarmer(values);

							values.setNPC(npc);

							values.getStorage().setFarmerID(npc.getEntityId());

						}

						else {

							values.setNPC(null);

							values.getStorage().setFarmerID(new Random().nextInt());

						}

						FarmerManager.insertCookie(uuid, values);
						
						if (autoCollect
								&& !FarmerManager.farmerIdMap.contains(values.getStorage().getOwnerUUID()))
							FarmerManager.farmerIdMap.add(values.getStorage().getOwnerUUID());

					}

				}

				catch (NullPointerException e1) {
					e1.printStackTrace();
					continue;
				}

			}

			resultSet.close();

			pst.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}

	}
	
	public static void autoCollectHandler() {

		String SQL_QUERY = "SELECT autoCollect, Owner, farmerID FROM Tablo";

		try (Connection con = ConnectionPool.getConnection()) {

			PreparedStatement pst = con.prepareStatement(SQL_QUERY);

			ResultSet resultSet = pst.executeQuery();

			while (resultSet.next()) {

				try {

					int farmerID = 96456;

					boolean autoCollect = false;
					
					String uuid = resultSet.getString("Owner");
					
					farmerID = resultSet.getInt("farmerID");

					if (farmerID == 96456)
						continue;
					
					if (resultSet.getInt("autoCollect") == 1 || onEnableShortcut.autoCollectWithoutFarmer) 
							autoCollect = true;

					if (farmerID != 96456) {
						
						if (autoCollect
								&& !FarmerManager.farmerIdMap.contains(uuid))
							FarmerManager.farmerIdMap.add(uuid);

					}

				}

				catch (NullPointerException e1) {
					e1.printStackTrace();
					continue;
				}

			}

			resultSet.close();

			pst.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}

	}

	public static void updateDataBaseAlter(String value, String type) {

		try (Connection connection = ConnectionPool.getConnection();
				Statement statement = connection.createStatement()) {

			DatabaseMetaData md = connection.getMetaData();

			ResultSet rs = md.getColumns(null, null, TABLE_NAME, value);

			if (rs.next())
				return;

			else {

				String SQL_QUERY = "ALTER TABLE Tablo ADD '" + value + "' " + type + ";";

				statement.addBatch(SQL_QUERY);

				statement.executeBatch();

			}

		} catch (SQLException e) {
		}

	}

	public static void registerProduct(String product) {

		try (Connection connection = ConnectionPool.getConnection();
				Statement statement = connection.createStatement()) {

			DatabaseMetaData md = connection.getMetaData();

			ResultSet rs = md.getColumns(null, null, TABLE_NAME, product);

			if (rs.next())
				return;

			else {

				String SQL_QUERY = "ALTER TABLE Tablo ADD '" + product + "' int DEFAULT 0;";

				statement.addBatch(SQL_QUERY);

				statement.executeBatch();

			}

		} catch (SQLException e) {
		}

	}

	public static boolean areaHasFarmer(String uuid) {

		String SQL_QUERY = "SELECT farmerID FROM Tablo WHERE Owner = ?";

		if (uuid == null)
			return false;

		try (Connection con = ConnectionPool.getConnection()) {

			PreparedStatement pst = con.prepareStatement(SQL_QUERY);

			pst.setString(1, uuid);

			ResultSet resultSet = pst.executeQuery();

			if (resultSet.next()) {

				resultSet.close();

				pst.close();

				return true;
			}

			else {

				resultSet.close();

				pst.close();

				return false;

			}

		} catch (SQLException e1) {
			return false;
		}

	}

	public static void updateFarmerLocation(String owner, Location location) {

		String SQL_QUERY = "UPDATE Tablo SET farmerLocation = ? WHERE Owner = ?";
		try (Connection con = ConnectionPool.getConnection()) {
			PreparedStatement pst = con.prepareStatement(SQL_QUERY);

			pst.setString(1, Manager.getStringFromLocation(location));

			pst.setString(2, owner);

			pst.executeUpdate();
			pst.close();
		}

		catch (SQLException e) {
		}

	}

	public static void updateFarmerSkin(String owner, String skin) {

		String SQL_QUERY = "UPDATE Tablo SET farmerSkin = ? WHERE Owner = ?";
		try (Connection con = ConnectionPool.getConnection()) {
			PreparedStatement pst = con.prepareStatement(SQL_QUERY);

			pst.setString(1, skin);

			pst.setString(2, owner);

			pst.executeUpdate();
			pst.close();
		}

		catch (SQLException e) {
		}

	}

	public static void toggleAutoSell(String owner, boolean autoSell) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {

			String SQL_QUERY = "UPDATE Tablo SET autoSell = ? WHERE Owner = ?";
			try (Connection con = ConnectionPool.getConnection()) {
				PreparedStatement pst = con.prepareStatement(SQL_QUERY);

				int status = 0;

				if (autoSell)
					status = 1;

				pst.setInt(1, status);

				pst.setString(2, owner);

				pst.executeUpdate();
				pst.close();
			}

			catch (SQLException e) {
			}

		});

	}

	public static void toggleAutoCollect(String owner, boolean autoCollect) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {

			String SQL_QUERY = "UPDATE Tablo SET autoCollect = ? WHERE Owner = ?";
			try (Connection con = ConnectionPool.getConnection()) {
				PreparedStatement pst = con.prepareStatement(SQL_QUERY);

				int status = 0;

				if (autoCollect)
					status = 1;

				pst.setInt(1, status);

				pst.setString(2, owner);

				pst.executeUpdate();
				pst.close();
			}

			catch (SQLException e) {
			}

		});

	}

	public static void toggleSpawnerKiller(String owner, boolean spawnerKill) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {

			String SQL_QUERY = "UPDATE Tablo SET spawnerKill = ? WHERE Owner = ?";
			try (Connection con = ConnectionPool.getConnection()) {
				PreparedStatement pst = con.prepareStatement(SQL_QUERY);

				int status = 0;

				if (spawnerKill)
					status = 1;

				pst.setInt(1, status);

				pst.setString(2, owner);

				pst.executeUpdate();
				pst.close();
			}

			catch (SQLException e) {
			}

		});

	}

	public static StorageAndValues registerIslandDatabase(String uuid, Location loc, int farmerLevel) {

		String SQL_QUERY = "INSERT INTO Tablo (Owner, farmerLvl, farmerID, sellingStatus, farmerLocation) VALUES (?, ?, ?, ?, ?)";

		int level = 1;

		if (farmerLevel != 0)
			level = farmerLevel;

		String owner = uuid;

		if (onEnableShortcut.USE_OWNER)
			owner = ApiFun.getOwnerViaID(uuid).getUniqueId().toString();

		try (Connection con = ConnectionPool.getConnection()) {

			int sellingStatus = 1;

			if (!Main.instance.getConfig().getBoolean("Settings.depositMethod.defaultDepositLeader"))
				sellingStatus = 0;

			PreparedStatement pst = con.prepareStatement(SQL_QUERY);

			pst.setString(1, uuid);

			pst.setInt(2, level);

			pst.setInt(3, 0);

			pst.setInt(4, sellingStatus);

			pst.setString(5, Manager.getStringFromLocation(loc));

			pst.executeUpdate();

			pst.close();

			Farmer storage = new Farmer(uuid, level, 0, sellingStatus, loc,
					ApiFun.getLandPlayers(Bukkit.getOfflinePlayer(UUID.fromString(owner)), uuid),
					Manager.getText("lang", "FarmerSkin"), false, false, false);

			HashMap<ConfigItems, Integer> itemValues = new HashMap<ConfigItems, Integer>();

			for (ConfigItems items : FarmerManager.STORED_ITEMS.values())
				itemValues.put(items, 0);

			return new StorageAndValues(storage, itemValues, null);

		} catch (SQLException e) {
			return null;
		}

	}

	@SuppressWarnings("deprecation")
	public static StorageAndValues getAllValuesOfPlayer(String uuid) {

		String SQL_QUERY = "SELECT * FROM Tablo WHERE Owner = ?";
		int farmerLevel = 0;
		int farmerID = 96456;
		int sellingStatus = 0;
		Location placedLocation = null;
		boolean autoSell = false;
		boolean autoCollect = false;
		boolean spawnerKill = false;
		String farmerSkin = Manager.getText("lang", "FarmerSkin");

		String owner = uuid;
		if (onEnableShortcut.USE_OWNER)
			owner = ApiFun.getOwnerViaID(uuid).getUniqueId().toString();

		// MEMBERS
		List<OfflinePlayer> playerList = new ArrayList<OfflinePlayer>();
		playerList.add(Bukkit.getOfflinePlayer(UUID.fromString(owner)));
		for (String members : Manager.getLore("data", "data." + uuid))
			playerList.add(Bukkit.getOfflinePlayer(members));
		//

		HashMap<ConfigItems, Integer> itemValues = new HashMap<ConfigItems, Integer>();

		try (Connection con = ConnectionPool.getConnection()) {

			PreparedStatement pst = con.prepareStatement(SQL_QUERY);
			pst.setString(1, String.valueOf(uuid));
			ResultSet resultSet = pst.executeQuery();

			if (resultSet.next()) {

				farmerLevel = resultSet.getInt("farmerLvl");
				farmerID = resultSet.getInt("farmerID");
				sellingStatus = resultSet.getInt("sellingStatus");
				if (resultSet.getString("farmerSkin") != null)
					farmerSkin = resultSet.getString("farmerSkin");
				placedLocation = Manager.getLocationFromString(resultSet.getString("farmerLocation"));
				
				if (resultSet.getInt("autoSell") == 1) {

					if (onEnableShortcut.getPermissions().playerHas(placedLocation.getWorld().getName(),
							Bukkit.getOfflinePlayer(UUID.fromString(owner)), "ciftci.autosell"))
						autoSell = true;
				}

				if (resultSet.getInt("autoCollect") == 1 || onEnableShortcut.autoCollectWithoutFarmer) {
					
					if (onEnableShortcut.getPermissions().playerHas(placedLocation.getWorld().getName(),
							Bukkit.getOfflinePlayer(UUID.fromString(owner)), "ciftci.autocollect"))
						autoCollect = true;
					
				}

				if (resultSet.getInt("spawnerKill") == 1 || onEnableShortcut.spawnerKillerWithoutFarmer)
				{
					
					if (onEnableShortcut.getPermissions().playerHas(placedLocation.getWorld().getName(),
							Bukkit.getOfflinePlayer(UUID.fromString(owner)), "ciftci.spawnerkill"))
						spawnerKill = true;
					
				}

				for (ConfigItems items : FarmerManager.STORED_ITEMS.values()) {

					String product = items.getItemMaterial().toLowerCase();
					if (items.getDamage() != 0)
						product = items.getItemMaterial().toLowerCase() + "_" + items.getDamage();
					int amount = resultSet.getInt(product);
					itemValues.put(items, amount);

				}

			}

			resultSet.close();

			pst.close();

			Farmer storage = new Farmer(uuid, farmerLevel, farmerID, sellingStatus, placedLocation, playerList,
					farmerSkin, autoSell, autoCollect, spawnerKill);

			StorageAndValues toStore = new StorageAndValues(storage, itemValues, null);

			return toStore;

		} catch (SQLException e1) {
			return null;
		}

	}

	public static void leaveEvent(String s2, StorageAndValues values) {

		try {

			try {
				if (FarmerManager.farmerCache.get(s2) != null && FarmerManager.farmerCache.get(s2).getNPC() != null
						& FarmerManager.farmerCache.get(s2).getNPC().isSpawned())
					FarmerManager.farmerCache.get(s2).getNPC().setSpawned(false);
			}

			catch (NullPointerException e1) {
			}

			String SQL_QUERY = "UPDATE Tablo SET farmerLvl = ?, farmerID = ?, sellingStatus = ?";

			List<ConfigItems> configItems = new ArrayList<ConfigItems>(FarmerManager.STORED_ITEMS.values());

			for (ConfigItems s : configItems) {

				String product = s.getItemMaterial().toLowerCase();

				if (s.getDamage() != 0)
					product = s.getItemMaterial().toLowerCase() + "_" + s.getDamage();

				else
					product = s.getItemMaterial().toLowerCase();

				SQL_QUERY = SQL_QUERY + ", " + product + " = ?";

			}

			SQL_QUERY = SQL_QUERY + " WHERE Owner = ?";

			try (Connection con = ConnectionPool.getConnection()) {

				PreparedStatement pst = con.prepareStatement(SQL_QUERY);

				pst.setInt(1, values.getStorage().getFarmerLevel());

				pst.setInt(2, values.getStorage().getFarmerID());

				pst.setInt(3, isLeaderChoose(values.getStorage().getSellingStatus()));

				for (int i = 4; i <= configItems.size() + 3; i++)
					pst.setInt(i, values.getItemValues().get(configItems.get(i - 4)));

				pst.setString(configItems.size() + 4, s2.toString());

				pst.executeUpdate();

				pst.close();

			}

			catch (SQLException | NullPointerException e) {
			}

		}

		catch (NullPointerException e1) {
			e1.printStackTrace();
		}

	}

	private static int isLeaderChoose(int paymentMethod) {

		if (Main.instance.getConfig().getBoolean("Settings.depositMethod.leaderChoose"))
			return paymentMethod;

		else {

			if (Main.instance.getConfig().getBoolean("Settings.depositMethod.defaultDepositLeader"))
				return 1;

			else
				return 0;

		}

	}

	public static void removeFarmer(String owner) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, new Runnable() {

			public void run() {

				String SQL_QUERY = "DELETE FROM Tablo WHERE Owner = ?";

				try (Connection con = ConnectionPool.getConnection()) {

					PreparedStatement pst = con.prepareStatement(SQL_QUERY);

					pst.setString(1, owner);

					pst.executeUpdate();

					pst.close();

				}

				catch (SQLException e) {
				}

			}

		});

	}

	public static void updateOwner(String oldOwner, String newOwner) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {

			String SQL_QUERY = "UPDATE Tablo SET Owner = ? WHERE Owner = ?";

			try (Connection con = ConnectionPool.getConnection()) {

				PreparedStatement pst = con.prepareStatement(SQL_QUERY);

				pst.setString(1, newOwner);

				pst.setString(2, oldOwner);

				pst.executeUpdate();

				pst.close();

			}

			catch (SQLException e) {
			}

		});

	}

	public static void autoSaver(String s2, StorageAndValues values) {

		try {

			String SQL_QUERY = "UPDATE Tablo SET farmerLvl = ?, farmerID = ?, sellingStatus = ?";

			List<ConfigItems> configItems = new ArrayList<ConfigItems>(FarmerManager.STORED_ITEMS.values());

			for (ConfigItems s : configItems) {

				String product = s.getItemMaterial().toLowerCase();

				if (s.getDamage() != 0)
					product = s.getItemMaterial().toLowerCase() + "_" + s.getDamage();

				else
					product = s.getItemMaterial().toLowerCase();

				SQL_QUERY = SQL_QUERY + ", " + product + " = ?";

			}

			SQL_QUERY = SQL_QUERY + " WHERE Owner = ?";

			try (Connection con = ConnectionPool.getConnection()) {

				PreparedStatement pst = con.prepareStatement(SQL_QUERY);

				pst.setInt(1, values.getStorage().getFarmerLevel());

				pst.setInt(2, values.getStorage().getFarmerID());

				pst.setInt(3, isLeaderChoose(values.getStorage().getSellingStatus()));

				for (int i = 4; i <= configItems.size() + 3; i++)
					pst.setInt(i, values.getItemValues().get(configItems.get(i - 4)));

				pst.setString(configItems.size() + 4, s2);

				pst.executeUpdate();

				pst.close();

			}

			catch (SQLException e) {
			}

		}

		catch (NullPointerException e1) {
			return;
		}

	}

}
