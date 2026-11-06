package fr.altherneum.bot.listeners;

import fr.altherneum.bot.Main;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class createButton {
    private createButton() {
    }

    public static void onCreateButton() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("createButton")) {
                    event.reply("\uD83D\uDC8E").queue();

                    GuildMessageChannel channel = event.getOption("channel").getAsChannel().asGuildMessageChannel();
                    String id = event.getOption("button_id").getAsString();
                    String text = event.getOption("button_text").getAsString();

                    channel.sendMessageComponents(ActionRow.of(Button.secondary(id, text))).queue();
                }
            }
        });
    }
}
