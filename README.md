### [Javadoc](https://poyrazinan.github.io/FarmerAPI/)
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

## Configuration and Lang File
<details>
  <summary>config.yml</summary>

    # Main settings of farmer
    settings:
    # if you want to give farmer with economy leave it true
    buyFarmer: true
    # price of farmer necessary if buyFarmer is true
    farmerPrice: 1000
    # crates farmer automatically (If plugin supports)
    # also bypass money requirement
    autoCreateFarmer: false
    # default farmer user value
    # you can give farmer.user.<amount> perm to owner of farmer
    defaultMaxFarmerUser: 3
    # language from lang file
    lang: en
    # farmer ignore collecting if item dropped by player
    ignorePlayerDrop: false
    # Allowed worlds
    allowedWorlds:
    - ASkyBlock
      - Island
      - SuperiorWorld
      - bskyblock_world
    
    # Tax rate
    # If you set it 0 then it useless
    # If you want to deposit tax amount to a player
    # use depositUser and set true the deposit settings
    tax:
    rate: 20
    deposit: false
    depositUser: Geyik
    
    # Farmer levels
    # Each level must has capacity and reqMoney
    # First level must has capacity only
    # Other settings are optional like reqPerm and tax
    # If you want to make custom tax for a level then add tax: 1 etc.
    # If you want to set a perm for level to purchase then add reqPerm: "my.perm"
    levels:
    first:
        capacity: 1000
    second:
        capacity: 2000
        reqMoney: 5000
    third:
        capacity: 10000
        reqMoney: 8000
        reqPerm: "my.custom.perm"
        tax: 15
</details>

