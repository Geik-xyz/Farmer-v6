package xyz.geik.farmer.database;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.geik.farmer.Main;
import xyz.geik.farmer.api.handlers.FarmerBoughtEvent;
import xyz.geik.farmer.api.handlers.FarmerRemoveEvent;
import xyz.geik.farmer.api.managers.FarmerManager;
import xyz.geik.farmer.model.Farmer;
import xyz.geik.farmer.model.FarmerLevel;
import xyz.geik.farmer.model.inventory.FarmerInv;
import xyz.geik.farmer.model.inventory.FarmerItem;
import xyz.geik.farmer.model.user.FarmerPerm;
import xyz.geik.farmer.model.user.User;
import xyz.geik.farmer.modules.FarmerModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * SQL Queries and abstracted methods exist in this class
 *
 * @author Amowny
 * @since b000
 */
public abstract class SQL {

	/**
	 * Constructor of class
	 */
	public SQL() {
	}

	/**
	 * Creates table on db
	 */
	public abstract void createTable();

	/**
	 * Updates all farmers which saves all data of farmers on cache
	 */
	public void updateAllFarmers() {
		Connection connection = null;
		try {
			connection = Main.getDatabase().getConnection();
			for (Farmer farmer : FarmerManager.getFarmers().values()) {
				farmer.saveFarmer();
				FarmerModule.databaseUpdateAttribute(connection, farmer);
			}
		} catch (SQLException throwable) {
			Main.getInstance().getLogger().info("Error while updating Farmers: " + throwable.getMessage());
		} finally {
			closeConnections(null, connection, null);
		}
	}

