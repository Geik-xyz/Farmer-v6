package xyz.geik.farmer.configuration.lang;


import lombok.Getter;
import lombok.Setter;
import xyz.geik.farmer.configuration.LangFile;
import xyz.geik.glib.shades.okaeri.configs.OkaeriConfig;
import xyz.geik.glib.shades.okaeri.configs.annotation.Comment;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameModifier;
import xyz.geik.glib.shades.okaeri.configs.annotation.NameStrategy;
import xyz.geik.glib.shades.okaeri.configs.annotation.Names;

import java.util.Arrays;
import java.util.List;

/**
 * LangFile
 * @author amownyy
 * @since 1.0
 */
@Getter
@Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class tr extends LangFile {

    /**
     * Settings menu of config
     */
    @Comment("Chat messages")
    private Messages messages = new Messages();

    /**
     * Messages of plugin
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Messages extends LangFile.Messages {

        @Comment("Prefix of messages")
        private String prefix = "&3Çiftçi &8»";

        @Comment("placeholders: {money} money which deposited to player {tax} tax amount.")
        private String sellComplate = "{prefix} &aEşyalar satıldı. &6Kazanç: &e{money}&f, &6Vergi: &e{tax}";
        private String wrongWorld = "{prefix} &cBunu bu dünyada yapamazsın.";
        private String noRegion = "{prefix} &cÇiftçi koymak için kendi bölgende olman gerek.";
        private String removedFarmer = "{prefix} &aÇiftçi başarıyla silindi.";
        private String noFarmer = "{prefix} &cBurada bir çiftçi yok.";
        private String mustBeOwner = "{prefix} &cBunun için bölgenin sahibi olmalısın.";
        private String inventoryFull = "{prefix} &cEnvanterin dolu!";
        @Comment("placeholders: {money} players money {req_money} required money.")
        private String notEnoughMoney = "{prefix} &cYeterli miktarda paran yok! Gereken miktar: &4{req_money}";
        @Comment("placeholders: {level} new upgraded level {capacity} new upgraded capacity.")
        private String levelUpgraded = "{prefix} &aÇiftçin &6{level}&a seviye oldu. Kapasite&2: &e{capacity}";
        @Comment("placeholders: {status} shows status of farmer status. (#toggledON, #toggledOFF)")
        private String toggleFarmer = "{prefix} &aToplama ayarı: &e{status}";
        private String featureDisabled = "{prefix} &cBu özellik devre dışı.";
        private String reloadSuccess = "{prefix} &aConfig dosyalarının yenilenmesi %ms% içinde tamamlandı.";
        private String boughtFarmer = "{prefix} &aÇiftçi başarıyla alındı.";
        private String inCooldown = "{prefix} &cBunu tekrar yapmak için {time}sn beklemelisin.";
        private String waitingInput = "{prefix} &a6 saniye içinde eklemek istediğin kullanıcı adını yaz veya iptal etmek için &c{cancel} &ayaz.";
        private String notOwner = "{prefix} &cBu özelliği kullanmak için bölge sahibi olman gerek.";
        private String inputCancel = "{prefix} &cKullanıcı ekleme işlemi iptal oldu.";
        private String userAdded = "{prefix} &2{player} &aEklendi.";
        private String userAlreadyExist = "{prefix} &4{player} &cZaten ekli.";
        private String userCouldntFound = "{prefix} &cBu oyuncu daha önce hiç oynamamış!";
        private String reachedMaxUser = "{prefix} &cDaha fazla üye alamazsın!";
        private String playerNotOnline = "{prefix} &cHedef oyuncu çevrimiçi değil.";
        private String playerNotAvailable = "{prefix} &cOyuncu mevcut değil.";
        private String targetPlayerNotAvailable = "{prefix} &cHedef oyuncu mevcut değil.";
        private String configReloaded = "{prefix} &aYapılandırma başarıyla yeniden yüklendi.";
        private String invalidArgument = "{prefix} &cGeçersiz argüman!";
        private String unknownCommand = "{prefix} &cBilinmeyen komut!";
        private String notEnoughArguments = "{prefix} &cYetersiz argüman girildi!";
        private String tooManyArguments = "{prefix} &cÇok fazla agüman girildi!";
        private String noPerm = "{prefix} &cBunun için yetkin yok!";
    }

    private Various various = new Various();

    /**
     * Various lang settings
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Various extends LangFile.Various {
        private String percentBar = "▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪▪";
        private String toggleOn = "&aAktif";
        private String toggleOff = "&cDevre Dışı";
        private String inputCancelWord = "iptal";
    }

    private BuyDisabled buyDisabled = new BuyDisabled();

    /**
     * BuyDisabled lang settings
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class BuyDisabled extends LangFile.BuyDisabled {
        private String title = "&6Çiftçi";
        private String subtitle = "&cÇiftçi için web sitemizi ziyaret edin";
    }

    private Roles roles = new Roles();

    /**
     * Roles lang settings
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Roles extends LangFile.Roles {
        private String owner = "&cLider";
        private String member = "&aÜye";
        private String coop = "&eİşçi";
    }

    private Gui gui = new Gui();

    /**
     * Gui lang settings
     *
     * @author amownyy
     * @since 2.0
     */
    @Getter
    @Setter
    public static class Gui extends LangFile.Gui {

        private NextPage nextPage = new NextPage();

        /**
         * NextPage item gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class NextPage extends LangFile.Gui.NextPage {
            @Comment("Placeholder: %nextpage% shows next page index.")
            private String name = "&eÖnceki Sayfa (%prevpage%)";
        }

        private PreviousPage previousPage = new PreviousPage();

        /**
         * PreviosPage item gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class PreviousPage extends LangFile.Gui.PreviousPage {
            @Comment("Placeholder: %prevpage% shows previous page index.")
            private String name = "&ePrevious Page (%prevpage%)";
        }

        private FarmerGui farmerGui = new FarmerGui();

        /**
         * FarmerGui gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class FarmerGui extends LangFile.Gui.FarmerGui {
            private String guiName = "&8Çiftçi Deposu";

            private Items items = new Items();

            /**
             * Items of farmer gui settings
             *
             * @author amownyy
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.FarmerGui.Items {

                private Manage manage = new Manage();

                /**
                 * Manage item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Manage extends LangFile.Gui.FarmerGui.Items.Manage {
                    private String name = "&eYönetim Paneli";
                    private List<String> lore = Arrays.asList(
                            "&7Sadece bölge sahibi bu",
                            "&7sayfaya erişebilir.",
                            "",
                            "&dÇiftçi istatistikleri:",
                            " &8▪ &7Seviye: &6{level}",
                            " &8▪ &7Kapasite: &6{capacity}",
                            " &8▪ &7Vergi Oranı: &6{tax}",
                            "",
                            "&aYönetim paneli için tıkla!"
                    );
                }

                private Help help = new Help();
                /**
                 * Help item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Help extends LangFile.Gui.FarmerGui.Items.Help {
                    private String name = "&eBilgilendirme";
                    private List<String> lore = Arrays.asList(
                            "&7Bu sayfa çiftçinin envanteridir.",
                            "&7Çiftçi eşyaları burada biriktiriyor.",
                            "&7Burada neler yapabilirsin:",
                            " &8▪ &6Bu eşyaları satabilirsin",
                            " &8▪ &6Bu eşyaları envanterine alabilirsin",
                            " &8▪ &6Yönetici paneline erişebilirsin (Sadece Lider)",
                            "",
                            "&cEğer COOP rolüne sahipsen",
                            "&csadece bu menüyü görüntüleyebilirsin."
                    );
                }

                @Comment({
                        "Placeholders:",
                        "{stock} Shows how many item farmer have.",
                        "{maxstock} Shows maximum stock of farmer.",
                        "{percent} Shows stock fullness percent.",
                        "{bar} Shows percent in bar format. Uses #percentBar.",
                        "{price} Shows item price (each).",
                        "{stack_price} Shows item stack price (Basically multiplies price x64)"
                })
                private GroupItems groupItems = new GroupItems();
                /**
                 * GroupItems item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class GroupItems extends LangFile.Gui.FarmerGui.Items.GroupItems {
                    private List<String> lore = Arrays.asList(
                            "",
                            " &8▪ &7Stok: &f{stock}&8/&c{maxstock}",
                            " &8▪ &7Fiyat: &f{price}$ adet",
                            "&8&l  [{bar}&8&l] &r{percent}%",
                            "",
                            "&7Ortalama Üretim (dakika): &f{prod_min}",
                            "&7Ortalama Üretim (saat): &f{prod_hour}",
                            "&7Ortalama Üretim (gün): &f{prod_day}",
                            "{prod_blank}",
                            "&7Envanterine Bir Stack Al &8[&eSol Tık&8]",
                            "&7Envanterine Maks Al &8[&eSağ Tık&8]",
                            "&7Hepsini Sat &8[&eShift+Sağ Tık&8]",
                            "",
                            "&4DIKKAT: &cHepsini sat özelliği",
                            "&4%{tax} &cvergi alıyor.!"
                    );
                }

            }
        }

        private ManageGui manageGui = new ManageGui();
        /**
         * Manage gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class ManageGui extends LangFile.Gui.ManageGui {

            private String guiName = "&8Yönetim Paneli";

            private Items items = new Items();

            /**
             * Items of manage gui settings
             *
             * @author amownyy
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.ManageGui.Items {

                @Comment({
                        "Placeholders:",
                        "{level} Shows level of farmer.",
                        "{max_level} Shows maximum level farmer can be.",
                        "{next_level} Shows next level of farmer.",
                        "{capacity} Shows farmer capacity.",
                        "{next_capacity} Shows the farmer's capacity at the next level.",
                        "{req_money} Shows required money of next level."
                })
                private UpgradeNext upgradeNext = new UpgradeNext();
                /**
                 * UpgradeNext item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class UpgradeNext extends LangFile.Gui.ManageGui.Items.UpgradeNext {
                    private String name = "&6{level}. &eSeviye Çiftçi";
                    private List<String> lore = Arrays.asList(
                            "",
                            " &8▪ &7Sonraki Seviye: &6{next_level}&7/&c{max_level}",
                            " &8▪ &7Sonraki Kapasite: &e{next_capacity}",
                            " &8▪ &7Gerekli Para: &6{req_money}",
                            "",
                            "&aSeviye yükseltmek için tıkla!"
                    );
                }

                @Comment({
                        "Placeholders:",
                        "{level} Shows level of farmer.",
                        "{capacity} Shows farmer capacity.",
                })
                private MaxLevel maxLevel = new MaxLevel();
                /**
                 * MaxLevel item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class MaxLevel extends LangFile.Gui.ManageGui.Items.MaxLevel {
                    private String name = "&6{level}. &aSeviye Çiftçi";
                    private List<String> lore = Arrays.asList(
                            "&7Çiftçi son seviye.",
                            "&7Daha fazla yükseltme yapamazsın.",
                            "",
                            " &8▪ &7Kapasite: &6{capacity}"
                    );
                }

                private CloseFarmer closeFarmer = new CloseFarmer();
                /**
                 * CloseFarmer item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class CloseFarmer extends LangFile.Gui.ManageGui.Items.CloseFarmer {
                    private String name = "&eToplamayı Değiştir";
                    private List<String> lore = Arrays.asList(
                            "&7Çiftçinin toplama özelliğini",
                            "&7Kapatır/Açar",
                            "",
                            " &8▪ &7Durum: &6{status}",
                            "",
                            "&aDeğiştirmek için tıkla!"
                    );
                }

                private Users users = new Users();
                /**
                 * Users item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Users extends LangFile.Gui.ManageGui.Items.Users {
                    private String name = "&eÜye Paneli";
                    private List<String> lore = Arrays.asList(
                            "&7Buradan çiftçiye üye",
                            "&7ekleyebilir/silebilir/düzenleyebilirsin",
                            "",
                            "&aAçmak için tıkla."
                    );
                }

                private Modules modules = new Modules();
                /**
                 * Modules item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Modules extends LangFile.Gui.ManageGui.Items.Modules {
                    private String name = "&eModüller";
                    private List<String> lore = Arrays.asList(
                            "&7Modül ayarlarına",
                            "&7buradan erişebilirsin.",
                            "",
                            "&aAçmak için tıkla."
                    );
                }
            }
        }

        private BuyGui buyGui = new BuyGui();
        /**
         * BuyGui gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class BuyGui extends LangFile.Gui.BuyGui {

            private String guiName = "&8Çiftçi Al";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author amownyy
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.BuyGui.Items {

                private BuyItem buyItem = new BuyItem();
                /**
                 * BuyItem settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class BuyItem extends LangFile.Gui.BuyGui.Items.BuyItem {
                    private String name = "&eÇiftçi Al";
                    private List<String> lore = Arrays.asList(
                            "&7Bu eşyaya tıklayarak",
                            "&7Çiftçi satın alabilirsin.",
                            "",
                            " &8▪ &7Fiyat: &6{price}",
                            "",
                            "&aSatın almak için tıkla!"
                    );
                }
            }
        }

        private UsersGui usersGui = new UsersGui();
        /**
         * Users gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class UsersGui extends LangFile.Gui.UsersGui {

            private String guiName = "&8Çiftçi Üyeleri";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author amownyy
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.UsersGui.Items {

                private User user = new User();
                /**
                 * User head item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class User extends LangFile.Gui.UsersGui.Items.User {
                    private String name = "&e%player_name%";
                    private List<String> lore = Arrays.asList(
                            "",
                            " &8▪ &7Rol: &6{role}",
                            "",
                            "&aSol veya Sağ Tık ile rolünü değiştirebilirsin",
                            "&4Shift+Sağ Tık ile üyeyi silebilirsin."
                    );
                }

                private Help help = new Help();
                /**
                 * Help item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class Help extends LangFile.Gui.UsersGui.Items.Help {
                    private String name = "&eBilgilendirme";
                    private List<String> lore = Arrays.asList(
                            "&7Buradan çiftçi üyelerini terfi/tenzil/silme",
                            "&7ve üye ekleme işlemlerini yapabilirsin.",
                            "",
                            "&7Rütbe Sıralaması:",
                            " &8▪ &eCoop sadece çiftçi menüsünü açabilir.",
                            " &8▪ &6Member eşyaları alıp satabilir.",
                            " &8▪ &cOwner her şeyi yapabilir."
                    );
                }

                private AddUser addUser = new AddUser();
                /**
                 * AddUser item settings
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class AddUser extends LangFile.Gui.UsersGui.Items.AddUser {
                    private String name = "&eÜye Ekle";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aÜye eklemek için tıkla."
                    );
                }
            }
        }

        private GeyserGui geyserGui = new GeyserGui();
        /**
         * GeyserGui gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class GeyserGui extends LangFile.Gui.GeyserGui {

            private String guiName = "&8Çiftçi Geyser Gui";

            private Items items = new Items();

            /**
             * Items of buy gui settings
             *
             * @author amownyy
             * @since 2.0
             */
            @Getter
            @Setter
            public static class Items extends LangFile.Gui.GeyserGui.Items {
                private LeftClick geyserLeftClick = new LeftClick();
                /**
                 * GeyserLeftClick item settings
                 * (Only for geyser player)
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class LeftClick extends LangFile.Gui.GeyserGui.Items.LeftClick {
                    private String name = "&eLeft Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aSeçilen öğeyi çiftçi envanterinizden",
                            "&abir yığın olarak al."
                    );
                }

                private RightClick rightClick = new RightClick();
                /**
                 * GeyserRightClick item settings
                 * (Only for geyser player)
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class RightClick extends LangFile.Gui.GeyserGui.Items.RightClick {
                    private String name = "&eRight Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aSeçilen öğeyi çiftçi envanterinizden",
                            "&ahepsini al."
                    );
                }

                private ShiftRightClick shiftRightClick = new ShiftRightClick();
                /**
                 * GeyserShiftRightClick item settings
                 * (Only for geyser player)
                 *
                 * @author amownyy
                 * @since 2.0
                 */
                @Getter
                @Setter
                public static class ShiftRightClick extends LangFile.Gui.GeyserGui.Items.ShiftRightClick {
                    private String name = "&eShift+Right Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "&aÇiftçi envanterinizdeki tüm öğeleri satın."
                    );
                }
            }
        }

        private ModuleGui moduleGui = new ModuleGui();
        /**
         * ModuleGui gui settings
         *
         * @author amownyy
         * @since 2.0
         */
        @Getter
        @Setter
        public static class ModuleGui extends LangFile.Gui.ModuleGui {
            private String guiName = "&8Çiftçi Eklentileri";
        }

    }
}