<details>
    <summary>lang.yml</summary>

    # placeholders: {money} money which deposited to player {tax} tax amount.
    sellComplete: "&6Farmer &8▸ &aItems sold. &6Profit: &e{money}&f, &6Tax: &e{tax}"
    wrongWorld: "&6Farmer &8▸ &cYou cannot do this in this world."
    noPerm: "&6Farmer &8▸ &cYou don't have permission!"
    noRegion: "&6Farmer &8▸ &cThere is no region for bound a farmer."
    removedFarmer: "&6Farmer &8▸ &aRemoved farmer successfully."
    noFarmer: "&6Farmer &8▸ &cThere is no farmer bound here."
    mustBeOwner: "&6Farmer &8▸ &cYou must to be Region Owner for this."
    inventoryFull: "&6Farmer &8▸ &cInventory full!"
    # placeholders: {money} players money {req_money} required money.
    notEnoughMoney: "&6Farmer &8▸ &cDon't have enough money! Required: &4{req_money}"
    # placeholders: {level} new upgraded level {capacity} new upgraded capacity.
    levelUpgraded: "&6Farmer &8▸ &aFarmer upgraded to &6{level}&a level. &2New Capacity: &e{capacity}"
    # placeholders: {status} shows status of farmer status. (#toggledON, #toggledOFF)
    toggleFarmer: "&6Farmer &8▸ &aFarmers collection settings changed to: &e{status}"
    toggleON: "&aActive"
    toggleOFF: "&cDisabled"
    featureDisabled: "&6Farmer &8▸ &cThis feature disallowed."
    reloadSuccess: "&6Farmer &8▸ &aConfig reloaded successfully. It took %ms%"
    boughtFarmer: "&6Farmer &8▸ &aFarmer bought successfully."
    # placeholders: {time} left for do it again.
    inCooldown: "&6Farmer &8▸ &cYou should wait {time}s for do it again."
    inputCancelWord: "cancel"
    waitingInput: "&6Farmer &8▸ &aType input to chat in 6sec and type &c{cancel} &afor cancel."
    notOwner: "&6Farmer &8▸ &cYou must be the owner of the region to use this command."
    inputCancel: "&6Farmer &8▸ &cNo longer waiting for input."
    userAdded: "&6Farmer &8▸ &2{player} &aAdded successfully."
    userAlreadyExist: "&6Farmer &8▸ &4{player} &cAlready added."
    userCouldntFound: "&6Farmer &8▸ &cUser has not played before!"
    reachedMaxUser: "&6Farmer &8▸ &cYou have reached max user capacity."
    percentBar: "▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪"
    buyDisabled:
      title: "&6Farmer"
      subtitle: "&cVisit our website for farmer"
    
    # Farmer roles
    roles:
      owner: "&cOwner"
      member: "&aMember"
      coop: "&eCoop"
    
    nextPage:
      # Placeholder: %nextpage% shows next page index.
      name: "&eNext Page (%nextpage%)"
    previousPage:
      # Placeholder: %prevpage% shows previous page index.
      name: "&ePrevious Page (%prevpage%)"
    guiFiller:
      use: true
      material: GRAY_STAINED_GLASS_PANE
    # MAIN GUI
    Gui:
      # m -> Management Panel item
      # g -> Item Group element item
      # p -> Previous Page item
      # n -> Next Page item
      # h -> Help item
      interface:
        - "    m    "
        - " ggggggg "
        - " ggggggg "
        - " ggggggg "
        - " ggggggg "
        - "p   h   n"
      guiName: "&8Farmer Storage"
      manage:
        # If you don't want skull you can remove "skull" and create "material: (YOUR_MATERIAL_HERE)"
        skull: "ewogICJ0aW1lc3RhbXAiIDogMTYyMDM5NzA2MjE1MSwKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0="
        name: "&eManagement Panel"
        lore:
          - '&7Only region owner can'
          - '&7open this panel.'
          - ''
          - '&dFarmer Stats:'
          - ' &8▪ &7Level: &6{level}'
          - ' &8▪ &7Capacity: &6{capacity}'
          - ' &8▪ &7Tax Rate: &6{tax}'
          - ''
          - '&aClick for management panel!'
      help:
        # If you don't want skull you can remove "skull" and create "material: (YOUR_MATERIAL_HERE)"
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE2Y2M1NzU1Y2RkMjYwZjdiNGI1YzFhMWYxZjNiZDMxODUxZmMxZDk4Yjc0NDM3YjJmYjRiZDZlYjhkMiJ9fX0="
        name: "&eInformation"
        lore:
          - '&7This is inventory of Farmer.'
          - '&7Farmer stores items here.'
          - '&7What you can do here:'
          - ' &8▪ &6Sell items'
          - ' &8▪ &6Take items to inventory'
          - ' &8▪ &6Management panel (Only Leader)'
          - ''
          - '&cIf you are coop you can'
          - '&conly see this menu.'
      # Placeholders:
      # {stock} Shows how many item farmer have.
      # {maxstock} Shows maximum stock of farmer.
      # {percent} Shows stock fullness percent.
      # {bar} Shows percent in bar format. Uses #percentBar.
      # {price} Shows item price (each).
      # {stack_price} Shows item stack price (Basically multiplies price x64)
      groupItem:
        lore:
          - ""
          - " &8▪ &7Stock: &f{stock}&8/&c{maxstock}"
          - " &8▪ &7Price: &f{price}$ each"
          - "&8&l  [{bar}&8&l] &r{percent}%"
          - ""
          - "&7Average Production (min): &f{prod_min}"
          - "&7Average Production (hour): &f{prod_hour}"
          - "&7Average Production (day): &f{prod_day}"
          - "{prod_blank}"
          - "&7Withdraw Stack &8[&eLeft Click&8]"
          - "&7Withdraw Max &8[&eRight Click&8]"
          - "&7Sell All &8[&eShift+Right Click&8]"
          - ""
          - "&4DANG: &cSell all feature takes"
          - "&4%{tax} &ctax.!"
    
    # Management Gui
    manageGui:
      # t -> taking situation icon
      # l -> level up icon
      # u -> user management icon
      interface:
        - "    m    "
        - " t  l  u "
        - "         "
      guiName: "&8Manager Panel"
      # Placeholders:
      # {level} Shows level of farmer.
      # {max_level} Shows maximum level farmer can be.
      # {next_level} Shows next level of farmer.
      # {capacity} Shows farmer capacity.
      # {next_capacity} Shows the farmer's capacity at the next level.
      # {req_money} Shows required money of next level.
      upgradeNext:
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZjNmVjM2I3NTM1NGI0OTIyMmE4OWM2NjNjNGFjYWQ1MjY0ZmI5NzdjYWUyNmYwYjU0ODNhNTk5YzQ2NCJ9fX0="
        name: '&6{level}. &eLevel Farmer'
        lore:
          - ''
          - ' &8▪ &7New Level: &6{next_level}&7/&c{max_level}'
          - ' &8▪ &7New Capacity: &e{next_capacity}'
          - ' &8▪ &7Required Money: &6{req_money}'
          - ''
          - '&aClick for upgrade level!'
      # Placeholders:
      # {level} Shows level of farmer.
      # {capacity} Shows farmer capacity.
      inMaxLevel:
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3OGNjMzkxYWZmYjgwYjJiMzVlYjczNjRmZjc2MmQzODQyNGMwN2U3MjRiOTkzOTZkZWU5MjFmYmJjOWNmIn19fQ=="
        name: '&6{level}. &eLevel Farmer'
        lore:
          - '&7Farmer is in max level.'
          - '&7You cannot upgrade much more.'
          - ''
          - ' &8▪ &7Capacity: &6{capacity}'
      closeFarmer:
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19"
        name: '&eClose Collecting'
        lore:
          - '&7Closes farmer and it will be'
          - '&7useless until reopen.'
          - ''
          - ' &8▪ &7Status: &6{status}'
          - ''
          - '&aClick for change!'
      users:
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjg1NDA2MGFhNTc3NmI3MzY2OGM4OTg2NTkwOWQxMmQwNjIyNDgzZTYwMGI2NDZmOTBjMTg2YzY1Yjc1ZmY0NSJ9fX0="
        name: "&eUser Management"
        lore:
          - '&7You can add/remove/modify'
          - '&7users in here.'
          - ''
          - '&aClick for open.'
      modules:
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZlZmM4NmRiOTIyMTdjNWEzODk2NzJiMjgyNDI3NWU3YTIwNmQ3ZWMwZjJjN2U0Y2E0ODNjNmUxN2M5ZjZkNSJ9fX0="
        name: "&eModules"
        lore:
          - '&7You can modify farmer'
          - '&7modules in here.'
          - ''
          - '&aClick for open.'
    
    # Buy Gui (Farmer)
    buyGui:
      interface:
        - "         "
        - "    b    "
        - "         "
      guiName: "&8Buy Farmer"
      item:
        skull: "ewogICJ0aW1lc3RhbXAiIDogMTYyMDM5NzA2MjE1MSwKICAicHJvZmlsZUlkIiA6ICI0ZGI2MWRkOTM0Mzk0M2M0YjhhOTZiNDQwMWM3MDM1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiZWVyYmVsbHltYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZTgxOTkwNmViMTc5NDM5YjhkZDU1NTExMzJlNTRlYjQ3MTczZTBmNDU4ODYxYWQyYThjOTM3OTE4Mzg5MSIKICAgIH0KICB9Cn0="
        name: "&eBuy Farmer"
        lore:
          - '&7You can buy farmer by'
          - '&7clicking this item.'
          - ''
          - ' &8▪ &7Price: &6{price}'
          - ''
          - '&aClick for buy!'
    
    # User gui for farmer
    usersGui:
      # h -> help
      # u -> user
      # p -> previous page
      # a -> add
      # n -> next page
      interface:
        - "    h    "
        - "uuuuuuuuu"
        - "uuuuuuuuu"
        - "p   a   n"
      guiName: "&8Farmer Users"
      user:
        lore:
          - ''
          - ' &8▪ &7Role: &6{role}'
          - ''
          - '&aLeft or Right click for promote/demote'
          - '&4Shift+Right click for delete'
      help:
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE2Y2M1NzU1Y2RkMjYwZjdiNGI1YzFhMWYxZjNiZDMxODUxZmMxZDk4Yjc0NDM3YjJmYjRiZDZlYjhkMiJ9fX0="
        name: "&eInformation"
        lore:
          - '&7You can promote/demote/remove'
          - '&7and add user here.'
          - ''
          - '&7Perm Graph:'
          - ' &8▪ &eCoop can only look farmer.'
          - ' &8▪ &6Member can sell and take items.'
          - ' &8▪ &cOwner can do everything.'
      addUser:
        skull: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19"
        name: "&eAdd user"
        lore:
          - ''
          - '&aClick for add user.'
    
    moduleGui:
      interface:
        - "         "
        - " s  k  h "
        - "         "
      guiName: "&8Farmer Modules"
