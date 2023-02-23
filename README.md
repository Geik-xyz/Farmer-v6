# Farmer
 Collects products of a region or island and store them in inventory of himself.

## How it's work?
This plugin creates a virtual assistant who collects items for you in your region or island.
This assistant known as farmer and can be buy-able by ingame money. Owner can add users to it and give them permission to sell stuffs in it. Also owner can close collecting feature and can level up farmer in management gui.
In farmer gui you can see stock availability which shown by colors and percentages of stock.

As administrator, you can control the farmers as if you are the owner.
You can configure items, levels etc. by config.

In the same time this plugins force-update lang file and don't require reload command execute for update self. (config still needs reload because it has dangerous settings)
You can design your menus in lang file.

## Images
Adding soon

## Commands
+ **/farmer** - Open buy or farmer gui depends on do you have it or not
+ **/farmer manage** - Management gui (Perm: farmer.admin or owner of farmer)
+ **/farmer info** - Info of farmer which stand on (Perm: farmer.admin)
+ **/farmer reload** - Reloads plugin (Perm: farmer.admin)

## API

### How to use?
```java
    // Returns Main class of plugin
    FarmerAPI.getInstance();

    // Removes farmer
    FarmerAPI.removeFarmer(String regionId);

    // Changes farmer owner
    FarmerAPI.changeOwner(UUID oldOwner, UUID newOwner, String regionId);
    
    // Checks if location has farmer
    FarmerAPI.hasFarmer(Location location);
```

### Listeners

* FarmerBoughtEvent
* FarmerItemCollectEvent
* FarmerItemSellEvent
* FarmerRemoveEvent
* FarmerStorageFullEvent

## Used Libraries

* [spigot-api (1.19-R0.3-SNAPSHOT)](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/browse)
* [lombok (LATEST)](https://github.com/projectlombok/lombok)
* [BStats](https://bstats.org)
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
* [SimplixStorage](https://www.spigotmc.org/resources/simplixstorage-awesome-library-to-store-data-in-a-better-way.67286/)
* [InventoryGUI](https://github.com/Phoenix616/InventoryGui)
* [AuthLib](https://mvnrepository.com/artifact/com.mojang/authlib/1.5.25)

### Integration Libraries (Optional)
* [SuperiorSkyblock2](https://github.com/BG-Software-LLC/SuperiorSkyblock2)
* [GriefPrevention](https://www.spigotmc.org/resources/griefprevention.1884/)
* [BentoBox](https://www.spigotmc.org/resources/bentobox-bskyblock-acidisland-skygrid-caveblock-aoneblock-boxed.73261/)