	/**
	 * Does same thing with #updateAllFarmers async
	 */
	public void updateAllFarmersAsync() {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), this::updateAllFarmers);
	}

	/**
	 * Loads all farmer data from sql to cache
	 */
	public void loadAllFarmers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Query of farmer
		final String FARMER_QUERY = "SELECT * FROM Farmers;";
		// Query of farmer users
		final String USERS_QUERY = "SELECT * FROM FarmerUsers WHERE farmerId = ?";
		try {
			connection = Main.getDatabase().getConnection();
			preparedStatement = connection.prepareStatement(FARMER_QUERY);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int farmerID = resultSet.getInt("id");
				String regionID = resultSet.getString("regionID");
				int state = resultSet.getInt("state");
				int levelID = resultSet.getInt("level");

				if (levelID < 0)
					levelID = 0;
				FarmerLevel level = (FarmerLevel.getAllLevels().size() - 1 >= levelID)
						? FarmerLevel.getAllLevels().get(levelID)
						: FarmerLevel.getAllLevels().get(FarmerLevel.getAllLevels().size() - 1);

				List<FarmerItem> items = FarmerItem.deserializeItems(resultSet.getString("items"));

				FarmerInv inv = new FarmerInv(items, level.getCapacity());

				PreparedStatement userStatement = connection.prepareStatement(USERS_QUERY);
				userStatement.setInt(1, farmerID);
				ResultSet userSet = userStatement.executeQuery();
				Set<User> users = new LinkedHashSet<>();

				while (userSet.next()) {
					String name = userSet.getString("name");
					String uuid = userSet.getString("uuid");

					FarmerPerm role = FarmerPerm.getRole(userSet.getInt("role"));
					users.add(new User(farmerID, name, UUID.fromString(uuid), role));
				}
				Farmer farmer = new Farmer(farmerID, regionID, users, inv, level, state);
				FarmerModule.databaseGetAttributes(connection, farmer);
				FarmerManager.getFarmers().put(regionID, farmer);
			}
		} catch (SQLException throwables) {
			Main.getInstance().getLogger().info("Error while loading Farmers: " + throwables.getMessage());
		} finally {
			closeConnections(preparedStatement, connection, resultSet);
		}
	}

	/**
	 * Creates farmer on sql
	 *
	 * @param farmer temp farmer
	 */
	public void createFarmer(@NotNull Farmer farmer) {
		Connection connection = null;
		PreparedStatement saveStatement = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		final String SQL_QUERY = "INSERT INTO Farmers (regionID, state, level) VALUES (?, ?, ?)";
		try {
			connection = Main.getDatabase().getConnection();
			saveStatement = connection.prepareStatement(SQL_QUERY);
			saveStatement.setString(1, farmer.getRegionID());
			saveStatement.setInt(2, farmer.getState());
			saveStatement.setInt(3, FarmerLevel.getAllLevels().indexOf(farmer.getLevel()));
			saveStatement.executeUpdate();

			selectStatement = connection.prepareStatement("SELECT id FROM Farmers WHERE regionID = ?");
			selectStatement.setString(1, farmer.getRegionID());
			resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
					farmer.setId(id);
					FarmerBoughtEvent boughtEvent = new FarmerBoughtEvent(farmer);
					Bukkit.getPluginManager().callEvent(boughtEvent);
				});
			}
		} catch (SQLException throwables) {
			Main.getInstance().getLogger().info("Error while creating Farmer: " + throwables.getMessage());
			throwables.printStackTrace();
		} finally {
			closeConnections(saveStatement, connection, resultSet);
			closeConnections(selectStatement, connection, resultSet);
		}
	}

	/**
	 * Removes farmer from sql
	 *
	 * @param farmer object of farmer
	 */
	public void removeFarmer(@NotNull Farmer farmer) {
		Connection connection = null;
		PreparedStatement removeFarmerStatement = null;
		PreparedStatement removeUsersStatement = null;
		String DELETE_FARMER = "DELETE FROM Farmers WHERE id = ?";
		String DELETE_USERS = "DELETE FROM FarmerUsers WHERE farmerId = ?";
		try {
			connection = Main.getDatabase().getConnection();
			removeFarmerStatement = connection.prepareStatement(DELETE_FARMER);
			removeFarmerStatement.setInt(1, farmer.getId());
			removeFarmerStatement.executeUpdate();

			removeUsersStatement = connection.prepareStatement(DELETE_USERS);
			removeUsersStatement.setInt(1, farmer.getId());
			removeUsersStatement.executeUpdate();

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
				FarmerRemoveEvent removeEvent = new FarmerRemoveEvent(farmer);
				Bukkit.getPluginManager().callEvent(removeEvent);
			});

			if (FarmerManager.getFarmers().containsKey(farmer.getRegionID()))
				FarmerManager.getFarmers().remove(farmer.getRegionID());

		} catch (SQLException throwables) {
			Main.getInstance().getLogger().info("Error while removing Farmer: " + throwables.getMessage());
		} finally {
			closeConnections(removeFarmerStatement, connection, null);
			closeConnections(removeUsersStatement, connection, null);
		}
	}

	/**
	 * Saves farmer sync
	 *
	 * @param farmer farmer object
	 */
	public void saveFarmer(@NotNull Farmer farmer) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		final String SQL_QUERY = "UPDATE Farmers SET regionID = ?, state = ?, items = ?, level = ? WHERE id = ?";
		try {
			connection = Main.getDatabase().getConnection();
			preparedStatement = connection.prepareStatement(SQL_QUERY);
			preparedStatement.setString(1, farmer.getRegionID());
			preparedStatement.setInt(2, farmer.getState());
			String serializedItems = FarmerItem.serializeItems(farmer.getInv().getItems());
			preparedStatement.setString(3, (serializedItems == "") ? null : serializedItems);
			preparedStatement.setInt(4, FarmerLevel.getAllLevels().indexOf(farmer.getLevel()));
			preparedStatement.setInt(5, farmer.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException throwable) {
			Main.getInstance().getLogger().info("Error while save Farmer: " + throwable.getMessage());
		} finally {
			closeConnections(preparedStatement, connection, null);
		}
	}

	/**
	 * Adds user to farmer in sql
	 *
	 * @param uuid   uuid of player
	 * @param name   name of player
	 * @param perm   perm of player
	 * @param farmer farmer of region
	 */
	public void addUser(UUID uuid, String name, FarmerPerm perm, @NotNull Farmer farmer) {
		farmer.getUsers().add(new User(farmer.getId(), name, uuid, perm));
		addUser(uuid, name, perm, farmer.getId());
	}

	/**
	 * Adds user to farmer in sql only
	 *
	 * @param uuid     of player
	 * @param name     of player
	 * @param perm     of player perm FarmerPerm#perm
	 * @param farmerId id of farmer
	 */
	public void addUser(@NotNull UUID uuid, String name, FarmerPerm perm, int farmerId) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			final String SQL_QUERY = "INSERT INTO FarmerUsers (farmerId, name, uuid, role) VALUES (?, ?, ?, ?)";
			try {
				connection = Main.getDatabase().getConnection();
				preparedStatement = connection.prepareStatement(SQL_QUERY);
				preparedStatement.setInt(1, farmerId);
				preparedStatement.setString(2, name);
				preparedStatement.setString(3, uuid.toString());
				preparedStatement.setInt(4, FarmerPerm.getRoleId(perm));
				preparedStatement.executeUpdate();
			} catch (SQLException throwables) {
				Main.getInstance().getLogger().info("Error while adding User: " + throwables.getMessage());
			} finally {
				closeConnections(preparedStatement, connection, null);
			}
		});
	}

	/**
	 * Removes user from farmer in sql
	 *
	 * @param user   user object
	 * @param farmer of region
	 * @return status of task
	 */
	public boolean removeUser(@NotNull User user, Farmer farmer) {
		if (user.getPerm().equals(FarmerPerm.OWNER))
			return false;
		farmer.getUsers().remove(user);
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			final String QUERY = "DELETE FROM FarmerUsers WHERE uuid = ? AND farmerId = ?";
			try {
				connection = Main.getDatabase().getConnection();
				preparedStatement = connection.prepareStatement(QUERY);
				preparedStatement.setString(1, user.getUuid().toString());
				preparedStatement.setInt(2, farmer.getId());
				preparedStatement.executeUpdate();
			} catch (SQLException throwables) {
				Main.getInstance().getLogger().info("Error while remove User: " + throwables.getMessage());
			} finally {
				closeConnections(preparedStatement, connection, null);
			}
		});
		return true;
	}

	/**
	 * Updates role of player
	 *
	 * @param uuid     uuid of player
	 * @param roleId   roleId of player (FarmerPerm#getRole)
	 * @param farmerId id of farmer
	 */
	public void updateRole(UUID uuid, int roleId, int farmerId) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			final String QUERY = "UPDATE FarmerUsers SET role = ? WHERE uuid = ? AND farmerId = ?";
			try {
				connection = Main.getDatabase().getConnection();
				preparedStatement = connection.prepareStatement(QUERY);
				preparedStatement.setInt(1, roleId);
				preparedStatement.setString(2, uuid.toString());
				preparedStatement.setInt(3, farmerId);
				preparedStatement.executeUpdate();
			} catch (SQLException throwables) {
				Main.getInstance().getLogger().info("Error while remove User: " + throwables.getMessage());
			} finally {
				closeConnections(preparedStatement, connection, null);
			}
		});
	}

	/**
	 * Database type of sql
	 *
	 * @return DatabaseType object
	 */
	public abstract DatabaseType getDatabaseType();

	/**
	 * Closes connection of sql
	 *
	 * @param preparedStatement statement
	 * @param connection        connection
	 * @param resultSet         resultSet
	 */
	protected void closeConnections(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
		if (connection == null)
			return;
		try {
			if (connection.isClosed())
				return;
			if (resultSet != null)
				resultSet.close();
			if (preparedStatement != null)
				preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fix database method
	 * <p>Fixes users and owners if there is any corruption occurred in older versions or blackouts.</p>
	 */
	public void fixDatabase() {
		updateAllFarmers();
		Main.getInstance().getLogger().info("Preparing data for fix please wait...");
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
			long ms = System.currentTimeMillis();
			FarmerManager.getFarmers().clear();
			fixUsersInDatabase(ms);
		}, 200L);
	}

	/**
	 * Check users if something exceptional in db
	 */
	private void fixUsersInDatabase(long ms) {
		Main.getInstance().getLogger().info("Farmer fixing users in progress..");
		final String QUERY = "DELETE FROM FarmerUsers\n" +
				"WHERE (farmerId, uuid, role) NOT IN (\n" +
				"    SELECT farmerId, uuid, MAX(role) AS max_role\n" +
				"    FROM FarmerUsers\n" +
				"    GROUP BY farmerId, uuid\n" +
				");";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = Main.getDatabase().getConnection();
			statement = connection.prepareStatement(QUERY);
			statement.executeUpdate();
		} catch (SQLException throwables) {
			Main.getInstance().getLogger().info("Error while trying to fix database: " + throwables.getMessage());
		} finally {
			closeConnections(statement, connection, null);
			Main.getInstance().getLogger().info("Farmer fixing users completed.");
			// Next step
			fixOwnersInDatabase(ms);
		}
	}

	/**
	 * Checks farmers if there is no owner on farmer in db
	 */
	private void fixOwnersInDatabase(long ms) {
		Main.getInstance().getLogger().info("Farmer fixing owners in progress..");
		final String QUERY = "SELECT * FROM Farmers WHERE id NOT IN (\n" +
				"  SELECT farmerId FROM FarmerUsers WHERE role = 2\n" +
				");";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet;
		try {
			connection = Main.getDatabase().getConnection();
			statement = connection.prepareStatement(QUERY);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int farmerID = resultSet.getInt("id");
				String regionID = resultSet.getString("regionID");
				OfflinePlayer owner = Bukkit.getOfflinePlayer(Main.getIntegration().getOwnerUUID(regionID));
				this.addUser(owner.getUniqueId(), owner.getName(), FarmerPerm.OWNER, farmerID);
				Main.getInstance().getLogger().info("Fixed owner in database " + owner.getName());
			}
		} catch (SQLException throwables) {
			Main.getInstance().getLogger().info("Error while trying to fix database: " + throwables.getMessage());
		} finally {
			closeConnections(statement, connection, null);
			Main.getInstance().getLogger().info("Farmer fixing owners completed.");
			Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
				loadAllFarmers();
				Main.getInstance().getLogger().info("Fixing database task has completed in " + (System.currentTimeMillis() - ms) + "ms");
			}, 200L);
		}
	}
}