</details>

## Commands
+ **/farmer** - Open buy or farmer gui depends on do you have it or not
+ **/farmer manage** - Management gui (Perm: farmer.admin or owner of farmer)
+ **/farmer info** - Info of farmer which stand on (Perm: farmer.admin)
+ **/farmer reload** - Reloads plugin (Perm: farmer.admin)

## Images

### Average Production Calculator
This is a module of Farmer which calculates average of items which farmer collects and shows it in gui. You can disable in module settings.

![Screenshot_1.png](images%2FScreenshot_1.png)
![Screenshot_2.png](images%2FScreenshot_2.png)

### Main Farmer Gui
![Screenshot_3.png](images%2FScreenshot_3.png)

### Farmer Management Gui
(You can see stock, level, collecting status, users and modules if exists)

![Screenshot_4.png](images%2FScreenshot_4.png)
![Screenshot_5.png](images%2FScreenshot_5.png)
![Screenshot_6.png](images%2FScreenshot_6.png)
![Screenshot_7.png](images%2FScreenshot_7.png)

### Modules Gui (If exists and enabled)

![Screenshot_8.png](images%2FScreenshot_8.png)

### User Gui
![Screenshot_9.png](images%2FScreenshot_9.png)
![Screenshot_10.png](images%2FScreenshot_10.png)

