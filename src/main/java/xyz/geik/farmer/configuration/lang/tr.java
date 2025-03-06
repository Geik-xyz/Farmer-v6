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
        private String prefix = "<aqua>Çiftçi <dark_gray>»";

        @Comment("placeholders: {money} money which deposited to player {tax} tax amount.")
        private String sellComplate = "{prefix} <green>Eşyalar satıldı. <gold>Kazanç: <yellow>{money}<white>, <gold>Vergi: <yellow>{tax}";
        private String wrongWorld = "{prefix} <red>Bunu bu dünyada yapamazsın.";
        private String noRegion = "{prefix} <red>Çiftçi koymak için kendi bölgende olman gerek.";
        private String removedFarmer = "{prefix} <green>Çiftçi başarıyla silindi.";
        private String noFarmer = "{prefix} <red>Burada bir çiftçi yok.";
        private String mustBeOwner = "{prefix} <red>Bunun için bölgenin sahibi olmalısın.";
        private String inventoryFull = "{prefix} <red>Envanterin dolu!";
        @Comment("placeholders: {money} players money {req_money} required money.")
        private String notEnoughMoney = "{prefix} <red>Yeterli miktarda paran yok! Gereken miktar: <dark_red>{req_money}";
        @Comment("placeholders: {level} new upgraded level {capacity} new upgraded capacity.")
        private String levelUpgraded = "{prefix} <green>Çiftçin <gold>{level}<green> seviye oldu. Kapasite<dark_green>: <yellow>{capacity}";
        @Comment("placeholders: {status} shows status of farmer status. (#toggledON, #toggledOFF)")
        private String toggleFarmer = "{prefix} <green>Toplama ayarı: <yellow>{status}";
        private String featureDisabled = "{prefix} <red>Bu özellik devre dışı.";
        private String reloadSuccess = "{prefix} <green>Config dosyalarının yenilenmesi %ms% içinde tamamlandı.";
        private String boughtFarmer = "{prefix} <green>Çiftçi başarıyla alındı.";
        private String inCooldown = "{prefix} <red>Bunu tekrar yapmak için {time}sn beklemelisin.";
        private String waitingInput = "{prefix} <green>6 saniye içinde eklemek istediğin kullanıcı adını yaz veya iptal etmek için <red>{cancel} <green>yaz.";
        private String notOwner = "{prefix} <red>Bu özelliği kullanmak için bölge sahibi olman gerek.";
        private String inputCancel = "{prefix} <red>Kullanıcı ekleme işlemi iptal oldu.";
        private String userAdded = "{prefix} <dark_green>{player} <green>Eklendi.";
        private String userAlreadyExist = "{prefix} <dark_red>{player} <red>Zaten ekli.";
        private String userCouldntFound = "{prefix} <red>Bu oyuncu daha önce hiç oynamamış!";
        private String reachedMaxUser = "{prefix} <red>Daha fazla üye alamazsın!";
        private String playerNotOnline = "{prefix} <red>Hedef oyuncu çevrimiçi değil.";
        private String playerNotAvailable = "{prefix} <red>Oyuncu mevcut değil.";
        private String targetPlayerNotAvailable = "{prefix} <red>Hedef oyuncu mevcut değil.";
        private String configReloaded = "{prefix} <green>Yapılandırma başarıyla yeniden yüklendi.";
        private String invalidArgument = "{prefix} <red>Geçersiz argüman!";
        private String unknownCommand = "{prefix} <red>Bilinmeyen komut!";
        private String notEnoughArguments = "{prefix} <red>Yetersiz argüman girildi!";
        private String tooManyArguments = "{prefix} <red>Çok fazla agüman girildi!";
        private String noPerm = "{prefix} <red>Bunun için yetkin yok!";
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
        private String toggleOn = "<green>Aktif";
        private String toggleOff = "<red>Devre Dışı";
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
        private String title = "<gold>Çiftçi";
        private String subtitle = "<red>Çiftçi için web sitemizi ziyaret edin";
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
        private String owner = "<red>Lider";
        private String member = "<green>Üye";
        private String coop = "<yellow>İşçi";
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
            private String name = "<yellow>Önceki Sayfa (%prevpage%)";
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
            private String name = "<yellow>Previous Page (%prevpage%)";
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
            private String guiName = "<dark_gray>Çiftçi Deposu";

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
                    private String name = "<yellow>Yönetim Paneli";
                    private List<String> lore = Arrays.asList(
                            "<gray>Sadece bölge sahibi bu",
                            "<gray>sayfaya erişebilir.",
                            "",
                            "<light_purple>Çiftçi istatistikleri:",
                            " <dark_gray>▪ <gray>Seviye: <gold>{level}",
                            " <dark_gray>▪ <gray>Kapasite: <gold>{capacity}",
                            " <dark_gray>▪ <gray>Vergi Oranı: <gold>{tax}",
                            "",
                            "<green>Yönetim paneli için tıkla!"
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
                    private String name = "<yellow>Bilgilendirme";
                    private List<String> lore = Arrays.asList(
                            "<gray>Bu sayfa çiftçinin envanteridir.",
                            "<gray>Çiftçi eşyaları burada biriktiriyor.",
                            "<gray>Burada neler yapabilirsin:",
                            " <dark_gray>▪ <gold>Bu eşyaları satabilirsin",
                            " <dark_gray>▪ <gold>Bu eşyaları envanterine alabilirsin",
                            " <dark_gray>▪ <gold>Yönetici paneline erişebilirsin (Sadece Lider)",
                            "",
                            "<red>Eğer COOP rolüne sahipsen",
                            "<red>sadece bu menüyü görüntüleyebilirsin."
                    );
                }

                private SellAll sellAll = new SellAll();
                /**
                 * SellAll item settings
                 *
                 * @author geik
                 * @since v6-b109
                 */
                @Getter
                @Setter
                public static class SellAll extends LangFile.Gui.FarmerGui.Items.SellAll {
                    private String name = "<yellow>Hepsini Sat";
                    private List<String> lore = Arrays.asList(
                            "<gray>Çiftçinin sahip olduğu",
                            "<gray>Tüm eşyaları satar.",
                            "<gray>",
                            "<green>Satmak için tıkla!"
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
                            " <dark_gray>▪ <gray>Stok: <white>{stock}<dark_gray>/<red>{maxstock}",
                            " <dark_gray>▪ <gray>Fiyat: <white>{price}$ adet",
                            "<dark_gray><bold>[{bar}<dark_gray><bold>] <reset>{percent}%",
                            "",
                            "<gray>Ortalama Üretim (dakika): <white>{prod_min}",
                            "<gray>Ortalama Üretim (saat): <white>{prod_hour}",
                            "<gray>Ortalama Üretim (gün): <white>{prod_day}",
                            "{prod_blank}",
                            "<gray>Envanterine Bir Stack Al <dark_gray>[<yellow>Sol Tık<dark_gray>]",
                            "<gray>Envanterine Maks Al <dark_gray>[<yellow>Sağ Tık<dark_gray>]",
                            "<gray>Hepsini Sat <dark_gray>[<yellow>Shift+Sağ Tık<dark_gray>]",
                            "",
                            "<dark_red>DIKKAT: <red>Hepsini sat özelliği",
                            "<dark_red>%{tax} <red>vergi alıyor.!"
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

            private String guiName = "<dark_gray>Yönetim Paneli";

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
                    private String name = "<gold>{level}. <yellow>Seviye Çiftçi";
                    private List<String> lore = Arrays.asList(
                            "",
                            " <dark_gray>▪ <gray>Sonraki Seviye: <gold>{next_level}<gray>/<red>{max_level}",
                            " <dark_gray>▪ <gray>Sonraki Kapasite: <yellow>{next_capacity}",
                            " <dark_gray>▪ <gray>Gerekli Para: <gold>{req_money}",
                            "",
                            "<green>Seviye yükseltmek için tıkla!"
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
                    private String name = "<gold>{level}. <green>Seviye Çiftçi";
                    private List<String> lore = Arrays.asList(
                            "<gray>Çiftçi son seviye.",
                            "<gray>Daha fazla yükseltme yapamazsın.",
                            "",
                            " <dark_gray>▪ <gray>Kapasite: <gold>{capacity}"
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
                    private String name = "<yellow>Toplamayı Değiştir";
                    private List<String> lore = Arrays.asList(
                            "<gray>Çiftçinin toplama özelliğini",
                            "<gray>Kapatır/Açar",
                            "",
                            " <dark_gray>▪ <gray>Durum: <gold>{status}",
                            "",
                            "<green>Değiştirmek için tıkla!"
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
                    private String name = "<yellow>Üye Paneli";
                    private List<String> lore = Arrays.asList(
                            "<gray>Buradan çiftçiye üye",
                            "<gray>ekleyebilir/silebilir/düzenleyebilirsin",
                            "",
                            "<green>Açmak için tıkla."
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
                    private String name = "<yellow>Modüller";
                    private List<String> lore = Arrays.asList(
                            "<gray>Modül ayarlarına",
                            "<gray>buradan erişebilirsin.",
                            "",
                            "<green>Açmak için tıkla."
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

            private String guiName = "<dark_gray>Çiftçi Al";

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
                    private String name = "<yellow>Çiftçi Al";
                    private List<String> lore = Arrays.asList(
                            "<gray>Bu eşyaya tıklayarak",
                            "<gray>Çiftçi satın alabilirsin.",
                            "",
                            " <dark_gray>▪ <gray>Fiyat: <gold>{price}",
                            "",
                            "<green>Satın almak için tıkla!"
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

            private String guiName = "<dark_gray>Çiftçi Üyeleri";

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
                    private String name = "<yellow>%player_name%";
                    private List<String> lore = Arrays.asList(
                            "",
                            " <dark_gray>▪ <gray>Rol: <gold>{role}",
                            "",
                            "<green>Sol veya Sağ Tık ile rolünü değiştirebilirsin",
                            "<dark_red>Shift+Sağ Tık ile üyeyi silebilirsin."
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
                    private String name = "<yellow>Bilgilendirme";
                    private List<String> lore = Arrays.asList(
                            "<gray>Buradan çiftçi üyelerini terfi/tenzil/silme",
                            "<gray>ve üye ekleme işlemlerini yapabilirsin.",
                            "",
                            "<gray>Rütbe Sıralaması:",
                            " <dark_gray>▪ <yellow>Coop sadece çiftçi menüsünü açabilir.",
                            " <dark_gray>▪ <gold>Member eşyaları alıp satabilir.",
                            " <dark_gray>▪ <red>Owner her şeyi yapabilir."
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
                    private String name = "<yellow>Üye Ekle";
                    private List<String> lore = Arrays.asList(
                            "",
                            "<green>Üye eklemek için tıkla."
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

            private String guiName = "<dark_gray>Çiftçi Geyser Gui";

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
                    private String name = "<yellow>Left Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "<green>Seçilen öğeyi çiftçi envanterinizden",
                            "<green>bir yığın olarak al."
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
                    private String name = "<yellow>Right Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "<green>Seçilen öğeyi çiftçi envanterinizden",
                            "<green>hepsini al."
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
                    private String name = "<yellow>Shift+Right Click";
                    private List<String> lore = Arrays.asList(
                            "",
                            "<green>Çiftçi envanterinizdeki tüm öğeleri satın."
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
            private String guiName = "<dark_gray>Çiftçi Eklentileri";
        }

    }
}