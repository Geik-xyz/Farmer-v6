package xyz.geik.farmer.modules.geyser.gui;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.api.GeyserApi;
import xyz.geik.farmer.modules.geyser.Geyser;

public class GeyserGui {

    public static void showGui(Player player) {
        SimpleForm form = SimpleForm.builder()
                .title(Geyser.getInstance().getLang().getText("title"))
                .content(Geyser.getInstance().getLang().getText("content"))
                .button(Geyser.getInstance().getLang().getText("left-click-button"), FormImage.Type.URL, "https://cdn-icons-png.flaticon.com/512/32/32041.png")
                .button(Geyser.getInstance().getLang().getText("right-click-button"), FormImage.Type.URL, "https://cdn-icons-png.flaticon.com/512/31/31532.png")
                .button(Geyser.getInstance().getLang().getText("shift-right-click-button"), FormImage.Type.URL, "https://github.com/GeyserMC.png?size=200")
                .validResultHandler(formResponse -> {
                    SimpleFormResponse response = formResponse;
                    if (response.getClickedButtonId() == 0) {
                        System.out.println("Left");
                    } else if (response.getClickedButtonId() == 1) {
                        System.out.println("Right");
                    } else if (response.getClickedButtonId() == 2) {
                        System.out.println("Shift Right");
                    }
                })
                .build();

        GeyserApi.api().sendForm(player.getUniqueId(), form);
    }
}