## API

### Maven:

Add this to your pom.xml if you use in maven.

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.poyrazinan</groupId>
    <artifactId>Farmer-v6</artifactId>
    <version>{RELEASE-VERSION}</version>
</dependency>
```

### Gradle:

Add this to your build.gradle if you use in gradle.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```
```groovy
dependencies {
    implementation 'com.github.poyrazinan:Farmer-v6:{RELEASE-VERSION}'
}
```

### How to use?

FarmerAPI has good javadoc.

You can check it out the farmer javadoc [Java-Doc](https://poyrazinan.github.io/Farmer-v6/)

```java
public class Main extends JavaPlugin {
    // Returns Main class of plugin
    Main farmerMain = FarmerAPI.getInstance();
    // Gets farmer manager
    FarmerManager farmerManager = FarmerAPI.getFarmerManager();
    // Gets storage manager
    StorageManager storageManager = FarmerAPI.getStorageManager();
    // Gets module manager
    ModuleManager moduleManager = FarmerAPI.getModuleManager();
    // Gets database manager
    DatabaseManager databaseManager = FarmerAPI.getDatabaseManager();
}
```

### Listeners

* FarmerBoughtEvent
* FarmerItemCollectEvent
* FarmerItemProductionEvent
* FarmerItemSellEvent
* FarmerMainGuiOpenEvent
* FarmerModuleGuiCreateEvent
* FarmerRemoveEvent
* FarmerStorageFullEvent

## Used Libraries

* [spigot-api (1.19-R0.3-SNAPSHOT)](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/spigot/browse)
* [lombok (LATEST)](https://github.com/projectlombok/lombok)
* [BStats](https://bstats.org)
* [Vault](https://www.spigotmc.org/resources/vault.34315/)
* [SimplixStorage](https://www.spigotmc.org/resources/simplixstorage-awesome-library-to-store-data-in-a-better-way.67286/)
* [InventoryGUI](https://github.com/Phoenix616/InventoryGui)
* [XSeries](https://github.com/CryptoMorin/XSeries)
* [AuthLib](https://mvnrepository.com/artifact/com.mojang/authlib/1.5.25)
* [NBT-API](https://github.com/tr7zw/Item-NBT-API)
* [WildStacker](https://github.com/BG-Software-LLC/WildStacker)

### Integration Libraries (Optional)
* [SuperiorSkyblock2](https://github.com/BG-Software-LLC/SuperiorSkyblock2)
* [GriefPrevention](https://www.spigotmc.org/resources/griefprevention.1884/)
* [BentoBox](https://www.spigotmc.org/resources/bentobox-bskyblock-acidisland-skygrid-caveblock-aoneblock-boxed.73261/)
* [ASkyBlock](https://www.spigotmc.org/resources/askyblock.1220/)